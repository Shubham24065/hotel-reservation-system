package com.hotel.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class DateTimeHelper {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd MMM yyyy   hh:mm:ss a");

    public static void startClock(Label label) {
        if (label == null) {
            return;
        }

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(0), event ->
                label.setText(LocalDateTime.now().format(FORMATTER))
            ),
            new KeyFrame(Duration.seconds(1))
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}