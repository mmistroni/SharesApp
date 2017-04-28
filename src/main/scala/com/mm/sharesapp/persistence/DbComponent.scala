package com.mm.sharesapp.persistence

import sorm._
import com.mm.sharesapp.entities._


trait DbComponent {
  this:DbConfigComponent =>
  
  val db:Instance = Db
  
  object Db extends Instance(
    entities = Set(
      Entity[Portfolio](),
      Entity[RssFeed](),
      Entity[SharePrice](),
      Entity[NewsItem]()
    ),
    url=config.dbUrl,
    user=config.username,
    password=config.password,
    initMode = InitMode.Create
  )
  
}