package sample.chirper.chirp.impl

import akka.stream.testkit.scaladsl.TestSink
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}
import sample.chirper.chirp.api.{Chirp, ChirpService, LiveChirpRequest}

import scala.concurrent.Await
import scala.concurrent.duration._

class ChirpServiceSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  val server = ServiceTest.startServer(ServiceTest.defaultSetup.withCassandra(true)) { ctx =>
    new ChirpApplication(ctx) with LocalServiceLocator
  }

  val chirpService = server.serviceClient.implement[ChirpService]

  override protected def afterAll() = server.stop()

  "Chirp service" should {

    "publish chirps to subscribers" in {
      val request = LiveChirpRequest(List("usr1", "usr2"))

      val chirps1 = Await.result(chirpService.getLiveChirps().invoke(request), 3 seconds)
      val probe1 = chirps1.runWith(TestSink.probe(server.actorSystem))(server.materializer)
      probe1.request(10)

      val chirps2 = Await.result(chirpService.getLiveChirps().invoke(request), 3 seconds)
      val probe2 = chirps2.runWith(TestSink.probe(server.actorSystem))(server.materializer)
      probe2.request(10)

      for (i <- 1 to 3) {
        val chirp = Chirp(s"usr$i", s"hello $i")
        Await.result(chirpService.addChirp(s"usr$i").invoke(chirp), 3 seconds)
        probe1.expectNext(chirp)
        probe2.expectNext(chirp)
      }

      probe1.cancel()
      probe2.cancel()

      // FIXME
      "1" should ===("1")

    }

  }

}
