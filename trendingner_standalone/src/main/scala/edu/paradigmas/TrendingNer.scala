package edu.paradigmas

import org.json4s.JsonDSL._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import scala.io._

import nermodel.{NERSimpleModel, NERCount}
import service.FeedService
import parser.{Parser, RSSParser, RedditParser}

case class Subscription(url: String, urlParams: List[String], urlType:String)

/*
 * Main class
 */
object TrendingNer extends App {

  // Initial configurations
  implicit val formats = DefaultFormats

  def readSubscriptions(): List[Subscription] = {
    // args is a list that receives the parameters passed by console.
    println(s"Reading subscriptions from ${args}")
    val filename = args.length match {
      case 0 => "subscriptions.json"
      case _ => args(0)
    }
    println(s"Reading subscriptions from ${filename}")
    val jsonContent = Source.fromFile(filename)
    (parse(jsonContent.mkString)).extract[List[Subscription]]
  }

  def countNes(feedTexts: Seq[String]): Seq[NERCount] = {
    println("Obtaining NERs:")
    val nerModel = new NERSimpleModel

    nerModel.getSortedNEs(feedTexts)
  }

  val subscriptions = readSubscriptions()
  
  val service = new FeedService()
  subscriptions.foreach {
      subs => {
        val parser = subs.urlType match {
          case "rss" => new RSSParser()
          case "reddit" => new RedditParser()
        }
        service.subscribe(subs.url, subs.urlParams, parser)
      }
  }

  val feedTexts = service.getText()

  val ners: Seq[NERCount] = countNes(feedTexts)
  println("Top 20 trending entities")
  ners.take(20).foreach { ner: NERCount =>
    println(ner.ner, ner.count.toString) }
}
