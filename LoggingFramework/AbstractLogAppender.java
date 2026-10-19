package LoggingFramework;

public abstract class AbstractLogAppender implements LogAppender{

    private volatile LogLevel level;

    private volatile LogFormatter formatter;

    protected AbstractLogAppender(LogLevel level, LogFormatter formatter){
        this.level = level;
        this.formatter = formatter;
    }

    @Override
    public final void append(LogMessage message){

        if(!message.getLevel().isGreaterOrEqual(this.level)){
            return;
        }

        write(formatter.format(message));
    }

    protected abstract void write(String formattedMessage);

    @Override
    public void setFormatter(LogFormatter formatter) { this.formatter = formatter; }
    @Override
    public LogFormatter getFormatter() { return this.formatter; }
    @Override
    public void setLevel(LogLevel level) { this.level = level; }
    @Override
    public LogLevel getLevel() { return level; }
}
