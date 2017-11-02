package sample.chirper.friend.impl

import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

class FriendEntity extends PersistentEntity{

  override type Command = FriendCommand[_]
  override type Event = FriendEvent
  override type State = FriendState

  override def initialState = FriendState(None)

  override def behavior = ???

}

object FriendSerializerRegistry extends JsonSerializerRegistry {
  override def serializers = List(
    JsonSerializer[GetUser]
  )
}

