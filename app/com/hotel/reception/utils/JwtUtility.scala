package com.hotel.reception.utils

import com.typesafe.config.ConfigFactory
import pdi.jwt._
import play.api.libs.json._

import java.time.Instant
import scala.util.{Failure, Success}

/**
 * Utility object for creating and validating JWT tokens.
 * Uses HS256 signing and expiry time from application.conf.
 */
object JwtUtility {

  private val config = ConfigFactory.load()
  private val secret = config.getString("jwt.secret")
  private val expirySeconds = config.getInt("jwt.expiry")

  /**
   * Creates a signed JWT token for the given username.
   *
   * @param username the authenticated user
   * @return JWT token as String
   */
  def createToken(username: String): String = {
    val now = Instant.now().getEpochSecond
    val exp = now + expirySeconds

    val claim = JwtClaim(
      content = Json.obj("user" -> username).toString,
      issuedAt = Some(now),
      expiration = Some(exp)
    )

    Jwt.encode(claim, secret, JwtAlgorithm.HS256)
  }

  /**
   * Validates and decodes a JWT token.
   *
   * @param token the incoming JWT string
   * @return Some(JSON payload) if valid, None if invalid
   */
  def validateToken(token: String): Option[JsValue] = {
    Jwt.decode(token, secret, Seq(JwtAlgorithm.HS256)) match {
      case Success(claim) =>
        Some(Json.parse(claim.content))
      case Failure(_) =>
        None
    }
  }
}
