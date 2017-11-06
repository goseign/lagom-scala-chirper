package sample.chirper.chirp.impl

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import sample.chirper.chirp.api.ChirpService

import scala.concurrent.{ExecutionContext, Future}

class ChirpServiceImpl(
                        persistentEntities: PersistentEntityRegistry,
                        chirpTopic: ChirpTopic,
                        chirps: ChirpRepository
                      )(implicit ec: ExecutionContext) extends ChirpService {

  override def addChirp(userId: String) = ServiceCall { chirp =>
    if (userId != chirp.userId) throw new IllegalArgumentException(s"UserId $userId did not match userId in $chirp")
    persistentEntities.refFor[ChirpTimelineEntity](userId)
      .ask(AddChirp(chirp))
      .map(_ => NotUsed.getInstance())
  }

  override def getLiveChirps() = ServiceCall { request =>
    chirps.getRecentChirps(request.userIds).map { recentChirps =>
      val sources = for (userId <- request.userIds) yield chirpTopic.subscriber(userId)
      val users = request.userIds.distinct
      val publishedChirps = Source(sources.toList)
        .flatMapMerge(sources.size, identity)
        .filter(c => users.contains(c.userId))

      // We currently ignore the fact that it is possible to get duplicate chirps
      // from the recent and the topic. That can be solved with a de-duplication stage.
      Source(recentChirps.toList).concat(publishedChirps)
    }
  }

  override def getHistoricalChirps() = ServiceCall { request =>
    val timestamp = request.fromTime.toEpochMilli
    val result = chirps.getHistoricalChirps(request.userIds, timestamp)
    Future.successful(result)
  }

}
