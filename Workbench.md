## Useful links
* [Gradle Project](https://docs.gradle.org/4.6/dsl/org.gradle.api.Project.html)
* [Gradle javadoc](https://docs.gradle.org/current/javadoc/index.html)
* [CLI](https://docs.gradle.org/4.6/userguide/command_line_interface.html)
* [Annotation Type EqualsAndHashCode](http://docs.groovy-lang.org/latest/html/api/index.html?groovy/transform/EqualsAndHashCode.html)
* [Mrhaki tips](http://mrhaki.blogspot.com/search/label/Groovy)
* [ConcurrentHashMap](https://ria101.wordpress.com/2011/12/12/concurrenthashmap-avoid-a-common-misuse/)
* [Concurrency in Groovy](https://www.slideshare.net/paulk_asert/groovy-and-concurrency)


## Misc
* Bumped gradle version to 4.6
* To run a task in a specific sub-project
    ```bash
    ./gradlew helloWorld -p chapter02/helloworld-task-doLast
    ```
* To stop daemon
    ```bash
    ./gradlew --stop
    ```

## Chapter 01
#### ant-build
```bash
java -cp lib/commons-lang3-3.7.jar:dist/my-app-1.0.jar com.mycompany.app.Main
```
or
```bash
java -cp lib/*:dist/* com.mycompany.app.Main
```

#### mvn-build
```bash
java -cp target/my-app-1.0.jar:/home/askuci/.m2/repository/org/apache/commons/commons-lang3/3.7/commons-lang3-3.7.jar com.mycompany.app.Main 
```
## Chapter 02

#### helloworld-task-doLast
From the root directory:
```bash
./gradlew helloWorld -p chapter02/helloworld-task-doLast
```
or from the project directory - please notice, that then one has to provide a path to gradlew:
```bash
../../gradlew helloWorld
```
e.g.
```bash
askuci@lelek:~/work/src/gradle-in-action-source/chapter02/helloworld-task-doLast (bump-version *=)$ ../../gradlew helloWorld

> Task :helloWorld 
Hello World


BUILD SUCCESSFUL in 0s
1 actionable task: 1 executed
```
There is though that very useful little tool [gdub](http://www.gdub.rocks/).
With that there is no need to specify that path, e.g.
```bash
askuci@lelek:~/work/src/gradle-in-action-source/chapter02/helloworld-task-doLast (bump-version *=)$ gw helloWorld
Using gradle at '/home/askuci/work/src/gradle-in-action-source/gradlew' to run buildfile '/home/askuci/work/src/gradle-in-action-source/chapter02/helloworld-task-doLast/build.gradle':


> Task :helloWorld 
Hello World


BUILD SUCCESSFUL in 0s
1 actionable task: 1 executed
```
###### More doFirst and doLast defined
If there are more ```doFirst``` and ```doLast``` defined the order in which they are executed is different:
* ```doLast``` are executed in the same order as they were defined
* ```doFirst``` are execute in the opposite order as they were defined

which really makes sense :)

#### helloworld-task-left-shift
```bash
./gradlew helloWorld -p chapter02/helloworld-task-left-shift
```
Please notice, that there is a warning reported there. To log its full content instead of a summary only:
```bash
./gradlew -p chapter02/helloworld-task-left-shift/ helloWorld --warning-mode=all 
```
e.g.
```bash
askuci@lelek:~/work/src/gradle-in-action-source (bump-version=)$ ./gradlew -p chapter02/helloworld-task-left-shift/ helloWorld --warning-mode=all

> Configure project : 
The Task.leftShift(Closure) method has been deprecated and is scheduled to be removed in Gradle 5.0. Please use Task.doLast(Action) instead.
        at build_1noaagxs7hgh1vk3yfdrlwbyp.run(/home/askuci/work/src/gradle-in-action-source/chapter02/helloworld-task-left-shift/build.gradle:1)
        (Run with --stacktrace to get the full stack trace of this deprecation warning.)

> Task :helloWorld 
Hello World


BUILD SUCCESSFUL in 0s
1 actionable task: 1 executed
```

#### dynamic-task-and-task-dependencies
(...)

## Chapter 03

#### todo-app and todo-app--groovy
* To run this app:
    ```bash
    java -cp build/libs/* com.manning.gia.todo.ToDoApp
    ```
* Easy way to add 'equals' and 'hash' method in groovy
    [EqualsAndHashCode](http://docs.groovy-lang.org/latest/html/api/groovy/transform/EqualsAndHashCode.html)
* To run a single test method:
    ```bash
    gw clean test --tests com.manning.gia.todo.model.ToDoItemSpec."should create new item"
    ```
* About 'equals' vs 'compareTo' (or '<=>')
    
    An excerpt from "Spock: Up and Running", by Rob Fletcher, O'Reilly Media, 2017
    > What Is == Really Doing?
    >
    > Although it’s typical (and usually safe) to consider == an alias for the Java-style equals
    > method, that’s something of an oversimplification.
    > Even though we can think of
    >
    >     assert a == b
    >
    > as the Groovy equivalent to the Java
    >
    >     assert a.equals(b);
    >
    > in fact, if the class of a implements Comparable , Groovy will use:
    >
    >     a.compareTo(b) == 0;
    >
    > The primary reason this is done is so that the == operator can be reflexive between 
    java.lang.String and groovy.lang.GString (the class that backs Groovy’s interpolated strings).
    Because java.lang.String is final , Groovy cannot extend it. Subsequent versions of Java have introduced
    the CharSequence class to work around this kind of problem in alternative JVM languages,
    but Groovy’s implementation predates this.

* java.util.concurrent.atomic.AtomicLang
    > A long value that may be updated atomically
  
    > \[...\] lock free thread safe
  
    > Atomic classes are not general purpose replacements for java.lang.Integer and related classes. They do not define methods such as equals, hashCode and compareTo.

* ConcurrentHashMap
    > A hash table supporting full concurrency of retrievals and high expected concurrency for updates
  
* Passing objects in java/groovy as functions arguments from [jonskeet.uk](http://jonskeet.uk/java/passing.html)
    
    >**Myth: "Objects are passed by reference, primitives are passed by value"**\
    Some proponents of this then say, "Ah, except for immutable objects which are passed by value \[etc\]"
    which introduces loads of rules without really tackling how Java works. 
    Fortunately the truth is much simpler:\
    **Truth #1: Everything in Java is passed by value. Objects, however, are never passed at all.**
    That needs some explanation - after all, if we can't pass objects, how can we do any work? 
    The answer is that we pass references to objects. 
    That sounds like it's getting dangerously close to the myth, until you look at truth #2:\
    **Truth #2: The values of variables are always primitives or references, never objects.**
    This is probably the single most important point in learning Java properly. 
    It's amazing how far you can actually get without knowing it, 
    in fact - but vast numbers of things suddenly make sense when you grasp it.
    
    **Example**
    ```groovy
    class Stuff {
        String name
    }
    def changeIt(Stuff st) {
        st.name = 'Changed!'
    }
    Stuff stuff = new Stuff(name:'Test')
    changeIt(stuff)
    assert stuff.name == 'Changed!'
    ```   