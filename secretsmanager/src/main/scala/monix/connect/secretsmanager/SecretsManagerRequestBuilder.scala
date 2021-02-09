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

import monix.execution.internal.InternalApi
import software.amazon.awssdk.services.secretsmanager.model._
import software.amazon.awssdk.awscore.AwsRequestOverrideConfiguration
import scala.jdk.CollectionConverters._

/**
  * A class that provides converter methods that given the required set of parameters for that
  * conversion, it builds the relative AWS java object.
  */
@InternalApi
private[secretsmanager] object SecretsManagerRequestBuilder {

  /** A builder for [[GetSecretValueRequest]] .*/
  def getSecretValue(
    secretId: String,
    versionId: Option[String] = None,
    versionStage: Option[String] = None,
    overrideConfiguration: Option[AwsRequestOverrideConfiguration] = None): GetSecretValueRequest = {
    val request = GetSecretValueRequest
      .builder()
      .secretId(secretId)
    versionId.map(request.versionId(_))
    versionStage.map(request.versionStage(_))
    overrideConfiguration.map(request.overrideConfiguration(_))
    request.build()
  }

  /**
    *  A builder for [ListSecretsRequest]
    */
  def listSecrets(
    maxResults: Option[Int] = None,
    filters: Option[List[Filter]] = None,
    sortOrder: Option[SortOrderType] = None,
    nextToken: Option[String] = None,
    overrideConfiguration: Option[AwsRequestOverrideConfiguration] = None): ListSecretsRequest = {
    val request = ListSecretsRequest
      .builder()
    maxResults.map(request.maxResults(_))
    overrideConfiguration.map(request.overrideConfiguration(_))
    filters.map(f => request.filters(f.asJava))
    sortOrder.map(request.sortOrder(_))
    nextToken.map(request.nextToken(_))
    request.build()
  }
}
