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
            passWord = encryptString3Rail(passWord)
            if (currentUserInfo.contains(passWord.toCharArray())) loginSuccess = true else println("Wrong Password . . . ") 
            
            tryCount+=1
            if (loginSuccess == false && tryCount >=10) startLogin() 

        } while (loginSuccess == false) 

        return currentUserInfo.contains("ADMIN")
    }

    def changeAccessLevel(user: String): Unit ={
        var tempPassword = scala.io.StdIn.readLine("Enter new password >> ")
        var al = "BASIC"
        if (tempPassword != scala.io.StdIn.readLine("Enter new password again >> ")) {println("Wrong Password"); changeAccessLevel(user)}

        if (scala.io.StdIn.readLine("Do you want this user to be admin too? (y/n) >> ").toLowerCase == "y") al = "ADMIN"
        
        tempPassword = encryptString3Rail(tempPassword)
        val usersString = linuxFileSystem.getCSVFile("/tmp/passwrd/users.csv")
        val newUsersString = (usersString.map(u => if (u.contains(user)) s"$user,$tempPassword,$al" else u)).mkString("\n")

        linuxFileSystem.overWriteFile("/tmp/passwrd/users.csv", newUsersString)
    }

    def addUser(user: String): Unit ={
        var tempPassword = scala.io.StdIn.readLine("Enter new password >> ")
        var al = "BASIC"
        if (tempPassword != scala.io.StdIn.readLine("Enter new password again >> ")) {println("Wrong Password"); changeAccessLevel(user)}

        if (scala.io.StdIn.readLine("Do you want this user to be admin too? (y/n) >> ").toLowerCase == "y") al = "ADMIN"

        tempPassword = encryptString3Rail(tempPassword)
        val usersString = linuxFileSystem.getCSVFile("/tmp/passwrd/users.csv")
        val newUsersString = (usersString :+ s"$user,$tempPassword,$al" ).mkString("\n")

        linuxFileSystem.overWriteFile("/tmp/passwrd/users.csv", newUsersString)
    }

    def removeUser(user: String): Unit ={

        val usersString = linuxFileSystem.getCSVFile("/tmp/passwrd/users.csv")
        val newUsersString = (usersString.filterNot(u => u.startsWith(user))).mkString("\n")

        linuxFileSystem.overWriteFile("/tmp/passwrd/users.csv", newUsersString)
    }

    def showUnencrypted(){
        val usersString = linuxFileSystem.getCSVFile("/tmp/passwrd/users.csv")
        val passwords = usersString.map(u => unencryptString3Rail(u.split(",")(1))).tail

        passwords.foreach(println)
    }

    def usersPage (stmt: Statement, al: Boolean): Unit = {

        val usersString = linuxFileSystem.getCSVFile("/tmp/passwrd/users.csv")  //JJ,12e45,ADMIN
        //usersString.foreach(println)
        escape = false
        while(escape == false) {
            println("What would you like to do?")
            scala.io.StdIn.readLine("\nChange Users?(c) " +
                                    "Change user info?(i) " +
                                    "Add new user?(a) " +
                                    "Remove User?(r) " +
                                    "Show unencrypted passwords?(u) " +
                                    "Quit?(q) ").toLowerCase match {
                case "c" => {mainMenu(stmt, startLogin())}
                case "i" => {
                    if (al == false) println("This user does not have access to these functions . . . ")
                    else {
                        val user = scala.io.StdIn.readLine("Enter Username  >> ")
                        if (!(usersString.exists(x => x.contains(user)))) println("Cannot find a user by that username . . . ")
                        else changeAccessLevel(user)
                    }
                }
                case "a" => {
                    if (al == false) println("This user does not have access to these functions . . . ")
                    else {
                        val user = scala.io.StdIn.readLine("Enter Username  >> ")
                        if ((usersString.exists(x => x.contains(user)))) println("User already exists  . . . ")
                        else addUser(user)
                    }
                }                        
                case "r" => {
                    if (al == false) println("This user does not have access to these functions . . . ")
                    else {
                        val user = scala.io.StdIn.readLine("Enter Username  >> ")
                        if (!(usersString.exists(x => x.contains(user)))) println("Cannot find a user by that username . . . ")
                        else removeUser(user)
                    }    
                }
                case "u" => showUnencrypted()
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
                    if ((scala.io.StdIn.readLine("Would you like to store the data in a table (y/n)?").toLowerCase == "y") && al) { Query1.storeInProperTable(stmt); Query1.printFormattedRealTable(stmt); scala.io.StdIn.readLine; linuxFileSystem.clear }
                    else Query1.printTempTable(stmt)
                }
                case "o" => { 
                    Query2.getAPIQueryData(stmt)
                    if ((scala.io.StdIn.readLine("Would you like to store the data in a table (y/n)?").toLowerCase == "y") && al) { Query2.storeInProperTable(stmt); Query2.printFormattedRealTable(stmt); scala.io.StdIn.readLine; linuxFileSystem.clear }
                    else Query2.printTempTable(stmt)
                }
                case "g" => { 
                    Query3.getAPIQueryData(stmt)
                    if ((scala.io.StdIn.readLine("Would you like to store the data in a table (y/n)?").toLowerCase == "y") && al) { Query3.storeInProperTable(stmt); Query3.printFormattedRealTable(stmt); scala.io.StdIn.readLine; linuxFileSystem.clear }
                    else Query3.printTempTable(stmt)
                }
                case "l" => { 

                    showLookup.getAPIQueryData(stmt, scala.io.StdIn.readLine("What show would you like to look up? >> "))
                    if ((scala.io.StdIn.readLine("Would you like to store the data in a table (y/n)?").toLowerCase == "y") && al) { showLookup.storeInProperTable(stmt); showLookup.printFormattedRealTable(stmt); scala.io.StdIn.readLine; linuxFileSystem.clear }
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
                    if ((scala.io.StdIn.readLine("Would you like to store the data in a table (y/n)?").toLowerCase == "y") && al) { Query4.storeInProperTable(stmt); Query4.printFormattedRealTable(stmt); scala.io.StdIn.readLine; linuxFileSystem.clear }
                    else Query4.printTempTable(stmt)
                }
                case "s" => { 
                    Query5.getAPIQueryData(stmt, scala.io.StdIn.readLine("What is the show ID? >> "))
                    if ((scala.io.StdIn.readLine("Would you like to store the data in a table (y/n)?").toLowerCase == "y") && al) { Query5.storeInProperTable(stmt); Query5.printFormattedRealTable(stmt); scala.io.StdIn.readLine; linuxFileSystem.clear }
                    else Query5.printTempTable(stmt)
                }
                case "e" => { 
                    Query6.getAPIQueryData(stmt)
                    if ((scala.io.StdIn.readLine("Would you like to store the data in a table (y/n)?").toLowerCase == "y") && al) { Query6.storeInProperTable(stmt); Query6.printFormattedRealTable(stmt); scala.io.StdIn.readLine; linuxFileSystem.clear }
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
