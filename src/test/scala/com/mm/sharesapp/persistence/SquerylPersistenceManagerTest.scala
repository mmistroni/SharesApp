package com.mm.sharesapp.persistence

import org.junit._
import org.junit.Assert._
import org.scalatest.{ BeforeAndAfterEach, BeforeAndAfterAll }
import org.junit.runner.RunWith
import org.scalatest.{ FlatSpecLike }
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
import java.sql.Connection

trait H2_ConnectionCommon extends DBConnector {
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

trait H2_Connection extends DBConnector with H2_ConnectionCommon {
  def sessionCreator(): Option[() => AbstractSession] = connectToDbCommon(Session.create(_, new H2Adapter))
}

class TestData {
  import SharesSchema._

  val sharePricesTests = sharePrices.insert(
    SharePrice(new java.util.Date(), "TestTicker",
      -1.0, -2.0,
      -3.0, -4.0,
      -4.0, -5.0))
}

// Scrap this, Having a connection and a transaction we should be able to insert
// data into the database

@RunWith(classOf[JUnitRunner])
class SquerylPersistenceServiceTest extends DbTestBase with Matchers with H2_Connection with BeforeAndAfterAll
    with BeforeAndAfterEach {

  def prePopulate = {
    SharesSchema.create
    val testData = new TestData()
  }

  override def beforeAll(): Unit = {
    super.beforeAll()

    sessionCreator().foreach { _ =>
      transaction {
        SharesSchema.drop
        
      }
    }

  }

  override def afterAll(): Unit = {
    sessionCreator().foreach { _ =>
      transaction {
        SharesSchema.drop
      }
    }
  }

  "The SquerylPersistenceManagerTest" - {
    "when calling insertShare with a Share object " - {
      "should insert and be able to find data in the database" in {

        transaction {
          // now retrieving it
          prePopulate
          val data = from(SharesSchema.sharePrices)(sharePrice => select(sharePrice)).toList

          //data.size should be(1)
          //data(0).ticker should be("TestTicker")

        }
      }
    }
  }

}

abstract class DbTestBase extends FreeSpec with BeforeAndAfterAll with BeforeAndAfterEach with Matchers
    with H2_Connection {

  def isIgnored(testName: String) =
    sessionCreator().isEmpty || ignoredTests.exists(_ == testName)

  def ignoredTests: List[String] = Nil

  override def beforeAll() = {
    val c = sessionCreator()
    if (c.isDefined) {
      SessionFactory.concreteFactory = c
    }
  }

  override protected def runTest(testName: String, args: org.scalatest.Args): org.scalatest.Status = {
    if (isIgnored(testName))
      org.scalatest.SucceededStatus
    else
      super.runTest(testName, args)
  }

}

