package com.mm.sharesapp.actors

import akka.actor._
// TODO Find a better way to get classes
import FakeActorFactory._

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
  
  val destinationActor = system.actorOf(Props(classOf[FakePersistenceActor]) ,"PersistenceActor")      
  val rssActor = system.actorOf(Props(classOf[FakeRssActor], destinationActor), "RssActor")      
  val newsActor = system.actorOf(Props(classOf[FakeStaticNewsActor], rssActor), "NewsActor")
  val sharePriceActor = system.actorOf(Props(classOf[FakeSharePriceActor], destinationActor), "SharePriceActor")
  val shareRouterActor = system.actorOf(Props(classOf[RealSharesRouterActor], sharePriceActor, rssActor), "SharesRouterActor")
  
  val routees = Seq(shareRouterActor, newsActor)
  val routerActor  = system.actorOf(Props(classOf[FakeCakeRouterActor], routees) ,"CakeRouterActor")
  
  val masterActor = system.actorOf(Props(classOf[MasterActor], routerActor) ,"Master")  // master actor
  
  println("Share Runner.... Kicking off application")
  
  masterActor ! Start
  
  
  
  
  
  
    
  
  
  
    
  
}