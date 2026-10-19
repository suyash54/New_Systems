package LoggingFramework;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.locks.ReentrantLock;

public class FileLogAppender extends AbstractLogAppender{

    private final BufferedWriter writer;
    private final ReentrantLock writeLock = new ReentrantLock();

    public FileLogAppender(String filePath){
        this(filePath,LogLevel.INFO,new SimpleTextFormatter());
    }

    public FileLogAppender(String filePath, LogLevel logLevel, LogFormatter formatter)  {
        super(logLevel,formatter);
        Path path = Paths.get(filePath);

        try{
            this.writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,StandardOpenOption.APPEND);
        } catch(IOException ex){
            throw new UncheckedIOException("Cannot open log file: "+filePath,ex);
        }
    }

    @Override
    protected void write(String formattedMessage) {
        writeLock.lock();
        try{
            writer.write(formattedMessage);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed writing log line",e);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void close(){
        writeLock.lock();
        try{
            writer.close();
        } catch (IOException ignored){

        } finally{
            writeLock.unlock();
        }
    }
}
