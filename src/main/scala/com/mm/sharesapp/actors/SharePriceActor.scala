package com.mm.sharesapp.actors

import akka.actor._
import akka.actor.SupervisorStrategy.{ Resume, Escalate, Restart }
import akka.event.Logging
import com.mm.sharesapp.services.SharePriceComponent
import com.mm.sharesapp.entities.Share

/**
 * Fetches Share prices
 *  */
class SharePriceActor(destination: ActorRef) extends Actor with ActorLogging{
    sharePriceComponent:SharePriceComponent =>
      
      def receive = {
        case share:Share =>
          log.info(s"fetching prices for share ${share.ticker}....")
          val sharePrice = sharePriceService.downloadSharePrice(share.ticker)
          sharePrice match {
            case Some(price) => log.info(s"We got $price.Sending it thru");destination ! price
            case None        => log.info(s"Unable to fetch data for ${share.ticker}")
          }
          
        case message => log.info(s"SharePriceActorsUnexpected msg:$message")
    }

  }
