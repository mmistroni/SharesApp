package com.mm.sharesapp.persistence
import org.squeryl.{Session, SessionFactory, AbstractSession}

trait BaseDBConnector {

  def sessionCreator() : Option[() => AbstractSession]

}
