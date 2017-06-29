package com.mm.sharesapp.persistence
import org.squeryl.{Session, SessionFactory}
import org.squeryl.adapters.{MySQLAdapter,MySQLInnoDBAdapter}
import org.squeryl.PrimitiveTypeMode._


trait DBConnector {
  Class.forName("com.mysql.jdbc.Driver");
    SessionFactory.concreteFactory = Some(() =>
      Session.create(
        java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/cameldb",
          "root", "m15tr0n1"),
        new MySQLInnoDBAdapter()))
  
  def withTransaction[A](codeBlock: => A) = {
      transaction {
        codeBlock
      }
    }
        
}