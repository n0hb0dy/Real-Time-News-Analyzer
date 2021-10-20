package example

import scala.io.Source._
import scala.collection.immutable._
import scala.jdk.CollectionConverters._
import org.mongodb.scala._
import com.mongodb.client._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Projections._
 import org.mongodb.scala.model.Updates._
import com.mongodb.client.model.UpdateOptions
import net.liftweb.json._
//import java.util.Scanner._
import example.Helpers._
import example.ProjectZero._

object Definitions {
    
    implicit val formats = DefaultFormats  // For Json Parser
    
    var tableHandler = new Table("demo","VehicleSales")         // See if this can be used indefinitly
    //val (db, c) = promptTable()          // Comment over for debugging with metals (skips initial user input)
    
    tableHandler.fileImport
    scala.io.StdIn.readLine()            // To pause


    def promptEorU(startingVins: Seq[String]): Unit = {

        var repeat: Boolean = false
        var encryptionHandler = new EncryptedVin(Seq[String]())
        var vinHandler = new Vin(Seq[String]())

        while (!repeat) {
            println("\nWhat do you want to do?")
            println("Encrypt All (E)? Store All(S)? Clear All (C)? Unencrypt All (U)? Quit (Q)?")

            scala.io.StdIn.readLine().toUpperCase match {                     // Remove for testing
                case "E" =>     {
                                 println("\nEncrypting VehicleSales Collection...")
                                 encryptionHandler = new EncryptedVin(startingVins)
                                 for(x <- encryptionHandler.encryptedVins) println(x)
                                 //var tempTable= new Table("demo","EncryptedVins")
                                 tableHandler.update("demo","EncryptedVins")
                                 Thread.sleep(550)
                                 println("\nStoring in encryption table...")
                                 tableHandler.tryInsertMany(encryptionHandler.reconstructMany(encryptionHandler.encryptedVins))
                                }
                case "U" =>     {
                                 println("\nUnencrypting From EncryptedVins...")
                                 //var tempTable = new Table("demo","EncryptedVins")
                                 tableHandler.update("demo","EncryptedVins")
                                 Thread.sleep(550)
                                 var temp = tableHandler.collection.find().projection(fields(include("vin"))).results()
                                 for(doc <- temp) yield println(doc.toJson())
                                 encryptionHandler = new EncryptedVin(for(doc <- temp) yield doc.toJson())
                                 //tempTable = new Table("demo","UnencryptedVins")
                                 tableHandler.update("demo","UnencryptedVins")
                                 Thread.sleep(550)
                                 //for(x<-encryptionHandler.unencryptedVins) println(x)
                                 tableHandler.tryInsertMany(encryptionHandler.reconstructMany(encryptionHandler.unencryptedVins))
                                }
                case "S" =>     {
                                 println("\nStoring encrypted keys in VehicleSales...")
                                 //var tempTable = new Table("demo","EncryptedVins")
                                 tableHandler.update("demo","EncryptedVins")
                                 Thread.sleep(550)
                                 var temp = tableHandler.collection.find().projection(fields(include("vin"))).results()
                                 vinHandler = new Vin(for(doc <- temp) yield doc.toJson())
                                 //tempTable = new Table("demo","VehicleSales")
                                 tableHandler.update("demo","VehicleSales")
                                 Thread.sleep(550)
                                 tableHandler.tryUpdateCollection(vinHandler.values)
                                 for (value<-vinHandler.values) println(value)
                                }
                case "C" =>     {
                                 println("\nClearing VehicleSales...")
                                 //var tempTable = new Table("demo","VehicleSales")
                                 tableHandler.update("demo","VehicleSales")
                                 Thread.sleep(550)
                                 tableHandler.tryDeleteCollection()
                                }
                case "Q" =>     repeat = true
                case _ =>       println("Invalid Entry!"); scala.io.StdIn.readLine()
            }

        (1 to 5).foreach(x => println(""))

        }

    }

    def promptTable(): (String, String) = {
        Thread.sleep(550)  // To get Mogno Connection messages out of the way
        println("\nWhat database and collection do you want to use?")
        val db = scala.io.StdIn.readLine("Database::   ")
        val c = scala.io.StdIn.readLine("Collection::   ")
        println(s"\nCreating $c in $db . . . \n")
        return (db, c)
    }


    class Table (dataBase: String, collectionName: String) {

        var client: MongoClient = MongoClient()
        var database: MongoDatabase = client.getDatabase(dataBase)
        var collection: MongoCollection[Document] = database.getCollection(collectionName)
        
        // val fileStream = getClass.getResource("""./VehicleSales.json""")
        // var importDocuments: List[String]= scala.io.Source.fromURL(fileStream).getLines().toList
        val filePath = """C:/Users/98jjm/OneDrive/Documents/Revature_Code/ProjectZERO/projectzero/src/main/resources/VehicleSales.json"""
        var importDocuments = scala.io.Source.fromFile(filePath).getLines().toList

