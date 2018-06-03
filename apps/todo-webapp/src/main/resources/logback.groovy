appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    }
}
appender("FILE", RollingFileAppender) {
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "logs/test.%d{yyyy-MM-dd}.log"
        maxHistory = 7
    }
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    }
}
//logger("com.manning.gia.todo", DEBUG, ["FILE", "STDOUT"])
//logger("spock.lang", DEBUG, ["FILE", "STDOUT"])
//logger("io.restassured", DEBUG, ["FILE", "STDOUT"])
//logger("org.apache.http", DEBUG, ["FILE", "STDOUT"])
//logger("org.apache.http.wire", ERROR, ["FILE", "STDOUT"])
//logger("org.apache.http.headers", DEBUG, ["FILE", "STDOUT"])
//logger("org.apache.http.client", DEBUG, ["FILE", "STDOUT"])
//logger("org.apache.http.impl.client", DEBUG, ["FILE", "STDOUT"])
//logger("com.fasterxml.jackson", DEBUG, ["FILE"])
//logger("javax.validation", DEBUG, ["FILE"])
root(DEBUG, ["FILE", "STDOUT"])
