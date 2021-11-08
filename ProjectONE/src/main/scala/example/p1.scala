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

        linuxFileSystem.clear
        
        var stmtHandler = Hive.startSession()
        
        var adminAccess: Boolean = Login.startLogin()
        Login.mainMenu(stmtHandler, adminAccess)

        Hive.endSession

    }
}





// Query6.getAPIQueryData(stmtHandler, "1396")
// Query6.printTempTable(stmtHandler)


// Query6.storeInProperTable(stmtHandler)
// //Query5.printFormattedRealTable(stmtHandler)
// Query6.printBasicRealTable(stmtHandler)
// API.storeURLData(stmtHandler,   API.specifySortBy("popularity.desc") + 
//                                 API.specifyAirDateGTE("2020-10-31")/*  + 
//                                 API.generateAirDateGTE("2020-10-31") */)
// import sys.process._

// val os   = new java.io.ByteArrayOutputStream
// val code = ("ls -al" #> os).!
// os.close()
// val opt  = if (code == 0) Some(os.toString("UTF-8")) else None

// println(opt.get)