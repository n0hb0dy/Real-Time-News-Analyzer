package example
/*API*/
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
/*Statement*/
import java.sql.Statement

object API {

    implicit val apiKey: String = "043fea9ea12d0cd4a05127315728edce"
    implicit var urlGetCount: Int = 1

    def getRestContent(url: String): String = {

        val httpClient = new DefaultHttpClient()
        val httpResponse = httpClient.execute(new HttpGet(url))
        val entity = httpResponse.getEntity()
        
        var content = ""
        if (entity != null) {
            val inputStream = entity.getContent()
            content = scala.io.Source.fromInputStream(inputStream).getLines.mkString
            inputStream.close
        }

        httpClient.getConnectionManager().shutdown()
        return content
    }

    
    // def storeShowDetails(stmt: Statement, id: Int, aK: String = apiKey, lang: String = "en-US"): Unit = {
    //     val data = getRestContent(s"https://api.themoviedb.org/3/tv/$id?api_key=$aK&language=$lang")
    //     linuxFileSystem.newFile("/tmp/json/rawShowDetails.json", data) //this is the root/tmp of the linux system
    //     // val src = "file:///home/maria_dev/files2.txt"
    //     Hive.createRawExternalTable(stmt, "rawShowDetails")
    //     try{Hive.loadRawTable(stmt, "rawShowDetails", "/tmp/json/rawShowDetails.json")} //try catch  java.sql.SQLException???
    //     catch{case ex: Throwable => ex.printStackTrace()}
    // }


    val defaultDiscoverURL = s"https://api.themoviedb.org/3/discover/tv?api_key=$apiKey"
    val defaultTVSearchURL = s"https://api.themoviedb.org/3/tv/"


    def storeURLData(stmt: Statement, tableName: String, urlBuilder: String): Unit = {
        println(s"Storing API data from $urlBuilder into $tableName ...")
        val siteData = getRestContent(urlBuilder).replaceAll("},", "}\n")
        println(siteData)
        linuxFileSystem.newFile(s"/tmp/json/rawTemp$urlGetCount.json",  siteData
                                                                          //skims the metadata and seperates documents by newlines
        )
        //linuxFileSystem.newFile(s"/tmp/json/rawTemp$urlGetCount.json",  siteData)

        
        Hive.createRawInternalTable(stmt, s"query$urlGetCount")
        try{Hive.loadRawTable(stmt, tableName, s"/tmp/json/rawTemp$urlGetCount.json")} 
        catch{case ex: Throwable => ex.printStackTrace()}
        urlGetCount += 1
    }

    def specifyTVid(urlBuilderValue: String): String = {
        var tempString = urlBuilderValue.replaceAll(" ", "%20")
        tempString = tempString + s"?api_key=$apiKey"
        return tempString
    }
    
    def specifySortBy(urlBuilderValue: String): String = {
        var tempString = urlBuilderValue.replaceAll(" ", "%20")
        tempString = "&sort_by=" + tempString
        return tempString
    }
    
    def specifyAirDateGTE(urlBuilderValue: String): String = {
        var tempString = urlBuilderValue.replaceAll(" ", "%20")
        tempString = "&air_date.gte=" + tempString
        return tempString
    }

    def specifyAirDateLTE(urlBuilderValue: String): String = {
        var tempString = urlBuilderValue.replaceAll(" ", "%20")
        tempString = "&air_date.lte=" + tempString
        return tempString
    }

    def specifyFirstAirDateLTE(urlBuilderValue: String): String = {
        var tempString = urlBuilderValue.replaceAll(" ", "%20")
        tempString = "&first_air_date.lte=" + tempString
        return tempString
    }

    def specifyFirstAirDateGTE(urlBuilderValue: String): String = {
        var tempString = urlBuilderValue.replaceAll(" ", "%20")
        tempString = "&first_air_date.gte=" + tempString
        return tempString
    }

    def specifyFirstAirYear(urlBuilderValue: String): String = {
        var tempString = urlBuilderValue.replaceAll(" ", "%20")
        tempString = "&first_air_date_year=" + tempString
        return tempString
    }
    
