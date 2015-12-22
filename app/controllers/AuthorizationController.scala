package controllers

import configuration.ApplicationContext
import models.{Authorization, AuthorizationRepository}
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.mvc.{Action, BodyParsers, Controller}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class AuthorizationIn(username: String, dateExpired: DateTime)

case class AuthorizationOut(username: String, dateExpired: DateTime, token: String)

trait AuthorizationController extends Controller {
  val repo: AuthorizationRepository

  def findAuthorization(username: String, token: String) = Action.async {
    repo.findAuthorization(username, token).map {
      case Some(auth) => Ok
      case None => NotFound
    }
  }

  def saveAuthorization = Action.async(BodyParsers.parse.json) { request =>
    request.body.validate[AuthorizationIn].fold(
      errors => Future.successful(BadRequest),
      authIn => {
        repo.saveAuthorization(Authorization(authIn.username, authIn.dateExpired)) map {
          case Some(auth) => Created(
            Json.toJson(AuthorizationOut(auth.username, auth.dateExpired, auth.token))
          )
          case None => Conflict
        }
      }
    )
  }
}

object AuthorizationController extends AuthorizationController {
  override val repo: AuthorizationRepository = ApplicationContext.authorizationRepository
}
