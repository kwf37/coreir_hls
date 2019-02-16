package object coreir{

sealed trait ValueType
// Add function to sealed trait to translate to JSON
case class Bool() extends ValueType
case class Int() extends ValueType
case class String() extends ValueType
case class CoreIRType() extends ValueType
case class Module() extends ValueType
case class JSON() extends ValueType
case class BitVector(value: String) extends ValueType

sealed trait Type
case class BitIn() extends Type
case class Bit() extends Type
case class Array(length: Int, typ: Type) extends Type
case class Record(fields: Map[String, Type]) extends Type
case class Named(namedref: NamedRef) extends Type

sealed trait Value
case class Arg(typ: ValueType, field: String) extends Value
case class Const(typ: ValueType, value: String) extends Value

type Values = Map[String, Value]

// JSON Class Definitions
case class NamedRef(namespace: String, name: String)
case class NamedType(flippedname: String, rawtype: Type)
case class TypeGen(params: List[Param], generated: Option[List[(Values, Type)]])


case class Instance(genref: Option[NamedRef], genargs: Option[Values], modref: Option[NamedRef], modargs: Option[Values])//, genargs: Option[Map[String, Value]]) //TODO

// Type of typ could be changed to Record- TODO doesn't capture everything in the schema
case class Modul(typ: Type, instances: Option[Map[String, Instance]], connections: Option[List[Connection]],  modparams: Option[Param], defaultmodargs: Option[Values])

case class Generator(typegen: NamedRef, genparams: List[Param], defaultgenargs: Option[List[Const]], modules: List[Modul])

case class Param(field_name: String, field_type: ValueType)

case class Connection(a: String, b: String)

case class Namespace(namedtypes: Option[Map[String, NamedType]], typegens: Option[Map[String, TypeGen]], modules: Option[Map[String, Modul]], generators: Option[Map[String, Generator]])

case class Top(name: NamedRef, namespaces: Map[String, Namespace])

}