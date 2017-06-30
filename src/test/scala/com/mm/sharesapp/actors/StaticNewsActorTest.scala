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
import com.mm.sharesapp.entities.{RssFeed, Share}
import com.mm.sharesapp.persistence.PersistenceServiceComponent

class StaticNewsActorTest extends TestKit(ActorSystem("testSystem")) with ImplicitSender
    with FlatSpecLike {

  "A StaticNewsActorTest" should "process an AllShares and send multiple RssFeedData message" in {

    val url = "testUrl"
    val description = "testDesc"
    val token = "testToken"
    val property = "Property"
    val mockRssFeed = RssFeed(description, url, token, property)
    val rssActor = TestProbe()
    
    
    trait MockPersistenceServiceComponent extends PersistenceServiceComponent {
      // we only see the component here, not outside, So we need to find a way to test
      // the dependency
      val persistenceService = Mockito.mock(classOf[this.PersistenceService])
      when(persistenceService.findAllRssFeed).thenReturn(Seq(mockRssFeed))
      
    }
    
    class MockStaticNewsActor(rssActor:ActorRef) extends StaticNewsActor(rssActor) 
            with MockPersistenceServiceComponent

    val staticNewsActor = TestActorRef(
      new MockStaticNewsActor(rssActor.ref))

    val expectedMsg = RSSRequest(ticker=description, url=url)
      
    within(1000 millis) {
      staticNewsActor ! AllShares(data=Seq[Share]())
      rssActor.expectMsg(1000 millis, expectedMsg)
      
    }
  }

}
