package com.mm.sharesapp.persistence.entities
import org.squeryl.annotations.Column
import org.squeryl.KeyedEntity
import org.squeryl.dsl.CompositeKey2
import org.squeryl.PrimitiveTypeMode._ 


case class EdgarNews(val ticker:String, 
                @Column("PUBLISHED_DATE")
                val publishedDate:java.util.Date,
                val title:String,
                val description:String,
                val link:String,
                val content:String) extends KeyedEntity[CompositeKey2[String, java.util.Date]] {
  
  def id = compositeKey(ticker, publishedDate)
  
  def this() = this("testTicker", new java.util.Date(),
          "test titel", "test desc",
          "test link", "test content")
}
