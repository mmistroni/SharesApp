package com.mm.sharesapp.persistence
import org.squeryl.PrimitiveTypeMode._
import SharesSchema._
import com.mm.sharesapp.entities._

trait SquerylPersistenceServiceComponent extends PersistenceServiceComponent {
  
  val persistenceService  = new SquerylPersistenceService()
  
  class SquerylPersistenceService extends PersistenceService with DBConnector{
    
    import SharesSchema._
    
    def insertShare(item: Share): Boolean = true

    def findAllShares2 = {
      withTransaction  {
        from(SharesSchema.shares)(share => select(share)).toList
      }
    }

    
    def findAllShares: Seq[Share] = {
      withTransaction   {
        val res = from(SharesSchema.shares)(share => select(share))
        res.map(s => s.copy()).toSeq
      }
    }
    
    def findAllRssFeed:Seq[RssFeed] = {
      transaction {
        val res = from(SharesSchema.rssFeeds)(rss => select(rss))
        res.map(s => s.copy()).toSeq
      }
    }
    
    def insertNews(item:NewsItem):Boolean = false
    
    def insertRssFeedData(rss:RssFeedData):Boolean = {
      transaction {
        rssFeedDatas.insert(rss) match {
          case s if s != null => true
          case _  => false
        }
        
      }
    }
    
    def insertSharePrice(item:SharePrice):Boolean  = {
      withTransaction {
        sharePrices.insert(item) match {
          case s if s != null => true
          case _  => false
        }
        
      }
    }
    
    def insertTradingNotes(note:TradingNotes):Boolean = false
    
    
  }
 
}
