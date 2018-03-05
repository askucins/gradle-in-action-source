## Misc
###### Version
Bumped gradle version to 4.6

###### To run a task in a specific sub-project
```
./gradlew helloWorld -p chapter02/helloworld-task-doLast
```

###### To stop daemon
```
./gradlew --stop
```

## Chapter 01
#### ant-build
```
java -cp lib/commons-lang3-3.7.jar:dist/my-app-1.0.jar com.mycompany.app.Main
```
or
```
java -cp lib/*:dist/* com.mycompany.app.Main
```

## Chapter 02

#### helloworld-task-doLast
```
./gradlew helloWorld -p chapter02/helloworld-task-doLast
```

#### helloworld-task-left-shift
```
./gradlew helloWorld -p chapter02/helloworld-task-left-shift
```
