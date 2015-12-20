package models

import org.joda.time.{DateTime, DateTimeZone}
import play.api.Logger
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.api.indexes.{Index, IndexType}
import reactivemongo.bson._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class Authorization(username: String, dateExpired: DateTime, token: String = BSONObjectID.generate.stringify, id: Option[BSONObjectID] = Some(BSONObjectID.generate))

object Authorization {

  implicit object AuthorizationBSONReader extends BSONDocumentReader[Authorization] {
    def read(doc: BSONDocument): Authorization = {
      Authorization(
        doc.getAs[BSONString]("username").get.value,
        new DateTime(doc.getAs[BSONDateTime]("dateExpired").get.value, DateTimeZone.forID("UTC")),
        doc.getAs[BSONString]("token").get.value,
        doc.getAs[BSONObjectID]("_id"))
    }
  }

  implicit object AuthorizationBSONWriter extends BSONDocumentWriter[Authorization] {
    override def write(a: Authorization): BSONDocument = {
      BSONDocument(
        "_id" -> a.id.getOrElse(BSONObjectID.generate),
        "username" -> a.username,
        "dateExpired" -> new BSONDateTime(a.dateExpired.getMillis),
        "token" -> a.token
      )
    }
  }

}

trait Repository {
  def findAuthorization(username: String, token: String, now: DateTime): Future[Option[Authorization]]

  def saveAuthorization(auth: Authorization): Future[Option[Authorization]]
}

class AuthorizationRepository(collectionName: String)(implicit val con: String => APIMongoConnection) extends Repository {
  private val collection: BSONCollection = con(collectionName).collection

  createIndex

  override def findAuthorization(username: String, token: String, now: DateTime = DateTime.now): Future[Option[Authorization]] = {
    val query = BSONDocument(
      "username" -> username,
      "token" -> token,
      "dateExpired" -> BSONDocument("$gte" -> BSONDateTime(now.getMillis)))

    collection.find(query).one[Authorization]
  }

  override def saveAuthorization(auth: Authorization): Future[Option[Authorization]] = {
    collection.insert(auth).map {
      res => if (res.ok) Some(auth) else None
    }
  }


  private def createIndex(): Future[Boolean] = {
    collection.indexesManager.ensure(
      Index(
        key = Seq("authorization.username" -> IndexType.Text),
        name = Some("search_auth"),
        unique = true
      )
    ).map {
      ok =>
        Logger.info(s"Index created for collection ${collectionName}: $ok")
        ok
    }.recover {
      case e =>
        Logger.error("Error creating index", e)
        false
    }
  }
}