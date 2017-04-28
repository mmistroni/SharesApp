package com.mm.sharesapp.actors

case object Start

case object Shutdown

case object FetchShares

case object FetchStaticNews

case class URLMessage(url:String)

case class RSSData(data:RSSData)