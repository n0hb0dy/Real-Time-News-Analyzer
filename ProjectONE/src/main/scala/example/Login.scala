package example

object Login {
    
    var userName = ""
    var passWord = ""
    var optionSpecified = ""
    var loginSuccess = false

    def startLogin (): Unit = {
        
        userName = scala.io.StdIn.readLine("Enter Username  >> ")
        
        for(i <- 1 to 10) {
            if (userName == userName) loginSuccess = true else println("Wrong Username . . . ")
        }
        
        if (loginSuccess == false) startLogin(); loginSuccess = false
        
        passWord = scala.io.StdIn.readLine(s"Enter the password for $userName  >> ")

        for(i <- 1 to 10) if (passWord == passWord) loginSuccess = true else println("Wrong Password . . . ") 

        if (loginSuccess == false) startLogin(); loginSuccess = false
    }

    def changeAccessLevel (): Unit = {

    }
}
