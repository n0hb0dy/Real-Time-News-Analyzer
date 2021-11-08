package example
/*Needs to see the Statement Class*/
// import java.io.IOException
// import java.sql.SQLException
// import java.sql.Connection
// import java.sql.ResultSet
import java.sql.Statement
// import java.sql.DriverManager


/******
Confusing terminogoly I'm bouncing back and forth with:

temp table == query# ==> what comes from the temp file that stores the raw JSON string
                ^^^ queries being request responses from te API

proper table ==> table with an actual schema to do actual Hive queries
  ^^^ gets converted from the external table where the JSON string was stored

*******/
object Query1 {

    val defaultTableName = "recent_shows"

    def getAPIQueryData(stmt: Statement): Unit = {
        val newestTable = "query" + API.urlGetCount.toString()
        API.storeURLData( stmt, newestTable,    API.defaultDiscoverURL + 
                                                API.specifySortBy("first_air_date.desc") + 
                                                API.specifyAirDateGTE("2021-11-01"))    //first 20 recent tvshows (discover url))
    }

    def storeInProperTable(stmt: Statement, properTableName: String = defaultTableName): Unit = {
        
        val newestTable = "query" + (API.urlGetCount - 1).toString()        // WOW this project is getting messy
        
        println(s"Creating table $properTableName from $newestTable..")
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS " + properTableName + " AS SELECT get_json_object(json,'$.name') AS name, " +
            "CAST(get_json_object(json,'$.id') AS Int) AS id, get_json_object(json,'$.first_air_date') AS first_air_date" +
            " FROM " + newestTable
        )
        println(s"$properTableName successfully created!")
    }

    def printTempTable(stmt: Statement): Unit = {
        val num = (API.urlGetCount - 1).toString()
        val response =  stmt.executeQuery(
                            s"SELECT * FROM query$num"
                        )
        if (response.next()) {
            System.out.println(response.getString(1))
        }
    }
    
    def printBasicRealTable(stmt: Statement): Unit = {
        val num = (API.urlGetCount - 1).toString()
        val response =  stmt.executeQuery(
            s"SELECT * FROM $defaultTableName"
        )
        while (response.next()) {
            System.out.println(
                response.getString(1) + "\t" + response.getString(2) + "\t" + response.getString(3)
            )
        }
    }

    def printFormattedRealTable(stmt: Statement): Unit = {
        val num = (API.urlGetCount - 1).toString()
        val response =  stmt.executeQuery(
            s"""SELECT CONCAT("Name: ", name, "\tID:\t", id, "\tFirst Air Date:\t", first_air_date) FROM $defaultTableName"""
        )
        while (response.next()) {
            System.out.println(
                response.getString(1)
            )
        }
    }
}

object Query2 {

    val defaultTableName = "oldest_shows"

    def getAPIQueryData(stmt: Statement): Unit = {
        val newestTable = "query" + API.urlGetCount.toString()
        API.storeURLData( stmt, newestTable,    API.defaultDiscoverURL + 
                                                API.specifySortBy("first_air_date.asc") +
                                                API.specifyVoteAverageGTE("1"))    //oldest shows (first 4 are mistaken entries)
    }

    def storeInProperTable(stmt: Statement, properTableName: String = defaultTableName): Unit = {
        
        val newestTable = "query" + (API.urlGetCount - 1).toString()        
        
        println(s"Creating table $properTableName from $newestTable..")
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS " + properTableName + " AS SELECT DISTINCT get_json_object(json,'$.name') AS name, " +
            "CAST(get_json_object(json,'$.id') AS Int) AS id, get_json_object(json,'$.first_air_date') AS first_air_date" +
            " FROM " + newestTable
        )
        println(s"$properTableName successfully created!")
    }

    def printTempTable(stmt: Statement): Unit = {
        val num = (API.urlGetCount - 1).toString()
        val response =  stmt.executeQuery(
                            s"SELECT * FROM query$num"
                        )
        if (response.next()) {
            System.out.println(response.getString(1))
        }
    }
    
    def printBasicRealTable(stmt: Statement): Unit = {
        val num = (API.urlGetCount - 1).toString()
        val response =  stmt.executeQuery(
            s"SELECT * FROM $defaultTableName"
        )
        while (response.next()) {
            System.out.println(
                response.getString(1) + "\t" + response.getString(2) + "\t" + response.getString(3)
            )
        }
    }

    def printFormattedRealTable(stmt: Statement): Unit = {
        val num = (API.urlGetCount - 1).toString()
        val response =  stmt.executeQuery(
            s"""SELECT CONCAT(name, " (", id, ") came out in ", first_air_date) FROM $defaultTableName""" //sorting would require another mapreduce
        )
        while (response.next()) {
            System.out.println(
                response.getString(1)
            )
        }
    }
}

object Query3 {

    val defaultTableName = "good_shows_now"

