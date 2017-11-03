package sample.chirper.chirp.api

import play.api.libs.json.{Format, Json}

case class LiveChirpRequest(userIds: Seq[String])

object LiveChirpRequest {
  implicit val format: Format[LiveChirpRequest] = Json.format[LiveChirpRequest]
}