    def specifyPage(urlBuilderValue: Int = 1): String = {
        var tempString = urlBuilderValue.toString.replaceAll(" ", "%20")
        tempString = "&page1=" + tempString
        return tempString
    }

    def specifyVoteAverageGTE(urlBuilderValue: String): String = {
        var tempString = urlBuilderValue.replaceAll(" ", "%20")
        tempString = "&vote_average.gte=" + tempString
        return tempString
    }

    def specifyVoteCountGTE(urlBuilderValue: String): String = {
        var tempString = urlBuilderValue.replaceAll(" ", "%20")
        tempString = "&vote_count.gte=" + tempString
        return tempString
    }

    def specifyWithGenres(urlBuilderValue: String): String = {
        var tempString = urlBuilderValue.replaceAll(" ", "%20")
        tempString = "&with_genres=" + tempString
        return tempString
    }

    def specifyWithRuntimeGTE(urlBuilderValue: String): String = {
        var tempString = urlBuilderValue.replaceAll(" ", "%20")
        tempString = "&with_runtime.gte=" + tempString
        return tempString
    }
    
    def specifyWithRuntimeLTE(urlBuilderValue: String): String = {
        var tempString = urlBuilderValue.replaceAll(" ", "%20")
        tempString = "&with_runtime.lte=" + tempString
        return tempString
    }
    
    def specifyWithOriginalLanguage(urlBuilderValue: String): String = {
        var tempString = urlBuilderValue.replaceAll(" ", "%20")
        tempString = "&with_original_language=" + tempString
        return tempString
    }
    
    def specifyWithoutKeywords(urlBuilderValue: String): String = {
        var tempString = urlBuilderValue.replaceAll(" ", "%20")
        tempString = "&without_keywords=" + tempString
        return tempString
    }
    
    def specifyScreenedTheatrically(urlBuilderValue: String): String = {
        var tempString = urlBuilderValue.replaceAll(" ", "%20")
        tempString = "&screened_theatrically=" + tempString
        return tempString
    }
    
    def specifyWithCompanies(urlBuilderValue: String): String = {
        var tempString = urlBuilderValue.replaceAll(" ", "%20")
        tempString = "&with_companies=" + tempString
        return tempString
    }
    
    def specifyWithKeywords(urlBuilderValue: String): String = {
        var tempString = urlBuilderValue.replaceAll(" ", "%20")
        tempString = "&with_keywords=" + tempString
        return tempString
    }
    
    def specifyWithWatchProviders(urlBuilderValue: String): String = {
        var tempString = urlBuilderValue.replaceAll(" ", "%20")
        tempString = "&with_watch_providers=" + tempString
        return tempString
    }
    
    def specifyWatchRegion(urlBuilderValue: String): String = {
        var tempString = urlBuilderValue.replaceAll(" ", "%20")
        tempString = "&watch_region=" + tempString
        return tempString
    }
    
    def specifyWithWatchMonetization(urlBuilderValue: String): String = {
        var tempString = urlBuilderValue.replaceAll(" ", "%20")
        tempString = "&with_watch_monetization_types=" + tempString
        return tempString
    }
    
    def specifyTimezone(urlBuilderValue: String): String = {
        var tempString = urlBuilderValue.replaceAll(" ", "%20")
        tempString = "&timezone=" + tempString
        return tempString
    }

    def specifyIncludeNullFirstAirDates(urlBuilderValue: String): String = {
        var tempString = urlBuilderValue.replaceAll(" ", "%20")
        tempString = "&include_null_first_air_dates=" + tempString
        return tempString
    }

    
    
}


