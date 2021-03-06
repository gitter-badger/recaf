
<img src="/resources/recaf.png" width="150">

[![Build Status](https://travis-ci.org/cwi-swat/recaf.svg?branch=master)](https://travis-ci.org/cwi-swat/recaf)

Our paper _Recaf: Java Dialects as Libraries_ will be presented at the _15th International Conference on Generative Programming: Concepts & Experience_ ([GPCE'16](http://conf.researchr.org/home/gpce-2016)) in Amsterdam (preprint to be available soon).

_Recaf_ is an open-source framework for authoring extensions (let's call them _dialects_) as libraries for Java,. You can redine every syntactic element of the language, add new ones and create your own flavor of Java that matches your needs. It can be used to give syntactic support to libraries, to generate code, to instrument code and experiment with ideas that involve the manipulation of the semantics of Java programs. 

The key point is that recaf transforms code at compile time, applying a predefined set of rewrite rules (no need to hack around it). The user does not get involved with parsers, language workbenchs and compilers.

### Hello World with a simple example!

Imagine we want to create our own try-with-resources statement for Java! Let's call it ```using```.
```Java
String method(String path)  {
  using (BufferedReader br : new BufferedReader(new FileReader(path))){ 
    return br.readLine();
  }
}
```

The code above is not valid Java, but it can, most certainly be through Recaf! Using two small additions, we enable the generic transformation of that snippet of code into something that we can override and define. In our case, our goal is to define what ```using``` does. So firstly, we decorate ```method``` with the ```recaf``` keyword. That enables the generic translation of our code. Secondly, we use a special field that we also decorate with the same keyword. By doing that we enable an extension for the scope of the whole compilation unit (the are other ways as well). Now every recaffeinated method uses the ```Using``` extension. As you may have guessed this object defines the behavior of our new keyword.

```Java
recaf Using<String> alg = new Using<String>();

recaf String method(String path)  {
  using (BufferedReader br : new BufferedReader(new FileReader(path))){ 
    return br.readLine();
  }
}
```

Without diving into the gory details of Recaf, the body of the method is transformed into method invocations to the ```Using``` object above (named ```alg```). Note that this is valid Java now.
```Java
String method(String path) {
  return alg.Method(
    alg.Using(() -> new BufferedReader(new FileReader(path)), (BufferedReader br) -> {
      return alg.Return(() -> br.readLine());
    }));
}	
```

#### Extensions

- Manipulating control flow
  - [Async](https://github.com/cwi-swat/recaf/blob/master/recaf-runtime/src/main/java/recaf/demo/cps/Async.java)
  - [Come From](https://github.com/cwi-swat/recaf/blob/master/recaf-runtime/src/main/java/recaf/demo/cps/ComeFrom.java)
  - [Backtrack](https://github.com/cwi-swat/recaf/blob/master/recaf-runtime/src/main/java/recaf/demo/cps/Backtrack.java)
  - [Coroutines](https://github.com/cwi-swat/recaf/blob/master/recaf-runtime/src/main/java/recaf/demo/cps/Coroutine.java)
  - [Yield](https://github.com/cwi-swat/recaf/blob/master/recaf-runtime/src/main/java/recaf/demo/cps/Iter.java) (Semi-coroutines)
  - [Rx/Observable](https://github.com/cwi-swat/recaf/blob/master/recaf-runtime/src/main/java/recaf/demo/cps/StreamExt.java)
- Direct
  - [Memoization](https://github.com/cwi-swat/recaf/blob/master/recaf-runtime/src/main/java/recaf/demo/direct/Memo.java)
  - [Security](https://github.com/cwi-swat/recaf/blob/master/recaf-runtime/src/main/java/recaf/demo/direct/Security.java)
- Fully Generic 
  - [Times/Unless/Until](https://github.com/cwi-swat/recaf/tree/master/recaf-runtime/src/main/java/recaf/demo/generic)

### Getting Started

```shell
> git clone git@github.com:cwi-swat/recaf.git
> cd recaf
> ./testgen # or testnogen to skip generation
```

### Team
- Aggelos Biboudis [@biboudis](https://twitter.com/biboudis)
- Pablo Inostroza [@metalinguist](https://twitter.com/metalinguist)
- Tijs van der Storm [@tvdstorm](https://twitter.com/tvdstorm)

### Powered by Rascal 
Under the hood we use the [Rascal Metaprogramming Language](http://www.rascal-mpl.org/). It is included as a runtime dependency in the project. 


