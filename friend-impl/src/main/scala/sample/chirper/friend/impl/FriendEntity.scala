package sample.chirper.friend.impl

import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

class FriendEntity extends PersistentEntity{

  override type Command = FriendCommand[_]
  override type Event = this.type
  override type State = this.type

  override def initialState = ???

  override def behavior = ???

}

object FriendSerializerRegistry extends JsonSerializerRegistry {
  override def serializers = List(
    JsonSerializer[GetUser]
  )
}