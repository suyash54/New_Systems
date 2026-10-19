package LoggingFramework;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LogManager {

    private static final String ROOT_NAME = "ROOT";
    private static final LogManager INSTANCE = new LogManager();

    private final ConcurrentMap<String, Logger> loggers = new ConcurrentHashMap<>();
    private final LogProcessor processor;
    private final Logger rootLogger;

    private LogManager() {
        this.processor = new AsyncLogProcessor();

        this.rootLogger = new Logger(ROOT_NAME, null, processor);
        this.rootLogger.setLevel(LogLevel.INFO);
        this.rootLogger.addAppender(new ConsoleLogAppender());
        loggers.put(ROOT_NAME, rootLogger);
    }

    public static LogManager getInstance() {
        return INSTANCE;
    }

    public Logger getRootLogger() {
        return rootLogger;
    }

    public Logger getLogger(String name) {
        if (name == null || name.isEmpty() || ROOT_NAME.equals(name)) {
            return rootLogger;
        }
        Logger existing = loggers.get(name);
        if (existing != null) {
            return existing;
        }

        Logger parent = resolveParent(name);
        return loggers.computeIfAbsent(name, n -> new Logger(n, parent, processor));
    }


    private Logger resolveParent(String name) {
        int lastDot = name.lastIndexOf('.');
        if (lastDot < 0) return rootLogger;
        return getLogger(name.substring(0, lastDot));
    }


    public void shutdown() {
        processor.shutdown();
    }


}
