package sample.chirper.friend.impl

import com.lightbend.lagom.scaladsl.api.ServiceCall
import sample.chirper.friend.api.{FriendService, User}

import scala.concurrent.Future

class FriendServiceImpl extends FriendService {

  override def getUser(userId: String) = ServiceCall { _ =>
    Future.successful(User("bob", "bob", Nil))
  }

  override def createUser() = ???

  override def addFriend(userId: String) = ???

  override def getFollowers(userId: String) = ???

}
