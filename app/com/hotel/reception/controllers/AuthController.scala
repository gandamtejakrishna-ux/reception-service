package com.hotel.reception.controllers

import play.api.mvc._
import play.api.libs.json._
import javax.inject._
import com.hotel.reception.utils.JwtUtility

/**
 * Authentication controller for the Reception Service.
 *
 * Provides a login endpoint that validates credentials (from application.conf)
 * and returns a signed JWT token for authenticated access.
 */
@Singleton
class AuthController @Inject()(
                                cc: ControllerComponents,
                                config: play.api.Configuration
                              ) extends AbstractController(cc) {

  private val validUser = config.get[String]("auth.username")
  private val validPass = config.get[String]("auth.password")

  /**
   * POST /auth/login
   * Validates username/password and returns a JWT if correct.
   */
  def login = Action(parse.json) { req =>
    val username = (req.body \ "username").as[String]
    val password = (req.body \ "password").as[String]

    if (username == validUser && password == validPass) {
      val token = JwtUtility.createToken(username)
      Ok(Json.obj("token" -> token))
    } else {
      Unauthorized(Json.obj("error" -> "Invalid username or password"))
    }
  }
}

