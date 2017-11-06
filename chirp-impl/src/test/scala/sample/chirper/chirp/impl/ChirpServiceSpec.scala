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

      val chirp1 = Chirp(s"usr1", s"hello 1")
      Await.result(chirpService.addChirp(s"usr1").invoke(chirp1), 3 seconds)
      probe1.expectNext(chirp1)
      probe2.expectNext(chirp1)

      val chirp2 = Chirp(s"usr1", s"hello 2")
      Await.result(chirpService.addChirp(s"usr1").invoke(chirp2), 3 seconds)
      probe1.expectNext(chirp2)
      probe2.expectNext(chirp2)

      val chirp3 = Chirp(s"usr2", s"hello 3")
      Await.result(chirpService.addChirp(s"usr2").invoke(chirp3), 3 seconds)
      probe1.expectNext(chirp3)
      probe2.expectNext(chirp3)

      probe1.cancel()
      probe2.cancel()

      // FIXME
      "1" should ===("1")

    }

    "should include some old chirps in live feed" in {

      val chirp1 = Chirp(s"usr3", s"hello 1")
      Await.result(chirpService.addChirp(s"usr3").invoke(chirp1), 3 seconds)

      val chirp2 = Chirp(s"usr4", s"hello 2")
      Await.result(chirpService.addChirp(s"usr4").invoke(chirp2), 3 seconds)

      val request = LiveChirpRequest(List("usr3", "usr4"))

      val chirps = Await.result(chirpService.getLiveChirps().invoke(request), 3 seconds)
      val probe = chirps.runWith(TestSink.probe(server.actorSystem))(server.materializer)
      probe.request(10)

//      val chirp1 = Chirp(s"usr3", s"hello 1")
//      Await.result(chirpService.addChirp(s"usr3").invoke(chirp1), 3 seconds)
//
//      val chirp2 = Chirp(s"usr4", s"hello 2")
//      Await.result(chirpService.addChirp(s"usr4").invoke(chirp2), 3 seconds)

      probe.expectNextUnordered(chirp1, chirp2)

      probe.cancel()

      // FIXME
      "1" should ===("1")

    }

  }

}





















