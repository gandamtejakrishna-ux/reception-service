//package com.hotel.reception.controllers
//
//import javax.inject._
//import play.api.mvc._
//import play.api.libs.json._
//import com.hotel.reception.services.BookingGatewayService
//import scala.concurrent.{ExecutionContext, Future}
//
//@Singleton
//class BookingGatewayController @Inject()(
//                                          cc: ControllerComponents,
//                                          bookingService: BookingGatewayService
//                                        )(implicit ec: ExecutionContext)
//  extends AbstractController(cc) {
//
//  def createBooking = Action.async(parse.json) { req =>
//    bookingService.createBooking(req.body).map(Ok(_))
//  }
//
//  def checkIn(id: String) = Action.async(parse.json) { req =>
//    bookingService.checkIn(id, req.body).map(Ok(_))
//  }
//
//  def checkOut(id: String) = Action.async(parse.json) { req =>
//    bookingService.checkOut(id, req.body).map(Ok(_))
//  }
//
//  def getAvailability = Action.async { req =>
//    val date = req.getQueryString("date").getOrElse("")
//    val category = req.getQueryString("category").getOrElse("")
//    bookingService.getAvailability(date, category).map(Ok(_))
//  }
//
//  def getBooking(id: String) = Action.async {
//    bookingService.getBooking(id).map(Ok(_))
//  }
//
//  def health = Action {
//    Ok("Reception Service Running!")
//  }
//}
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

  // ---------------------------------------------------
  // CREATE BOOKING
  // ---------------------------------------------------
  def createBooking = secured.async(parse.json) { req: Request[JsValue] =>
    bookingService.createBooking(req.body).map { wsResponse =>
      Status(wsResponse.status)(wsResponse.body)
        .as("application/json")
    }
  }

  // ---------------------------------------------------
  // CHECK-IN
  // ---------------------------------------------------
//  | Step                        | Meaning                             |
//  | --------------------------- | ----------------------------------- |
//  | `Status(wsResponse.status)` | creates a function expecting a body |
//    | `(wsResponse.body)`         | passes the body to that function    |

//  makeCoffee(size)(milk)
//  Where:
//
//    first call selects size
//
//      second call adds milk type

  def checkIn(id: String) = secured.async(parse.json) { req: Request[JsValue] =>
    bookingService.checkIn(id, req.body).map { wsResponse =>
      Status(wsResponse.status)(wsResponse.body)
        .as("application/json")
    }
  }

  // ---------------------------------------------------
  // CHECK-OUT
  // ---------------------------------------------------
  def checkOut(id: String) = secured.async(parse.json) { req: Request[JsValue] =>
    bookingService.checkOut(id, req.body).map { wsResponse =>
      Status(wsResponse.status)(wsResponse.body)
    }
  }


  // ---------------------------------------------------
  // AVAILABILITY
  // ---------------------------------------------------
  def getAvailability = secured.async { req =>
    val date = req.getQueryString("date").getOrElse("")
    val category = req.getQueryString("category").getOrElse("")

    bookingService.getAvailability(date, category).map { wsResponse =>
      Status(wsResponse.status)(wsResponse.body)
        .as("application/json")
    }
  }

  // ---------------------------------------------------
  // GET BOOKING DETAILS
  // ---------------------------------------------------
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
