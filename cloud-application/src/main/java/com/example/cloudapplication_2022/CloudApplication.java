package com.example.cloudapplication_2022;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class CloudApplication extends Application {
    public static Scene scene;
    public static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("for_login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        CloudApplication.scene = scene;
        CloudApplication.stage = stage;
        stage.setTitle("login");
        stage.setScene(scene);
        stage.show();
    }
}