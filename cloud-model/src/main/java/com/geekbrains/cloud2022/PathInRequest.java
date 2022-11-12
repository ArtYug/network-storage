package com.geekbrains.cloud2022;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PathInRequest implements CloudMessage {
    @Getter
    @NonNull
    private String name;
    private StringBuilder stringBuilder;

}
