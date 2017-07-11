package com.mm.sharesapp.actors

import akka.actor._
import akka.actor.SupervisorStrategy.{ Resume, Escalate, Restart }
import akka.event.Logging


class MasterActor(router: ActorRef) extends Actor {
    
    val log = Logging(context.system.eventStream, this)
    def receive = {

      case Start =>
        log.info("Master.Starting system")
        router ! FetchShares
        
      case Shutdown =>
        log.info(" SHUTTING DOWN NOW........")
        context.system.shutdown()
        
        
      case message => log.info(s"Unexpected msg:$message")
    }

  }

object MasterActor {
  
  def props(router:ActorRef):Props = Props(new MasterActor(router))
}