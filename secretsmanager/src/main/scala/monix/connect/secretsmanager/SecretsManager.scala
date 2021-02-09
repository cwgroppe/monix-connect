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

import cats.effect.Resource
import monix.connect.aws.auth.AppConf

import monix.reactive.Observable
import monix.eval.Task
import monix.execution.annotations.UnsafeBecauseImpure
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.http.async.SdkAsyncHttpClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.awscore.AwsRequestOverrideConfiguration
import software.amazon.awssdk.services.secretsmanager.SecretsManagerAsyncClient
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse,GetSecretValueRequest,SecretListEntry,Filter,SortOrderType}

import scala.jdk.CollectionConverters._

object SecretsManager { self =>

  /**
    * Creates a [[Resource]] that will use the values from a
    * configuration file to allocate and release a [[SecretsManager]].
    * Thus, the api expects an `application.conf` file to be present
    * in the `resources` folder.
    *
    * @see how does the expected `.conf` file should look like
    *      https://github.com/monix/monix-connect/blob/master/aws-auth/src/main/resources/reference.conf`
    *
    * @see the cats effect resource data type: https://typelevel.org/cats-effect/datatypes/resource.html
    *
    * @return a [[Resource]] of [[Task]] that allocates and releases [[SecretsManager]].
    */
  def fromConfig: Resource[Task, SecretsManager] = {
    Resource.make {
      for {
        clientConf  <- Task.eval(AppConf.loadOrThrow)
        asyncClient <- Task.now(AsyncClientConversions.fromMonixAwsConf(clientConf.monixAws))
      } yield {
        self.createUnsafe(asyncClient)
      }
    } { _.close }
  }

  /**
    * Creates a [[Resource]] that will use the passed-by-parameter
    * AWS configurations to acquire and release [[SecretsManager]].
    *
    * ==Example==
    *
    * {{{
    *   import cats.effect.Resource
    *   import monix.eval.Task
    *   import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
    *   import software.amazon.awssdk.regions.Region
    *
    *   val defaultCredentials = DefaultCredentialsProvider.create()
    *   val s3Resource: Resource[Task, SecretsManager] = S3.create(defaultCredentials, Region.AWS_GLOBAL)
    * }}}
    *
    * @param credentialsProvider strategy for loading credentials and authenticate to AWS S3
    * @param region an Amazon Web Services region that hosts a set of Amazon services.
    * @param endpoint the endpoint with which the SDK should communicate.
    * @param httpClient sets the [[SdkAsyncHttpClient]] that the SDK service client will use to make HTTP calls.
    * @return a [[Resource]] of [[Task]] that allocates and releases [[SecretsManager]].
    **/
  def create(
    credentialsProvider: AwsCredentialsProvider,
    region: Region,
    endpoint: Option[String] = None,
    httpClient: Option[SdkAsyncHttpClient] = None): Resource[Task, SecretsManager] = {
    Resource.make {
      Task.eval {
        val asyncClient = AsyncClientConversions.from(credentialsProvider, region, endpoint, httpClient)
        createUnsafe(asyncClient)
      }
    } { _.close }
  }

  /**
    * Creates a instance of [[SecretsManager]] out of a [[S3AsyncClient]].
    *
    * It provides a fast forward access to the [[SecretsManager]] that avoids
    * dealing with [[Resource]].
    *
    * Unsafe because the state of the passed [[SecretsManagerAsyncClient]] is not guaranteed,
    * it can either be malformed or closed, which would result in underlying failures.
    *
    * @see [[SecretsManager.fromConfig]] and [[SecretsManager.create]] for a pure usage of [[SecretsManager]].
    * They both will make sure that the s3 connection is created with the required
    * resources and guarantee that the client was not previously closed.
    *
    * ==Example==
    *
    * {{{
    *   import java.time.Duration
    *   import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
    *   import software.amazon.awssdk.regions.Region.AWS_GLOBAL
    *   import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient
    *   import software.amazon.awssdk.services.s3.S3AsyncClient
    *
    *   // the exceptions related with concurrency or timeouts from any of the requests
    *   // might be solved by raising the `maxConcurrency`, `maxPendingConnectionAcquire` or
    *   // `connectionAcquisitionTimeout` from the underlying netty http async client.
    *   // see below an example on how to increase such values.
    *
    *   val httpClient = NettyNioAsyncHttpClient.builder()
    *     .maxConcurrency(500)
    *     .maxPendingConnectionAcquires(50000)
    *     .connectionAcquisitionTimeout(Duration.ofSeconds(60))
    *     .readTimeout(Duration.ofSeconds(60))
    *     .build()
    *
    *   val s3AsyncClient: S3AsyncClient = S3AsyncClient
    *     .builder()
    *     .httpClient(httpClient)
    *     .credentialsProvider(DefaultCredentialsProvider.create())
    *     .region(AWS_GLOBAL)
    *     .build()
    *
    *     val s3: S3 = S3.createUnsafe(s3AsyncClient)
    * }}}
    *
    * @param s3AsyncClient an instance of a [[S3AsyncClient]].
    * @return An instance of [[SecretsManager]]
    */
  @UnsafeBecauseImpure
  def createUnsafe(secretsManagerAsyncClient: SecretsManagerAsyncClient): SecretsManager = {
    new SecretsManager {
      override val secretsManagerClient: SecretsManagerAsyncClient = secretsManagerAsyncClient
    }
  }

