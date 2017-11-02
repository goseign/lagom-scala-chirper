package sample.chirper.friend.impl

import play.api.libs.json.{Format, Json}
import sample.chirper.friend.api.User

case class FriendState(user: Option[User])

object FriendState {
  implicit val format: Format[FriendState] = Json.format[FriendState]
}