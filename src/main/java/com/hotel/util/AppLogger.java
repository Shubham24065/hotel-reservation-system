package com.hotel.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class AppLogger {
    private static final Logger LOGGER = Logger.getLogger("GrandStayHotel");
    private static boolean configured;

    private AppLogger() {
    }

    public static Logger getLogger() {
        if (!configured) {
            configure();
        }
        return LOGGER;
    }

    private static synchronized void configure() {
        if (configured) {
            return;
        }
        try {
            Files.createDirectories(Path.of("logs"));
            FileHandler fileHandler = new FileHandler("logs/system_logs.%g.log", 1024 * 1024, 10, true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setUseParentHandlers(true);
            configured = true;
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Failed to initialize logger", exception);
        }
    }
}
