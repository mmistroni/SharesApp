package com.mm.sharesapp.actors

import akka.actor._
import akka.actor.SupervisorStrategy.{ Resume, Escalate, Restart }
import akka.event.Logging
import com.mm.sharesapp.services.{SharePriceComponent, DataDownloaderComponent}
import com.mm.sharesapp.entities._
import scala.collection.mutable.ArrayBuffer


/**
 * Fetches Share prices for multiple shares
 *  */
class SharePriceAggregatorActor(destination: ActorRef,
                                dataDownloader:ActorRef) extends Actor with ActorLogging{
    sharePriceComponent:SharePriceComponent =>
      var counter = 0
      val results = ArrayBuffer.empty[StockData]
      def receive = {
        case NYSEShares(shares) =>
          counter = shares.size
          log.info(s"counter initalized to $counter") 
          
        case data:StockData =>
            results += data
            collectResults
          
          
        case message => log.info(s"SharePriceActorsUnexpected msg:$message")
      }
      
      def createMarketBreadthMessage = {
        val advancing = results.filter(data => data.latestPrice > data.previousClose).size
        MarketBreadth(advancing, results.size - advancing)
      }
      
      def collectResults = {
        if (results.size == counter) {
          destination ! createMarketBreadthMessage // Make sure it becomes immutable
          context.stop(self)
         }
      }

}

class NYSEPriceDownloader(destination:ActorRef) extends Actor with ActorLogging {
  
  dataDownloaderComponent:DataDownloaderComponent =>
    
    private val nyseUrl = "https://www.nasdaq.com/screening/companies-by-name.aspx?letter=0&exchange=nyse&render=download"
  
    def receive = {
      case NYSEMessage => {
        log.info("Received message to download nyse//")
        val nyseData =dataDownloaderComponent.dataDownloader.downloadCSV(nyseUrl)
        destination ! NYSEShares(removeUnwantedCompanies(nyseData))
      }
    }
  
    def removeUnwantedCompanies(companies:List[List[String]]):List[String] = {
      //List(Symbol, Name, LastSale, MarketCap, IPOyear, Sector, industry, Summary Quote, "")
      companies.filter(lst => 
             !lst(0).contains("^") &&  !lst(6).contains("n/a"))
             .map(lst => lst(0))
      
    }
    
  
        
  
  
}
    


