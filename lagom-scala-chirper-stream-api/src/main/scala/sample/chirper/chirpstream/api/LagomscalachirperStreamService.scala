package sample.chirper.chirpstream.api

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
  * The lagom-scala-chirper stream interface.
  *
  * This describes everything that Lagom needs to know about how to serve and
  * consume the LagomscalachirperStream service.
  */
trait LagomscalachirperStreamService extends Service {

  def stream: ServiceCall[Source[String, NotUsed], Source[String, NotUsed]]

  override final def descriptor = {
    import Service._

    named("lagom-scala-chirper-stream")
      .withCalls(
        namedCall("stream", stream)
      ).withAutoAcl(true)
  }
}

