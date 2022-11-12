package com.geekbrains.cloud2022;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AuthRequest implements CloudMessage {
    @NonNull
    public String name;
    @NonNull
    public String password;
}

