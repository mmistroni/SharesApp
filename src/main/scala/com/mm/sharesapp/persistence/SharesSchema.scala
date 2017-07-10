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
  
  val edgarNews = table[EdgarNews]("EDGAR_NEWS")
  val sharePrices = table[SharePrice]("SHARE_PRICE")
  val shares = table[Share]("SHARES")
  val rssFeedDatas = table[RssFeedData]("NEWS_ITEM")
  val rssFeeds = table[RssFeed]("RSS_FEED")
  
  override def drop = {
    println("dropping schema..")
    Session.cleanupResources
    super.drop
  }
  
}
