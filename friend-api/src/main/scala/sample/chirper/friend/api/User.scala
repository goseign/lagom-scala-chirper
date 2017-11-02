package sample.chirper.friend.api

import play.api.libs.json.{Format, Json}

case class User(userId: String, name: String, friends: Seq[String])

object User {

  implicit val format: Format[User] = Json.format[User]

}

