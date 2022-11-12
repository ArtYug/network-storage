package com.geekbrains.cloud2022;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RemoveLogin  implements CloudMessage {
        @NonNull
        public String removeLoginFromMap;;
    }

