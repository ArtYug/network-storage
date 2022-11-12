package authorization2022;

import com.geekbrains.cloud2022.User;

public interface AuthService {
    String getNickNameByLoginAndPassword(String login, String password);

    void registration(String login, String password, String userName);

    User getUser(String login);
}
