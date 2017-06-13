package com.mm.sharesapp.actors

import akka.actor._
import akka.actor.SupervisorStrategy.{ Resume, Escalate, Restart }
import akka.event.Logging
import com.mm.sharesapp.services.SharePriceComponent
import com.mm.sharesapp.entities.Share

/**
 * Fetches RSS daa
 */
class SharePriceActor(destination: ActorRef) extends Actor {
    sharePriceComponent:SharePriceComponent =>
      
      val log = Logging(context.system, this)
      
      def receive = {
        case share:Share =>
          log.info("fetching rss for share....")
          val sharePrice = sharePriceService.downloadSharePrice(share.ticker)
          sharePrice match {
            case Some(price) => destination ! price
            case None        => log.info(s"Unable to fetch data for ${share.ticker}")
          }
          destination ! sharePrice
        case message => log.info(s"Unexpected msg:$message")
    }

  }
