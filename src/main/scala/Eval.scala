import ast._

sealed trait Result
case class Val(v: Value) extends Result
case class Exn(msg: java.lang.String) extends Result

class Interpreter{
  private var instanceNo = 0
  

  def eval (e: Expr) : Result =
    e match {
      case Eq(eq, e2) => Exn("Unimplemented")
    }
}

