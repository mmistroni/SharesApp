package com.mm.sharesapp.util
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Schema
import org.squeryl.annotations.Column
import java.util.Date
import java.sql.Timestamp
import com.mm.sharesapp.persistence.SharesSchema._
import com.mm.sharesapp.persistence.entities.EdgarNews

object SquerylDbApp extends App  with com.mm.sharesapp.persistence.DBConnector {
  
  println("Startig Squeryl applicatin...")
  
  
  println("Inserting edgar news...")
  
  transaction {
    edgarNews.insert(new EdgarNews("MyTickerWTrans", new java.util.Date(),
                            "EdgarTitle", "EdgarDesc", "EgarLink", "EdgarContent"))
       
  }
  
  println("OUtta here")

    
  
}