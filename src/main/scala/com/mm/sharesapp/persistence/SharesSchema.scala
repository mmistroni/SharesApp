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
import org.squeryl.{Session, SessionFactory}
import org.squeryl.adapters.MySQLAdapter



object SharesSchema extends Schema {
  
  val edgarNews = table[EdgarNews]("edgar_news")
  
  def startDatabaseSession(): Unit = {

    Class.forName("com.mysql.jdbc.Driver");
    SessionFactory.concreteFactory = Some(() =>
      Session.create(
        java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/cameldb",
          "root", "m15tr0n1"),
        new MySQLAdapter()))

  }

  

  
  
  
}
