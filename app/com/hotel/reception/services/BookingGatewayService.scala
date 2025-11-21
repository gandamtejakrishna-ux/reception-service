//package com.hotel.reception.services
//
//import javax.inject._
//import play.api.libs.ws._
//import play.api.libs.json._
//import scala.concurrent.{Future, ExecutionContext}
//import play.api.Configuration
//
//@Singleton
//class BookingGatewayService @Inject()(
//                                       ws: WSClient,
//                                       config: Configuration
//                                     )(implicit ec: ExecutionContext) {
//
//  private val baseUrl: String = config.get[String]("booking.service.url")
//
//  def createBooking(json: JsValue): Future[JsValue] =
//    ws.url(s"$baseUrl/internal/bookings")
//      .post(json)
//      .map(_.json)
//
//  def checkIn(id: String, json: JsValue): Future[JsValue] =
//    ws.url(s"$baseUrl/internal/bookings/$id/checkin")
//      .post(json)
//      .map(_.json)
//
//  def checkOut(id: String, json: JsValue): Future[JsValue] =
//    ws.url(s"$baseUrl/internal/bookings/$id/checkout")
//      .post(json)
//      .map(_.json)
//
//  def getAvailability(date: String, category: String): Future[JsValue] =
//    ws.url(s"$baseUrl/internal/rooms")
//      .addQueryStringParameters("date" -> date, "category" -> category)
//      .get()
//      .map(_.json)
//
//  def getBooking(id: String): Future[JsValue] =
//    ws.url(s"$baseUrl/internal/bookings/$id")
//      .get()
//      .map(_.json)
//}
package com.hotel.reception.services

import play.api.libs.ws._
import play.api.libs.json._
import play.api.Configuration
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import play.api.Logging

@Singleton
class BookingGatewayService @Inject()(
                                       ws: WSClient,
                                       config: Configuration
                                     )(implicit ec: ExecutionContext) extends Logging {

  private val bookingServiceUrl: String =
    config.get[String]("booking.service.url")

  logger.info(s"[Reception-Service] Booking Service URL: $bookingServiceUrl")

  // -----------------------------
  // CREATE BOOKING
  // -----------------------------
  def createBooking(json: JsValue): Future[WSResponse] = {
    ws.url(s"$bookingServiceUrl/internal/bookings")
      .addHttpHeaders("Content-Type" -> "application/json")
      .post(json)
  }

  // -----------------------------
  // CHECK-IN
  // -----------------------------
  def checkIn(id: String, json: JsValue): Future[WSResponse] = {
    ws.url(s"$bookingServiceUrl/internal/bookings/$id/checkin")
      .addHttpHeaders("Content-Type" -> "application/json")
      .post(json)
  }

  // -----------------------------
  // CHECK-OUT
  // -----------------------------
  def checkOut(id: String, json: JsValue): Future[WSResponse] = {
    ws.url(s"$bookingServiceUrl/internal/bookings/$id/checkout")
      .addHttpHeaders("Content-Type" -> "application/json")
      .post(json)
  }


  // -----------------------------
  // ROOM AVAILABILITY
  // -----------------------------
  def getAvailability(date: String, category: String): Future[WSResponse] = {
    ws.url(s"$bookingServiceUrl/internal/rooms/availability")
      .addQueryStringParameters(
        "date" -> date,
        "category" -> category
      )
      .get()
  }

  // -----------------------------
  // GET BOOKING DETAILS
  // -----------------------------
  def getBooking(id: String): Future[WSResponse] = {
    ws.url(s"$bookingServiceUrl/internal/bookings/$id")
      .get()
  }
}
