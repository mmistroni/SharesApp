package com.mm.sharesapp.actors

import akka.actor._
import akka.actor.SupervisorStrategy.{ Resume, Escalate, Restart }
import akka.event.Logging
import com.mm.sharesapp.persistence.ShareDbService


class RouterActor(routees: Seq[ActorRef]) extends Actor {
    shareDbService:ShareDbService =>
    val log = Logging(context.system, this)
    
    
    def fetchAll = shareDbService.findAll
    
    def receive = {

      case Start =>
        log.info("sending messages for all shares....")
        for (routee <- routees;
             msg  <- fetchAll) { routee ! msg}
      case message => log.info(s"Unexpected msg:$message")
    }

  }
