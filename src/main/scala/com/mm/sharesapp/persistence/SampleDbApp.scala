package com.mm.sharesapp.persistence

import com.mm.sharesapp.entities._
import sorm._
object SampleDbApp extends App {

  object Db extends Instance(
  entities = Set(
    Entity[Portfolio](),
    Entity[RssFeed](),
    Entity[SharePrice](),
    Entity[NewsItem]()
  ),
  url="jdbc:mysql://localhost/cameldb",
  user="root",
  password="m15tr0n1",
    
  initMode = InitMode.Create
)

  
  /**
  // Creating Portflio
  Db.save( Portfolio("portfolioId", "marco",
                     new org.joda.time.LocalDate(),
                     1.2))
                   
  // Creating Portflio
  val x =  Db.save( Portfolio("portfolioId2", "marco",
                     new org.joda.time.LocalDate(),
                     3.3))
  
                     
                     println("Fetching...............")
  
  **/ 
  val allPtf = Db.query[Portfolio].fetch().map{ptf => (ptf.portfolioId, ptf.cash)}
  
  println(allPtf.mkString("\n"))
  

}