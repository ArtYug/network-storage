package com.geekbrains.cloud2022;

import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Data
public class FileMessage implements CloudMessage {
    private long size;
    private final byte[] data;
    private String name;

    public FileMessage(Path path) throws IOException {
        size = Files.size(path);
        data = Files.readAllBytes(path);
        name = path.getFileName().toString();
    }
}
