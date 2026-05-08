package com.hotel.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class CsvExporter {
    private CsvExporter() {
    }

    public static Path export(String fileName, List<String> rows) {
        try {
            Path exportDir = Path.of("exports");
            Files.createDirectories(exportDir);
            Path path = exportDir.resolve(fileName);
            Files.write(path, rows);
            return path;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to export file", exception);
        }
    }
}
