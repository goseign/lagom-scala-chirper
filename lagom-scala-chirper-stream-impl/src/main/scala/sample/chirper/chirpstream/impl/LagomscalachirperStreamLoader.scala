package sample.chirper.chirpstream.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import sample.chirper.chirpstream.api.LagomscalachirperStreamService
import sample.chirper.chirp.api.LagomscalachirperService
import com.softwaremill.macwire._

class LagomscalachirperStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new LagomscalachirperStreamApplication(context) {
      override def serviceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new LagomscalachirperStreamApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[LagomscalachirperStreamService])
}

abstract class LagomscalachirperStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[LagomscalachirperStreamService](wire[LagomscalachirperStreamServiceImpl])

  // Bind the LagomscalachirperService client
  lazy val lagomscalachirperService = serviceClient.implement[LagomscalachirperService]
}
