package com.example.cloudapplication_2022;

import com.geekbrains.cloud2022.RegistrationRequest;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.cloudapplication_2022.CloudApplication.stage;

public class SignUpController implements Initializable {
    private static Network network;
    public Button signUp;
    public TextField passwordText;
    public CheckBox checkboxShowPassword;
    public Text textAreaLoginExist;
    public PasswordField passwordField;
    public TextField loginField;
    public TextField userName;
    public Label labelBackToSignIn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            network = new Network(8189);
            passwordField.setVisible(true);
            passwordText.setVisible(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeVisibilityTest() {
        showPasswordCheckBox(checkboxShowPassword, passwordText, passwordField);
    }

    static void showPasswordCheckBox(CheckBox checkboxShowPassword, TextField passwordText, PasswordField passwordField) {
        if (checkboxShowPassword.isSelected()) {
            passwordText.setText(passwordField.getText());
            passwordText.setVisible(true);
            passwordField.setVisible(false);
            return;
        }
        passwordField.setText(passwordText.getText());
        passwordField.setVisible(true);
        passwordText.setVisible(false);
    }

    public void signUpMethod() {
        String login = loginField.getText();
        String password = passwordField.getText();
        String user = userName.getText();

        System.out.println(login + " " + "login");
        System.out.println(password + " " + "password");
        System.out.println(user + " " + "user");

        if (login != null && password != null && user != null) {
            try {
                network.write(new RegistrationRequest(login, password, user));
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("final-registration.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                CloudApplication.scene = scene;
                stage.setTitle("Login");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void backToLoginMenu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("for_login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        CloudApplication.scene = scene;
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
}
