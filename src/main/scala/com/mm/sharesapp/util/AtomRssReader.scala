package com.mm.sharesapp.util



import java.net.URL
import com.rometools.rome.feed.synd.{SyndFeed}
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import scala.collection.JavaConversions._
import org.apache.http.impl.client._
import org.apache.http.client.methods._

object AtomRssReader extends App {

    // NOTE: code can throw exceptions
    val feedUrl = "https://www.jobserve.com/MySearch/903E8CA5E642DE63.rss"
    
    /**
    val client = HttpClients.createMinimal()
    val request = new HttpGet(feedUrl);
    val response  = client.execute(request);
    val stream = response.getEntity().getContent()
    **/

    
    
    
    val input = new SyndFeedInput
    val feed: SyndFeed = input.build(new XmlReader(new URL(feedUrl)))
    //input.build(new XmlReader(stream))
    //println(feed)


    // `feed.getEntries` has type `java.util.List[SyndEntry]`
    val entries = asScalaBuffer(feed.getEntries).toVector

    for (entry <- entries) {
        println("Title: " + entry.getTitle)
        println("URI:   " + entry.getUri)
        println("Date:  " + entry.getUpdatedDate)
        println("Content: " + entry.getComments )
        // java.util.List[SyndLink]
        val links = asScalaBuffer(entry.getLinks).toVector
        for (link <- links) {
            println("Link: " + link.getHref)
        }

        val contents = asScalaBuffer(entry.getContents).toVector
        for (content <- contents) {
            println("Content: " + content.getValue)
        }

        val categories = asScalaBuffer(entry.getCategories).toVector
        for (category <- categories) {
            println("Category: " + category.getName)
        }

        println("")

    }

}

