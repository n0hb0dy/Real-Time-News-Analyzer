// package example
// /*For JSON Parser*/
// import net.liftweb._
// import net.liftweb.json.JsonAST
// import net.liftweb.json.JsonDSL._

// object JSONparser {
//     implicit val formats = net.liftweb.json.DefaultFormats

//     def toMap(data: String): Map[String, String] = {
//         return net.liftweb.json.Extraction.flatten(net.liftweb.json.parse(data))
//     }

//     def getMaxKey(flattenedJSON: Map[String, String]): Int = {
//         return flattenedJSON.keySet.map(k => k.charAt(1).toString().toInt).max[Int]
//     }

//     def makeClassInstance(data: String) {}

//     // case class popularTvShows ( title: String, year: Int, ids_slug: String, 
//     //                             ids_tvdb: Int, ids_imdb: String, ids_tmdb: Int) 

// }
