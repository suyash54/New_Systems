package LoggingFramework;

import lombok.Getter;

import java.time.Instant;


public class LogMessage {

    private final LogLevel level;
    private final String message;
    private final String loggerName;
    private final String threadName;
    private final Instant timeStamp;


    public LogMessage(LogLevel level, String message, String loggerName){
        this.level = level;
        this.message = message;
        this.loggerName = loggerName;
        this.threadName = Thread.currentThread().getName();
        this.timeStamp = Instant.now();
    }

    public LogLevel getLevel() { return level; }
    public String getMessage() { return message;}
    public String getLoggerName() {return loggerName; }
    public String getThreadName() { return threadName; }
    public Instant getTimeStamp() {return timeStamp;}
}
