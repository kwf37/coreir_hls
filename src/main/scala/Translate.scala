import argonaut._, Argonaut._


object CoreIR{

    sealed trait ValueType
    // Add function to sealed trait to translate to JSON
    case class Bool() extends ValueType
    case class Int() extends ValueType
    case class String() extends ValueType
    case class CoreIRType() extends ValueType
    case class Module() extends ValueType
    case class JSON() extends ValueType
    case class BitVector(value: scala.Int) extends ValueType

    sealed trait Type
    case class BitIn() extends Type
    case class Bit() extends Type
    case class Array(length: scala.Int, typ: Type) extends Type
    case class Record(fields: Map[java.lang.String, Type]) extends Type
    case class Named(namedref: NamedRef) extends Type

    sealed trait Value
    case class Arg(typ: ValueType, field: java.lang.String) extends Value
    case class Const(typ: ValueType, value: java.lang.String) extends Value

    type Values = Map[java.lang.String, Value]

    type Parameters = List[Param]

    type Connections = List[Connection]

    type Instances = Map[java.lang.String, Instance]

    // JSON Class Definitions
    case class NamedRef(namespace: java.lang.String, name: java.lang.String)
    case class NamedType(flippedname: java.lang.String, rawtype: Type)
    case class TypeGen(params: Parameters, generated: Map[Values, Type])


    case class Instance(genref: Option[NamedRef], genargs: Option[Values], modref: Option[NamedRef], modargs: Option[Values])//, genargs: Option[Map[String, Value]]) //TODO

    // Type of typ could be changed to Record- TODO doesn't capture everything in the schema
    case class Modul(typ: Type, instances: Option[Instances], connections: Option[List[Connection]],  modparams: Option[Parameters], defaultmodargs: Option[Values])

    case class GeneratedModule(values: Values, module: Modul)

    case class Generator(typegen: NamedRef, genparams: Parameters, defaultgenargs: Option[List[Const]], modules: List[GeneratedModule])

    case class Param(field_name: java.lang.String, field_type: ValueType)

    case class Connection(a: java.lang.String, b: java.lang.String)

    case class Namespace(namedtypes: Option[Map[java.lang.String, NamedType]], typegens: Option[Map[java.lang.String, TypeGen]], modules: Option[Map[java.lang.String, Modul]], generators: Option[Map[java.lang.String, Generator]])

    case class Top(name: NamedRef, namespaces: Map[java.lang.String, Namespace])

    // Implicit defs for JSON encoding:

    def encode_valuetype (vt: ValueType) = vt match{
            case Bool() => jString("Bool") 
            case Int() => jString("Int")
            case String() => jString("String")
            case CoreIRType() => jString("CoreIRType")
            case Module() => jString("Module")
            case JSON() => jString("Json")
            case BitVector(value: scala.Int) => jString("BitVector") -->>: jNumber(value) -->>: jEmptyArray
    }

    def encode_record (r: Record): Json = 
      jString("Record") -->>:
        (r.fields.foldLeft(jEmptyArray)((acc, kv) => (jString(kv._1) -->>: encode_type(kv._2) -->>: jEmptyArray) -->>: acc)) -->>: jEmptyArray

    def encode_type (t: Type): Json = t match{
            case BitIn() => jString("BitIn")
            case Bit() => jString("Bit")
            case Array(length: scala.Int, typ: Type) => jString("Array") -->>: jNumber(length) -->>: encode_type(typ) -->>: jEmptyArray 
            case Record(fields: Map[java.lang.String, Type])  => encode_record(Record(fields))
            case Named(namedref: NamedRef) => jString("Named") -->>: encode_namedref(namedref) -->>: jEmptyArray
    }

    def encode_namedref (nr: NamedRef) = jString(nr.namespace + "." + nr.name)

    def encode_namedtype (nt: NamedType) = ("flippedname" := nt.flippedname) ->: ("rawtype" := nt.rawtype) ->: jEmptyObject

    def encode_parameters (ps: Parameters) = ps.foldLeft(jEmptyObject)((acc:Json, p: Param) => (p.field_name := p.field_type) ->: acc)

    def encode_arg (a: Arg) = encode_valuetype(a.typ) -->>: jString("Arg") -->>: jString(a.field) -->>: jEmptyArray

