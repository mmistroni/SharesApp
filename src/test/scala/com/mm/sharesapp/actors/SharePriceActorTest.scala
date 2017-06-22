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
import com.mm.sharesapp.services.{SharePriceComponent, DataDownloaderComponent, SharePriceService}
import scala.util.Random
import com.mm.sharesapp.entities.{Share, SharePrice}
import com.mm.sharesapp.persistence.PersistenceServiceComponent

class SharePriceActorTest extends TestKit(ActorSystem("testSystem")) with ImplicitSender
    with FlatSpecLike {

  "A StaticNewsActorTest" should "process an AllShares and send multiple RssFeedData message" in {

    val testTicker = "testTicker"
    val priceMsg = Share(ticker=testTicker, name="",
                  price=Double.NaN, qty=Long.MaxValue,
                  portfolio=null, industry=null,
                  sector=null, cik="")

    val mockSharePrice = SharePrice(asOfDate= new org.joda.time.LocalDate(), 
                                    ticker="test",
                                    latestPrice=1.0,
                                    peg = 2.0,
                                    eps=3.0, 
                                    fwdEps=4.0,
                                    mvAvg=5.0, 
                                    mvAvg50=6.0)
                                      
    val destinationActor = TestProbe()
      
    trait MockSharePriceComponent extends SharePriceComponent with DataDownloaderComponent {
      // we only see the component here, not outside, So we need to find a way to test
      // the dependency
      override val sharePriceService = Mockito.mock(classOf[SharePriceService])
      when(sharePriceService.downloadSharePrice(testTicker)).thenReturn(Some(mockSharePrice))
    
    }
    
    
    class MockSharePriceActor(destination:ActorRef)
              extends SharePriceActor(destination) with MockSharePriceComponent
            
    // TODO instantiat this using Props..
    val sharePriceActor = TestActorRef(
      new MockSharePriceActor(destinationActor.ref))

    within(1000 millis) {
      sharePriceActor ! priceMsg
      destinationActor.expectMsg(1000 millis, mockSharePrice)
      
    }
  }

}
