package sample.chirper.chirp.impl

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.ServiceCall
import sample.chirper.chirp.api.{Chirp, ChirpService}

import scala.collection.mutable
import scala.concurrent.Future

class ChirpServiceImpl extends ChirpService {

  val chirps = mutable.HashMap.empty[String, Seq[Chirp]]

  override def addChirp(userId: String) = ServiceCall { chirp =>
    chirps.get(userId).orElse {
      chirps.put(userId, List(chirp))
    }.map { existingChirps =>
      chirps.put(userId, existingChirps :+ chirp)
    }
    Future.successful(NotUsed.getInstance())
  }

  override def getLiveChirps() = ???

  override def getHistoricalChirps() = ???

}
