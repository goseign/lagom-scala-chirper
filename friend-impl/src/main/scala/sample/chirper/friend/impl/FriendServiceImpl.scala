package sample.chirper.friend.impl

import akka.Done
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRef, PersistentEntityRegistry}
import sample.chirper.friend.api.{FriendId, FriendService, User}

import scala.concurrent.ExecutionContext

class FriendServiceImpl(
                         persistentEntities: PersistentEntityRegistry,
                         db: CassandraSession
                       )(implicit ec: ExecutionContext) extends FriendService {

  override def getUser(userId: String) = ServiceCall { _ =>
    friendEntityRef(userId).ask(GetUser())
      .map(_.user.getOrElse(throw NotFound(s"user $userId not found")))
  }

  override def createUser(): ServiceCall[User, Done] = ServiceCall { request =>
    friendEntityRef(request.userId).ask(CreateUser(request))
  }

  override def addFriend(userId: String): ServiceCall[FriendId, Done] = ServiceCall { request =>
    friendEntityRef(userId).ask(AddFriend(request.friendId))
  }

  override def getFollowers(userId: String) = ServiceCall { _ =>
    db.selectAll("SELECT * FROM follower WHERE userId = ?", userId).map { rows =>
      rows.map(_.getString("followedBy"))
    }
  }

  private def friendEntityRef(userId: String): PersistentEntityRef[FriendCommand[_]] =
    persistentEntities.refFor[FriendEntity](userId)

  override def friendsTopic() = TopicProducer.singleStreamWithOffset { fromOffset =>
    persistentEntities.eventStream(FriendEvent.Tag, fromOffset).map { event =>
      (convertEvent(event), event.offset)
    }

  }

  private def convertEvent(friendEvent: EventStreamElement[FriendEvent]) = {
    friendEvent.event match {
      case FriendAdded(userId, friendId, timestamp) => sample.chirper.friend.api.FriendAdded(userId, friendId, timestamp)
      case _ => sample.chirper.friend.api.FriendAdded("", "") // no other events supported yet
    }
  }
}
