package example
import java.sql.Statement

object Login {
    
    var userName = ""
    var passWord = ""
    var adminPrivalege: Boolean = false         //False means BASIC
    var loginSuccess = false

    var escape = false

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

        return currentUserInfo.contains("ADMIN")
    }

    def changeAccessLevel(user: String): Unit ={
        val tempPassword = scala.io.StdIn.readLine("Enter new password >> ")
        var al = "BASIC"
        if (tempPassword != scala.io.StdIn.readLine("Enter new password again >> ")) {println("Wrong Password"); changeAccessLevel(user)}

        if (scala.io.StdIn.readLine("Do you want this user to be admin too? (y/n) >> ").toLowerCase == "y") al = "ADMIN"

        val usersString = linuxFileSystem.getCSVFile("/tmp/passwrd/users.csv")
        val newUsersString = (usersString.map(u => if (u.contains(user)) s"$user,$tempPassword,$al" else u)).mkString("\n")

        linuxFileSystem.overWriteFile("/tmp/passwrd/users.csv", newUsersString)
    }

    def addUser(user: String): Unit ={
        val tempPassword = scala.io.StdIn.readLine("Enter new password >> ")
        var al = "BASIC"
        if (tempPassword != scala.io.StdIn.readLine("Enter new password again >> ")) {println("Wrong Password"); changeAccessLevel(user)}

        if (scala.io.StdIn.readLine("Do you want this user to be admin too? (y/n) >> ").toLowerCase == "y") al = "ADMIN"

        val usersString = linuxFileSystem.getCSVFile("/tmp/passwrd/users.csv")
        val newUsersString = (usersString :+ s"$user,$tempPassword,$al" ).mkString("\n")

        linuxFileSystem.overWriteFile("/tmp/passwrd/users.csv", newUsersString)
    }

    def removeUser(user: String): Unit ={

        val usersString = linuxFileSystem.getCSVFile("/tmp/passwrd/users.csv")
        val newUsersString = (usersString.filterNot(u => u.contains(user))).mkString("\n")

        linuxFileSystem.overWriteFile("/tmp/passwrd/users.csv", newUsersString)
    }

    def usersPage (stmt: Statement, al: Boolean): Unit = {

        val usersString = linuxFileSystem.getCSVFile("/tmp/passwrd/users.csv")  //JJ,12e45,ADMIN
        escape = false
        while(escape == false) {
            println("What would you like to do?")
            scala.io.StdIn.readLine("\nChange Users?(c) " +
                                    "Change user info?(i) " +
                                    "Add new user?(a) " +
                                    "Remove User?(r) " +
                                    "Quit?(q) ").toLowerCase match {
                case "c" => {mainMenu(stmt, startLogin())}
                case "l" => {
                    if (al == false) println("This user does not have access to these functions . . . ")
                    else {
                        val user = scala.io.StdIn.readLine("Enter Username  >> ")
                        if (!(usersString.contains(user))) println("Cannot find a user by that username . . . ")
                        else changeAccessLevel(user)
                    }
                }
                case "a" => {
                    if (al == false) println("This user does not have access to these functions . . . ")
                    else {
                        val user = scala.io.StdIn.readLine("Enter Username  >> ")
                        if ((usersString.contains(user))) println("User already exists  . . . ")
                        else addUser(user)
                    }
                }                        
                case "r" => {
                    if (al == false) println("This user does not have access to these functions . . . ")
                    else {
                        val user = scala.io.StdIn.readLine("Enter Username  >> ")
                        if (!(usersString.contains(user))) println("Cannot find a user by that username . . . ")
                        else removeUser(user)
                    }    
                }
                case "q" => escape = true 
                case _ => println("Invalid entry . . . ")                 
                }
            }
        mainMenu(stmt, al)
    }

    def mainMenu (stmt: Statement, al: Boolean): Boolean = {

        escape = false
        while(escape == false) {
            println("What would you want to do? >> ")
            scala.io.StdIn.readLine("\nShow Recent Shows?(r) " +
                                    "Oldest Shows?(o) " +
                                    "Great Free Shows?(g) " +
                                    "Show Lookup(l) " +
                                    "More?(m) " +
                                    "Quit?(q) ").toLowerCase match {
                case "r" => {
                    Query1.getAPIQueryData(stmt)
                    if (scala.io.StdIn.readLine("Would you like to store the data in a table (y/n)?").toLowerCase == "y") { Query1.storeInProperTable(stmt); Query1.printFormattedRealTable(stmt) }
                    else Query1.printTempTable(stmt)
                }
                case "o" => { 
                    Query2.getAPIQueryData(stmt)
                    if (scala.io.StdIn.readLine("Would you like to store the data in a table (y/n)?").toLowerCase == "y") { Query2.storeInProperTable(stmt); Query2.printFormattedRealTable(stmt) }
                    else Query2.printTempTable(stmt)
                }
                case "g" => { 
                    Query3.getAPIQueryData(stmt)
                    if (scala.io.StdIn.readLine("Would you like to store the data in a table (y/n)?").toLowerCase == "y") { Query3.storeInProperTable(stmt); Query3.printFormattedRealTable(stmt) }
                    else Query3.printTempTable(stmt)
                }
                case "l" => { 

                    showLookup.getAPIQueryData(stmt, scala.io.StdIn.readLine("What show would you like to look up? >> "))
                    if (scala.io.StdIn.readLine("Would you like to store the data in a table (y/n)?").toLowerCase == "y") { showLookup.storeInProperTable(stmt); showLookup.printFormattedRealTable(stmt) }
                    else showLookup.printTempTable(stmt)
                }
                case "m" => mainMenuCont(stmt, al)
                case "q" => escape = true
                case _ => println("Invalid entry . . . ")

            }
        }
        return true
    }

    def mainMenuCont(stmt: Statement, al: Boolean): Boolean = {

        escape = false
        while(escape == false) {
            scala.io.StdIn.readLine("\nCast?(c) " +
                                    "Seasons?(s) " +
                                    "Episodes?(e) " +
                                    "User Control?(u) " +
                                    "Back?(b) " +
                                    "Quit?(q) ").toLowerCase match {
                case "c" => {
                    Query4.getAPIQueryData(stmt, scala.io.StdIn.readLine("What is the show ID? >> "))
                    if (scala.io.StdIn.readLine("Would you like to store the data in a table (y/n)?").toLowerCase == "y") { Query4.storeInProperTable(stmt); Query4.printFormattedRealTable(stmt) }
                    else Query4.printTempTable(stmt)
                }
                case "s" => { 
                    Query5.getAPIQueryData(stmt, scala.io.StdIn.readLine("What is the show ID? >> "))
                    if (scala.io.StdIn.readLine("Would you like to store the data in a table (y/n)?").toLowerCase == "y") { Query5.storeInProperTable(stmt); Query5.printFormattedRealTable(stmt) }
                    else Query5.printTempTable(stmt)
                }
                case "e" => { 
                    Query6.getAPIQueryData(stmt)
                    if (scala.io.StdIn.readLine("Would you like to store the data in a table (y/n)?").toLowerCase == "y") { Query6.storeInProperTable(stmt); Query6.printFormattedRealTable(stmt) }
                    else Query6.printTempTable(stmt)
                }
                case "u" => { 
                    usersPage(stmt, al)
                }
                case "b" => mainMenu(stmt, al)
                case "q" => escape = true
                case _ => println("Invalid entry . . . ")
            }
        }
        return true
    }

    //case class User (userName: String, passWord: String, accessLevel: String)

}
