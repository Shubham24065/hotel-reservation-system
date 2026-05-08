package com.hotel.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.net.URL;

public class SceneNavigator {

    public static void switchScene(Node sourceNode, String fxmlPath, String title) {
        try {
            URL resource = SceneNavigator.class.getResource(fxmlPath);

            if (resource == null) {
                throw new RuntimeException("FXML not found on classpath: " + fxmlPath);
            }

            System.out.println("Loading FXML: " + fxmlPath);
            System.out.println("Resolved URL: " + resource);

            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            Stage stage = (Stage) sourceNode.getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Could not open screen");
            alert.setContentText(
                    "FXML: " + fxmlPath + "\n\n" +
                    e.getClass().getSimpleName() + ": " + e.getMessage()
            );
            alert.showAndWait();
        }
    }

    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showValidation(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation");
        alert.setHeaderText("Please check your input");
        alert.setContentText(message);
        alert.showAndWait();
    }
}