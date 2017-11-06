package sample.chirper.chirp.impl

import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, AggregateEventTagger}
import sample.chirper.chirp.api.Chirp

sealed trait ChirpTimelineEvent extends AggregateEvent[ChirpTimelineEvent] {
  override def aggregateTag: AggregateEventTagger[ChirpTimelineEvent] = ChirpTimelineEvent.Tag
}

object ChirpTimelineEvent {
  val NumShards = 3
  val Tag = AggregateEventTag.sharded[ChirpTimelineEvent](NumShards)
}

case class ChirpAdded(chirp: Chirp) extends ChirpTimelineEvent
