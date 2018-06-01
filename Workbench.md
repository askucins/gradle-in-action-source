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
* To run this app (original java version):
    ```bash
    java -cp build/libs/* com.manning.gia.todo.ToDoApp
    ```
    or
    ```bash
    java -cp build/classes/java/main com.manning.gia.todo.ToDoApp
    ```
    or (once the built jar file is copied somewhere, e.g. to a current directory)
    ```bash
    java -cp todo-app.jar com.manning.gia.todo.ToDoApp
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
* Groovy vs java arrays initialization
    * Java way:
    ```java
    char[] stuff = new char[] {'x'};
    ```
    
    * Groovy way:
    ```groovy
    char[] stuff = ['x'] as char[]
    ```

    Source: [Groovy lang](http://docs.groovy-lang.org/next/html/documentation/core-syntax.html#_arrays)
    
* Building and running
    
    * building:
    ```bash
    gw clean build
    ```
    * running
    ```bash
    java -cp build/classes/groovy/main:$GROOVY_HOME/embeddable/groovy-all-2.4.14.jar com.manning.gia.todo.ToDoApp
    ```

* Debugging console-dependent apps in IDE - problem with NPE!
    
    * See the Intellij tracker: [System.console() returns null](https://youtrack.jetbrains.com/issue/IDEABKL-5949)
    * And [Console javadoc](http://docs.oracle.com/javase/7/docs/api/java/io/Console.html)
        > If the virtual machine is started from an interactive command line 
        without redirecting the standard input and output streams then its console will exist 
        and will typically be connected to the keyboard and display from which the virtual machine was launched. 
        If the virtual machine is started automatically, for example by a background job scheduler, 
        then it will typically not have a console.
        If this virtual machine has a console then it is represented by a unique instance of this class 
        which can be obtained by invoking the System.console() method. 
        If no console device is available then an invocation of that method will return null.
    * Response to that is e.g. switching to [Scanner](https://docs.oracle.com/javase/7/docs/api/java/util/Scanner.html)
    
* Problem with some imports (groovy vs Intellij)
 
    A basic _run and exit_ scenario exits with an error message
    ```bash
    askuci@lelek:~/work/src/gradle-in-action-source/chapter03/todo-app--groovy (chapter03>)$ java -cp build/classes/groovy/main:$GROOVY_HOME/embeddable/groovy-all-2.4.14.jar com.manning.gia.todo.ToDoApp
    
    --- To Do Application ---
    Please make a choice:
    (a)ll items
    (f)ind a specific item
    (i)nsert a new item
    (u)pdate an existing item
    (d)elete an existing item
    (e)xit
    > e
    Exception in thread "main" groovy.lang.MissingPropertyException: No such property: FIND_ALL for class: com.manning.gia.todo.utils.CommandLineInputHandler
    	at org.codehaus.groovy.runtime.ScriptBytecodeAdapter.unwrap(ScriptBytecodeAdapter.java:66)
    	at org.codehaus.groovy.runtime.callsite.GetEffectivePogoPropertySite.getProperty(GetEffectivePogoPropertySite.java:87)
    	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.callGroovyObjectGetProperty(AbstractCallSite.java:310)
    	at com.manning.gia.todo.utils.CommandLineInputHandler.processInput(CommandLineInputHandler.groovy:31)
    	at com.manning.gia.todo.utils.CommandLineInputHandler$processInput$1.call(Unknown Source)
    	at org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)
    	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:116)
    	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:128)
    	at com.manning.gia.todo.ToDoApp.main(ToDoApp.groovy:19)
    ```
    
    However once imports are added to the com.manning.gia.todo.utils.CommandLineInputHandler it stops complaining
    ```groovy
    import static com.manning.gia.todo.utils.CommandLineInput.*
    ```
    
* Two words about enum - [Enum values and valueOf](https://programming.guide/java/javadoc-for-enum-values.html)

    ```java
    /**
     * Returns an array containing the constants of this enum 
     * type, in the order they're declared.  This method may be
     * used to iterate over the constants as follows:
     *
     *    for(E c : E.values())
     *        System.out.println(c);
     *
     * @return an array containing the constants of this enum 
     * type, in the order they're declared
     */
     public static E[] values();
    /**
     * Returns the enum constant of this type with the specified
     * name.
     * The string must match exactly an identifier used to declare
     * an enum constant in this type.  (Extraneous whitespace 
     * characters are not permitted.)
     * 
     * @return the enum constant with the specified name
     * @throws IllegalArgumentException if this enum type has no
     * constant with the specified name
     */
    public static E valueOf(String name);
    ```
* Capturing 'println'

    Sometimes one needs to capture what is printed on the System.out during tests (ok, **I needed**). 
    That is doable, a baisc solution is presented on this [StackOverflow question](https://stackoverflow.com/questions/3228427/redirect-system-out-println)
    ```java
    // Start capturing
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    System.setOut(new PrintStream(buffer));
    
    // Run what is supposed to output something
    ...
    
    // Stop capturing
    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    
    // Use captured content
    String content = buffer.toString();
    buffer.reset();
    ```
* Stub vs Mock vs Spy

    See this [StackOverflow discussion](https://stackoverflow.com/questions/24413184/can-someone-explain-the-difference-between-mock-stub-and-spy-in-spock-framewor)    

#### Further customization

* Changes in the MANIFEST.FM
    
    ```java
    apply plugin: 'java'
    
    version = 0.1
    sourceCompatibility = 1.8
    
    jar {
        manifest {
            attributes 'Main-Class': 'com.manning.gia.todo.ToDoApp'
        }
    }
    ```

    With that one can run the built app like this:
    ```bash
    java -jar ./build/libs/listing_03_04-todo-app-changing-properties-0.1.jar
    ```
&#9632;