package sample.chirper.chirp.impl

import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import sample.chirper.chirp.api.Chirp

import scala.concurrent.Future

class ChirpRepositoryImpl(
                           db: CassandraSession
                         ) extends ChirpRepository {

  private val NUM_RECENT_CHIRPS = 10
  private val SELECT_HISTORICAL_CHIRPS = "SELECT * FROM chirp WHERE userId = ? AND timestamp >= ? ORDER BY timestamp ASC"
  private val SELECT_RECENT_CHIRPS = "SELECT * FROM chirp WHERE userId = ? ORDER BY timestamp DESC LIMIT ?"

  // ???
  // private val pSequenceCollector = Collectors.collectingAndThen(Collectors.toList, TreePVector.from)

  override def getHistoricalChirps(userIds: Seq[String], timestamp: Long): Unit = ???

  override def getRecentChirps(userIds: Seq[String]): Future[Seq[Chirp]] = ???
}
