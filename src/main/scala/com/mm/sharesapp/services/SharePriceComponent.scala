package com.mm.sharesapp.services

import com.mm.sharesapp.entities.{SharePrice, StockData}
import scala.util.{ Try, Success, Failure }
import com.mm.sharesapp.util.Utilities._
import java.util.Calendar
import scala.io.Source
import org.json4s._
import org.json4s.native.JsonMethods._

/**
 * Trait for a SharePriceComponent which uses a DataDownloader
 * to fetch data
 *
 *
 */

trait SharePriceService extends JsonDataDownloader{

  def downloadSharePrice(ticker: String): Option[SharePrice]

  def downloadHistoricalPrices(ticker: String): Seq[SharePrice]
  
}

trait SharePriceComponent {
  this: DataDownloaderComponent =>

  val sharePriceService: SharePriceService = new ConcreteSharePriceService()

  class ConcreteSharePriceService extends SharePriceService with com.mm.sharesapp.util.LogHelper {

    val sharesUrl = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=<ticker>&interval=1min&apikey=3K8QCCZYO839KMOU&datatype=csv"

    
    //"http://download.finance.yahoo.com/d/quotes.csv?s=<ticker>&f=sl1d1e7e8m4m3r5"
    val historicalSharesUrl = "...."

    def downloadSharePrice(ticker: String): Option[SharePrice] = {
      val url = sharesUrl.replace("<ticker>", ticker)
      logger.info("Downloadign data from url:" + url)
      val head = dataDownloader.downloadCSV(url).tail.head
      logger.info("head is:" + head)
      val res = SharePriceConverter.convertToSharePriceNoTicker(head, ticker).toOption
      logger.info("Returing:" + res.toString())
      res
    }

    def downloadHistoricalPrices(ticker: String): Seq[SharePrice] = {
      val url = sharesUrl.replace("<ticker", ticker)
      logger.debug(s"Loading historical prices")
      val allPrices = dataDownloader.downloadCSV(url)
      SharePriceConverter.convertToHistoricalPrices(allPrices) match {
        case Success(lines) => lines
        case Failure(ex)    => List[SharePrice]()
      }
    }

  }

  private object SharePriceConverter {
    private val format = new java.text.SimpleDateFormat("yyyy-MM-dd")

    def convertToSharePriceNoTicker(dataList: List[String], ticker: String): Try[SharePrice] = {
      def convertToDate(dateString: String): java.util.Date = {
        val date = format.parse(dateString)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        if (date.before(calendar.getTime())) {
          throw new IllegalArgumentException(s"Invalid date:$dateString")
        } else date

      }
      println("Converting:" + dataList.mkString(","))
      Try {

        SharePrice(
          convertToDate(dataList(0)), // asOfDate
          ticker, //ticker
          getDouble(dataList(4)), // price
          getDouble(dataList(1)), // peg
          getDouble(dataList(2)), //currenteps
          getDouble(dataList(3)), //forwardeps
          getDouble(dataList(5)), //morvingav
          getDouble(dataList(6))) //movingAvg50      
      }

    }

    def convertToSharePrice(dataList: List[String]): Try[SharePrice] = {
      def convertToDate(dateString: String): java.util.Date = {
        val date = format.parse(dateString)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        if (date.before(calendar.getTime())) {
          throw new IllegalArgumentException(s"Invalid date:$dateString")
        } else date

      }

      Try {

        SharePrice(
          convertToDate(dataList(2)), // asOfDate
          dataList(0), //ticker
          dataList(1).toDouble, // price
          getDouble(dataList(7)), // peg
          getDouble(dataList(3)), //currenteps
          getDouble(dataList(4)), //forwardeps
          getDouble(dataList(5)), //morvingav
          getDouble(dataList(6))) //movingAvg50      
      }
    }

    def convertToHistoricalPrices(lines: List[List[String]]): Try[List[SharePrice]] = null

  }

}

  

trait FakeSharePriceComponent extends SharePriceComponent with DataDownloaderComponent {

  override val sharePriceService = new FakeSharePriceService()

  class FakeSharePriceService extends SharePriceService with com.mm.sharesapp.util.LogHelper {

    val sharesUrl = "....."
    val historicalSharesUrl = "...."

    def downloadSharePrice(ticker: String): Option[SharePrice] = {
      Some(SharePrice(asOfDate = new java.util.Date(),
        ticker = "test",
        latestPrice = 1.0,
        peg = 2.0,
        eps = 3.0,
        fwdEps = 4.0,
        mvAvg = 5.0,
        mvAvg50 = 6.0))
    }

    def downloadHistoricalPrices(ticker: String): Seq[SharePrice] = {
      val templateSharePrice = SharePrice(asOfDate = new java.util.Date(),
        ticker = "test",
        latestPrice = 1.0,
        peg = 2.0,
        eps = 3.0,
        fwdEps = 4.0,
        mvAvg = 5.0,
        mvAvg50 = 6.0)
      val anotherPrice = templateSharePrice.copy(latestPrice = 3.0)

      Seq(templateSharePrice, anotherPrice)
    }

  }

  private object SharePriceConverter {

    def convertToSharePrice(lines: Iterator[String]): Try[SharePrice] = null

    def convertToHistoricalPrices(lines: List[List[String]]): Try[List[SharePrice]] = null

  }

}
trait JsonDataDownloader {
  import org.json4s._
  import org.json4s.native.JsonMethods._
  implicit val formats = DefaultFormats
    

  val sharePriceJsonUrl = "https://api.iextrading.com/1.0/stock/<ticker>/quote";
  
  def downloadFromJson(ticker: String):Option[StockData] = { 
    Try {
      val json =  parseToJValue(sharePriceJsonUrl.replace("<ticker", ticker))
      json.extract[StockData]
    }.toOption
  }
  
  private def parseToJValue(url:String):JValue = {
    parse(loadFromURL(url))
  }
    
  

  private def loadFromURL(url: String) = {
    Source.fromURL(url).mkString
  }

}

