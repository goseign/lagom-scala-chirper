package sample.chirper.chirp.api

import java.time.Instant
import java.util.UUID

import play.api.libs.json.Json

case class Chirp(userId: String, message: String, timestamp: Instant = Instant.now(), uuid: String = UUID.randomUUID().toString)

object Chirp {
  implicit val format = Json.format[Chirp]
}