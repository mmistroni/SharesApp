package com.mm.sharesapp.persistence

import com.mm.sharesapp.entities._
object SampleDbApp extends App {

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