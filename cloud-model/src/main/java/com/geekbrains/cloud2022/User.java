package com.geekbrains.cloud2022;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class User {
    @NonNull
    private final String userName;
}
