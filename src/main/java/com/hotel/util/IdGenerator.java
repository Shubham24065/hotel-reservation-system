package com.hotel.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public final class IdGenerator {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    private IdGenerator() {
    }

    public static String reservationCode() {
        return "RSV-" + LocalDateTime.now().format(FORMATTER) + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    public static String loyaltyNumber() {
        return "LOY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
