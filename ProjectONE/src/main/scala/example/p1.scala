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

        var stmtHandler = Hive.startSession()

        API.storeShowDetails(stmtHandler, 1396)
        // Thread.sleep(5000)

        Hive.endSession
    }
}

case class User (userName: String, passWord: String, accessLevel: String)