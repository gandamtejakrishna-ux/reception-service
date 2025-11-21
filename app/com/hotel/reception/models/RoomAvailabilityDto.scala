package com.hotel.reception.models

import play.api.libs.json._

case class RoomInfo(roomId: Int, floor: Int, category: String)
case class AvailabilityResponse(availableRooms: Seq[RoomInfo])

object RoomAvailabilityDto {
  implicit val roomInfoFmt = Json.format[RoomInfo]
  implicit val availabilityFmt = Json.format[AvailabilityResponse]
}
