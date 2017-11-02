package sample.chirper.friend.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

trait FriendService extends Service {

  def getUser(userId: String): ServiceCall[NotUsed, User]

  def createUser(): ServiceCall[User, Done]

  def addFriend(userId: String): ServiceCall[FriendId, Done]

  def getFollowers(userId: String): ServiceCall[NotUsed, Seq[String]]

  override def descriptor = {
    import Service._

    named("friendservice").withCalls(
      pathCall("/api/users/:userId", getUser _),
      namedCall("/api/users", createUser _),
      pathCall("/api/users/:userId/friends", addFriend _),
      pathCall("/api/users/:userId/followers", getFollowers _)
    ).withAutoAcl(true)

  }
}
