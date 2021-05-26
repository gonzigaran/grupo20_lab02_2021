package edu.paradigmas.service

import scala.collection.mutable.MutableList
import edu.paradigmas.parser.{Parser, RSSParser, RedditParser}

class FeedService() {
    var urls: MutableList[(String, Parser)] = new MutableList[(String, Parser)]()
    
    def subscribe(urlTemplate: String, params: Seq[String], parser: Parser) = {
        if (params == Seq()){
            urls ++= Seq((urlTemplate, parser))
        } else {
            val result = params.map(x => urlTemplate.format(x)).map(y =>(y, parser))
            urls ++= result
        }
    }
    
    def getText(): Seq[String] = {
        urls.flatMap{case (a, b) => b.readText(a)}
    }
}