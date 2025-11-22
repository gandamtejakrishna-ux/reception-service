package com.hotel.reception.services

import play.api.libs.ws._
import play.api.libs.json._
import play.api.Configuration
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import play.api.Logging


/**
 * BookingGatewayService
 *
 * Acts as a client for the Booking Service.
 * Forwards all API calls from Reception Service → Booking Service (via REST).
 */
@Singleton
class BookingGatewayService @Inject()(
                                       ws: WSClient,
                                       config: Configuration
                                     )(implicit ec: ExecutionContext) extends Logging {

  private val bookingServiceUrl: String =
    config.get[String]("booking.service.url")

  logger.info(s"[Reception-Service] Booking Service URL: $bookingServiceUrl")

  /**
   * Creates a new booking.
   * @param json booking request payload
   * @return Booking Service HTTP response
   */
  def createBooking(json: JsValue): Future[WSResponse] = {
    ws.url(s"$bookingServiceUrl/internal/bookings")
      .addHttpHeaders("Content-Type" -> "application/json")
      .post(json)
  }

  /**
   * Sends check-in request for a booking.
   * @param id booking ID
   * @param json check-in details
   */
  def checkIn(id: String, json: JsValue): Future[WSResponse] = {
    ws.url(s"$bookingServiceUrl/internal/bookings/$id/checkin")
      .addHttpHeaders("Content-Type" -> "application/json")
      .post(json)
  }


  /**
   * Sends check-out request for a booking.
   * @param id booking ID
   * @param json check-out details
   */
  def checkOut(id: String, json: JsValue): Future[WSResponse] = {
    ws.url(s"$bookingServiceUrl/internal/bookings/$id/checkout")
      .addHttpHeaders("Content-Type" -> "application/json")
      .post(json)
  }



  /**
   * Fetches available rooms based on date + category.
   */
  def getAvailability(date: String, category: String): Future[WSResponse] = {
    ws.url(s"$bookingServiceUrl/internal/rooms/availability")
      .addQueryStringParameters(
        "date" -> date,
        "category" -> category
      )
      .get()
  }

  /**
   * Retrieves booking details for a given booking ID.
   */
  def getBooking(id: String): Future[WSResponse] = {
    ws.url(s"$bookingServiceUrl/internal/bookings/$id")
      .get()
  }
}
