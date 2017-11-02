package sample.chirper.friend.api

import java.time.Instant

import play.api.libs.json.{Format, Json}

case class User(userId: String, name: String, friends: Seq[String] = Nil)

object User {
  implicit val format: Format[User] = Json.format[User]
}

case class FriendAdded(userId: String, friendId: String, timestamp: Instant = Instant.now())

object FriendAdded {
  implicit val format: Format[FriendAdded] = Json.format[FriendAdded]
}