package com.geekbrains.cloud2022;

import lombok.Data;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ListFiles implements CloudMessage {
    @Getter
    private final List<String> files;

    public ListFiles(Path path) throws IOException {
        files = Files.list(path)
                .map(p -> p.getFileName().toString())
                .collect(Collectors.toList());
    }
}
