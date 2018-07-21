package com.mm.sharesapp.util

import com.mm.sharesapp.entities._
import com.mm.sharesapp.services.{SharePriceComponent, DataDownloaderComponent}

object Utilities extends App{
  import scala.util.control.Exception.allCatch
    
  def getDouble(doubleStr:String):Double = 
    allCatch opt doubleStr.toDouble match {
    case Some(doubleNum) => doubleNum
    case _ => 0.0
  }
  
  def getSharesData:Unit = {
    val component = new SharePriceComponent with DataDownloaderComponent{}
    val service = component.sharePriceService
    println("Got:" + service.downloadSharePrice("GE").toString())
    
  }
  
  getSharesData
}