package com.mm.sharesapp.persistence
import com.mm.sharesapp.entities._
import sorm._

abstract class DbService {

  object Db extends Instance(
    entities = Set(
      Entity[Portfolio](),
      Entity[RssFeed](),
      Entity[SharePrice](),
      Entity[NewsItem]()),
    url = "jdbc:mysql://localhost/cameldb",
    user = "root",
    password = "m15tr0n1",
    initMode = InitMode.Create)

  
  type DomainObject <: AnyRef

    def insert(item: DomainObject): Boolean 
    
    def findAll: Seq[DomainObject]

    def findByExample(example: DomainObject): Seq[DomainObject]

  }



  