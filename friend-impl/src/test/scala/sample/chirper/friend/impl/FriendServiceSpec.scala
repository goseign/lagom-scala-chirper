package sample.chirper.friend.impl

import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}
import sample.chirper.friend.api.{FriendService, User}

import scala.concurrent.Future

class FriendServiceSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  val server = ServiceTest.startServer(ServiceTest.defaultSetup) { ctx =>
    new FriendApplication(ctx) with LocalServiceLocator
  }

  val client = server.serviceClient.implement[FriendService]

  override protected def afterAll() = server.stop()

  "Friend service" should {

    "get user" in {
      val fUser: Future[User] = client.getUser("bob").invoke()
      fUser.map(user =>
        user.userId should ===("bob")
      )
    }

  }

}