////////HELL
//(
    //"https://gorest.co.in/public/v1/posts"
    //"https://reqres.in/api/users"
    //"https://private-4dda7a-trakt.apiary-mock.com/shows/popular"
    
    
    // "https://api.themoviedb.org/3/trending/tv/day?api_key=043fea9ea12d0cd4a05127315728edce" //trending today
    //"https://api.themoviedb.org/3/tv/on_the_air?api_key=043fea9ea12d0cd4a05127315728edce&language=en-US&page=1" // recent playing on tv right now
    //"https://api.themoviedb.org/3/search/tv?api_key=043fea9ea12d0cd4a05127315728edce&language=en-US&page=1&query=breaking%20bad&include_adult=true" //search for tv show
    //"https://api.themoviedb.org/3/search/person?api_key=043fea9ea12d0cd4a05127315728edce&language=en-US&query=bryan%20cranston&page=1&include_adult=true" // search actor
    //"https://api.themoviedb.org/3/tv/1396?api_key=043fea9ea12d0cd4a05127315728edce&language=en-US" //get show details which contains "last_air_date"
    //"https://api.themoviedb.org/3/search/tv?api_key=043fea9ea12d0cd4a05127315728edce&language=en-US&page=1&query=maid&include_adult=true&first_air_date_year=2021" //search for a show this year
    //"https://api.themoviedb.org/3/tv/changes?api_key=043fea9ea12d0cd4a05127315728edce&end_date=2021-11-03&start_date=2021-11-02&page=1" //Get recently changed pages in the last two days
    
    
    
    //"https://api.themoviedb.org/3/discover/tv?api_key=043fea9ea12d0cd4a05127315728edce&language=en-US&sort_by=first_air_date.desc&page=1&timezone=America%2FNew_York&include_""_first_air_dates=false&with_watch_monetization_types=flatrate" //shows coming out
    //"https://api.themoviedb.org/3/discover/tv?api_key=043fea9ea12d0cd4a05127315728edce&language=en-US&sort_by=first_air_date.desc&first_air_date.lte=2021-11-03&page=1&timezone=America%2FNew_York&include_""_first_air_dates=false&with_watch_monetization_types=flatrate" //shows recently capped by today (NEED TO FILTER BY RATING)
    
    
    
    
    
    
    //        ).replaceAll("[","").replaceAll("]","").replaceAll("\r","").replaceAll("},","}\n")
    // def getRecentPlaying(aK: String = apiKey, lang: String = "en-US"): String = {
    //     return getRestContent(s"https://api.themoviedb.org/3/tv/on_the_air?api_key=$aK&language=$lang&page=1")
    // }
    
    // def getShowDetails(id: Int, aK: String = apiKey, lang: String = "en-US"): String = {
    //     return getRestContent(s"https://api.themoviedb.org/3/tv/$id?api_key=$aK&language=$lang")
    // }




    // def discoverTVSURI( stmt: Statement, id: Int, aK: String = apiKey, lang: String = "en-US",
    // sortBy: String = "popularity.desc", airDateGTE: String = "", airDateLTE: String ="",
    // airDateYear: String = "", pageNum: Int = 1, timeZone: String ="America%2FNew_York",
    // voteAverageGTE: String = "", voteCountGTE: String = "", withGenres: String = "",
    // withRuntimeGTE: String = "", withRuntimeLTE: String = "", includeNullFirstAirDates: Boolean = false,
    // withOriginalLanguage: String = "", withoutKeywords: String = "", screenTheatrically: String = "",
    // withCompanies: String = "", withKeywords: String = "", withWatchProviders: String = "",
    // watchRegion: String = "", withWatchMonetizationTypes: String = ""): String = {
        
        
        // val newSortBy = "&sort_by=" + sortBy
        // if (airDateGTE != "") {val newXXX = "&air_date.gte=" + airDateGTE}
        // if (airDateLTE != "") {val newAirDateLTE = "&air_date.lte=" + airDateLTE}
        // if (airDateYear != "") {val newAirDateYear = "&first_air_date_year=" + airDateYear}
        // val newPageNum = "&page1=" + pageNum.toString()
        // if (voteAverageGTE != "") {val newVoteAverageGTE = "&vote_average.gte=" + voteAverageGTE}
        // if (voteCountGTE != "") {val newVoteCountGTE = "&vote_count.gte=" + voteCountGTE}
        // if (withGenres != "") {val newWithGenres = "&with_genres=" + withGenres}
        // if (withRuntimeGTE != "") {val newWithRuntimeGTE = "&with_runtime.gte=" + withRuntimeGTE}
        // if (withRuntimeLTE != "") {val newWithRuntimeLTE = "&with_runtime.lte=" + withRuntimeLTE}
        // if (withOriginalLanguage != "") {val newWithOriginalLanguage = "&with_original_language=" + withOriginalLanguage}
        // if (withoutKeywords != "") {val newWithoutKeywords = "&without_keywords=" + withoutKeywords}
        // if (screenTheatrically != "") {val newScreenTheatrically = "&screened_theatrically=" + screenTheatrically}
        // if (withCompanies != "") {val newWithCompanies = "&with_companies=" + withCompanies}
        // if (withKeywords != "") {val newWithKeywords = "&with_keywords=" + withKeywords}
        // if (withWatchProviders != "") {val newWithWatchProviders = "&with_watch_providers=" + withWatchProviders}
        // if (watchRegion != "") {val newWatchRegion = " &watch_region=" + watchRegion}
        // if (withWatchMonetizationTypes != "") {val newWithWatchMonetizationTypes = "&with_watch_monetization_types=" + withWatchMonetizationTypes}
        
        
        
        // return ("""https://api.themoviedb.org/3/discover/tv?api_key=043fea9ea12d0cd4a05127315728edce&language=en-US" +
        //         &sort_by=popularity.desc&air_date.gte=APPLE&air_date.lte=BANANA&first_air_date.gte=ORANGE
        //         &first_air_date.lte=2021-11-03&first_air_date_year=GRAPE&page=1&timezone=America%2FNew_York
        //         &vote_average.gte=AVACADO&vote_count.gte=CELERY&with_genres=CARROT&with_networks=BOK%20CHOY
        //         &without_genres=TOMATO&with_runtime.gte=POTATO&with_runtime.lte=PICKLE
        //         &include_null_first_air_dates=false&with_original_language=CUCUMBER&without_keywords=ME
        //         &screened_theatrically=YOU&with_companies=US&with_keywords=FAMILY&with_watch_providers=SUIT
        //         &watch_region=TIE&with_watch_monetization_types=flatrate""")
        
        // return (s"""https://api.themoviedb.org/3/discover/tv?api_key=043fea9ea12d0cd4a05127315728edce&language=en-US" +
        // $newSortBy$newXXX$newAirDateLTE&first_air_date.gte=ORANGE
        // &first_air_date.lte=2021-11-03$newAirDateYear&page=1&timezone=America%2FNew_York
        // &vote_average.gte=AVACADO&vote_count.gte=CELERY&with_genres=CARROT&with_networks=BOK%20CHOY
        // &without_genres=TOMATO&with_runtime.gte=POTATO&with_runtime.lte=PICKLE
        // &include_null_first_air_dates=false&with_original_language=CUCUMBER&without_keywords=ME
        // &screened_theatrically=YOU&with_companies=US&with_keywords=FAMILY&with_watch_providers=SUIT
        // &watch_region=TIE&with_watch_monetization_types=flatrate""")}

        // implicit val formats = net.liftweb.json.DefaultFormats
