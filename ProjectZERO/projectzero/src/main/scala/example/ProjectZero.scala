package example

import example.Helpers._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Projections._

//import org.json4s._       // Library collects all 6 JSON APIs?!?!?

import example.Definitions._

object ProjectZero {
    
    def main (args: Array[String]): Unit = {
           
      Thread.sleep(555)
      println("")
      // Get the initial vins imported from the file 
      val vinQuery = tableHandler.collection.find().projection(fields(include("vin"), excludeId())).results()
      val startingOff = for(doc <- vinQuery) yield doc.toJson()
      //for(x <- startingOff) println(x)

      Definitions.promptEorU(startingOff)

      Definitions.tableHandler.client.close()
  }
}

