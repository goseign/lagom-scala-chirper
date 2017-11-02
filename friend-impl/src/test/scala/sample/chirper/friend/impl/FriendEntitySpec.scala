package sample.chirper.friend.impl

import akka.actor.ActorSystem
import akka.testkit.TestKit
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.lagom.scaladsl.testkit.PersistentEntityTestDriver
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

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

  }

}
