package com.mm.sharesapp.actors

import akka.actor._
import akka.actor.SupervisorStrategy.{ Resume, Escalate, Restart }
import akka.event.Logging
import com.mm.sharesapp.services.RssServiceComponent
import com.mm.sharesapp.entities.Share

/**
 * Fetches RSS daa
 */
class RssActor(destination: ActorRef) extends Actor {
    rssServiceComponent:RssServiceComponent =>
      
      val log = Logging(context.system, this)
    
    
      def receive = {

        case RSSRequest(ticker, url) =>
          log.info("fetching rss for share....")
          val rssData = rssService.fetchDataForCompany(ticker, url)
          rssData match {
            case Some(data) => destination ! data
            case None => log.info(s"Nothing returned for $url") 
          }
          
        case message => log.info(s"Unexpected msg:$message")
    }

  }
