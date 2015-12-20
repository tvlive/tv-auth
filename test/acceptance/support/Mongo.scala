package acceptance.support

import com.mongodb.casbah.Imports._
import org.bson.types.ObjectId
import org.joda.time.DateTime

trait Mongo {

  val mongoDB: MongoDB

  def createCollection(name: String): MongoCollection = mongoDB(name)

  def dropCollection(coll: MongoCollection) = coll.drop()

  def removeCollection(coll: MongoCollection) = coll.remove(MongoDBObject())

  def insert(coll: MongoCollection, c: DBObject) = coll.insert(c)
}

object Mongo {
  def apply(db: String) = new Mongo {
    override val mongoDB: MongoDB = MongoClient()(db)
  }
}


object AuthBuilder {
  def apply(username: String, token: String, dateExpired: DateTime) =
    MongoDBObject(
      "username" -> username,
      "token" -> token,
      "dateExpired" -> dateExpired.toDate,
      "_id" -> ObjectId.get()
    )
}