// val popularShows = """{"a": 1,"b": "peter, griffin","c": "hello"}
//                       {"a": 2,"b": "peter, cliff","c": "hello"}"""

// //val presentablePopTvShows = popularShows.replaceAll("\\s+", " ").replace("[", "").replace("]", "")
// //val popDocuments = presentablePopTvShows.replace("},", "}\n").split("\n")

// //print(presentablePopTvShows)
// //print(popDocuments)

// val all_docs = net.liftweb.json.parse(popularShows)
// // print(net.liftweb.json.parse(popularShows).find(_==net.liftweb.json.parse("a")))
// //println(JsonAST.render(all_docs))
// //println(JsonAST.render(all_docs.extract[String]))
// val map = net.liftweb.json.Extraction.flatten(net.liftweb.json.parse(popularShows))
// println(map.get(".b").get)
// println(map.keySet)

    // val presentablePopTvShows = popularShows.replaceAll("\\s+", " ").replace("[", "").replace("]", "")
    // val popDocuments = presentablePopTvShows.replace("},", "}\n").split("\n")

    // val all_docs = net.liftweb.json.parse(popularShows)
    // print(net.liftweb.json.parse(popularShows).find(_==net.liftweb.json.parse("Community")))
    // println(JsonAST.render(all_docs))
    
    // println(JsonAST.render(all_docs.filterField(x => x== new JsonAST.JField("year", JsonAST.JInt(1989))))) // Checking if field exists
    
    // //println(JsonAST.render(all_docs.extract[String]))

    // val all_docs_flat = net.liftweb.json.Extraction.flatten(all_docs) // outputs ([0].ids.tvdb, 121361) and others...
    // val all_docs_map_grouped = all_docs_flat.filter((kv) => kv._1.startsWith("[0]"))// .groupBy(x => x.startsWith("([0]")) //.values //.keySet //toSeq.sortBy(_._1) //filter((k) => k._1.startsWith("[0]")) //groupBy((k) => k._1.startsWith("[0]"))
    //             //.keySet.map(k => k.charAt(1).toString().toInt).max[Int]
    // val justSchemaNValue = all_docs_map_grouped.map((kv) => (kv._1.replace("[0].", ""), kv._2)) //.map(x => x._1.replace("[0].", ""))
    // val popTVInstance = new popularTvShows(
    //                             justSchemaNValue.apply("title"),
    //                             justSchemaNValue.apply("year").toInt,
    //                             justSchemaNValue.apply("ids.slug"),
    //                             justSchemaNValue.apply("ids.tvdb").toInt,
    //                             justSchemaNValue.apply("ids.imdb"),
    //                             justSchemaNValue.apply("ids.tmdb").toInt    
    // )

    // all_docs_map_grouped.foreach(println)
    // print(all_docs_map_grouped)
    //  justSchemaNValue.foreach(println)
    // println()
    // //popTVInstance.println
    // print(popTVInstance.toCVSString())

    
    
    
    
    
    
    
    // // val json = List(1, 2, 3)
    // // println((JsonAST.render(json)))

    // // val map = Map("fname" -> "Alvin", "lname" -> "Alexander")
    // // println((JsonAST.render(map)))

    // // net.liftweb.json.parse(popularShows).extractOrElse()





 





