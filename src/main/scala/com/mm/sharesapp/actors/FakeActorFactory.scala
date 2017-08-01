package com.mm.sharesapp.actors

import com.mm.sharesapp.persistence.{SquerylPersistenceServiceComponent, FakePersistenceServiceComponent}
import com.mm.sharesapp.services.{RssServiceComponent, SharePriceComponent, DataDownloaderComponent}
import com.mm.sharesapp.util.SharesMapperComponent
import com.mm.sharesapp.services.SharePriceComponent
import akka.actor._


trait FakeActorFactory  {
  
  trait RealSharePriceComponent extends SharePriceComponent with DataDownloaderComponent
  
  class RealSharesRouterActor(sharePriceActor:ActorRef, newsActor:ActorRef) 
        extends SharesRouterActor(sharePriceActor, newsActor) with SharesMapperComponent
  // Fake actors
  class RealPersistenceActor extends PersistenceActor with FakePersistenceServiceComponent
  class RealRssActor(destinationActor:ActorRef) extends RssActor(destinationActor) with RssServiceComponent
  class RealStaticNewsActor(destinationActor:ActorRef) extends StaticNewsActor(destinationActor)
        with FakePersistenceServiceComponent
  class RealSharePriceActor(destinationActor:ActorRef) extends SharePriceActor(destinationActor) with  RealSharePriceComponent   
  class RealCakeRouterActor(routees:Seq[ActorRef]) extends CakeRouterActor(routees) 
        with FakePersistenceServiceComponent
  
  
}