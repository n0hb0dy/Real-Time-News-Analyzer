package example
/*Hive*/
import java.io.IOException
import java.sql.SQLException
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement
import java.sql.DriverManager

object Hive {

    var con: java.sql.Connection = null;

    val currQuery: Statement = startSession()
   
    def startSession (): Statement = {

        ////val hiveClient = new HiveClient //For Hive 1
        ////val conStr hiveClient.executeHQL("SELECT * FROM trucks")
        
        var driverName = "org.apache.hive.jdbc.HiveDriver"
        val conStr = "jdbc:hive2://sandbox-hdp.hortonworks.com:10000/tv"
        try{
            Class.forName(driverName)
            con = DriverManager.getConnection(conStr, "", "")
            
            println("Database sucessfully connected . . . !")
            return con.createStatement()
        }catch{
            case ex: Throwable => ex.printStackTrace()
            throw new Exception(s"${ex.getMessage}")
        }

    }

    def endSession(): Unit = {
        try{
            if (con != null)
                con.close()
        }catch{
            case ex: Throwable => ex.printStackTrace();
            throw new Exception(s"${ex.getMessage}")
        }
    }
    
    def createRawInternalTable(statement: Statement, tableName: String): Unit = {
        println(s"Making $tableName...")
        statement.execute(
            s"""
            CREATE TABLE IF NOT EXISTS $tableName (json string)
            """
        )
        println(s"Loaded $tableName correctly!")
    }

    def loadRawInternalTable(statement: Statement, tableName: String, filePath: String): Unit ={
        println(s"Making and loading $tableName from $filePath...")
        statement.execute(
            s"""
            LOAD DATA LOCAL INPATH '${filePath}' INTO TABLE $tableName
            """
        )
        println(s"Loaded $tableName correctly!")
    }

    def loadRawExternalTable(statement: Statement, tableName: String, filePath: String): Unit ={
        println(s"Making and loading $tableName from $filePath...")
        statement.execute(
            s"""
            LOAD DATA INPATH '$filePath' INTO TABLE $tableName
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


    def loadExternalTableHDFS(statement: Statement, tableName: String, filePath: String): Unit = {
        println(s"Making and loading $tableName from $filePath...")
        statement.execute(
            s"""
            LOAD DATA INPATH '${filePath}' INTO TABLE $tableName
            """
        )
        println(s"Loaded $tableName correctly!")
    }


}



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