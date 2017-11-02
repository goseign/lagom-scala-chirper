package sample.chirper.friend.impl

import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, AggregateEventTagger}

sealed trait FriendEvent extends AggregateEvent[FriendEvent] {
  override def aggregateTag: AggregateEventTagger[FriendEvent] = FriendEvent.Tag
}

object FriendEvent {
  val Tag = AggregateEventTag[FriendEvent]
}
