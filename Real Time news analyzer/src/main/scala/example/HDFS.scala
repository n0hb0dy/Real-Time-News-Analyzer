package example
/*HDFS*/
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import java.io.PrintWriter
import scala.io.Source;
import java.io.File;
import java.io._;
import scala.io.StdIn._
import scala.sys.process._          // for the clear function
/*For JSON Encoding and Decoding*/
// import net.liftweb._
// import net.liftweb.json._

object HDFS {
  

    def copyFromLocal(oldFilePath: String, newFilePath: String): Unit = {
        println(s"Copying local file ...")
        
        val conf = new Configuration()
        val fs = FileSystem.get(conf)
        
        val localpath = new Path(oldFilePath)
        val hdfspath = new Path(newFilePath)
        
        fs.copyFromLocalFile(false, localpath, hdfspath)

        println(s"Done copying local file $oldFilePath to $newFilePath ...")
    }

    def overwriteHDFSFile(filePath: String, dataString: String): Unit = {
        
        println(s"Creating file $filePath ...")
        
        val conf = new Configuration()
        val fs = FileSystem.get(conf)
      
        // Check if file exists. If yes, delete it.
        println("Checking if it already exists...")
        
        val filepath = new Path(filePath)
        val isExisting = fs.exists(filepath)
        
        if(isExisting) {
            println("Yes it does exist. Deleting it...")
            fs.delete(filepath, false)
        }

        val output = fs.create(new Path(filePath))

        val writer = new PrintWriter(output)
        writer.write(dataString)
        writer.close()
        
        println(s"Done creating file $filePath ...")
        
    }
}

object linuxFileSystem{

    def newFile(filePath: String, dataString: String): Unit = {
        // val file = new java.io.File(filePath)
        // val source = scala.io.Source.fromFile(file)
        // var writeHandler = new FileWriter(file, false)
        println(s"Making new file at $filePath")
        val writerHandler = new PrintWriter(new File(filePath))
        writerHandler.write(dataString)

        writerHandler.close()
    }

    def overWriteFile(filePath: String, dataString: String): Unit = {

        println(s"Overwriting file at $filePath")

        val isExisting = new java.io.File(filePath).isFile
        
        if(isExisting) {
            //println("Yes it does exist. Deleting it...")
            new File(filePath).delete()
        }

        val writerHandler = new PrintWriter(new File(filePath))
        writerHandler.write(dataString)

        writerHandler.close()
    }

    def getCSVFile(filePath: String): Array[String] = {

        val src = scala.io.Source.fromFile(filePath)
        var str = try src.mkString finally src.close()

        return str.split("\n")
    }

    def clear() : Unit = {
        "clear".!
        // print("\u001b[2J")   ???
        Thread.sleep(1000)
    }
}



// print(net.liftweb.json.parse(popularShows).find(_==net.liftweb.json.parse("a")))
    //println(JsonAST.render(all_docs))
    //println(JsonAST.render(all_docs.extract[String]))
// def newJsonFile(filePath: String, dataString: String): Unit = {
//         // val file = new java.io.File(filePath)
//         // val source = scala.io.Source.fromFile(file)
//         // var writeHandler = new FileWriter(file, false)
//         println(s"Making new file at $filePath")
//         val writerHandler = new PrintWriter(new File(filePath))
//         writerHandler.write(dataString)

//         writerHandler.close()
//     }

//     def getJsonFile(filePath: String): Map[String, String] = {
//         // val source = scala.io.Source.fromFile(file)
//         // var writeHandler = new FileWriter(file, false)
//         println(s"Reading JSON File at $filePath")
        
//         val src = scala.io.Source.fromFile(filePath)
//         val userString = try src.mkString finally src.close()
//         print(userString)


//         return net.liftweb.json.Extraction.flatten(net.liftweb.json.parse(userString))
        
        
//     // }