  /**
    * Creates a new [[SecretsManager]] instance out of the the passed AWS configurations.
    *
    * It provides a fast forward access to the [[SecretsManager]] that avoids
    * dealing with [[Resource]], however in this case, the created
    * resources will not be released like in [[create]].
    * Thus, it is the user's responsability to close the [[SecretsManager]] connection.
    *
    * ==Example==
    *
    * {{{
    *   import monix.execution.Scheduler.Implicits.global
    *   import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
    *   import software.amazon.awssdk.regions.Region
    *
    *   val defaultCred = DefaultCredentialsProvider.create()
    *   val s3: S3 = S3.createUnsafe(defaultCred, Region.AWS_GLOBAL)
    *   // do your stuff here
    *   s3.close.runToFuture
    * }}}
    *
    * @param credentialsProvider Strategy for loading credentials and authenticate to AWS S3
    * @param region An Amazon Web Services region that hosts a set of Amazon services.
    * @param endpoint The endpoint with which the SDK should communicate.
    * @param httpClient Sets the [[SdkAsyncHttpClient]] that the SDK service client will use to make HTTP calls.
    * @return a [[Resource]] of [[Task]] that allocates and releases [[SecretsManager]].
    */
  @UnsafeBecauseImpure
  def createUnsafe(
    credentialsProvider: AwsCredentialsProvider,
    region: Region,
    endpoint: Option[String] = None,
    httpClient: Option[SdkAsyncHttpClient] = None): SecretsManager = {
    val secretsManagerAsyncClient = AsyncClientConversions.from(credentialsProvider, region, endpoint, httpClient)
    self.createUnsafe(secretsManagerAsyncClient)
  }

}

/**
  * Represents the Monix SecretsManager client which can
  * be created using the builders from its companion object.
  */
trait SecretsManager { self =>

  private[secretsmanager] val secretsManagerClient: SecretsManagerAsyncClient

  def getSecretValue(
    secretId: String,
    versionId: Option[String] = None,
    versionStage: Option[String] = None,
    overrideConfiguration: Option[AwsRequestOverrideConfiguration] = None): Task[GetSecretValueResponse] = {
    Task.from(
      secretsManagerClient.getSecretValue(
        SecretsManagerRequestBuilder.getSecretValue(secretId, versionId, versionStage, overrideConfiguration)))
  }

  def getSecretValue(request: GetSecretValueRequest): Task[GetSecretValueResponse] = ???

  def listSecrets(
    maxResults: Option[Int] = None,
    filters: Option[List[Filter]] = None,
    sortOrder: Option[SortOrderType] = None,
    nextToken: Option[String] = None,
    overrideConfiguration: Option[AwsRequestOverrideConfiguration] = None): Observable[SecretListEntry] = {
    for{
      listSecrets   <- ListSecretsObservable(maxResults,filters,sortOrder,nextToken,overrideConfiguration,secretsManagerClient)
      secretsObject <- Observable.fromIterable(listSecrets.secretList().asScala)
    } yield secretsObject
  }
  /**
    * Closes the underlying [[SecretsManagerAsyncClient]].
    */
  def close: Task[Unit] = Task.evalOnce(secretsManagerClient.close())

}