        def this (dataBase: String) {
            this (dataBase, "temp")
            println("Did not provide a table name so used `temp`...")
        }

        def update(dataBase: String, collectionName: String) {
            //this.client = MongoClient()
            this.database = client.getDatabase(dataBase)
            this.collection = database.getCollection(collectionName)
        }

        def fileImport = {
            try{collection.insertMany(importDocuments.map(doc=>Document(doc))).printResults()}
            catch{case _: Throwable => println(" . . . ")}

            println(s"\nRetrieving file from $filePath")
            println("Ready to Work with Collection")
            println(" . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ")
        }

        def tryInsert() = {
            // 
        }

        def tryInsertMany(documents: Seq[String]) = {
            try{collection.insertMany(documents.map(doc=>Document(doc))).printResults()}
            catch{case _: Throwable => println(" . . . ")}   
        }

        def tryDeleteCollection(){
            try{collection.deleteMany(gte("_id", 0)).printResults()}
            catch{case _: Throwable => println(" . . . ")}   
        }

        def tryUpdateCollection(updateStrings: Seq[String]){
            try{var i = 0
                for(vin<-updateStrings) {
                    i+=1
                    collection.updateMany(equal("_id", i), set("vin", vin))
                }
            }
            catch{case _: Throwable => println(" . . . ")}   
        }

    }

    class Vin (documentStringSeq: Seq[String]) {
        
        val KEY: String = "vin"
        val values: Seq[String] = for (vin <- documentStringSeq) yield parseSingleValue(vin, KEY)


        def parseSingleValue (documentString: String, key: String): (String) = {
            return (JsonParser.parse(documentString) \ key).extract[String]
        }
        
        def print (documentStringSeq: Seq[String]): Unit = {
            documentStringSeq.foreach(vin => println(vin))
        }

        def print (documentStringSeq: String): Unit = {
            println(documentStringSeq)
        }

        def printValues (documentStringSeq: Seq[String]): Unit = {
            documentStringSeq.foreach(vin => println(parseSingleValue(vin, KEY)))
        }

        def toSeq: Seq[String] = {
            return documentStringSeq
        }

    }

    class EncryptedVin (vinStringSeq: Seq[String]) {

        val KEY: String = "vin"
        val encryptedVins: Seq[String] = encrypt
        val unencryptedVins: Seq[String] = unencrypt

        def parseSingleValue (documentString: String, key: String): String = {
            return (JsonParser.parse(documentString) \ key).extract[String]
        }
        
        def encrypt: Seq[String] = {
            for (vin <- vinStringSeq) yield encryptString3Rail(parseSingleValue(vin, KEY))
        }

        def unencrypt: Seq[String] = {
            for (vin <- vinStringSeq) yield unencryptString3Rail(parseSingleValue(vin,KEY))
        }

        def reconstructMany(valueSeq: Seq[String]): Seq[String] = {
            // could have  Seq[Document] return for(doc <- valueSeq) yield Document("{$KEY:$doc}")
            //for(docValue <- valueSeq) yield (s"""{"$KEY":"$docValue"}""")
            var i = 0
            return valueSeq.map(docValue => {
                i+=1
                s"""{"_id": $i, "$KEY":"$docValue"}""" 
            })

        }
        
        //Uses railroad encryption/unencryption; diffusion in the future update  /// hahahaha there is no future update, actually :)
        def encryptString3Rail(s: String): String = {
            var (t, i) = (1, 0)
            var (str1, str2, str3) = ("","","")

            while (t<=3) {
                t match {
                    case 1  =>  while(i<s.length()) {str1 = str1.concat(s.charAt(i).toString()); i+=4}
                    case 2  =>  i = 1; while(i<s.length()) {str2 = str2 + s.charAt(i).toString(); i+=2}
                    case 3  =>  i = 2; while(i<s.length()) {str3 = str3 + s.charAt(i).toString(); i+=4}
                    case _ => "Invalid rail"
                }

                t+=1
            }

            return str1 + str2 + str3
        }

        def unencryptString3Rail(s: String): String = { 
            var (t, i, c) = (1, 0, 0)
            var decryptedString= Array.fill(s.length){'-'}

            while (t<=3) {
                t match {
                    case 1  =>  while(i<s.length()){decryptedString(i) = s.charAt(c); i+=4; c+=1}
                    case 2  =>  i = 1; while(i<s.length()) {decryptedString(i) = s.charAt(c); i+=2; c+=1}
                    case 3  =>  i = 2; while(i<s.length()) {decryptedString(i) = s.charAt(c); i+=4; c+=1}
                    case _ => "Invalid rail"
                }

                t+=1
            }

            return decryptedString.mkString
        }

    }
}