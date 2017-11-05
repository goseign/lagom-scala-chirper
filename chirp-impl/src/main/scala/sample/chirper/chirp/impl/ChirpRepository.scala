package sample.chirper.chirp.impl

import sample.chirper.chirp.api.Chirp

import scala.concurrent.Future

/**
  * Provides access to past chirps. See {@link ChirpTopic} for real-time access to new chirps.
  */
trait ChirpRepository {

  def getHistoricalChirps(userIds: Seq[String], timestamp: Long)

  def getRecentChirps(userIds: Seq[String]): Future[Seq[Chirp]]

}
