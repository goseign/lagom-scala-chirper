package sample.chirper.friend.impl

import akka.Done
import akka.actor.ActorSystem
import akka.testkit.TestKit
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.InvalidCommandException
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.lagom.scaladsl.testkit.PersistentEntityTestDriver
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}
import sample.chirper.friend.api.User

class FriendEntitySpec extends WordSpec with Matchers with BeforeAndAfterAll {

  val system = ActorSystem("FriendEntitySpec", JsonSerializerRegistry.actorSystemSetupFor(FriendSerializerRegistry))

  override protected def afterAll() = {
    TestKit.shutdownActorSystem(system)
  }

  def withTestDriver(test: PersistentEntityTestDriver[FriendCommand[_], FriendEvent, FriendState] => Unit): Unit = {
    val testDriver = new PersistentEntityTestDriver(system, new FriendEntity, "friend-1")
    test(testDriver)
    testDriver.getAllIssues should have size 0
  }

  "Friend entity" should {

    "not be initialized by default" in withTestDriver { driver =>
      val outcome = driver.run(GetUser())
      outcome.replies should contain only GetUserReply(None)
    }

    "create user" in withTestDriver { driver =>
      val alice = User("alice", "Alice")
      val outcome = driver.run(CreateUser(alice))
      outcome.replies should contain only Done
      outcome.events.size should ===(1)
      outcome.events.head should matchPattern { case UserCreated("alice", "Alice", _) => }
    }

    "reject duplicate create" in withTestDriver { driver =>
      val alice = User("alice", "Alice")
      driver.run(CreateUser(alice))
      val outcome = driver.run(CreateUser(alice))
      outcome.replies should contain only InvalidCommandException("User Alice is already created")
    }

    "create user with initial friends" in withTestDriver { driver =>
      val alice = User("alice", "Alice", List("bob", "peter"))
      val outcome = driver.run(CreateUser(alice))
      outcome.replies should contain only Done
      outcome.events.size should ===(3)
    }

  }

}
