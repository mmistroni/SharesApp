package com.mm.sharesapp.actors

import akka.actor._

import com.mm.sharesapp.persistence.FakePersistenceServiceComponent
import com.mm.sharesapp.services.{RssServiceComponent, SharePriceComponent, DataDownloaderComponent}
import com.mm.sharesapp.util.SharesMapperComponent
import com.mm.sharesapp.services.SharePriceComponent

object SharesRunner extends App {
  
  /**
   * Flow
   * Master ==> Router(shares, static)  ===> SharesRouter (s
   * 															      ===>SharePriceActor  ==> PersistenceActor
   *                                    ===>NewsActor        ==> PersistenceActor
   *                    
   *                    ===> StaticNewsActor
   *                                    ===>PersistenceActor
   *
   *                   
   */
  
  
  val system = ActorSystem("Shares-App-System")
  
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
  
  
  val destinationActor = system.actorOf(Props(classOf[FakePersistenceActor]))      
  val rssActor = system.actorOf(Props(classOf[FakeRssActor], destinationActor))      
  val persistenceActor = system.actorOf(Props(classOf[FakePersistenceActor]))
  val newsActor = system.actorOf(Props(classOf[FakeStaticNewsActor], rssActor))
  val sharePriceActor = system.actorOf(Props(classOf[FakeSharePriceActor], destinationActor))
  val shareRouterActor = system.actorOf(Props(classOf[RealSharesRouterActor], sharePriceActor, rssActor))
  
  val routees = Seq(shareRouterActor, newsActor)
  val routerActor  = system.actorOf(Props(classOf[FakeCakeRouterActor], routees))
  
  val masterActor = system.actorOf(Props(classOf[MasterActor], routerActor))  // master actor
  
  println("Share Runner.... Kicking off application")
  
  masterActor ! Start
  
  
  
  
  
  
    
  
  
  
    
  
}