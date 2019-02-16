package ast

sealed trait Expr

case class Eq(e1: Expr, e2: Expr) extends Expr
case class Lt(e1: Expr, e2: Expr) extends Expr 
case class Gt(e1: Expr, e2: Expr) extends Expr 
case class Le(e1: Expr, e2: Expr) extends Expr 
case class Ge(e1: Expr, e2: Expr) extends Expr 
case class Add(e1: Expr, e2: Expr) extends Expr 
case class Sub(e1: Expr, e2: Expr) extends Expr 
case class Mul(e1: Expr, e2: Expr) extends Expr 
case class Div(e1: Expr, e2: Expr) extends Expr 

case class And(e1: Expr, e2: Expr) extends Expr 
case class Or(e1: Expr, e2: Expr) extends Expr 
case class Not(e: Expr) extends Expr 

case class Func(id: String, args: Map[String, Typ], body: Expr) extends Expr

sealed trait Value extends Expr

case class True() extends Expr
case class False() extends Expr
case class Integer(int: Int) extends Expr
case class Input(name: String) extends Expr

sealed trait Typ

case class TInt(width: Int) extends Typ
case class TBool() extends Typ



