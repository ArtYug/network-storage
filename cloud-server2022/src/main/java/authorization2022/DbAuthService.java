package authorization2022;

import com.geekbrains.cloud2022.User;


import java.sql.*;

public class DbAuthService implements AuthService {
    private static Connection connection;
    private static Statement statement;
    public void connection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:mydatabase.db");
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("can't connect to database");
        }
    }

    @Override
    public String getNickNameByLoginAndPassword(String login, String password) {
        String query = String.format("select nickname from users where login = '%s' and password = '%s' ; ", login, password);
        try (ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getString("nickname");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void registration(String login, String password, String userName) {
        String query = String.format("insert into users (login,password,nickname) values " + "(" + "\"" + login + "\"" + "," + "\"" + password + "\"" + "," + "\"" + userName + "\"" + ")" + ";");
        System.out.println(query + " " + "query ");
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUser(String login) {
        String query = String.format("select nickname from users where nickname = '%s'; ", login);
        try (ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return new User(resultSet.getString("nickname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void disconnect() {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
