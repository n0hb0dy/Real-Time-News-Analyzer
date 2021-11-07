package example

/*Basic*/
import scala.io.StdIn._
import scala.util.Try

/*spark-submit --packages net.liftweb:lift-json_2.11:2.6 project-one_2.11-0.1.0-SNAPSHOT.jar*/
// import example.Login
// import example.API
// import example.HDFS
// import example.Hive




object p1 {
    
    def main (args: Array[String]): Unit = {

        //var adminAccess: Boolean = Login.startLogin()
        linuxFileSystem.clear

        var stmtHandler = Hive.startSession()

        Query6.getAPIQueryData(stmtHandler, "1396")
        Query6.printTempTable(stmtHandler)


        Query6.storeInProperTable(stmtHandler)
        //Query5.printFormattedRealTable(stmtHandler)
        Query6.printBasicRealTable(stmtHandler)

        Hive.endSession
        
        
    }
}

// API.storeURLData(stmtHandler,   API.specifySortBy("popularity.desc") + 
//                                 API.specifyAirDateGTE("2020-10-31")/*  + 
//                                 API.generateAirDateGTE("2020-10-31") */)
                                