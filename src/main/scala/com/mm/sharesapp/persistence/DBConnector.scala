package com.mm.sharesapp.persistence
import com.mm.sharesapp.util.AppConfig

import org.squeryl.{Session, SessionFactory, AbstractSession}
import org.squeryl.adapters.{MySQLAdapter,MySQLInnoDBAdapter}
import org.squeryl.PrimitiveTypeMode._

trait BaseDBConnector {

  def sessionCreator() : Option[() => AbstractSession]

}

trait DBConnector {
  Class.forName("com.mysql.jdbc.Driver");
    SessionFactory.concreteFactory = Some(() =>
      Session.create(
        java.sql.DriverManager.getConnection(AppConfig.DbConfig.url,
          AppConfig.DbConfig.username, AppConfig.DbConfig.password),
        new MySQLInnoDBAdapter()))
  
  def withTransaction[A](codeBlock: => A) = {
      transaction {
        codeBlock
      }
    }
        
}