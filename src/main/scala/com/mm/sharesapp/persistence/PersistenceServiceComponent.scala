package com.mm.sharesapp.persistence
import com.mm.sharesapp.entities._
import sorm._
import org.squeryl.PrimitiveTypeMode._

trait PersistenceServiceComponent {
  // We need to inject DbComponent to expose the db instance.
  // We will do so in a subclass

  val persistenceService:PersistenceService
  
  
  trait PersistenceService {
    // Let's use a coarse-grained service for now as we dont have many CRUD operations
    
    def insertShare(item: Share): Boolean  // squeryl sorted

    def findAllShares: Seq[Share]          // squeryl sorted
    
    def findAllRssFeed:Seq[RssFeed]        // squeryl sorted
    
    def insertNews(item:NewsItem):Boolean  // not sorted
    
    def insertRssFeedData(rss:RssFeedData):Boolean // squeryl sorted
    
    def insertSharePrice(item:SharePrice):Boolean  // squeryl sorted
    
    def insertTradingNotes(note:TradingNotes):Boolean
    
    
  }
 
}


trait FakePersistenceServiceComponent extends PersistenceServiceComponent {
  // We need to inject DbComponent to expose the db instance.
  // We will do so in a subclass

  val persistenceService:PersistenceService = new FakePersistenceService()
  
  
  class FakePersistenceService extends PersistenceService {
    // Let's use a coarse-grained service for now as we dont have many CRUD operations
    
    def insertShare(item: Share): Boolean = true

    def findAllShares: Seq[Share] = {
      Seq(
          Share(ticker="GE", 
                name="General Electric",
                price=1.0, 
                qty = 2,
                portfolio="MAIN", 
                industry="N/A",
                sector="N/A", 
                cik="1110000")
           )
    }
    
    def findAllRssFeed:Seq[RssFeed] = {
      Seq(
          RssFeed(
              description="ECONOMICS-CHINA",
              feedUrl="https://www.tradingeconomics.com/china/rss",
              token=null, property=null)
          )
    }
    
    def insertNews(item:NewsItem):Boolean = false
    
    def insertRssFeedData(rss:RssFeedData):Boolean = false
    
    def insertSharePrice(item:SharePrice):Boolean  = false
    
    def insertTradingNotes(note:TradingNotes):Boolean = false
    
    
  }
 
}






