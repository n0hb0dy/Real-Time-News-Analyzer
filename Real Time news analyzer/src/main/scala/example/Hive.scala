package example
/*Hive*/
import java.io.IOException
import java.sql.SQLException
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement
import java.sql.DriverManager

object Hive {

    var conWrapper: Option[java.sql.Connection] = None
   
    def startSession (): Statement = {
        
        Class.forName("org.apache.hive.jdbc.HiveDriver")
        conWrapper = Some(DriverManager.getConnection("jdbc:hive2://sandbox-hdp.hortonworks.com:10000/tv", "", ""))
        
        println("Database sucessfully connected . . . !")
        return conWrapper.get.createStatement()
    }

    def endSession(): Unit = {
        
        if (conWrapper.get != None)
            conWrapper.get.close()
        
    }




    // def startSession (): Statement = {

    //     ////val hiveClient = new HiveClient //For Hive 1
    //     ////val conStr hiveClient.executeHQL("SELECT * FROM trucks")
        
    //     var driverName = "org.apache.hive.jdbc.HiveDriver"
    //     val conStr = "jdbc:hive2://sandbox-hdp.hortonworks.com:10000/tv"
    //     try{
    //         Class.forName(driverName)
    //         con = DriverManager.getConnection(conStr, "", "")
            
    //         println("Database sucessfully connected . . . !")
    //         return con.createStatement()
    //     }catch{
    //         case ex: Throwable => ex.printStackTrace()
    //         throw new Exception(s"${ex.getMessage}")
    //     }

    // }

    // def endSession(): Unit = {
    //     try{
    //         if (con != null)
    //             con.close()
    //     }catch{
    //         case ex: Throwable => ex.printStackTrace();
    //         throw new Exception(s"${ex.getMessage}")
    //     }
    // }





    // def clear(statement: Statement): Unit ={ // its not in Hive :(
    //     statement.execute(
    //         """
    //         !clear
    //         """
    //     )
    // }
    
    def createRawInternalTable(statement: Statement, tableName: String): Unit = {
        println(s"Making $tableName...")
        statement.execute(
            s"""
            CREATE TABLE IF NOT EXISTS $tableName (json string)
            """
        )
        println(s"Created $tableName correctly!")
    }

    def createRawExternalTable(statement: Statement, tableName: String): Unit = {
        println(s"Making $tableName...")
        statement.execute(
            s"""
            CREATE EXTERNAL TABLE IF NOT EXISTS $tableName (json string)
            """
        )
        println(s"Created $tableName correctly!")
    }

    def loadRawTable(statement: Statement, tableName: String, filePath: String): Unit ={
        println(s"Making and loading $tableName from $filePath...")
        statement.execute(
            s"""
            LOAD DATA LOCAL INPATH '$filePath' INTO TABLE $tableName
            """
        )
        println(s"Loaded $tableName correctly!")
    }

    def createExternalTable(statement: Statement, tableName: String) : Unit = {
        println(s"Creating table $tableName..")
        statement.execute(
            s"CREATE EXTERNAL TABLE $tableName(movieId int, title string, genre string) row format delimited fields terminated by ',';"
        )
        println(s"$tableName successfully created!")
    }

    // def makeProperTable(statement: Statement, query: String, oldTableName: String, newTableName: String) : Unit = {
    //     println(s"Creating table $newTableName from $oldTableName..")
    //     statement.execute(
    //         s"CREATE TABLE $newTableName(movieId int, title string, genre string) row format delimited fields terminated by ',';"
    //     )
    //     println(s"$tableName successfully created!")
    // }

    
}


// def loadExternalTable(statement: Statement, tableName: String, filePath: String): Unit = {
//     println(s"Making and loading $tableName from $filePath...")
//     statement.execute(
//         s"""
//         LOAD DATA INPATH '${filePath}' INTO TABLE $tableName
//         """
//     )
//     println(s"Loaded $tableName correctly!")
// }

// def createDataBase(statement: Statement, baseName: String): Unit = {
    //     println(s"Creating database $baseName..")
    //     statement.execute(
    //         s"CREATE DATABASE IF NOT EXISTS $baseName;"
    //     )
    // }

    // def createTable(statement: Statement, tableName: String) : Unit = {
    //     println(s"Creating table $tableName..")
    //     statement.execute(
    //         s"CREATE EXTERNAL TABLE IF NOT EXISTS $tableName (list_count int, Id int, title string, year int) row format delimited fields terminated by ',';"
    //     )
    // }

    // def loadExistingTable(statement: Statement, tableName: String, filePath: String) : Unit = {
    //     println(s"Loading $filePath into $tableName..")
    //     statement.execute(
    //         s"""
    //         LOAD DATA LOCAL INPATH $filePath INTO TABLE IF NOT EXISTS $tableName;
    //         """
    //     )
    // }

    


    // def showTable(statement: Statement, tableName: String) : Unit = {
    //     var sql = "show tables '" + tableName + "'"
    //     System.out.println("Running: " + sql)
    //     var result = statement.executeQuery(sql)
    //     if (result.next()) {
    //         System.out.println(result.getString(1))
    //     }
    // }

    // def getAll (tableName: String): Unit = {

    //     println(s"Executing SELECT * FROM $tableName...")
    //     var result = currQuery.executeQuery(s"SELECT * FROM $tableName")
    //     while (result.next()) {
    //         println(s"${result.getString(1)}, ${result.getString(3)}")
    //   }
    // }