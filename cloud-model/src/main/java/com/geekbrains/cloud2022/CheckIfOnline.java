package com.geekbrains.cloud2022;

import lombok.Data;
@Data
public class CheckIfOnline  implements CloudMessage {
        private final boolean checkIsOnline;
}
