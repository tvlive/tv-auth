import org.joda.time.DateTime
import play.api.libs.json.{Json, Writes, Reads, Format}

/**
 * Created by alvarovilaplana on 21/12/2015.
 */
package object controllers {
  val pattern = "dd/MM/yyyy hh:mm:ss"

  implicit val dateFormat = Format[DateTime](
    Reads.jodaDateReads(pattern),
    Writes.jodaDateWrites(pattern))

  implicit val authInFmt = Json.format[AuthorizationIn]
  implicit val authOutFmt = Json.format[AuthorizationOut]

}
