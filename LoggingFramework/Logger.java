package LoggingFramework;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

public class Logger {

    private final String name;
    private final Logger parent;
    private final LogProcessor processor;
    private final List<LogAppender> appenderList = new CopyOnWriteArrayList<>();

    private volatile LogLevel level;

    private volatile boolean additive = true;


    public Logger(String name, Logger parent, LogProcessor logProcessor){
        this.name =  name;
        this.parent = parent;
        this.processor = logProcessor;
    }

    public void addAppender(LogAppender appender){
        appenderList.add(appender);
    }

    public void removeAppender(LogAppender appender){
        appenderList.remove(appender);
    }

    public void setLevel(LogLevel level){
        this.level = level;
    }

    public void setAdditive(boolean additive){
        this.additive = additive;
    }

    public String getName(){
        return name;
    }

    public LogLevel getEffectiveLevel(){
        for(Logger l = this; l!=null; l=l.parent){
            if(l.level !=null) return l.level;
        }
        return LogLevel.INFO;
    }

    public boolean isEnabled(LogLevel level){
        return level.isGreaterOrEqual(getEffectiveLevel());
    }

    public void log(LogLevel level, Supplier<String> messageSupplier){
        if(!isEnabled(level)) return;
        LogMessage message = new LogMessage(level,messageSupplier.get(),name);

        processor.process(message, collectAppenders());
    }

    public void log(LogLevel level,String message){
        if(!isEnabled(level))  return;
        processor.process(new LogMessage(level,message,name),collectAppenders());
    }

    public void debug(String msg) { log(LogLevel.DEBUG, msg); }
    public void info(String msg)  { log(LogLevel.INFO,  msg); }
    public void warn(String msg)  { log(LogLevel.WARN,  msg); }
    public void error(String msg) { log(LogLevel.ERROR, msg); }
    public void fatal(String msg) { log(LogLevel.FATAL, msg); }

    public void debug(Supplier<String> msg) { log(LogLevel.DEBUG, msg); }
    public void info(Supplier<String> msg)  { log(LogLevel.INFO,  msg); }
    public void warn(Supplier<String> msg)  { log(LogLevel.WARN,  msg); }
    public void error(Supplier<String> msg) { log(LogLevel.ERROR, msg); }
    public void fatal(Supplier<String> msg) { log(LogLevel.FATAL, msg); }


    private List<LogAppender> collectAppenders(){
        List<LogAppender> result = new ArrayList<>(appenderList);
        Logger current = this;
        while(current.additive && current.parent != null){
            current = current.parent;
            result.addAll(current.appenderList);
        }
        return result;
    }
}
