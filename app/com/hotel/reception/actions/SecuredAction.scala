package com.hotel.reception.actions

import javax.inject.Inject
import play.api.mvc._
import com.hotel.reception.utils.JwtUtility

import scala.concurrent.{ExecutionContext, Future}

class SecuredAction @Inject()(parser: BodyParsers.Default)(implicit ec: ExecutionContext)
  extends ActionBuilderImpl(parser) {

  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {

    val maybeToken = request.headers.get("Authorization").map(_.replace("Bearer ", ""))

    maybeToken match {
      case Some(token) =>
        JwtUtility.validateToken(token) match {
          case Some(_) => block(request)
          case None    => Future.successful(Results.Unauthorized("Invalid or expired token"))
        }
      case None =>
        Future.successful(Results.Unauthorized("Missing Authorization token"))
    }
  }
}