    def getAPIQueryData(stmt: Statement): Unit = {
        val newestTable = "query" + API.urlGetCount.toString()
        API.storeURLData( stmt, newestTable,    API.defaultDiscoverURL + 
                                                API.specifySortBy("first_air_date.desc") + 
                                                API.specifyFirstAirDateLTE("2021-11-07") +
                                                API.specifyVoteAverageGTE("8") + 
                                                API.specifyWithWatchMonetization("free") + 
                                                API.specifyIncludeNullFirstAirDates("false"))    // Good rated recent tv shows for FREE
    }

    def storeInProperTable(stmt: Statement, properTableName: String = defaultTableName): Unit = {
        
        val newestTable = "query" + (API.urlGetCount - 1).toString()        
        
        println(s"Creating table $properTableName from $newestTable..")
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS " + properTableName + " AS SELECT get_json_object(json,'$.name') AS name, " +
            "CAST(get_json_object(json,'$.id') AS Int) AS id, get_json_object(json,'$.first_air_date') AS first_air_date, " +
            "CAST(get_json_object(json,'$.vote_average') AS Int) AS vote_average FROM " + newestTable
        )
        println(s"$properTableName successfully created!")
    }

    def printTempTable(stmt: Statement): Unit = {
        val num = (API.urlGetCount - 1).toString()
        val response =  stmt.executeQuery(
                            s"SELECT * FROM query$num"
                        )
        if (response.next()) {
            System.out.println(response.getString(1))
        }
    }
    
    def printBasicRealTable(stmt: Statement): Unit = {
        val num = (API.urlGetCount - 1).toString()
        val response =  stmt.executeQuery(
            s"SELECT * FROM $defaultTableName"
        )
        while (response.next()) {
            System.out.println(
                response.getString(1) + "\t" + response.getString(2) + "\t" + response.getString(3) + "\t" + response.getString(4)
            )
        }
    }

    def printFormattedRealTable(stmt: Statement): Unit = {
        val num = (API.urlGetCount - 1).toString()
        val response =  stmt.executeQuery(
            s"""SELECT CONCAT(name, " (", id, ") has an average vote of  ", vote_average) FROM $defaultTableName"""
        )
        while (response.next()) {
            System.out.println(
                response.getString(1)
            )
        }
    }
}

object Query4 {

    val defaultTableName = "tvshow_castinfo"

    def getAPIQueryData(stmt: Statement, showID: String): Unit = {
        val newestTable = "query" + API.urlGetCount.toString()
        API.storeURLData( stmt, newestTable, s"https://api.themoviedb.org/3/tv/$showID/credits?api_key=" + API.apiKey + "&language=en-US")    //searches for a tv show by id Breaking Bad
    }

    def storeInProperTable(stmt: Statement, properTableName: String = defaultTableName): Unit = {
        
        val newestTable = "query" + (API.urlGetCount - 1).toString()        
        
        println(s"Creating table $properTableName from $newestTable..")
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS " + properTableName + " AS SELECT get_json_object(json,'$.cast.name') AS name, " +
            "CAST(get_json_object(json,'$.cast.id') AS Int) AS id, CAST(get_json_object(json,'$.cast.order') AS Int) AS ord" +
            " FROM " + newestTable
        )
        println(s"$properTableName successfully created!")
    }

    def printTempTable(stmt: Statement): Unit = {
        val num = (API.urlGetCount - 1).toString()
        val response =  stmt.executeQuery(
                            s"SELECT * FROM query$num"
                        )
        if (response.next()) {
            System.out.println(response.getString(1))
        }
    }
    
    def printBasicRealTable(stmt: Statement): Unit = {
        val num = (API.urlGetCount - 1).toString()
        val response =  stmt.executeQuery(
            s"SELECT * FROM $defaultTableName"
        )
        while (response.next()) {
            System.out.println(
                response.getString(1) + "\t" + response.getString(2) + "\t" + response.getString(3)
            )
        }
    }

    def printFormattedRealTable(stmt: Statement): Unit = {
        val num = (API.urlGetCount - 1).toString()
        val response =  stmt.executeQuery(
            s"""SELECT CONCAT(name, " (", id, ") is of the ", ord, " order in the casting") FROM $defaultTableName WHERE ord = 0"""
        )
        while (response.next()) {
            System.out.println(
                response.getString(1)
            )
        }
    }
}

object Query5 {

    val defaultTableName = "tvshow_seasonsinfo"

    def getAPIQueryData(stmt: Statement, showID: String): Unit = {
        val newestTable = "query" + API.urlGetCount.toString()
        API.storeURLData( stmt, newestTable,    s"https://api.themoviedb.org/3/tv/$showID?api_key=${API.apiKey}&language=en-US")    //oldest movies (first 4 are mistaken entries)
    }

