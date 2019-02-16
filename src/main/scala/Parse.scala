import scala.util.parsing.combinator._
import ast._
  
  class ArithLogic extends RegexParsers with PackratParsers { 

    def int: Parser[Int] = "(-)?[0-9]+".r ^^ {_.toInt}

    def iden: Parser[String] = "[a-zA-Z_][a-zA-Z0-9_]*".r

    def typ: Parser[Typ] = (
        "Int["~>int<~"]" ^^ {i => TInt(i)} 
      | "Bool" ^^ {_=>TBool()}
    )

    def arg : Parser[(String, Typ)] = iden~":"~typ ^^ { case id~":"~t => (id, t)}

    def args_all: Parser[Map[String, Typ]] = rep1(arg) ^^ { Map() ++ _ }

    def func: Parser[Expr] = "fun"~>iden~args_all~"="~expr ^^ {
      case id~args~_~expr => Func(id, args, expr)
    }

    def expr: Parser[Expr] = term~rep("+"~term | "-"~term | "||"~term | ">"~term | "<"~term | ">="~term | "<="~term | "=="~term) ^^ {
      case value ~ list => (value /: list) {
        case (x, "+" ~y) => Add(x, y)
        case (x, "-" ~y) => Sub(x, y)
        case (x, "||" ~y) => Or(x, y)
        case (x, ">" ~y) => Gt(x, y)
        case (x, "<" ~y) => Lt(x, y)
        case (x, ">=" ~y) => Ge(x, y)
        case (x, "<=" ~y) => Le(x, y)
        case (x, "==" ~y) => Eq(x, y)
      }
    }
    def term: Parser[Expr] = factor~rep("*"~factor  | "/"~factor | "&&"~factor) ^^ {
      case value ~ list => (value /: list) {
        case (x, "*" ~y) => Mul(x, y)
        case (x, "/" ~y) => Div(x, y)
        case (x, "&&" ~y) => And(x, y)
      }
    }

    def factor: Parser[Expr] = ( 
        value
      | "("~>expr<~")" 
      | "!"~>factor ^^ { e => Not(e) }
    )

    def value: Parser[Expr] = (
        int ^^ { i => Integer(i) } 
      | iden ^^ { id => Input(id) }
      | "true" ^^ { _ => True() }
      | "false" ^^ { _ => False() }
    )
  }
