package com.mm.sharesapp.actors

import org.junit._
import org.junit.Assert._
import akka.actor.ActorSystem
import akka.actor.{ ActorRef, Props, Terminated }
import akka.actor.Actor
import akka.actor.Props
import akka.testkit.{ TestKit, TestActorRef, ImplicitSender, TestProbe }
import scala.concurrent.duration._
import scala.concurrent.Await
import akka.pattern.ask
import akka.util.Timeout
import org.junit.runner.RunWith
import org.scalatest.{FlatSpecLike}

class MasterActorTest extends TestKit(ActorSystem("testSystem")) with ImplicitSender
    with FlatSpecLike {
      
  
  "A TestProbeTest" should "test TestProbes" in {
    val downloaderProbe = TestProbe() 
    val routerProbe = TestProbe()
    val retriever = TestActorRef(new MasterActor(routerProbe.ref))
        
    within(1000 millis) {
      retriever ! Start
      routerProbe.expectMsg(1000 millis, FetchShares)
      
    }    
  }
}
