# CoreIR Notes
CoreIR is an intermediate hardware representation. The coreir C library lets users define Modules and Generators.

## Modules- Using C bindings
Each coreIR module must have the following basic features:

* Context- object that stores all namespaces and types, and performs memory management. 
  Modules are kept in Contexts. Context should be deleted at the end of a coreIR program. 
  By default all Contexts contain the "coreir" namespace which features the coreir primitives.
* Port definition- The input and outputs of the module must be defined in a Record Type.
* Module Declaration- Modules need to be declared before they can be defined.
* Definition- Module definitions are created under the ModuleDef type. 
  Definition is assigned to a module using the setDef() function
* CoreIR primitives- These basic constructs can then be added to a ModuleDef using the addInstance function. 
  These are kind of the basic building blocks of coreIR.
* Connections- The basic components come with interfaces, and connections must be made between modules. 
  These connections are made using the ModuleDef connect() function.
  
At a high level, the modules in coreIR contain 
1. I/O definitions (a type interface)
2. Instances (what components is it composed of)
3. Connections (how are sub components wired together)


## Generators
Generators are the coreIR framework for parameterizing modules. For example, a counter module can
be parameterized on its output bit width and whether it has enable/reset. To do this, one has to
create a generator for the counter module.

To define a generator, one must do the following:

1. Define a set of Params, which is an object formatted like a record.
2. Define an instance of TypeGen. The important part of a TypeGen object is a function from Params->Type,
   where Params must match the Params object defined in step 1. The Type returned is a Record, and defines
   the ports for the generated module. At a high level, TypeGen takes in Params and returns a port definition.
3. Create a Generator object that takes in a Params and a TypeGen. At this stage, modules can be instanced in larger modules without
   definitions. However, it would be more useful if the definitions could be generated as well.
4. Call the setGeneratorDefFromFun() function on your Generator object. This is where you can implement your parameterized module
   definition.
5. To put it all together, in a top level you would define Params, TypeGen, and Generator, then use the Generator's
   getModule(Param args) function to instance modules, then call runGenerator() on the instanced modules
