package com.mm.sharesapp.services

import com.mm.sharesapp.entities.SharePrice
import scala.util.{Try, Success, Failure}
import com.mm.sharesapp.util.Utilities._


/**
 * Trait for a SharePriceComponent which uses a DataDownloader
 * to fetch data
 * 
 * 
 */

trait SharePriceService {

  def downloadSharePrice(ticker:String):Option[SharePrice]
    
  def downloadHistoricalPrices(ticker:String):Seq[SharePrice]
}  

trait SharePriceComponent  {
  this:DataDownloaderComponent =>
  
  val sharePriceService:SharePriceService = new ConcreteSharePriceService()  
    
    
  class ConcreteSharePriceService extends SharePriceService with com.mm.sharesapp.util.LogHelper{
  
    val sharesUrl = "http://download.finance.yahoo.com/d/quotes.csv?s=<ticker>&f=sl1d1e7e8m4m3r5"
    val historicalSharesUrl = "...."
    
    def downloadSharePrice(ticker:String):Option[SharePrice] = {
      val url  = sharesUrl.replace("<ticker>", ticker)
      logger.info("Downloadign data from url:" + url)
      val head = dataDownloader.downloadCSV(url).head
      SharePriceConverter.convertToSharePrice(head).toOption
      
    }
    
    def downloadHistoricalPrices(ticker:String):Seq[SharePrice] = {
      val url  = sharesUrl.replace("<ticker", ticker)
      logger.debug(s"Loading historical prices")
      val allPrices = dataDownloader.downloadCSV(url)
      SharePriceConverter.convertToHistoricalPrices(allPrices) match {
        case Success(lines) => lines
        case Failure(ex) => List[SharePrice]()
      }
    }
    
  }
  
  private object SharePriceConverter {
    private val format = new java.text.SimpleDateFormat("MM/dd/yyyy")
    def convertToSharePrice(dataList:List[String]):Try[SharePrice] = {
      Try {
        
        SharePrice(
            format.parse(dataList(2)), // asOfDate
            dataList(0),    //ticker
            getDouble(dataList(1)),   // price
            getDouble(dataList(7)), // peg
            getDouble(dataList(3)),  //currenteps
            getDouble(dataList(4)),  //forwardeps
            getDouble(dataList(5)),  //morvingav
            getDouble(dataList(6)))  //movingAvg50      
      }
    }
    
    def convertToHistoricalPrices(lines:List[List[String]]):Try[List[SharePrice]] = null
    
  }
  
}

trait FakeSharePriceComponent extends SharePriceComponent with DataDownloaderComponent {
  
  override val sharePriceService = new FakeSharePriceService()  
    
    
  class FakeSharePriceService extends SharePriceService with com.mm.sharesapp.util.LogHelper{
  
    val sharesUrl = "....."
    val historicalSharesUrl = "...."
    
    def downloadSharePrice(ticker:String):Option[SharePrice] = {
      Some(SharePrice(asOfDate= new java.util.Date(), 
                                    ticker="test",
                                    latestPrice=1.0,
                                    peg = 2.0,
                                    eps=3.0, 
                                    fwdEps=4.0,
                                    mvAvg=5.0, 
                                    mvAvg50=6.0))
    }
    
    def downloadHistoricalPrices(ticker:String):Seq[SharePrice] = {
      val templateSharePrice = SharePrice(asOfDate= new java.util.Date(), 
                                    ticker="test",
                                    latestPrice=1.0,
                                    peg = 2.0,
                                    eps=3.0, 
                                    fwdEps=4.0,
                                    mvAvg=5.0, 
                                    mvAvg50=6.0)
      val anotherPrice = templateSharePrice.copy(latestPrice=3.0)
       
      Seq(templateSharePrice, anotherPrice)
    }
    
    
  }
  
  private object SharePriceConverter {
    
    def convertToSharePrice(lines:Iterator[String]):Try[SharePrice] = null
    
    def convertToHistoricalPrices(lines:List[List[String]]):Try[List[SharePrice]] = null
    
  }
  
}

