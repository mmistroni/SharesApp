package com.mm.sharesapp.persistence

import org.junit._
import org.junit.Assert._
import org.scalatest.{ BeforeAndAfterEach, BeforeAndAfterAll }
import org.junit.runner.RunWith
import org.scalatest.{ FlatSpecLike, FunSuite }
import org.mockito._
import org.mockito.Mockito._
import com.mm.sharesapp.services.{ SharePriceComponent, DataDownloaderComponent, SharePriceService }
import scala.util.Random
import SharesSchema._
import com.mm.sharesapp.entities._
import org.junit.runner.RunWith
import org.scalatest.FreeSpec
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner
import org.squeryl.{ Session, SessionFactory, AbstractSession }
import org.squeryl.adapters.{ MySQLAdapter, MySQLInnoDBAdapter }
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.adapters.H2Adapter
import org.squeryl.Schema
import org.squeryl.adapters.DerbyAdapter
import java.sql.Connection

/**
trait H2_ConnectionCommon extends BaseDBConnector {
  def connectToDbCommon(sessionFunc: Connection => AbstractSession): Option[() => AbstractSession] = {
    {
      Class.forName("org.h2.Driver")
      Some(() => {
        val c = java.sql.DriverManager.getConnection(
          "jdbc:h2:mem:test",
          "h2.user",
          "h2.password")
        sessionFunc(c)
      })
    }
  }
}

**/

trait DerbyConnectionCommon extends BaseDBConnector {
  def connectToDbCommon(sessionFunc: Connection => AbstractSession): Option[() => AbstractSession] = {
    {
      Class.forName("org.h2.Driver")
      Some(() => {
        val c = java.sql.DriverManager.getConnection(
          "jdbc:derby:memory:test;create=true",
          "app",
          "")
        c.setAutoCommit(false)
        sessionFunc(c)
      })
    }
  }
}


trait H2_Connection extends BaseDBConnector with DerbyConnectionCommon {
  def sessionCreator(): Option[() => AbstractSession] = connectToDbCommon(Session.create(_, new DerbyAdapter))
}

class TestData {
  import SharesSchema._

  val sharePricesTests = sharePrices.insert(
    SharePrice(new java.util.Date(), "TestTicker",
      -1.0, -2.0,
      -3.0, -4.0,
      -4.0, -5.0))
  
  val staticFeedsTests  = rssFeeds.insert(RssFeed(
                        description="ECONOMICS-UK",
                        feedUrl="https://www.tradingeconomics.com/uk/rss",
                        token=None, property=None)
                          )
  
}

class SquerylPersistenceServiceTest extends SchemaTester with QueryTester with RunTestsInsideTransaction 
        with Matchers  with BeforeAndAfterAll with H2_Connection {
  val schema = SharesSchema
  override def prePopulate = {
     println("Pre populating.............")
     insertData
  }

  def insertData = {
    val testData = new TestData()
  }
  
  
  test("Queries ") {
    lazy val data = from(SharesSchema.sharePrices)(sharePrice => select(sharePrice)).toList
    data.size should be(1)
    lazy val feeds = from(SharesSchema.rssFeeds)(feed => select(feed)).toList
    feeds.size should be(1)
    feeds(0).description should be("ECONOMICS-UK")
    feeds(0).feedUrl should be("https://www.tradingeconomics.com/uk/rss")
  }
  
  
  
}


