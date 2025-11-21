package com.hotel.reception.controllers

import javax.inject._
import play.api.mvc._

@Singleton
class UIController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index = Action { implicit request =>
    Ok(views.html.index())
  }
  def checkInPage = Action { implicit request =>
    Ok(views.html.checkin())
  }

  def checkOutPage = Action { implicit request =>
    Ok(views.html.checkout())
  }

  def availabilityPage = Action { implicit request =>
    Ok(views.html.availability())
  }


}
