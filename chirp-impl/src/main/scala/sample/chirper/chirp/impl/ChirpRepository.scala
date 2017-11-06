package sample.chirper.chirp.impl

import akka.NotUsed
import akka.stream.scaladsl.Source
import sample.chirper.chirp.api.Chirp

import scala.concurrent.Future

/**
  * Provides access to past chirps. See {@link ChirpTopic} for real-time access to new chirps.
  */
trait ChirpRepository {

  def getHistoricalChirps(userIds: Seq[String], timestamp: Long): Source[Chirp, NotUsed]

  def getRecentChirps(userIds: Seq[String]): Future[Seq[Chirp]]

}
