package com.mm.sharesapp.util

import com.mm.sharesapp.entities.Share

/**
 * Map a share to a corresponding URL
 * These URLs should be configured via prop files
 */

object ShareToUrlMapper {
  
  private val sharesUrls = Seq("")
  
  def mapToUrl(share:Share):Seq[String] = {
    sharesUrls.map(url => url.replace("<share>", share.ticker))
  }
  
}