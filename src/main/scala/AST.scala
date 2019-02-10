sealed trait AST

case class Eq(e1: AST, e2: AST) extends AST
case class Lt(e1: AST, e2: AST) extends AST 
case class Gt(e1: AST, e2: AST) extends AST 
case class Le(e1: AST, e2: AST) extends AST 
case class Ge(e1: AST, e2: AST) extends AST 
case class Add(e1: AST, e2: AST) extends AST 
case class Sub(e1: AST, e2: AST) extends AST 
case class Mul(e1: AST, e2: AST) extends AST 
case class Div(e1: AST, e2: AST) extends AST 

case class And(e1: AST, e2: AST) extends AST 
case class Or(e1: AST, e2: AST) extends AST 
case class Not(e: AST) extends AST 

sealed trait Value extends AST

case class True() extends AST
case class False() extends AST
case class Integer(int: Int) extends AST

