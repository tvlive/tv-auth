import org.joda.time.{DateTime, DateTimeZone}
import reactivemongo.bson._

package object models {

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
