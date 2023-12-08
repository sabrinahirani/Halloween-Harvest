package main;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Logging utility.
 */
public class Log {
    private static Logger logger;
    private static Handler handler;

    public static Logger getLogger() {
        if (logger == null) {
            logger = Logger.getLogger("");
            logger.setUseParentHandlers(false); // No need to send output to parent loggers

            try {
                // Create the file if it doesn't exist
                String path = getLogFilePath();
                File file = new File(path);
                if (!file.isFile()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }

                // Set the formatter and add handler to logger
                SimpleFormatter fmt = new SimpleFormatter();
                handler = new FileHandler(path, true);
                handler.setFormatter(fmt);
                logger.addHandler(handler);

                // Indicate a new log session is starting.
                info("= = = = = SESSION START = = = = =");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("WARN: Failed to access log file (data/halloweenharvest.log).");
            }
        }

        return logger;
    }

    public static void info(String msg) {
        getLogger().info(msg);
    }

    public static void warn(String msg) {
        getLogger().log(Level.WARNING, msg);
    }

    public static void error(String msg) {
        getLogger().log(Level.SEVERE, msg);
    }

    public static Handler getHandler() {
        return logger.getHandlers()[0];
    }

    /**
     * @return the absolute path for the log file.
     */
    public static String getLogFilePath() {
        // Current Working Directory
        String cwd = System.getProperty("user.dir");
        // Join paths to form the absolute path for the Log file.
        String path = FileSystems.getDefault().getPath(cwd, "data", "halloweenharvest.log").toString();
        return path;
    }
}
