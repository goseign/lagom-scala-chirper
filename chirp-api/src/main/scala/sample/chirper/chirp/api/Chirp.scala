package sample.chirper.chirp.api

import java.time.Instant
import java.util.UUID

import play.api.libs.json.{Format, Json}

case class Chirp(userId: String, message: String, timestamp: Instant = Instant.now(), uuid: String = UUID.randomUUID().toString)

object Chirp {
  implicit val format: Format[Chirp] = Json.format[Chirp]
}