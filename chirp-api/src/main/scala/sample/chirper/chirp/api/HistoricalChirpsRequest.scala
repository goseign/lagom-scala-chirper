package sample.chirper.chirp.api

import java.time.Instant

import play.api.libs.json.{Format, Json}

case class HistoricalChirpsRequest(fromTime: Instant, userIds: Seq[String])

object HistoricalChirpsRequest {
  implicit val format: Format[HistoricalChirpsRequest] = Json.format[HistoricalChirpsRequest]
}