    def encode_const(c: Const) = encode_valuetype(c.typ) -->>: jString(c.value) -->>: jEmptyArray

    def encode_values(vs: Values) = vs.foldLeft(jEmptyObject)((acc, kv) => (kv._1 := kv._2) ->: acc)

    def encode_connection(c: Connection) = jString(c.a) -->>: jString(c.b) -->>: jEmptyArray

    def encode_connections(cs: Connections) = cs.foldLeft(jEmptyArray)((acc, c) => encode_connection(c) -->>: acc)

    def encode_instance(i: Instance) = ("genref" :=? i.genref) ->?: ("genargs" :=? i.genargs) ->?: ("modref" :=? i.modref) ->?: ("modargs" :=? i.modargs) ->?: jEmptyObject

    def encode_instances(i: Instances) = i.foldLeft(jEmptyObject)((acc, kv) => (kv._1 := kv._2) ->: acc)

    def encode_generator(g: Generator) = ("typegen" := g.typegen) ->: ("genparams" := g.genparams) ->: ("defaultgenargs" :=? g.defaultgenargs) ->?: ("modules" := g.modules) ->: jEmptyObject
    
    def encode_module(m: Modul) = ("type" := m.typ) ->: ("modparams" :=? m.modparams) ->?: ("defaultmodargs" :=? m.defaultmodargs) ->?: ("instances" :=? m.instances) ->?: ("connections" :=? m.connections) ->?: jEmptyObject

    def encode_generatedmodule(gm: GeneratedModule) = encode_values(gm.values) -->>: encode_module(gm.module) -->>: jEmptyArray

    def encode_typegen(tg: TypeGen) = encode_parameters(tg.params) -->>: jString("sparse") -->>: tg.generated.foldLeft(jEmptyArray)((acc, kv) => (encode_values(kv._1) -->>: encode_type(kv._2) -->>: jEmptyArray) -->>: jEmptyArray)

    def encode_namespace(ns: Namespace) = ("namedtypes" :=? ns.namedtypes) ->?: ("typegens" :=? ns.typegens) ->?: ("modules" :=? ns.modules) ->?: ("generators" :=? ns.generators) ->?: jEmptyObject

    def encode_top(t: Top) = ("top" := t.name) ->: ("namespaces" := t.namespaces.foldLeft(jEmptyObject)((acc, kv) => (kv._1 := kv._2) ->: acc)) ->: jEmptyObject

    implicit def ValueTypeEncodeJSON: EncodeJson[ValueType] =
        EncodeJson(encode_valuetype)
    
    implicit def TypeEncodeJSON: EncodeJson[Type] =
        EncodeJson(encode_type)
        
    implicit def NamedRefEncodeJSON: EncodeJson[NamedRef] =
        EncodeJson(encode_namedref)
        
    implicit def NamedTypeEncodeJSON: EncodeJson[NamedType] =
        EncodeJson(encode_namedtype)
        
    implicit def ParametersEncodeJSON: EncodeJson[Parameters] =
        EncodeJson(encode_parameters)
        
    implicit def ValueEncodeJSON: EncodeJson[Value] =
        EncodeJson((v: Value) => v match{
            case Arg(typ, field) => encode_arg(Arg(typ, field))
            case Const(typ, value) => encode_const(Const(typ, value))
        })
        
    implicit def ValuesEncodeJSON: EncodeJson[Values] =
        EncodeJson(encode_values)
        
    implicit def ConstsEncodeJSON: EncodeJson[Const] =
        EncodeJson(encode_const)

    implicit def InstanceEncodeJSON: EncodeJson[Instance] =
        EncodeJson(encode_instance)

    implicit def ConnectionsEncodeJSON: EncodeJson[Connections] =
        EncodeJson(encode_connections)

    implicit def ModuleEncodeJSON: EncodeJson[Modul] =
        EncodeJson(encode_module)

    implicit def GeneratedModuleEncodeJSON: EncodeJson[GeneratedModule] =
        EncodeJson(encode_generatedmodule)

    implicit def GeneratorEncodeJSON: EncodeJson[Generator] =
        EncodeJson(encode_generator)

    implicit def TypeGenEncodeJSON: EncodeJson[TypeGen] =
        EncodeJson(encode_typegen)

    implicit def NamespaceEncodeJSON: EncodeJson[Namespace] =
        EncodeJson(encode_namespace)
      
      

}