// //val test: Json = """"title": "Community", "year": 2009, "ids": 1""".asJson
//     //popularShowsJSON.mapString(_.trim())
//     //print(test.as[popTVShow])
    
//     //print(popularShows.split("},").foreach(println))
    
//     val presentablePopTvShows = popularShows.replaceAll("\\s+", " ").replace("[", "").replace("]", "")
//     val popDocuments = presentablePopTvShows.replace("},", "}\n").split("\n")


//     //println(presentablePopTvShows)
//     //println()
//     //popDocuments.foreach(println)
//     println()
//     val popJson = popDocuments.map(doc => parse(doc).getOrElse(Json.Null))

//     val iterKeys = popJson.head.hcursor.keys

//     for(key <- iterKeys){
//       for(iter<- key){
//         print(iter)
//       }
//     }

//     popJson.foreach(println)



//     val year = popJson.head.hcursor.get[Int]("year")
//     println(year.getOrElse(print("DID NOT PARSE IT CORRECTLY")))



//     // getting everyvalue and parsing it into csv format

//     //val popKeys = popJson.toMap(x => x.hcursor.get(Int))
    
    

//     //val popValues = popJson.map(_.hcursor.get[Any](new Any))


// implicit val popTVShowEncoder: Encoder[popTVShow] = popTVShow => Json.obj(
//       "title" -> popTVShow.title.asJson,
//       "year" -> popTVShow.year.asJson,
//       "ids" -> popTVShow.ids.asJson
//     ) // This is for turning instances of his class to JSON objects

//     implicit val popTVShowDecoder: Decoder[popTVShow] = 
//       Decoder.forProduct3("title","year","ids")(popTVShow.apply)








//     //popJson.head.hcursor.downField("year").withFocus(println)
//     //val popMap = popJson.map(_.as[Map[String, String]])
//     println()

//     //optics ............................................

//     // val _title = root.title.string

//     // println(_title.getOption)






//   Thread.sleep(3000) // Program does not print the whole list other wise