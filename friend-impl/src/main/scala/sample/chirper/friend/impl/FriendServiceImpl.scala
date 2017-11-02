package sample.chirper.friend.impl

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import sample.chirper.friend.api.{FriendId, FriendService, User}

import scala.collection.mutable
import scala.concurrent.Future

class FriendServiceImpl extends FriendService {

  val users = mutable.Map.empty[String, User]

  override def getUser(userId: String) = ServiceCall { _ =>
    Future.successful(users.getOrElse(userId, throw NotFound(userId)))
  }

  override def createUser(): ServiceCall[User, NotUsed] = ServiceCall { user =>
    users.put(user.userId, user)
    Future.successful(NotUsed.getInstance())
  }

  override def addFriend(userId: String): ServiceCall[FriendId, NotUsed] = ServiceCall { req =>
    val user = users.getOrElse(userId, throw NotFound(userId))
    val friend = users.getOrElse(req.friendId, throw NotFound(req.friendId))
    users.put(userId, user.copy(friends = user.friends :+ req.friendId))
    Future.successful(NotUsed.getInstance())
  }

  override def getFollowers(userId: String) = ServiceCall { req =>
    val user = users.getOrElse(userId, throw NotFound(userId))
    Future.successful(user.friends)
  }

}
