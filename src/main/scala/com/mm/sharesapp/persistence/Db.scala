package com.mm.sharesapp.persistence

import sorm._
import com.mm.sharesapp.entities._

object Db extends Instance(
  entities = Set(
    Entity[Portfolio](),
    Entity[RssFeed](),
    Entity[SharePrice](),
    Entity[NewsItem]()
  ),
  //url = "jdbc:h2:mem:test",
  //user = "",
  //password = "",
  url="jdbc:mysql://localhost/cameldb",
  user="root",
  password="m15tr0n1",
    
  initMode = InitMode.Create
)

