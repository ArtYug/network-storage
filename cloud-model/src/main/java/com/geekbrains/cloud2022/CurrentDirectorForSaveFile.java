package com.geekbrains.cloud2022;


import lombok.Data;

@Data
public class CurrentDirectorForSaveFile implements CloudMessage {
    private String name;

    public CurrentDirectorForSaveFile(String name) {
        this.name = name;
    }
}
