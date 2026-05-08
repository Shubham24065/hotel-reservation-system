package com.hotel.app;

import com.hotel.config.AppConfig;
import com.hotel.config.JpaUtil;
import com.hotel.util.AppLogger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class HotelKioskApp extends Application {

    private URL resolveView(String fxmlFile) throws Exception {
        URL classpathUrl = getClass().getResource("/view/" + fxmlFile);
        if (classpathUrl != null) {
            return classpathUrl;
        }

        Path filePath = Path.of("src", "main", "resources", "view", fxmlFile);
        if (Files.exists(filePath)) {
            return filePath.toUri().toURL();
        }

        throw new IllegalStateException("Could not find FXML: " + fxmlFile);
    }

    @Override
    public void start(Stage stage) throws Exception {
        AppLogger.getLogger();
        AppConfig.databaseInitializer().initialize();
        FXMLLoader loader = new FXMLLoader(resolveView("welcome.fxml"));
        stage.setTitle("Grand Stay Hotel");
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    @Override
    public void stop() {
        JpaUtil.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
