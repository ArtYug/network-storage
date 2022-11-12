package com.geekbrains.cloud2022;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RegistrationRequest implements CloudMessage {
    @NonNull
    public String login;
    @NonNull
    public String password;
    @NonNull
    public String userName;
}
