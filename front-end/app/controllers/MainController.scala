package controllers

import play.api.mvc.{AbstractController, ControllerComponents}

class MainController(components: ControllerComponents) extends AbstractController(components) {

  def index = Action {
    Ok(views.html.index.render())
  }

  def userStream(userId: String) = Action {
    Ok(views.html.index.render())
  }

  def circuitBreaker = Action {
    Ok(views.html.circuitbreaker.render())
  }

}
