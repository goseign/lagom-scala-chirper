package sample.chirper.friend.impl

import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

class FriendEntity extends PersistentEntity{

  override type Command = FriendCommand[_]
  override type Event = FriendEvent
  override type State = FriendState

  override def initialState = FriendState(None)

  override def behavior = {
    case _ => notInitialized.orElse(getUser)
  }

  val notInitialized = Actions()

  val getUser = Actions().onReadOnlyCommand[GetUser, GetUserReply] {
    case (GetUser(), ctx, state) => ctx.reply(GetUserReply(state.user))
  }

}

object FriendSerializerRegistry extends JsonSerializerRegistry {
  override def serializers = List(
    JsonSerializer[GetUser],
    JsonSerializer[GetUserReply],
    JsonSerializer[FriendState]
  )
}

