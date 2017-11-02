package sample.chirper.friend.impl

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import sample.chirper.friend.api.User

class FriendEntity extends PersistentEntity {

  override type Command = FriendCommand[_]
  override type Event = FriendEvent
  override type State = FriendState

  override def initialState = FriendState(None)

  override def behavior = {
    case FriendState(None) => notInitialized
    case FriendState(Some(user)) => initialized
  }

  val onGetUser = Actions().onReadOnlyCommand[GetUser, GetUserReply] {
    case (GetUser(), ctx, state) => ctx.reply(GetUserReply(state.user))
  }

  val notInitialized = {
    Actions().
      onCommand[CreateUser, Done] {
      case (CreateUser(user), ctx, state) =>
        val event = UserCreated(user.userId, user.name)
        ctx.thenPersist(event) { _ =>
          ctx.reply(Done)
        }
    }.
      onEvent {
        case (UserCreated(userId, name, timestamp), state) => FriendState(User(userId, name))
      }
  }.orElse(onGetUser)

  val initialized = {
    Actions().
      onCommand[CreateUser, Done] {
      case (CreateUser(user), ctx, state) =>
        ctx.invalidCommand(s"User ${user.name} is already created")
        ctx.done
    }
  }.orElse(onGetUser)

}

object FriendSerializerRegistry extends JsonSerializerRegistry {
  override def serializers = List(
    JsonSerializer[GetUser],
    JsonSerializer[GetUserReply],
    JsonSerializer[FriendState],
    JsonSerializer[CreateUser],
    JsonSerializer[UserCreated]
  )
}
