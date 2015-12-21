package configuration

import models.{AuthorizationRepository, APIMongoConnection}
import play.api.Logger._
import play.api.Play._


trait ApplicationContext {

  val mongodbURI: String
  val mongodbDatabaseName: String
  implicit val conn: String => APIMongoConnection
  val authorizationRepository: AuthorizationRepository
}

object ApplicationContext extends ApplicationContext with RunMode  {
  info(s"loading the environment with run mode $env")
  val mongodbURI : String = configuration.getString(s"$env.mongodbURI").getOrElse(throw new IllegalArgumentException("mongodbURI is not defined"))
  val mongodbDatabaseName : String = configuration.getString(s"$env.mongodbDatabaseName").getOrElse(throw new IllegalArgumentException("mongodbDatabaseName is not defined"))

  implicit val conn: String => APIMongoConnection = {
    cn => new APIMongoConnection(mongodbURI, mongodbDatabaseName) {
      override lazy val collectionName = cn
    }
  }

  val authorizationRepository = new AuthorizationRepository("authorization")
}
