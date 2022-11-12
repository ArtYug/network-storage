package com.geekbrains.cloud2022;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ServerNameFolder implements CloudMessage{
   @NonNull
   private String name;
}
