# Exploring Play 2 Async with Scala and Java

## Motivation

This project started out as an exercise in understanding the sample code in Yevgeniy Brinkman's
blog post, "[Play Framework:  async I/O without the thread pool and callback hell](http://engineering.linkedin.com/play/play-framework-async-io-without-thread-pool-and-callback-hell)."
The sample code illustrates a number of different ways of working with async operations fundamental
 to Play 2 and, while it was all very interesting to *read* the code, I wanted to see it in action.

The post's sample code is written in Scala and, while that's all well and good, the Play 2 projects
that I'll be working with in the short term will primarily use Java.

## Implementation
`ScalaController` contains runnable implementations of all of the sample code from the blog post.

`JavaController` implements all of the same functionality found in `ScalaController` but in Java. 
 Prior to Java 8, the language has no functional constructs so I took a stab at building up some "functional operations"
 that would accomplish roughly the same thing as what Scala has baked into the language.  They get
 the job done and are pretty well generalized for this exercise.

Finally, Java 8 arrived on the scene and so it only made sense to take the same set of operations and
rewrite them in `Java8Controller`. This implementation takes advantage of Java 8 
[lambda expressions](http://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html).

## Code Stats
Since Play supports roughly the same set of operations between both Java and Scala one interesting 
 comparison is the amount of code in each implementation. I've tried to maintain consistency between 
 the implementations including comments, etc. Lines-of-code stats below includes only the class 
 definition and excludes the package declaration and imports. Since I implemented the functional 
 operations in `JavaController`, it gets counts both with and without them. 

* Scala:  92 LOC
* Java 8:  103 LOC
* Java:  150 LOC
* Java:  171 LOC including functional operations such as `map` & `flatMap`
