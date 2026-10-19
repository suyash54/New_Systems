package LoggingFramework;

public class JsonFormatter implements LogFormatter {

    @Override
    public String format(LogMessage m) {
        return "{"
                + "\"timestamp\":\"" + m.getTimeStamp() + "\","
                + "\"level\":\"" + m.getLevel() + "\","
                + "\"thread\":\"" + escape(m.getThreadName()) + "\","
                + "\"logger\":\"" + escape(m.getLoggerName()) + "\","
                + "\"message\":\"" + escape(m.getMessage()) + "\""
                + "}";
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}