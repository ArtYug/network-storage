package com.example.cloudapplication_2022;


import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.ResourceBundle;

public class SecondForOpenFile implements Initializable {
    public Label labelSecond;
    public TextArea text;
    public Text nameText;
    public TextField fileNameTextField;
    public ImageView imageView;
    public AnchorPane anchorPaneId;
    public Label forText;
    public Button buttonBack;
    public BorderPane borderPaneSecondWindow;
    public SplitPane splitPaneTop;
    public Pane rightLabel;
    public Label leftLabel;

    public ScrollPane scrollPaneSecond;
    public AnchorPane anchorScroll;
    DataSingleton dataSingleton = DataSingleton.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Path path = Paths.get(dataSingleton.getUserName());
        File file = new File(String.valueOf(path));
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        System.out.println(mimeType + " " + "mime type");
        BorderPane.setMargin(buttonBack, new Insets(10, 10, 10, 10));


        splitPaneTop.setPadding(new Insets(1, 1, 1, 1));
        borderPaneSecondWindow.setTop(splitPaneTop);
        BorderPane.setMargin(splitPaneTop, new Insets(10, 10, 10, 10));
        // LEFT
        leftLabel.setPadding(new Insets(5, 5, 5, 5));
        borderPaneSecondWindow.setLeft(leftLabel);
        // Set margin for left area.
        BorderPane.setMargin(leftLabel, new Insets(10, 10, 10, 10));

        // CENTER
        scrollPaneSecond.setPadding(new Insets(1, 1, 1, 1));
        //  anchorScroll.setPadding(new Insets(1, 1, 1, 1));
        borderPaneSecondWindow.setCenter(scrollPaneSecond);
        // Alignment.
        BorderPane.setAlignment(anchorPaneId, Pos.BOTTOM_CENTER);
        // RIGHT
        rightLabel.setPadding(new Insets(5, 5, 5, 5));
        borderPaneSecondWindow.setRight(rightLabel);
        // Set margin for right area.
        BorderPane.setMargin(rightLabel, new Insets(10, 10, 10, 10));
        if (Files.exists(path)) {
            if (mimeType.contains("text/plain")) {
                if (Files.isRegularFile(path)) {
                    byte[] bytes;
                    try {
                        bytes = Files.readAllBytes(path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(new String(bytes));
                    nameText.setText(new String(bytes));
                    fileNameTextField.setText(String.valueOf(path.getFileName()));

                }
            } else {
                System.out.println(mimeType + " " + "mimetype");
                InputStream stream;
                try {
                    stream = new FileInputStream(String.valueOf(path));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                Image image = new Image(stream);
                //Creating the image view
                //Setting image to the image view
                imageView.setImage(image);
                //Setting the image view parameters
                imageView.setPreserveRatio(true);
                fileNameTextField.setText(String.valueOf(path.getFileName()));
                borderPaneSecondWindow.setCenter(imageView);
                // Alignment.
            }
        } else {
            System.out.println("cant find file");
            nameText.setText(path + "not exist");
        }
    }

    public void backToFirstScene() {
        Stage stage = (Stage) borderPaneSecondWindow.getScene().getWindow();
        Parent root;
        try {
            root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("hello-view.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Cloud application");
        stage.setScene(new Scene(root));
    }
}
