package sample.chirper.chirp.impl

import akka.NotUsed
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}
import sample.chirper.chirp.api.{Chirp, ChirpService}

class ChirpServiceSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  val server = ServiceTest.startServer(ServiceTest.defaultSetup.withCassandra(true)) { ctx =>
    new ChirpApplication(ctx) with LocalServiceLocator
  }

  val client = server.serviceClient.implement[ChirpService]

  override protected def afterAll() = server.stop()

  "Chirp service" should {

    "add chirp" in {
      client.addChirp("alice").invoke(Chirp("alice", "Hello")).map { result =>
        result should ===(NotUsed.getInstance())
      }
    }

  }

}
