package sample.chirper.chirp.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import sample.chirper.chirp.api.LagomscalachirperService
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.softwaremill.macwire._

class LagomscalachirperLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new LagomscalachirperApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new LagomscalachirperApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[LagomscalachirperService])
}

abstract class LagomscalachirperApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[LagomscalachirperService](wire[LagomscalachirperServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry = LagomscalachirperSerializerRegistry

  // Register the lagom-scala-chirper persistent entity
  persistentEntityRegistry.register(wire[LagomscalachirperEntity])
}
