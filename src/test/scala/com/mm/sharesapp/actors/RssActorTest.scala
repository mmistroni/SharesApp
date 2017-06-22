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
import com.mm.sharesapp.services.RssServiceComponent
import scala.util.Random
import com.mm.sharesapp.entities.RssFeedData
import sorm._

class RssActorTest extends TestKit(ActorSystem("testSystem")) with ImplicitSender
    with FlatSpecLike {

  "A RssActorTest" should "process an RssRequest and send a RssFeedData message" in {

    val url = "testUrl"
    val ticker = "testTicker"
    val testRssRequest = RSSRequest(ticker, url)
    val mockRssData = Mockito.mock(classOf[RssFeedData])
    val destinationActor = TestProbe()
    
    trait MockRssServiceComponent extends RssServiceComponent {
      // we only see the component here, not outside, So we need to find a way to test
      // the dependency
      override val rssService = Mockito.mock(classOf[this.RssService])
      when(rssService.fetchDataForCompany(ticker, url)).thenReturn(Seq(mockRssData))
    }
    
    class MockRssActor(destination:ActorRef) extends RssActor(destination) with MockRssServiceComponent

    val rssActor = TestActorRef(
      new MockRssActor(destinationActor.ref))

    within(1000 millis) {
      rssActor ! testRssRequest
      destinationActor.expectMsg(1000 millis, mockRssData)
      
    }
  }


  "A RssActorTest" should "process an RssRequest and not send anything" in {

    // mayb a  silly test
    val url = "testUrl"
    val ticker = "testTicker"
    val testRssRequest = RSSRequest(ticker, url)
    val destinationActor = TestProbe()
    
    trait MockRssServiceComponent extends RssServiceComponent {
      // we only see the component here, not outside, So we need to find a way to test
      // the dependency
      override val rssService = Mockito.mock(classOf[this.RssService])
      when(rssService.fetchDataForCompany(ticker, url)).thenReturn(Seq[RssFeedData]())
    }
    
    class MockRssActor(destination:ActorRef) extends RssActor(destination) with MockRssServiceComponent

    val rssActor = TestActorRef(
      new MockRssActor(destinationActor.ref))

    rssActor ! testRssRequest
    destinationActor.expectNoMsg()
    
  }




}
