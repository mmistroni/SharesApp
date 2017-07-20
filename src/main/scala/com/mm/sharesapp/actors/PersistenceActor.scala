package com.mm.sharesapp.actors

import akka.actor._
import akka.actor.SupervisorStrategy.{ Resume, Escalate, Restart }
import akka.event.Logging
import com.mm.sharesapp.persistence.PersistenceServiceComponent
import com.mm.sharesapp.entities.{ SharePrice, NewsItem, TradingNotes, RssFeedData }

/**
 * Fetches RSS daa
 */
class PersistenceActor extends Actor with ActorLogging {
  this: PersistenceServiceComponent =>

  def receive = {

    case rssFeedData: RssFeedData => {
      log.info("Storing rss.. for:" + rssFeedData.description)
      persistenceService.insertRssFeedData(rssFeedData)
    }

    case sharePrice: SharePrice => {
      persistenceService.insertSharePrice(sharePrice)
    }

    case note: TradingNotes => {
      persistenceService.insertTradingNotes(note)
    }

    case message => log.info(s"PersistenceActor.Unexpected msg:$message")
  }

}


