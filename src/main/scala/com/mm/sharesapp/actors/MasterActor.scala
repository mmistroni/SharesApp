package com.mm.sharesapp.actors

import akka.actor._
import akka.actor.SupervisorStrategy.{ Resume, Escalate, Restart }
import akka.event.Logging


class MasterActor(router: ActorRef) extends Actor {
    
    val log = Logging(context.system, this)
    def receive = {

      case Start =>
        log.info("Master.Starting system")
        router ! FetchShares
        
        
      case message => log.info(s"Unexpected msg:$message")
    }

  }
