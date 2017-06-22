package com.mm.sharesapp.util
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Schema
import org.squeryl.annotations.Column
import java.util.Date
import java.sql.Timestamp
import com.mm.sharesapp.persistence.SharesSchema
import com.mm.sharesapp.persistence.entities.EdgarNews

object SquerylDbApp extends App  {
  
  println("Startig Squeryl applicatin...")
  
  SharesSchema.startDatabaseSession
  
  println("Inserting edgar news...")
  transaction {
      SharesSchema.edgarNews.insert(new EdgarNews("MyTicker3", new java.util.Date(),
                            "EdgarTitle", "EdgarDesc", "EgarLink", "EdgarContent"))
      
    }
  
  println("OUtta here")

    
  
}