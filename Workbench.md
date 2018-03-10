## Misc
###### Version
Bumped gradle version to 4.6

###### To run a task in a specific sub-project
```bash
./gradlew helloWorld -p chapter02/helloworld-task-doLast
```

###### To stop daemon
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