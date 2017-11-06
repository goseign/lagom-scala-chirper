package sample.chirper.chirp.impl

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

class ChirpTimelineEntity(val topic: ChirpTopic) extends PersistentEntity {

  override type Command = ChirpTimelineCommand[_]
  override type Event = ChirpTimelineEvent
  override type State = NotUsed

  override def initialState = NotUsed.getInstance()

  override def behavior =
    Actions()
      .onCommand[AddChirp, Done] {
      case (AddChirp(chirp), ctx, state) =>
        val event = ChirpAdded(chirp)
        ctx.thenPersist(event) { _ =>
          ctx.reply(Done)
          topic.publish(chirp)
        }
    }
      .onEvent {
        case (ChirpAdded(chirp), state) =>
          println("onEvent ChirpAdded")
          state
      }

}

object ChirpTimelineSerializerRegistry extends JsonSerializerRegistry {
  override def serializers = List(
    JsonSerializer[AddChirp],
    JsonSerializer[ChirpAdded]
  )
}
