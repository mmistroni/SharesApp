package com.mm.sharesapp.actors

import akka.actor._
import akka.actor.SupervisorStrategy.{ Resume, Escalate, Restart }
import akka.event.Logging
import com.mm.sharesapp.persistence.{PersistenceServiceComponent, ShareDbService}
import com.mm.sharesapp.entities._
import com.mm.sharesapp.util.MapperComponent


class RouterActor(routees: Seq[ActorRef],
    shareService:ShareDbService) extends Actor  {
    
    val log = Logging(context.system, this)
    
    def fetchAll = shareService.findAll
    
    def receive = {

      case Start =>
        log.info("sending messages for all shares....")
        for (routee <- routees;
             msg  <- fetchAll) { routee ! msg}
      case message => log.info(s"Unexpected msg:$message")
    }

  }

class CakeRouterActor(routees: Seq[ActorRef]) extends Actor with ActorLogging {
  this:PersistenceServiceComponent =>
    
    def fetchAll = persistenceService.findAllShares
    
    def receive = {

      case FetchShares =>
        log.info("sending messages for all shares....")
        val allShares = fetchAll
        for (routee <- routees) { routee ! AllShares(data=allShares)}
      case message => log.info(s"CakeRouterActorUnexpected msg:$message")
    }

  }

class SharesRouterActor(sharePriceActor: ActorRef,
                        rssActor:ActorRef) extends Actor  with ActorLogging{
    this:MapperComponent =>
    
    def _sendToPrices(shares:Seq[Share]) = { 
      log.info("sending sharePrice service for all shares....")
      shares.foreach { share => sharePriceActor ! share }
    }
    
    def _sendToNews(shares:Seq[Share]) = {
      val rssRequests = 
        shares.flatMap(share => 
                mapper.map(share.ticker).map(url => RSSRequest(share.ticker, url)))
      rssRequests.foreach(request => rssActor ! request)
    }
      
    def receive = {

      case AllShares(data) =>
        // TODO: clean distinction between a Share and a ShareMg?
        _sendToPrices(data)
        _sendToNews(data)
        
      case message => log.info(s"ShareRuoterActorUnexpected msg:$message")
    }

  }



