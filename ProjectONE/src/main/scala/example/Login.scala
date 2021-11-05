package example

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

    //case class User (userName: String, passWord: String, accessLevel: String)

}
