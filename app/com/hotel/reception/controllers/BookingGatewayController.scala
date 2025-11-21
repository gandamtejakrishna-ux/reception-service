package com.hotel.reception.controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.{ExecutionContext, Future}
import com.hotel.reception.services.BookingGatewayService
import play.api.Logging

@Singleton
class BookingGatewayController @Inject()(
                                          cc: ControllerComponents,
                                          bookingService: BookingGatewayService,
                                          secured: com.hotel.reception.actions.SecuredAction
                                        )(implicit ec: ExecutionContext)
  extends AbstractController(cc)
    with Logging {

  // CREATE BOOKING
  def createBooking = secured.async(parse.json) { req: Request[JsValue] =>
    bookingService.createBooking(req.body).map { wsResponse =>
      Status(wsResponse.status)(wsResponse.body)
        .as("application/json")
    }
  }

  def checkIn(id: String) = secured.async(parse.json) { req: Request[JsValue] =>
    bookingService.checkIn(id, req.body).map { wsResponse =>
      Status(wsResponse.status)(wsResponse.body)
        .as("application/json")
    }
  }

  
  // CHECK-OUT
  def checkOut(id: String) = secured.async(parse.json) { req: Request[JsValue] =>
    bookingService.checkOut(id, req.body).map { wsResponse =>
      Status(wsResponse.status)(wsResponse.body)
    }
  }


  
  // AVAILABILITY
  def getAvailability = secured.async { req =>
    val date = req.getQueryString("date").getOrElse("")
    val category = req.getQueryString("category").getOrElse("")

    bookingService.getAvailability(date, category).map { wsResponse =>
      Status(wsResponse.status)(wsResponse.body)
        .as("application/json")
    }
  }

  
  // GET BOOKING DETAILS
  def getBooking(id: String) = secured.async { _ =>
    bookingService.getBooking(id).map { wsResponse =>
      Status(wsResponse.status)(wsResponse.body)
        .as("application/json")
    }
  }

  // HEALTH
  def health = Action {
    Ok(Json.obj("status" -> "UP"))
  }
}
