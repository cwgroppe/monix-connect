/*
 * Copyright (c) 2020-2021 by The Monix Connect Project Developers.
 * See the project homepage at: https://connect.monix.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package monix.connect.secretsmanager

import monix.eval.Task
import monix.execution.{Ack, Cancelable}
import monix.execution.internal.InternalApi
import monix.reactive.Observable
import monix.reactive.observers.Subscriber
import software.amazon.awssdk.services.secretsmanager.SecretsManagerAsyncClient
import software.amazon.awssdk.services.secretsmanager.model.{
  Filter,
  ListSecretsRequest,
  ListSecretsResponse,
  SortOrderType
}
import software.amazon.awssdk.awscore.AwsRequestOverrideConfiguration
import scala.jdk.CollectionConverters._

@InternalApi
private[secretsmanager] class ListSecretsObservable(
  maxResults: Option[Int] = None,
  filters: Option[List[Filter]] = None,
  sortOrder: Option[SortOrderType] = None,
  nextToken: Option[String] = None,
  overrideConfiguration: Option[AwsRequestOverrideConfiguration] = None,
  secretsManagerAsyncClient: SecretsManagerAsyncClient)
  extends Observable[ListSecretsResponse] {

  require(maxResults.getOrElse(1) > 0, "The max number of results, if defined, needs to be higher or equal than 1.")

  private[this] val firstRequestSize = maxResults.map(maxResults => math.min(maxResults, 10)) //might make domain later
  private[this] val initialRequest: ListSecretsRequest =
    SecretsManagerRequestBuilder.listSecrets(
      maxResults = firstRequestSize,
      filters = filters,
      sortOrder = sortOrder,
      overrideConfiguration = overrideConfiguration)

  def unsafeSubscribeFn(subscriber: Subscriber[ListSecretsResponse]): Cancelable = {
    val s = subscriber.scheduler
    nextListSecretsRequest(subscriber, maxResults, initialRequest).runToFuture(s)
  }

  private[this] def prepareNextRequest(nextToken: String, pendingKeys: Option[Int]): ListSecretsRequest = {
    val requestBuilder = initialRequest.toBuilder.nextToken(nextToken)
    pendingKeys.map { n =>
      val nextMaxKeys = math.min(n, 10)
      requestBuilder.maxResults(nextMaxKeys)
    }
    requestBuilder.build()
  }

  private[this] def nextListSecretsRequest(
    sub: Subscriber[ListSecretsResponse],
    pendingKeys: Option[Int],
    request: ListSecretsRequest): Task[Unit] = {

    for {
      r <- {
        Task.from(secretsManagerAsyncClient.listSecrets(request)).onErrorHandleWith { ex =>
          sub.onError(ex)
          Task.raiseError(ex)
        }
      }
      ack <- Task.deferFuture(sub.onNext(r))
      nextRequest <- {
        ack match {
          case Ack.Continue => {
            if (r.nextToken() != null) {
              val updatedPendingKeys = pendingKeys.map(_ - r.secretList().size)
              updatedPendingKeys match {
                case Some(pendingKeys) =>
                  if (pendingKeys <= 0) { sub.onComplete(); Task.unit }
                  else
                    nextListSecretsRequest(
                      sub,
                      updatedPendingKeys,
                      prepareNextRequest(r.nextToken(), updatedPendingKeys))
                case None =>
                  nextListSecretsRequest(sub, Option.empty[Int], prepareNextRequest(r.nextToken(), None))
              }
            } else {
              sub.onComplete()
              Task.unit
            }
          }
          case Ack.Stop => Task.unit
        }
      }
    } yield nextRequest
  }
}

private[secretsmanager] object ListSecretsObservable {
  def apply(
    maxResults: Option[Int],
    filters: Option[List[Filter]],
    sortOrder: Option[SortOrderType],
    nextToken: Option[String],
    overrideConfiguration: Option[AwsRequestOverrideConfiguration],
    secretsManagerAsyncClient: SecretsManagerAsyncClient): ListSecretsObservable =
    new ListSecretsObservable(
      maxResults,
      filters,
      sortOrder,
      nextToken,
      overrideConfiguration,
      secretsManagerAsyncClient)
}
