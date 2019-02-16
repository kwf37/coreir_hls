import scala.io.StdIn._

object ParseExpr extends ArithLogic {
  def main(args: Array[String]){
    //repl()
  }

  def repl():Unit = {
    println("")
    print(">>>")
    val input = readLine().trim()
    if(input == "quit" || input == "exit"){
      println("Exiting REPL")
      System.exit(0)
    }else{
      println("Input : " + input)
      parseAll(func, input) match {
        case Success(result, _) => println(result)
        case failure : NoSuccess => scala.sys.error(failure.msg)
      }
      repl()
    }
  }
}
