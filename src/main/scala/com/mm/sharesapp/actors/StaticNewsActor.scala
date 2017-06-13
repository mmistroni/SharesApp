package com.mm.sharesapp.actors

import akka.actor._
import akka.actor.SupervisorStrategy.{ Resume, Escalate, Restart }
import akka.event.Logging
import com.mm.sharesapp.persistence.PersistenceServiceComponent
import com.mm.sharesapp.entities._

class StaticNewsActor(rssActorRef:ActorRef) extends Actor {
  this:PersistenceServiceComponent =>
   
    val log = Logging(context.system, this)
    
    def fetchAll = persistenceService.findAllRssFeed
    
    def receive = {

      case AllShares(_) =>
        log.info("Fetching Static Shares.....")
        fetchAll.map { rssFeed => RSSRequest(ticker=rssFeed.description, url=rssFeed.feedUrl) }
                .foreach { rssRequest => rssActorRef ! rssRequest }
        
      case message => log.info(s"Unexpected msg:$message")
    }
    
}