import scala.util.parsing.combinator._
  
  class ArithLogic extends RegexParsers with PackratParsers { 

    def expr: Parser[AST] = term~rep("+"~term | "-"~term | "||"~term | ">"~term | "<"~term | ">="~term | "<="~term | "=="~term) ^^ {
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
    def term: Parser[AST] = factor~rep("*"~factor  | "/"~factor | "&&"~factor) ^^ {
      case value ~ list => (value /: list) {
        case (x, "*" ~y) => Mul(x, y)
        case (x, "/" ~y) => Div(x, y)
        case (x, "&&" ~y) => And(x, y)
      }
    }


    def factor: Parser[AST] = ( 
        value
      | "("~>expr<~")" 
      | "!"~>factor ^^ { e => Not(e) }
    )

    def value: Parser[AST] = (
        "(-)?[0-9]+".r ^^ {i => Integer(i.toInt)} 
      | "true" ^^ { _ => True()}
      | "false" ^^ { _ => False()}
    )
  }
