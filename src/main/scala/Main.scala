import scala.io.StdIn._

import argonaut._, Argonaut._

object ParseExpr extends ArithLogic {
  
  def main(args: Array[String]){
    //repl()
    val test1: CoreIR.ValueType = CoreIR.BitVector(5)
    val testjson = test1.asJson
    println(testjson)

    val test2: CoreIR.Type = CoreIR.Record(Map("type1" -> CoreIR.BitIn(), "type2" -> CoreIR.Bit()))
    println(test2.asJson)

    val test3: CoreIR.NamedType = CoreIR.NamedType("my_type", test2)
    println(test3.asJson)
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
