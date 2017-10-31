package sample.chirper.chirp.impl

import sample.chirper.chirp.api
import sample.chirper.chirp.api.{LagomscalachirperService}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRegistry}

/**
  * Implementation of the LagomscalachirperService.
  */
class LagomscalachirperServiceImpl(persistentEntityRegistry: PersistentEntityRegistry) extends LagomscalachirperService {

  override def hello(id: String) = ServiceCall { _ =>
    // Look up the lagom-scala-chirper entity for the given ID.
    val ref = persistentEntityRegistry.refFor[LagomscalachirperEntity](id)

    // Ask the entity the Hello command.
    ref.ask(Hello(id))
  }

  override def useGreeting(id: String) = ServiceCall { request =>
    // Look up the lagom-scala-chirper entity for the given ID.
    val ref = persistentEntityRegistry.refFor[LagomscalachirperEntity](id)

    // Tell the entity to use the greeting message specified.
    ref.ask(UseGreetingMessage(request.message))
  }


  override def greetingsTopic(): Topic[api.GreetingMessageChanged] =
    TopicProducer.singleStreamWithOffset {
      fromOffset =>
        persistentEntityRegistry.eventStream(LagomscalachirperEvent.Tag, fromOffset)
          .map(ev => (convertEvent(ev), ev.offset))
    }

  private def convertEvent(helloEvent: EventStreamElement[LagomscalachirperEvent]): api.GreetingMessageChanged = {
    helloEvent.event match {
      case GreetingMessageChanged(msg) => api.GreetingMessageChanged(helloEvent.entityId, msg)
    }
  }
}
