package com.mm.sharesapp.actors

import akka.actor._
import akka.actor.SupervisorStrategy.{ Resume, Escalate, Restart }
import akka.event.Logging
import com.mm.sharesapp.persistence.PersistenceServiceComponent
import com.mm.sharesapp.entities._

class StaticNewsActor(rssActorRef:ActorRef) extends Actor  with ActorLogging{
  this:PersistenceServiceComponent =>
   
    def fetchAll = persistenceService.findAllRssFeed
    
    def receive = {

      case AllShares(_) =>
        log.info("Fetching Static Shares....." )
        val rss = fetchAll
        log.info("got:" +fetchAll.mkString("\n"))
        val allRssData = rss.map { rssFeed => RSSRequest(ticker=rssFeed.description, url=rssFeed.feedUrl) }
        log.info(s"We got ${allRssData.size} news")
        allRssData.foreach { rssRequest => rssActorRef ! rssRequest }
        
      case message => log.info(s"StaticNewsUnexpected msg:$message")
    }
    
}