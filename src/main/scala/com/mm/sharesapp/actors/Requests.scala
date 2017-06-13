package com.mm.sharesapp.actors

import com.mm.sharesapp.entities._

case object Start

case object Shutdown

case object FetchShares

case object FetchStaticNews

case class URLMessage(url:String)

case class RSSRequest(ticker:String, url:String)

case class AllShares(data:Seq[Share])