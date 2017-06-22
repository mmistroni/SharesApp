package com.mm.sharesapp.services

import scala.io._
import scala.xml._
import java.util.ArrayList
import com.mm.sharesapp.entities.RssFeedData

import scala.util.{Try, Success, Failure}

trait RssServiceComponent {

  val rssService = new RssService()
  
  
  class RssService extends com.mm.sharesapp.util.LogHelper {

    private val YAHOO_URL =
      "http://finance.yahoo.com/rss/headline?s=%s"

    private val dateFormat = new java.text.SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z")

    def fetchRSS(url: String): Try[scala.xml.Elem] = 
      Try(XML.load(url))

    def fetchDataForCompany(ticker:String, url: String): Seq[RssFeedData] = {
      logger.info(s"Fetching data from URL $url")
      fetchRSS(url) match {
        case Success(xmlElem) => logger.info("Success, we got something..");buildRssData(ticker, xmlElem)
        case Failure(exception) => logger.info("Nohting returned...");Seq[RssFeedData]()
      }
    }

    def buildRssData(ticker: String, xml: scala.xml.Elem):Seq[RssFeedData] = {
      val mainTag: String = if ((xml \\ "item" length) > 0) "item" else "entry"
      val results = (xml \\ mainTag).flatMap {xmlRoot => createRssData(ticker, xmlRoot)}
      results.toSeq
    }
    
    private def createRssData(ticker:String, xmlRoot:scala.xml.Node):Option[RssFeedData]= {
      Try {
            RssFeedData(ticker,
              new java.sql.Timestamp(
                if ((xmlRoot \\ "pubDate").text.length > 0) 
                  dateFormat.parse((xmlRoot \\ "pubDate").text).getTime() else new java.util.Date().getTime()),
              (xmlRoot \\ "title").text,
              (xmlRoot \\ "content").text,
              (xmlRoot \\ "description").text,
              (xmlRoot \\ "link").text)
           }.toOption
    }
    

  }
}