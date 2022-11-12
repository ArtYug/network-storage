package com.example.cloudapplication_2022;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.cloudapplication_2022.CloudApplication.stage;

public class FinalRegistrationWindow implements Initializable {
    public Label backToSignInFromRegWindow;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Network network = new Network(8189);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void backFromRegWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("for_login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        CloudApplication.scene = scene;
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
}