    def storeInProperTable(stmt: Statement, properTableName: String = defaultTableName): Unit = {
        
        val newestTable = "query" + (API.urlGetCount - 1).toString()        
        
        println(s"Creating table $properTableName from $newestTable..")
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS " + properTableName + " AS SELECT get_json_object(json,'$.season_number') AS season_number " +
            "FROM " + newestTable + "WHERE get_json_object(json, '$.season_number') >= 0"
        )
        println(s"$properTableName successfully created!")
    }

    def printTempTable(stmt: Statement): Unit = {
        val num = (API.urlGetCount - 1).toString()
        val response =  stmt.executeQuery(
                            s"SELECT * FROM query$num"
                        )
        if (response.next()) {
            System.out.println(response.getString(1))
        }
    }
    
    def printBasicRealTable(stmt: Statement): Unit = {
        val num = (API.urlGetCount - 1).toString()
        val response =  stmt.executeQuery(
            s"SELECT * FROM $defaultTableName"
        )
        while (response.next()) {
            System.out.println(
                response.getString(1) + "\t" + response.getString(2) + "\t" + response.getString(3)
            )
        }
    }

    def printFormattedRealTable(stmt: Statement): Unit = {
        val num = (API.urlGetCount - 1).toString()
        val response =  stmt.executeQuery(
            s"""SELECT CONCAT(name, " (", id, ") came out in ", first_air_date) FROM $defaultTableName"""
        )
        while (response.next()) {
            System.out.println(
                response.getString(1)
            )
        }
    }
}

object Query6 {

    val defaultTableName = "oldest_shows"

    def getAPIQueryData(stmt: Statement): Unit = {
        val newestTable = "query" + API.urlGetCount.toString()
        API.storeURLData( stmt, newestTable,    API.specifySortBy("first_air_date.asc"))    //oldest movies (first 4 are mistaken entries)
    }

    def storeInProperTable(stmt: Statement, properTableName: String = defaultTableName): Unit = {
        
        val newestTable = "query" + (API.urlGetCount - 1).toString()        
        
        println(s"Creating table $properTableName from $newestTable..")
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS " + properTableName + " AS SELECT get_json_object(json,'$.name') AS name, " +
            "CAST(get_json_object(json,'$.id') AS Int) AS id, get_json_object(json,'$.first_air_date') AS first_air_date" +
            " FROM " + newestTable
        )
        println(s"$properTableName successfully created!")
    }

    def printTempTable(stmt: Statement): Unit = {
        val num = (API.urlGetCount - 1).toString()
        val response =  stmt.executeQuery(
                            s"SELECT * FROM query$num"
                        )
        if (response.next()) {
            System.out.println(response.getString(1))
        }
    }
    
    def printBasicRealTable(stmt: Statement): Unit = {
        val num = (API.urlGetCount - 1).toString()
        val response =  stmt.executeQuery(
            s"SELECT * FROM $defaultTableName"
        )
        while (response.next()) {
            System.out.println(
                response.getString(1) + "\t" + response.getString(2) + "\t" + response.getString(3)
            )
        }
    }

    def printFormattedRealTable(stmt: Statement): Unit = {
        val num = (API.urlGetCount - 1).toString()
        val response =  stmt.executeQuery(
            s"""SELECT CONCAT(name, " (", id, ") came out in ", first_air_date) FROM $defaultTableName"""
        )
        while (response.next()) {
            System.out.println(
                response.getString(1)
            )
        }
    }
}


object showLookup{
    val defaultTableName = "tvshows_search"

    def getAPIQueryData(stmt: Statement, searchParam: String): Unit = {
        val newestTable = "query" + API.urlGetCount.toString()
        val temp = searchParam.replaceAll(" ", "%20")
        API.storeURLData( stmt, newestTable,    s"https://api.themoviedb.org/3/search/tv?api_key=${API.apiKey}" +
                                                s"&language=en-US&page=1&query=$temp&include_adult=true")    //oldest movies (first 4 are mistaken entries)
    }

        def storeInProperTable(stmt: Statement, properTableName: String = defaultTableName): Unit = {
        
        val newestTable = "query" + (API.urlGetCount - 1).toString()        
        
        println(s"Creating table $properTableName from $newestTable..")
        stmt.execute(
            "CREATE TABLE IF NOT EXISTS " + properTableName + " AS SELECT get_json_object(json,'$.name') AS name, " +
            "CAST(get_json_object(json,'$.id') AS Int) AS id, get_json_object(json,'$.first_air_date') AS first_air_date" +
            " FROM " + newestTable
        )
        println(s"$properTableName successfully created!")
    }

    def printTempTable(stmt: Statement): Unit = {
        val num = (API.urlGetCount - 1).toString()
        val response =  stmt.executeQuery(
                            s"SELECT * FROM query$num"
                        )
        if (response.next()) {
            System.out.println(response.getString(1))
        }
    }
    
    def printBasicRealTable(stmt: Statement): Unit = {
        val num = (API.urlGetCount - 1).toString()
        val response =  stmt.executeQuery(
            s"SELECT * FROM $defaultTableName"
        )
        while (response.next()) {
            System.out.println(
                response.getString(1) + "\t" + response.getString(2) + "\t" + response.getString(3)
            )
        }
    }

    def printFormattedRealTable(stmt: Statement): Unit = {
        val num = (API.urlGetCount - 1).toString()
        val response =  stmt.executeQuery(
            s"""SELECT CONCAT(name, " (", id, ") came out in ", first_air_date) FROM $defaultTableName"""
        )
        while (response.next()) {
            System.out.println(
                response.getString(1)
            )
        }
    }
}