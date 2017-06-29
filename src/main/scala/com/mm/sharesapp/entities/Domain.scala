package com.mm.sharesapp.entities
import org.squeryl.annotations.Column
import org.squeryl.KeyedEntity
import org.squeryl.dsl.CompositeKey2
import org.squeryl.PrimitiveTypeMode._ 
import scala.util.Random
import org.joda.time.LocalDate
import  java.util.Date


/**
 * Abstract a MarketIndicator, used for evaluating how market is moving
 */
case class MarketIndicator(marketKey:String, 
                           valuationDate:org.joda.time.LocalDate,
                           value:Double)

/**
 * Abstract a NewsItem, representing news for companies and industries
 * #Not needed. we will consider RssFeedData
 */
case class NewsItem(ticker:String, publishedDate:org.joda.time.LocalDate,
                    content:String, title:String, description:String,
                    link:String)
                           
/**
 * Abstact a portfolio                           
 */
case class Portfolio(portfolioId:String, createdBy:String,
                     creationDate:org.joda.time.LocalDate,
                     cash:Double)

/**
 * Abstract an object which represent an RSS Feed source  
 * # SQUERYL -SORTED                   
 */
case class RssFeed(description:String, 
                   @Column("FEED_URL")
                   feedUrl:String,
                   token:String, property:String)


/**
 * Abstract  a Share
 * # SQUERYL SORTED
 */
case class Share(ticker:String, name:String,
                  price:Double, qty:Long,
                  portfolio:String, industry:String,
                  sector:String, cik:String) {
  
  
  def this() = this("testTicker", "ShareName",
           new Random().nextDouble(), new Random().nextLong(),
          "test ptf", "test industry",
          "test sector", "test cik")

}

/**
 * Abstract share price information for a share
 * SQUERYL-SORTED
 */
case class SharePrice(val asOfDate: java.util.Date, val ticker:String,
                      val latestPrice:Double, val peg:Double,
                      val eps:Double, val fwdEps:Double,
                      val mvAvg:Double,val mvAvg50:Double) 
                extends KeyedEntity[CompositeKey2[java.util.Date, String]] {
  
  def id = compositeKey(asOfDate, ticker)
  
  def this() = this(new java.util.Date(), "SharePriceTicker" + new Random().nextInt(),
          new Random().nextDouble(), new Random().nextDouble(), 
          new Random().nextDouble(), new Random().nextDouble(),
          new Random().nextDouble(), new Random().nextDouble())
}

/**
 * Abstract trading notes, to document why a buy/sell action was done
 * # TO BE SORTED FOR AUTO-INCREMENT
 */
case class TradingNotes(noteId:Long, asOfDate:org.joda.time.LocalDate,
                        note:String)

/**
 * Abstract volatility of a share
 */
case class Volatility(asOfDate:org.joda.time.LocalDate, ticker:String,
                      volatility:Double, mean:Double)


/**
 * RssItem    # Table newsitem             
 * SQUERYL-SORTED      
 */
case class RssFeedData(ticker:String, 
                       @Column("PUBLISHED_DATE")
                       date: java.sql.Timestamp,
	    					       title:String, 
	    					       val content:String,
	    					       val description:String,
	    					       val link : String) extends KeyedEntity[CompositeKey2[String, java.sql.Timestamp]] {
  
  def id = compositeKey(ticker, date)
  
  def this() = this("testTicker", new java.sql.Timestamp(new java.util.Date().getTime()),
          "test titel", "test desc",
          "test link", "test content")
}
