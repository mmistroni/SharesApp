package com.mm.sharesapp.util

object Utilities {
  import scala.util.control.Exception.allCatch
    
  def getDouble(doubleStr:String):Double = 
    allCatch opt doubleStr.toDouble match {
    case Some(doubleNum) => doubleNum
    case _ => Double.NaN
  }
  
  
  
  
}