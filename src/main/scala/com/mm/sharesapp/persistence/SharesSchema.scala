package com.mm.sharesapp.persistence
import org.squeryl.{Session, SessionFactory}
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Schema
import org.squeryl.annotations.Column
import java.util.Date
import java.sql.Timestamp
import org.squeryl.{Session, SessionFactory}
import org.squeryl.adapters.MySQLInnoDBAdapter
import com.mm.sharesapp.persistence.entities.EdgarNews
import com.mm.sharesapp.entities._

object SharesSchema extends Schema {
  
  val edgarNews = table[EdgarNews]("edgar_news")
  val sharePrices = table[SharePrice]("shareprice")
  val shares = table[Share]("shares")
  val rssFeedDatas = table[RssFeedData]("newsitem")
  val rssFeeds = table[RssFeed]("rssfeed")
  
}
