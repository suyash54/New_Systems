package LoggingFramework;

import java.util.List;

public abstract class AbstractLogProcessor implements LogProcessor {


    protected void dispatch(LogMessage message, List<LogAppender> appenderList){
        appenderList.forEach( appender -> {
            try{
                appender.append(message);
            } catch(Throwable t){
                System.err.println("[logging] appender failed: "+t.getMessage());
            }
        });
    }

}
