package com.mm.sharesapp.actors

import com.mm.sharesapp.persistence.FakePersistenceServiceComponent
import com.mm.sharesapp.services.{RssServiceComponent, SharePriceComponent, DataDownloaderComponent}
import com.mm.sharesapp.util.SharesMapperComponent
import com.mm.sharesapp.services.SharePriceComponent
import akka.actor._


trait ActorFactorComponent {
  
  val actorFactory  = FakeActorFactory
}


object FakeActorFactory  {
  
  trait RealSharePriceComponent extends SharePriceComponent with DataDownloaderComponent
  class RealSharesRouterActor(sharePriceActor:ActorRef, newsActor:ActorRef) 
        extends SharesRouterActor(sharePriceActor, newsActor) with SharesMapperComponent
  // Fake actors
  class FakePersistenceActor extends PersistenceActor with FakePersistenceServiceComponent
  class FakeRssActor(destinationActor:ActorRef) extends RssActor(destinationActor) with RssServiceComponent
  class FakeStaticNewsActor(destinationActor:ActorRef) extends StaticNewsActor(destinationActor)
        with FakePersistenceServiceComponent
  class FakeSharePriceActor(destinationActor:ActorRef) extends SharePriceActor(destinationActor) with  RealSharePriceComponent   
  class FakeCakeRouterActor(routees:Seq[ActorRef]) extends CakeRouterActor(routees) with FakePersistenceServiceComponent
  
  def shareRouterActor(sharePriceActor:ActorRef, newsActor:ActorRef):Props = 
          Props(new RealSharesRouterActor(sharePriceActor, newsActor))
  
  def persistenceActor = Props(new FakePersistenceActor())
  def staticNewsActor(destination:ActorRef) = Props(new FakeStaticNewsActor(destination))
  def rssActor(destination:ActorRef) = Props(new FakeRssActor(destination))
  def sharePriceActor(destination:ActorRef) = Props(new FakeSharePriceActor(destination))
  def cakeRouterActor(routees:Seq[ActorRef]) = Props(new FakeCakeRouterActor(routees))
  
}