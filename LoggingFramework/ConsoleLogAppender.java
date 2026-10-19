package LoggingFramework;

public class ConsoleLogAppender extends AbstractLogAppender{

    public ConsoleLogAppender(){
        this(LogLevel.DEBUG,new SimpleTextFormatter());
    }

    public ConsoleLogAppender(LogLevel logLevel, LogFormatter formatter) {
        super(logLevel,formatter);
    }

    @Override
    protected  void write(String formattedMessage) {
        System.out.println(formattedMessage);
    }
}
