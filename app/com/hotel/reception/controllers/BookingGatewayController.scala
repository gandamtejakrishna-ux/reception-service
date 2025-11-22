package com.hotel.reception.controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.{ExecutionContext, Future}
import com.hotel.reception.services.BookingGatewayService
import play.api.Logging

/**
 * Controller for the Reception Service.
 *
 * Acts as the entry point for all guest-facing operations such as:
 *  - Creating bookings
 *  - Checking in and checking out
 *  - Checking room availability
 *  - Fetching booking details
 *
 * Every endpoint is secured with JWT (SecuredAction).
 * This controller does NOT contain business logic — it forwards requests
 * to the Booking Service using BookingGatewayService.
 */
@Singleton
class BookingGatewayController @Inject()(
                                          cc: ControllerComponents,
                                          bookingService: BookingGatewayService,
                                          secured: com.hotel.reception.actions.SecuredAction
                                        )(implicit ec: ExecutionContext)
  extends AbstractController(cc)
    with Logging {

  /**
   * POST /api/bookings
   *
   * Creates a new booking by forwarding the JSON request
   * to the Booking Service.
   */
  def createBooking = secured.async(parse.json) { req: Request[JsValue] =>
    bookingService.createBooking(req.body).map { wsResponse =>
      Status(wsResponse.status)(wsResponse.body)
        .as("application/json")
    }
  }

  /**
   * POST /api/bookings/:id/checkin
   *
   * Sends check-in data (including ID proof URL) to Booking Service.
   */
  def checkIn(id: String) = secured.async(parse.json) { req: Request[JsValue] =>
    bookingService.checkIn(id, req.body).map { wsResponse =>
      Status(wsResponse.status)(wsResponse.body)
        .as("application/json")
    }
  }


  /**
   * POST /api/bookings/:id/checkout
   *
   * Forwards checkout information to Booking Service.
   */
  def checkOut(id: String) = secured.async(parse.json) { req: Request[JsValue] =>
    bookingService.checkOut(id, req.body).map { wsResponse =>
      Status(wsResponse.status)(wsResponse.body)
    }
  }

  /**
   * GET /api/rooms/availability
   *
   * Queries Booking Service to return available rooms for a given
   * date and room category.
   */
  def getAvailability = secured.async { req =>
    val date = req.getQueryString("date").getOrElse("")
    val category = req.getQueryString("category").getOrElse("")

    bookingService.getAvailability(date, category).map { wsResponse =>
      Status(wsResponse.status)(wsResponse.body)
        .as("application/json")
    }
  }


  /**
   * GET /api/bookings/:id
   *
   * Fetches full booking details for the given booking ID.
   */
  def getBooking(id: String) = secured.async { _ =>
    bookingService.getBooking(id).map { wsResponse =>
      Status(wsResponse.status)(wsResponse.body)
        .as("application/json")
    }
  }

  /**
   * GET /api/health
   *
   * Basic health check endpoint.
   */
  def health = Action {
    Ok(Json.obj("status" -> "UP"))
  }
}
