package example
import java.sql.Statement

object Login {
    
    var userName = ""
    var passWord = ""
    var adminPrivalege: Boolean = false         //False means BASIC
    var loginSuccess = false

    

    def startLogin (): Boolean = {

        val usersString = linuxFileSystem.getCSVFile("/tmp/passwrd/users.csv")  //JJ,12e45,ADMIN

        userName = scala.io.StdIn.readLine("Enter Username  >> ")
        val currentUserInfo = usersString.filter(_.startsWith(userName)).head       //returns the first result of what was entered
        
        if (currentUserInfo.startsWith(userName)) loginSuccess = true else println("No user by that name . . . ")
        
        if (loginSuccess == false) startLogin() 
        loginSuccess = false

        // while(scala.io.StdIn.readLine(s"Enter the password for $userName  >> ").toCharArray()))   if (currentUserInfo.contains(
        //                                                 ) loginSuccess = true 
        //                     else println("Wrong Password . . . ") 

        do {
            var tryCount = 0
            passWord = scala.io.StdIn.readLine(s"Enter the password for $userName  >> ")
            if (currentUserInfo.contains(passWord.toCharArray())) loginSuccess = true else println("Wrong Password . . . ") 
            
            tryCount+=1
            if (loginSuccess == false && tryCount >=10) startLogin() 

        } while (loginSuccess == false) 

        return currentUserInfo.contains("BASIC")
    }

    def changeAccessLevel (): Unit = {

        val usersString = linuxFileSystem.getCSVFile("/tmp/passwrd/users.csv")  //JJ,12e45,ADMIN
        val userName = scala.io.StdIn.readLine("Enter Username  >> ")
    }

    def mainMenu (stmt: Statement): Boolean = {

        println("What would you want to do? >> ")
        scala.io.StdIn.readLine("Show Recent Shows? (r) " +
                                "Search for TV? (s)" +
                                "Edit Users? (e)" +
                                "More? (m)").toLowerCase match {
            case r => {
                Query1.getAPIQueryData(stmt)
                if (scala.io.StdIn.readLine("Would you like to store the data in a table (y/n)?").toLowerCase == "y") Quer1.storeInProperTable()
                else 
            }
            case s => ;
            case e => ;
            case m => ;
        }

        return true
    }

    //case class User (userName: String, passWord: String, accessLevel: String)

}
