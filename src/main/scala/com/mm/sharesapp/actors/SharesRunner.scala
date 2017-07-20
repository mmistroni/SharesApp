package com.mm.sharesapp.actors

import akka.actor._
import RealActorFactory._
import scala.concurrent.duration.Duration;
import java.util.concurrent.TimeUnit;
    

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
  
  val destinationActor = system.actorOf(Props(classOf[RealPersistenceActor]) ,"PersistenceActor")      
  val rssActor = system.actorOf(Props(classOf[RealRssActor], destinationActor), "RssActor")      
  val newsActor = system.actorOf(Props(classOf[RealStaticNewsActor], rssActor), "NewsActor")
  val sharePriceActor = system.actorOf(Props(classOf[RealSharePriceActor], destinationActor), "SharePriceActor")
  val shareRouterActor = system.actorOf(Props(classOf[RealSharesRouterActor], sharePriceActor, rssActor), "SharesRouterActor")
  
  val routees = Seq(shareRouterActor, newsActor)
  val routerActor  = system.actorOf(Props(classOf[RealCakeRouterActor], routees) ,"CakeRouterActor")
  
  val masterActor = system.actorOf(Props(classOf[MasterActor], routerActor) ,"Master")  // master actor
  
  println("Share Runner.... Kicking off application")
  
  
  // Timer
  // Creating Timer
  system.scheduler.scheduleOnce(
        Duration.create(15, TimeUnit.MINUTES), 
        masterActor, Shutdown)( system.dispatcher)
  
  masterActor ! Start
  
  
  
  
  
  
    
  
  
  
    
  
}