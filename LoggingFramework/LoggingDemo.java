package LoggingFramework;

import java.util.concurrent.CountDownLatch;

/**
 * Exercises the framework end to end:
 *  - logger hierarchy + inherited effective level
 *  - per-appender levels and per-appender formatters on ONE logger
 *  - lazy Supplier messages (supplier skipped when suppressed)
 *  - additive appenders up the hierarchy
 */
public class LoggingDemo {


    static class ThreadRevealingAppender extends AbstractLogAppender {
        private final CountDownLatch latch;
        ThreadRevealingAppender(CountDownLatch latch) {
            super(LogLevel.DEBUG, new SimpleTextFormatter());
            this.latch = latch;
        }
        @Override
        protected void write(String formattedMessage) {
            System.out.println("  write-time thread = "
                    + Thread.currentThread().getName()
                    + "  |  line -> " + formattedMessage);
            latch.countDown();
        }
    }

    public static void main(String[] args) throws Exception {
        LogManager manager = LogManager.getInstance();

        // ROOT(INFO, console) -> com.app(DEBUG) -> com.app.OrderService
        Logger appLogger = manager.getLogger("com.app");
        appLogger.setLevel(LogLevel.DEBUG);

        Logger orderLogger = manager.getLogger("com.app.OrderService");

        // Same logger, two sinks with DIFFERENT levels and DIFFERENT formats:
        //   file  -> INFO+  as plain text
        //   alert -> ERROR+ as JSON
        FileLogAppender file  = new FileLogAppender("app.log", LogLevel.INFO,  new SimpleTextFormatter());
        ConsoleLogAppender alert = new ConsoleLogAppender(LogLevel.ERROR, new JsonFormatter());
        orderLogger.addAppender(file);
        orderLogger.addAppender(alert);

        System.out.println("--- orderLogger (effective level DEBUG, inherited from com.app) ---");
        // DEBUG passes the effective level, so the supplier RUNS.
        orderLogger.debug(() -> "expensive dump: " + buildBigString());
        orderLogger.info("order 1001 created");        // console + file      (not alert)
        orderLogger.warn("inventory low");             // console + file      (not alert)
        orderLogger.error("payment gateway timeout");  // console + file + alert(JSON)

        System.out.println("--- quietLogger (effective level WARN) ---");
        Logger quiet = manager.getLogger("com.app.Report");
        quiet.setLevel(LogLevel.WARN);
        // DEBUG < WARN: suppressed, so the supplier NEVER runs (no expensive build).
        quiet.debug(() -> "NEVER built: " + buildBigString());
        quiet.error("report generation failed");

        Thread.sleep(200);   // let async workers drain
        manager.shutdown();
        file.close();
        System.out.println("--- done; see app.log for the file sink output ---");

        AsyncLogProcessor async = new AsyncLogProcessor(2); // 2 workers
        Logger root = new Logger("ROOT", null, async);
        root.setLevel(LogLevel.DEBUG);

        int n = 4;
        CountDownLatch latch = new CountDownLatch(n);
        root.addAppender(new ThreadRevealingAppender(latch));

        System.out.println("caller thread = " + Thread.currentThread().getName());
        for (int i = 1; i <= n; i++) {
            root.info("event " + i);
        }
        System.out.println("caller returned immediately (writes happen async)");

        latch.await();       // wait for all workers to finish
        async.shutdown();
    }

    private static String buildBigString() {
        System.out.println("   [supplier ran -> building expensive string]");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) sb.append("chunk").append(i).append(' ');
        return sb.toString().trim();
    }
}
