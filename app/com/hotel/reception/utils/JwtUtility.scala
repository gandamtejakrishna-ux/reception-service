package com.hotel.reception.utils

import com.typesafe.config.ConfigFactory
import pdi.jwt._
import play.api.libs.json._

import java.time.Instant
import scala.util.{Failure, Success}

object JwtUtility {

  private val config = ConfigFactory.load()
  private val secret = config.getString("jwt.secret")
  private val expirySeconds = config.getInt("jwt.expiry")

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

  def validateToken(token: String): Option[JsValue] = {
    Jwt.decode(token, secret, Seq(JwtAlgorithm.HS256)) match {
      case Success(claim) =>
        Some(Json.parse(claim.content))
      case Failure(_) =>
        None
    }
  }
}
