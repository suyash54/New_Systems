package LoggingFramework;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class SimpleTextFormatter implements LogFormatter {

    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            .withZone(ZoneId.systemDefault());

    @Override
    public String format(LogMessage m){
        return String.format("[%s] [%s] [%s] [%s] - %s",
                TS.format(m.getTimeStamp()),
                m.getLevel(),
                m.getThreadName(),
                m.getLoggerName(),
                m.getMessage());
    }
}
