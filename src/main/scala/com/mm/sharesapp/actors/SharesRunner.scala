package com.mm.sharesapp.actors

import akka.actor._
import scala.concurrent.duration.Duration;
import java.util.concurrent.TimeUnit;
    

object SharesRunner extends App with ActorFactory {
  
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
  
  val destinationActorRef = system.actorOf(Props(new RealPersistenceActor()) ,"PersistenceActor")      
  val rssActor = system.actorOf(Props(new RealRssActor(destinationActorRef)), "RssActor")      
  val newsActor = system.actorOf(Props(new RealStaticNewsActor(rssActor)), "NewsActor")
  val sharePriceActor = system.actorOf(Props(new RealSharePriceActor(destinationActorRef)), "SharePriceActor")
  val shareRouterActor = system.actorOf(Props(new RealSharesRouterActor(sharePriceActor, rssActor)), "SharesRouterActor")
  
  val routees = Seq(shareRouterActor, newsActor)
  val routerActor  = system.actorOf(Props(new RealCakeRouterActor(routees)) ,"CakeRouterActor")
  
  val masterActor = system.actorOf(Props(new MasterActor(routerActor)) ,"Master")  // master actor
  
  println("Share Runner.... Kicking off application")
  
  
  // Timer
  // Creating Timer
  system.scheduler.scheduleOnce(
        Duration.create(15, TimeUnit.MINUTES), 
        masterActor, Shutdown)( system.dispatcher)
  
  masterActor ! Start
  
  
}