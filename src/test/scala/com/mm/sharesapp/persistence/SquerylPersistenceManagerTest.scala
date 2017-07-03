package com.mm.sharesapp.persistence

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
import SharesSchema._
import com.mm.sharesapp.entities._

import org.junit.runner.RunWith
import org.scalatest.FreeSpec
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner

import org.squeryl.{Session, SessionFactory}
import org.squeryl.adapters.{MySQLAdapter,MySQLInnoDBAdapter}
import org.squeryl.PrimitiveTypeMode._

import org.squeryl.adapters.H2Adapter

import org.squeryl.{AbstractSession, Session}
import java.sql.Connection


trait H2_Connection  {
     Class.forName("org.h2.Driver")
      
      SessionFactory.concreteFactory = Some(() =>
      Session.create(
        java.sql.DriverManager.getConnection("jdbc:h2:mem:test",
          "h2.user", "h2.password"),
        new H2Adapter))
  
    }


/**
trait H2_Connection extends BaseDBConnector with H2_ConnectionCommon {
  def sessionCreator() : Option[() => AbstractSession] = connectToDbCommon(Session.create(_, ))
}
**/


class TestData{
  import SharesSchema._

  val sharePricesTests = sharePrices.insert(
      SharePrice( new java.util.Date(), "TestTicker",
                  -1.0, -2.0,
                  -3.0, -4.0,
                  -4.0, -5.0)) 
}

// Scrap this, Having a connection and a transaction we should be able to insert
// data into the database



@RunWith(classOf[JUnitRunner])
class SquerylPersistenceServiceTest extends FreeSpec with Matchers with H2_Connection {
                      
  "The SquerylPersistenceManagerTest" - {
    "when calling insertShare with a Share object " - {
      "should insert and be able to find data in the database" in {
       
       transaction {
         
         val testData =  new TestData()
         
         // now retrieving it
         val data = from(SharesSchema.sharePrices)(sharePrice => select(sharePrice)).toList
         
         data.size should be (1)
         data(0).ticker should be ("TestTicker")
         
       }
      }   
    }
  }

}
