package com.mm.sharesapp.persistence
import org.squeryl.{ Session, SessionFactory, AbstractSession }
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.adapters.H2Adapter
import org.squeryl.Schema
import org.squeryl.adapters.DerbyAdapter
import java.sql.Connection

trait DerbyConnectionCommon extends BaseDBConnector {
  def connectToDbCommon(sessionFunc: Connection => AbstractSession): Option[() => AbstractSession] = {
    {
      Class.forName("org.h2.Driver")
      Some(() => {
        val c = java.sql.DriverManager.getConnection(
          "jdbc:derby:memory:test;create=true",
          "app",
          "")
        c.setAutoCommit(false)
        sessionFunc(c)
      })
    }
  }
}


trait Derby_Connection extends BaseDBConnector with DerbyConnectionCommon {
  def sessionCreator(): Option[() => AbstractSession] = connectToDbCommon(Session.create(_, new DerbyAdapter))
}
