package sample.chirper.chirp.impl

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.lightbend.lagom.scaladsl.pubsub.{PubSubRef, PubSubRegistry, TopicId}
import sample.chirper.chirp.api.Chirp

class ChirpTopicImpl(pubSubRegistry: PubSubRegistry) extends ChirpTopic {

  override def publish(chirp: Chirp): Unit = {
    refFor(chirp.userId).publish(chirp)
  }

  override def subscriber(userId: String): Source[Chirp, NotUsed] = {
    refFor(userId).subscriber
  }

  // Helpers -----------------------------------------------------------------------------------------------------------

  private def refFor(userId: String): PubSubRef[Chirp] = {
    val tmp: TopicId[Chirp] = TopicId(topicQualifier(userId))
    pubSubRegistry.refFor(tmp)
  }

  private def topicQualifier(userId: String): String = {
    String.valueOf(scala.math.abs(userId.hashCode) % ChirpTopicImpl.MaxTopics)
  }

}

object ChirpTopicImpl {
  val MaxTopics = 1024
}