package sample.chirper.chirpstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import sample.chirper.chirpstream.api.LagomscalachirperStreamService
import sample.chirper.chirp.api.LagomscalachirperService

import scala.concurrent.Future

/**
  * Implementation of the LagomscalachirperStreamService.
  */
class LagomscalachirperStreamServiceImpl(lagomscalachirperService: LagomscalachirperService) extends LagomscalachirperStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(lagomscalachirperService.hello(_).invoke()))
  }
}
