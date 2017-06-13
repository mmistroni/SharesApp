package com.mm.sharesapp.services

import scala.io._
import scala.xml._
import java.util.ArrayList
import collection.mutable._
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

    def fetchDataForCompany(ticker:String, url: String): Option[RssFeedData] = {
      logger.info(s"Fetching data from URL $url")
      fetchRSS(url) match {
        case Success(xmlElem) => logger.info("Success, we got something..");Some(buildRssData(ticker, xmlElem))
        case Failure(exception) => logger.info("Nohting returned...");None
      }
    }

    def buildRssData(ticker: String, xml: scala.xml.Elem):RssFeedData = {
      val mainTag: String = if ((xml \\ "item" length) > 0) "item" else "entry"
      val results = (xml \\ mainTag).map { item =>
                                RssFeedData(ticker,
                                  new java.sql.Timestamp(
                                    if ((item \\ "pubDate").text.length > 0) dateFormat.parse((item \\ "pubDate").text).getTime() else new java.util.Date().getTime()),
                                  (item \\ "title").text,
                                  (item \\ "content").text,
                                  (item \\ "description").text,
                                  (item \\ "link").text)
                                  }
      results.head
    }

  }
}