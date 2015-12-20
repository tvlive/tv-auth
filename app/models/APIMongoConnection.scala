package models

import reactivemongo.api.collections.default.BSONCollection

class APIMongoConnection(uri: String, databaseName: String) {

  import reactivemongo.api._

  import scala.concurrent.ExecutionContext.Implicits.global

  val driver = new MongoDriver
  val connection = driver.connection(List(uri))
  val db = connection(databaseName)
  lazy val collectionName: String = ???
  lazy val collection: BSONCollection = db(collectionName)
}



