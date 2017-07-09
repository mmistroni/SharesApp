package com.mm.sharesapp.actors

import akka.actor._
import akka.actor.SupervisorStrategy.{ Resume, Escalate, Restart }
import akka.event.Logging
import com.mm.sharesapp.services.RssServiceComponent
import com.mm.sharesapp.entities.Share

/**
 * Fetches RSS daa
 */
class RssActor(destination: ActorRef) extends Actor with ActorLogging{
    rssServiceComponent:RssServiceComponent =>
      
    
      def receive = {

        case RSSRequest(ticker, url) =>
          log.debug("fetching rss for share....")
          val rssData = rssService.fetchDataForCompany(ticker, url)
          log.debug(s"We got ${rssData.size} news for $url") 
          rssData.foreach(rssMessage =>
              {
                log.debug("sending to persistence.");  
                destination ! rssMessage 
              })
          
        case message => log.info(s"RssActorUnexpected msg:$message")
    }

  }
