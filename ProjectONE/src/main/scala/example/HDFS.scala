package example
/*HDFS*/
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import java.io.PrintWriter

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

    def overwriteFile(filePath: String, dataString: String): Unit = {
        
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
