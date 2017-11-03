package sample.chirper.friend.impl

import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.{ProducerStub, ProducerStubFactory, ServiceTest}
import org.scalatest.concurrent.{Eventually, ScalaFutures}
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{Matchers, WordSpec}
import sample.chirper.friend.api.{FriendService, FriendAdded => ApiFriendAdded}

class FriendEventSpec extends WordSpec with Matchers with Eventually with ScalaFutures {

  var producerStub: ProducerStub[ApiFriendAdded] = _

  "TODO" ignore {
    "publish updates on friends added" in
      ServiceTest.withServer(ServiceTest.defaultSetup) { ctx =>
        new FriendApplication(ctx) with LocalServiceLocator {

          // (1) creates an in-memory topic and binds it to a producer stub
          val stubFactory = new ProducerStubFactory(actorSystem, materializer)
          producerStub = stubFactory.producer[ApiFriendAdded](FriendService.TopicName)

          // (2) Override the default Friend service with our service stub which gets the producer stub injected
          // TODO!!!!!!!!!!!! override lazy val friendService = new FriendServiceStub(producerStub)
        }
      } { server =>

        // (3) produce a message in the stubbed topic via it's producer
        producerStub.send(ApiFriendAdded("userId", "friendId"))

        // create a service client to assert the message was consumed
        eventually(timeout(Span(5, Seconds))) {
          // cannot use async specs here because eventually only detects raised exceptions to retry.
          // if a future fail at the first time, eventually won't retry though future will succeed later.
          // see https://github.com/lagom/lagom/issues/876 for detail info.
          //                    val futureResp = server.serviceClient.implement[AnotherService].foo.invoke()
          //                    whenReady(futureResp) { resp =>
          //                      resp should ===("Hi there!")
          //                    }
        }
      }
  }
}

// (2) a Service stub that will use the in-memory topic bound to our producer stub
class FriendServiceStub(stub: ProducerStub[ApiFriendAdded]) extends FriendService {

  override def friendsTopic() = stub.topic

  override def getUser(userId: String) = ???

  override def createUser() = ???

  override def addFriend(userId: String) = ???

  override def getFollowers(userId: String) = ???

}