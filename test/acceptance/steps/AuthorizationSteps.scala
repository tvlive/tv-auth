package acceptance.steps

import acceptance.support.{Http, _}
import cucumber.api.DataTable
import cucumber.api.scala.{EN, ScalaDsl}
import org.joda.time.format.DateTimeFormat
import org.scalatest.Matchers

import scala.collection.JavaConverters._

class AuthorizationSteps extends ScalaDsl with EN with Matchers with Http with Env {

  import Context._

  val dtf = DateTimeFormat.forPattern("dd/MM/yyyy hh:mm:ss")

  Given( """^The users with credentials to use the API:$""") { (users: DataTable) =>
    val credentials = users.asLists(classOf[String]).asScala.tail.head
    val username = credentials.get(0)
    val token = credentials.get(1)
    val dateExpired = credentials.get(2)

    authCollection.insert(
      AuthBuilder(
        username,
        token,
        dtf.parseDateTime(dateExpired)
      )
    )
  }

  When( """^I GET the resource '(.*)'$""") { (authorizationURL: String) =>
    val (status, body) = get(s"$host$authorizationURL")
    world = world += ("httpStatus" -> status)
  }

  Then( """^the HTTP response is '(.*)'$""") { (httpStatus: String) =>
    statusCode.get(httpStatus) shouldBe world.get("httpStatus")
  }

  When("""^I POST the resource '(.*)' with:$"""){ (authorizationURL: String, crendential:DataTable) =>
    val cred = crendential.asLists(classOf[String]).asScala.tail.head
    val username = cred.get(0)
    val dateExpired = cred.get(1)
    def authorizationIn = s"""{"username":"$username","dateExpired":"$dateExpired"}"""

    val (status, body) = post(s"$host$authorizationURL", authorizationIn)
    world = world += ("httpStatus" -> status)
  }

  When("""^I POST the resource '(.*)'$"""){ (authorizationURL: String) =>
    val (status, body) = post(s"$host$authorizationURL")
    world = world += ("httpStatus" -> status)
  }

}