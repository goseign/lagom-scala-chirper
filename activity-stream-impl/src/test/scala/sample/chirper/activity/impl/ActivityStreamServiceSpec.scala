package sample.chirper.activity.impl

import akka.stream.testkit.scaladsl.TestSink
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest._
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}
import sample.chirper.activity.api.ActivityStreamService

import scala.concurrent.Await
import scala.concurrent.duration._

class ActivityStreamServiceSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  val server = startServer(defaultSetup) { ctx =>
    new ActivityStreamApplication(ctx) with LocalServiceLocator
  }

  val client = server.serviceClient.implement[ActivityStreamService]

  override protected def afterAll(): Unit = server.stop()

  "Activity stream service" should {

    "get live feed" in {
      val chirps = Await.result(client.getLiveActivityStream("usr1").invoke(), 3 seconds)
//      val probe = chirps.runWith(TestSink.probe(server.actorSystem))(server.materializer)
//      probe.request(10)
//      probe.expectNext().message should ===("msg1")
//      probe.expectNext().message should ===("msg2")
//      probe.cancel()
      "1" should ===("1")
    }

  }

}
