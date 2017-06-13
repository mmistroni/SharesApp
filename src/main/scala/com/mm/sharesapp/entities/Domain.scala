package com.mm.sharesapp.entities

/**
 * Abstract a MarketIndicator, used for evaluating how market is moving
 */
case class MarketIndicator(marketKey:String, 
                           valuationDate:org.joda.time.LocalDate,
                           value:Double)

/**
 * Abstract a NewsItem, representing news for companies and industries
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
 */
case class RssFeed(description:String, feedUrl:String,
                   token:String, property:String)


/**
 * Abstract  a Share
 */
case class Share(ticker:String, name:String,
                  price:Double, qty:Long,
                  portfolio:String, industry:String,
                  sector:String, cik:String)

/**
 * Abstract share price information for a share
 */
case class SharePrice(asOfDate:org.joda.time.LocalDate, ticker:String,
                      latestPrice:Double, peg:Double,
                      eps:Double, fwdEps:Double,
                      mvAvg:Double, mvAvg50:Double)

/**
 * Abstract trading notes, to document why a buy/sell action was done
 */
case class TradingNotes(noteId:Long, asOfDate:org.joda.time.LocalDate,
                        note:String)

/**
 * Abstract volatility of a share
 */
case class Volatility(asOfDate:org.joda.time.LocalDate, ticker:String,
                      volatility:Double, mean:Double)


/**
 * RssItem                      
 */
case class RssFeedData(ticker:String, 
							         date: java.sql.Timestamp,
	    					       title:String, 
	    					       val content:String,
	    					       val description:String,
	    					       val link : String)