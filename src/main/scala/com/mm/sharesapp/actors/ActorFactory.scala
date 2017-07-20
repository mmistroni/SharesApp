package com.mm.sharesapp.actors

import com.mm.sharesapp.persistence.{SquerylPersistenceServiceComponent, FakePersistenceServiceComponent}
import com.mm.sharesapp.services.{RssServiceComponent, SharePriceComponent, DataDownloaderComponent}
import com.mm.sharesapp.util.SharesMapperComponent
import com.mm.sharesapp.services.SharePriceComponent
import akka.actor._


trait ActorFactorComponent {
  
  val actorFactory  = RealActorFactory
}


object RealActorFactory  {
  
  trait RealSharePriceComponent extends SharePriceComponent with DataDownloaderComponent
  trait RealPersistenceServiceComponent extends SquerylPersistenceServiceComponent
  class RealSharesRouterActor(sharePriceActor:ActorRef, newsActor:ActorRef) 
        extends SharesRouterActor(sharePriceActor, newsActor) with SharesMapperComponent
  // Fake actors
  class RealPersistenceActor extends PersistenceActor with RealPersistenceServiceComponent
  class RealRssActor(destinationActor:ActorRef) extends RssActor(destinationActor) with RssServiceComponent
  class RealStaticNewsActor(destinationActor:ActorRef) extends StaticNewsActor(destinationActor)
        with RealPersistenceServiceComponent
  class RealSharePriceActor(destinationActor:ActorRef) extends SharePriceActor(destinationActor) with  RealSharePriceComponent   
  class RealCakeRouterActor(routees:Seq[ActorRef]) extends CakeRouterActor(routees) 
        with RealPersistenceServiceComponent
  
  def shareRouterActor(sharePriceActor:ActorRef, newsActor:ActorRef):Props = 
          Props(new RealSharesRouterActor(sharePriceActor, newsActor))
  
  def persistenceActor = Props(new RealPersistenceActor())
  def staticNewsActor(destination:ActorRef) = Props(new RealStaticNewsActor(destination))
  def rssActor(destination:ActorRef) = Props(new RealRssActor(destination))
  def sharePriceActor(destination:ActorRef) = Props(new RealSharePriceActor(destination))
  def cakeRouterActor(routees:Seq[ActorRef]) = Props(new RealCakeRouterActor(routees))
  
}