package com.mm.sharesapp.util

import com.mm.sharesapp.entities.Share

/**
 * Map a share to a corresponding URL
 * These URLs should be configured via prop files
 */

trait MapperComponent {

  val mapper:Mapper

  trait Mapper {
     
     def map(input:String):Seq[String]
  }
}

trait SharesMapperComponent extends MapperComponent {
  
  val mapper = ShareToUrlMapper

  
  object ShareToUrlMapper extends Mapper{
  
    private val sharesUrls = Seq("https://feeds.finance.yahoo.com/rss/2.0/headline?s=<share>")
  
    def map(ticker:String):Seq[String] = {
      sharesUrls.map(url => url.replace("<share>", ticker))
    }
  
  }
}