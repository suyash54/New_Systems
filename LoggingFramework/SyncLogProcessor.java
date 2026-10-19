package LoggingFramework;

import java.util.List;

public class SyncLogProcessor extends AbstractLogProcessor{


    @Override
    public void process(LogMessage message, List<LogAppender> appenderList){
        dispatch(message,appenderList);
    }

}
