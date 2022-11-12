package com.geekbrains.cloud2022;

import lombok.Data;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ListUsers implements CloudMessage {
    @Getter
    private final List<String> users;

    public ListUsers(Path path) throws IOException {
        users = Files.list(path)
                .map(p -> p.getFileName().toString())
                .collect(Collectors.toList());
    }

}
