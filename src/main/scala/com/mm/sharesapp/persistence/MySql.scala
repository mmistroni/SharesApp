package com.mm.sharesapp.persistence
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Schema
import org.squeryl.annotations.Column
import java.util.Date
import java.sql.Timestamp
import org.squeryl.{ Session, SessionFactory }
import org.squeryl.adapters.MySQLInnoDBAdapter
import org.squeryl.adapters.MSSQLServer

import org.squeryl._

trait MySql extends  {
  def sessionCreator(): Option[() => Session] = {

    Class.forName("com.mysql.jdbc.Driver")

    Some(() => {
      val c = java.sql.DriverManager.getConnection(
        ("jdbc:mysql://localhost/cameldb"))
      c.setAutoCommit(false)
      Session.create(c, new MySQLInnoDBAdapter())
    })

  }
}
