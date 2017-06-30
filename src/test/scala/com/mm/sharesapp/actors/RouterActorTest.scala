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
import org.scalatest.{ FlatSpecLike }
import org.mockito._
import org.mockito.Mockito._
import com.mm.sharesapp.persistence._
import scala.util.Random
import com.mm.sharesapp.entities.Share
import com.mm.sharesapp.util.MapperComponent

class RouterActorTest extends TestKit(ActorSystem("testSystem")) with ImplicitSender
    with FlatSpecLike {

  val mockShareService = Mockito.mock(classOf[ShareDbService])

  val random = new Random()
  def createMockShare(ticker: String): Share = {
    return Share(ticker, ticker,
      random.nextDouble(),
      random.nextLong(),
      "ptf", "industry",
      "sector", "cik")

  }

  "A RouterActorTest" should "send message to multple actors" in {
    val shareActor = TestProbe()
    val newsActor = TestProbe()
    val routees = Seq(shareActor, newsActor)
    val sampleShare = createMockShare("GE")
    val mockShares = Seq(sampleShare)

    val shareService = Mockito.mock(classOf[ShareDbService])

    val routerActor = TestActorRef(Props(classOf[RouterActor],
      Seq(shareActor.ref, newsActor.ref), shareService))

    when(shareService.findAll).thenReturn(mockShares)

    within(1000 millis) {
      routerActor ! Start
      shareActor.expectMsg(1000 millis, sampleShare)
      newsActor.expectMsg(1000 millis, sampleShare)
    }
  }

  "A CakeRouterActorTest" should "send message to multple actors" in {

    val shareActor = TestProbe()
    val newsActor = TestProbe()
    val routees = Seq(shareActor, newsActor)
    val sampleShare = createMockShare("GE")
    val mockShares = Seq(sampleShare)
    
    
    
    trait MockPersistenceServiceComponent extends PersistenceServiceComponent {
      // we only see the component here, not outside, So we need to find a way to test
      // the dependency
      val persistenceService = Mockito.mock(classOf[this.PersistenceService])
      when(persistenceService.findAllShares).thenReturn(mockShares)
      
    }
    class MockCakeRouterActor(routees: Seq[ActorRef])
      extends CakeRouterActor(routees) with MockPersistenceServiceComponent

    val expectedMsg = AllShares(data=mockShares)   
      
    val routerActor = TestActorRef(
      new MockCakeRouterActor(Seq(shareActor.ref, newsActor.ref)))

    

    within(1000 millis) {
      routerActor ! FetchShares
      shareActor.expectMsg(1000 millis, expectedMsg)
      newsActor.expectMsg(1000 millis, expectedMsg)
    }
  }

  "A ShareRouterActorTest" should "send message to multple newsActor and SharePriceActor" in {

    val testShareUrl = "u.go/myshares"
    val shareActor = TestProbe()
    val newsActor = TestProbe()
    val sampleShare = createMockShare("GE")
    val mockShares = Seq(sampleShare)
    
    
    trait MockMapperComponent extends MapperComponent {
      // we only see the component here, not outside, So we need to find a way to test
      // the dependency
      val mapper = Mockito.mock(classOf[this.Mapper])
      when(mapper.map(sampleShare.ticker)).thenReturn(Seq(testShareUrl))
      
    }
    
    
    class MockSharesRouterActor(shareActor:ActorRef,
                              newsActor:ActorRef)
      extends SharesRouterActor(shareActor, newsActor) with MockMapperComponent
    
    val expectedSharesMessage = sampleShare
    val expectedNewsMessage = RSSRequest(sampleShare.ticker, testShareUrl)
    
    val allSharesMsg = AllShares(data=mockShares)   
      
    val sharesRouterActor = TestActorRef(
      new MockSharesRouterActor(shareActor.ref, newsActor.ref))

    within(1000 millis) {
      sharesRouterActor ! allSharesMsg
      shareActor.expectMsg(1000 millis, expectedSharesMessage)
      newsActor.expectMsg(1000 millis, expectedNewsMessage)
    }
  }
  
  
  
  
  
}
