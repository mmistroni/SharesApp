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
import com.mm.sharesapp.entities.{RssFeed, SharePrice, TradingNotes, RssFeedData, NewsItem}
import com.mm.sharesapp.persistence.PersistenceServiceComponent

class PersistenceActorTest extends TestKit(ActorSystem("testSystem")) with ImplicitSender
    with FlatSpecLike {

  
    val mockTradingNote = Mockito.mock(classOf[TradingNotes])
    val mockSharePrice  = Mockito.mock(classOf[SharePrice])
    val mockRssFeedData   = Mockito.mock(classOf[RssFeedData])
    
    
    trait MockPersistenceServiceComponent extends PersistenceServiceComponent {
      // we only see the component here, not outside, So we need to find a way to test
      // the dependency
      val persistenceService = Mockito.mock(classOf[this.PersistenceService])
      when(persistenceService.insertTradingNotes(mockTradingNote)).thenReturn(true)
      when(persistenceService.insertSharePrice(mockSharePrice)).thenReturn(true)
      when(persistenceService.insertRssFeedData(mockRssFeedData)).thenReturn(true)
      
      
    }
  
  
  "A PersistenceActorTest" should "call PersistenceService to persist TradingNotes" in {

    class MockPersistenceActor extends PersistenceActor 
            with MockPersistenceServiceComponent

    val testPersistenceActor = TestActorRef(
      new MockPersistenceActor())

    val expectedMsg = mockTradingNote
      
    within(1000 millis) {
      testPersistenceActor ! expectedMsg
      
    }
    Mockito.verify(testPersistenceActor.underlyingActor.persistenceService, 
                   Mockito.times(1)).insertTradingNotes(mockTradingNote)
  }

  
  "A PersistenceActorTest" should "call PersistenceService to persist SharePrices" in {

    class MockPersistenceActor extends PersistenceActor 
            with MockPersistenceServiceComponent

    val testPersistenceActor = TestActorRef(
      new MockPersistenceActor())

    val expectedMsg = mockSharePrice
      
    within(1000 millis) {
      testPersistenceActor ! expectedMsg
      
    }
    Mockito.verify(testPersistenceActor.underlyingActor.persistenceService, 
                   Mockito.times(1)).insertSharePrice(mockSharePrice)
  }

  /**
   * suspended for the moment
  "A PersistenceActorTest" should "call PersistenceService to persist RssFeedData" in {

    class MockPersistenceActor extends PersistenceActor 
            with MockPersistenceServiceComponent

    val testPersistenceActor = TestActorRef(
      new MockPersistenceActor())

    val expectedMsg = mockRssFeedData
      
    within(1000 millis) {
      testPersistenceActor ! expectedMsg
      
    }
    Mockito.verify(testPersistenceActor.underlyingActor.persistenceService, 
                   Mockito.times(1)).insertRssFeedData(mockRssFeedData)
  }

  **/
}
