package LoggingFramework;

import java.util.List;

public interface LogProcessor {

    void process(LogMessage message, List<LogAppender> appenderList);
    default void shutdown() {}
}
