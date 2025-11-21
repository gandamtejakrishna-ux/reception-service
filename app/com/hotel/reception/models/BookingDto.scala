package com.hotel.reception.models

import play.api.libs.json._

case class GuestDto(
                     firstName: String,
                     lastName: String,
                     email: String,
                     phone: String
                   )

case class CreateBookingRequest(
                                 guest: GuestDto,
                                 category: String,
                                 checkInDate: String,
                                 checkOutDate: String
                               )

case class CreateBookingResponse(
                                  bookingId: String,
                                  status: String,
                                  message: String
                                )

object BookingDtos {
  implicit val guestFmt = Json.format[GuestDto]
  implicit val createBookingReqFmt = Json.format[CreateBookingRequest]
  implicit val createBookingRespFmt = Json.format[CreateBookingResponse]
}
