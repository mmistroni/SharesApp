package com.mm.sharesapp.services

import scala.io.{Source, BufferedSource}
import com.github.tototoshi.csv._
import com.mm.sharesapp.util.LogHelper

/**
 * Trait for a DataDowloader which fetches data
 * from a supplied URL
 * These are the URL that we need
 * 
 * # view all companies for a sector. the one below is Financials
	#https://biz.yahoo.com/p/csv/431conameu.csv

  # list of all sectors from yahoo
  #http://biz.yahoo.com/ic/ind_index_alpha.html

  # historical prices
  #'http://ichart.finance.yahoo.com/table.csv?s=%(ticker)s&a=%(fromMonth)s&b=%(fromDay)s&c=%(fromYear)s&d=%(toMonth)s&e=%(toDay)s&f=%(toYear)s&g=d&ignore=.csv' 
 * Investigate dependency injection in Scala and see if you can use it
 * 
 * 
 */

trait DataDownloaderComponent  {
  
  val dataDownloader = new DataDownloader()
 
  class  DataDownloader extends LogHelper{
  
    def downloadFromURL(url:String):Iterator[String] = {
      logger.info("Downloadign data from url:" + url)
      _getFromUrl(url).getLines()
    }
    
    def downloadCSV(url:String):List[List[String]] = {
      logger.debug(s"Loading from:$url|")
      _getFromCSVReader(url)
    }
    
    
    private[services] def _getFromCSVReader(url:String):List[List[String]] = {
      CSVReader.open(_getFromUrl(url)).all()
    }
    
    private[services] def _getFromUrl(url:String):BufferedSource = Source.fromURL(url)
      
  }
}