package sample.chirper.friend.impl

import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}
import sample.chirper.friend.api.{FriendId, FriendService, User}

class FriendServiceSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  val server = ServiceTest.startServer(ServiceTest.defaultSetup.withCassandra(true)) { ctx =>
    new FriendApplication(ctx) with LocalServiceLocator
  }

  val client = server.serviceClient.implement[FriendService]

  override protected def afterAll() = server.stop()

  "Friend service" should {

    "be able to create users and connect friends" in {
      val usr1 = User("usr1", "User 1")
      val usr2 = User("usr2", "User 2")
      val usr3 = User("usr3", "User 3")
      for {
        _ <- client.createUser().invoke(usr1)
        _ <- client.createUser().invoke(usr2)
        _ <- client.createUser().invoke(usr3)
        _ <- client.addFriend("usr1").invoke(FriendId("usr2"))
        _ <- client.addFriend("usr1").invoke(FriendId("usr3"))
        fetchedUsr1 <- client.getUser("usr1").invoke()
      } yield {
        fetchedUsr1 should matchPattern { case User(usr1.userId, usr1.name, _) => }
        fetchedUsr1.friends should ===(List("usr2", "usr3"))
      }
    }

    "create and get user" in {
      val bob = User("bob", "Bob")
      for {
        _ <- client.createUser().invoke(bob)
        bobResp <- client.getUser(bob.userId).invoke()
      } yield {
        bobResp should ===(bob)
      }
    }

    "add friend" in {
      val alice = User("alice", "Alice")
      val bob = User("bob", "Bob")
      for {
        _ <- client.createUser().invoke(alice)
        _ <- client.createUser().invoke(bob)
        _ <- client.addFriend(alice.userId).invoke(FriendId(bob.userId))
        aliceResp <- client.getUser(alice.userId).invoke()
      } yield {
        aliceResp.friends.head should ===(bob.userId)
      }
    }

    "get user's followers" in {
      val alice = User("alice", "Alice")
      val bob = User("bob", "Bob")
      val carl = User("carl", "Carl")
      for {
        _ <- client.createUser().invoke(alice)
        _ <- client.createUser().invoke(bob)
        _ <- client.createUser().invoke(carl)
        _ <- client.addFriend(alice.userId).invoke(FriendId(bob.userId))
        _ <- client.addFriend(alice.userId).invoke(FriendId(carl.userId))
        aliceResp <- client.getUser(alice.userId).invoke()
      } yield {
        aliceResp.friends should ===(List(bob.userId, carl.userId))
      }
    }

    "handle user not found" in {
      val fUser = client.getUser("unknown-user").invoke()
      ScalaFutures.whenReady(fUser.failed) { e =>
        e shouldBe a[NotFound]
      }
    }

  }

}
