package LoggingFramework;

public enum LogLevel {
    DEBUG(10),
    INFO(20),
    WARN(30),
    ERROR(40),
    FATAL(50);


    private final int severity;

    LogLevel(int severity){
        this.severity = severity;
    }

    public boolean isGreaterOrEqual(LogLevel threshold){
        return this.severity >= threshold.severity;
    }
}
