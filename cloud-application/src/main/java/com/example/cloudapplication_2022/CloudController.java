package com.example.cloudapplication_2022;

import com.geekbrains.cloud2022.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
public class CloudController implements Initializable {
    public Button upload;
    public Button download;
    public Button btxExit;
    public TableView<FileInfo> client_is_list_table;
    public TableView<FileInfo> server_is_list_table;
    public TextField client_text_field;
    public TextField server_text_field;
    public ImageView imageForTest;
    public AnchorPane ancorPane;
    public ContextMenu contextMenu;
    public MenuItem refreshItem;
    public MenuItem menuContextOpen;
    public MenuItem menuQuit;
    public Font x11;
    public ImageView imageLogin;
    public Menu helpMenu;
    public Label dateTime;
    public ListView<String> listViewForOnlineUsers;
    public Label nickNameLabel;
    private Network network;
    private String homeDir;
    private String dir;
    DataSingleton dataSingleton = DataSingleton.getInstance();
    private static String staticVal;
    private static String forRemovePathLogin;
    private static final List<String> list = new ArrayList<>();

    private static String nameUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            dir = "specific_server_folder";
            homeDir = "specific_client_folder";
            createPathForClient();
            network = LoginController.getNetwork();
            Thread readThread = new Thread(this::readLoop);
            readThread.setDaemon(true);
            readThread.start();
            vos();
            sendPath();
            initTable();
            initTableForClient();
            imageForTest = new ImageView(new Image("C:\\gb kurs vitalik\\Network storage development in Java\\image\\image_txt_file.png"));
            //select context menu on window right click
            ancorPane.setOnContextMenuRequested(event -> contextMenu.show(ancorPane, event.getScreenX(), event.getScreenY()));
            MultipleSelectionModel<FileInfo> lvSelModel =
                    client_is_list_table.getSelectionModel();
            lvSelModel.setSelectionMode(SelectionMode.MULTIPLE);
            MultipleSelectionModel<FileInfo> lvSelModel1 =
                    server_is_list_table.getSelectionModel();
            lvSelModel1.setSelectionMode(SelectionMode.MULTIPLE);

            showLocalTime();
            //  shareToUser();
            userT();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private void showLocalTime() {
        LocalDate d1 = LocalDate.now();
        String d1Str = d1.format(DateTimeFormatter.ISO_DATE);
        dateTime.setText("Today's date : " + d1Str);

    }

    public void vos() {
        if (dataSingleton.getUserName() != null) {
            if (String.valueOf(Path.of(dataSingleton.getUserName()).getParent()).contains("specific_client_folder")) {
                homeDir = String.valueOf(Path.of(dataSingleton.getUserName()).getParent());
                System.out.println(staticVal + "static value");
                dir = staticVal;
                nameUser = nameUser.substring(nameUser.lastIndexOf("\\") + 1);
            } else {
                dir = String.valueOf(Path.of(dataSingleton.getUserName()).getParent());
                String splitSerPath = String.valueOf(staticVal);
                String last = splitSerPath.substring(splitSerPath.lastIndexOf("\\") + 1);
                Path path1 = Path.of(homeDir, last);
                System.out.println(path1 + " path1");
                homeDir = String.valueOf(path1);
            }
        }
    }


    private void userT() {
        for (String s : list) {
            if (staticVal.endsWith(s)) {
                int index = list.indexOf(s);
                System.out.println(index + " index");
                list.stream().skip(index).collect(Collectors.toList());
                System.out.println(s + "skip");
                nickNameLabel.setText("Your user name is :" + s.toUpperCase(Locale.ROOT));
            } else {
                listViewForOnlineUsers.getItems().add(s);
            }
        }
    }

    public void createPathForClient() {
        try {
            CloudMessage message = network.read();
            if (message instanceof SendName sendName) {
                System.out.println(sendName.getName() + " " + "sendName");
                log.info(sendName.getName() + " " + "sendName");
            }

        } catch (Exception e) {
            System.err.println("Connection lost");
            log.error("Connection lost");
        }
    }

    private void sendPath() throws IOException {
        String forCopy = client_text_field.getText();
        network.write(new CurrentDirectorForSaveFile(forCopy));
        log.info(forCopy + " " + "forCopy in sendPath");
    }

    private void initTable() throws IOException {
        TableColumn<FileInfo, String> fileInfoStringTableColumn2 = new TableColumn<>();
        fileInfoStringTableColumn2.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getName()));
        fileInfoStringTableColumn2.setPrefWidth(24);
        TableColumn<FileInfo, String> fileInfoColumn2 = new TableColumn<>("Name");
        fileInfoColumn2.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFilename()));
        fileInfoColumn2.setPrefWidth(120);
        TableColumn<FileInfo, Long> fileSizeColumn2 = new TableColumn<>("Size");
        fileSizeColumn2.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSize()));
        fileSizeColumn2.setPrefWidth(130);
        fileSizeColumn2.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    String text = String.format("%,d bytes", item);
                    if (item == -1L) {
                        text = "[dir]";
                    }
                    setText(text);
                }
            }
        });
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TableColumn<FileInfo, String> fileDateColumn2 = new TableColumn<>("Date modified");
        fileDateColumn2.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastModified().format(dtf2)));
        fileDateColumn2.setPrefWidth(120);
        server_is_list_table.getSortOrder().add(fileInfoColumn2);
        server_is_list_table.getColumns().addAll(fileInfoColumn2, fileInfoStringTableColumn2, fileSizeColumn2, fileDateColumn2);
        update2(Paths.get(dir), server_is_list_table, server_text_field);
    }

    private void initTableForClient() {
        TableColumn<FileInfo, String> fileInfoStringTableColumn = new TableColumn<>();
        fileInfoStringTableColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getName()));
        fileInfoStringTableColumn.setPrefWidth(24);
        TableColumn<FileInfo, String> fileInfoColumn = new TableColumn<>("Name");
        fileInfoColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFilename()));
        fileInfoColumn.setPrefWidth(120);

        TableColumn<FileInfo, Long> fileSizeColumn = new TableColumn<>("Size");
        fileSizeColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSize()));
        fileSizeColumn.setPrefWidth(100);
        fileSizeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    String text = String.format("%,d bytes", item);
                    if (item == -1L) {
                        text = "[dir]";
                    }
                    setText(text);
                }
            }
        });

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TableColumn<FileInfo, String> fileDateColumn = new TableColumn<>("Date Modified");
        fileDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastModified().format(dtf)));
        fileDateColumn.setPrefWidth(120);
        client_is_list_table.getColumns().addAll(fileInfoColumn, fileInfoStringTableColumn, fileSizeColumn, fileDateColumn);
        //initTable();
        update2(Paths.get(homeDir), client_is_list_table, client_text_field);
    }

    private void readLoop() {
        try {
            while (true) {
                CloudMessage message = network.read();
                if (message instanceof ListFiles) {
                    //?????
                    Platform.runLater(() -> {
                        update2(Path.of(client_text_field.getText()), client_is_list_table, client_text_field);
                        update2(Path.of(server_text_field.getText()), server_is_list_table, server_text_field);
                    });
                } else if (message instanceof FileMessage fileMessage) {
                    Path current = Path.of(homeDir).resolve(fileMessage.getName());
                    Files.write(current, fileMessage.getData());
                    Platform.runLater(() -> {
                    });
                } else if (message instanceof SendName sendName) {
                    log.info(sendName.getName() + " " + "sendName.getName() network read");
                    homeDir = sendName.getName();

                } else if (message instanceof ServerNameFolder serverNameFolder) {
                    System.out.println(serverNameFolder.getName() + " " + "serverNameFolder");
                    String loginTest = serverNameFolder.getName();
                    dir = loginTest;
                    staticVal = loginTest;
                    nameUser = staticVal;
                    forRemovePathLogin = serverNameFolder.getName();
                    System.out.println(forRemovePathLogin + " forRemovePathLogin");
                    System.out.println(staticVal + " " + "staticVal");
                    String listOfUsers = staticVal;
                    System.out.println(listOfUsers + " serverNameFolder  listOfUsers");
                } else if (message instanceof ListUsers listUsers) {
                    list.addAll(listUsers.getUsers());
                }
            }
        } catch (Exception e) {
            System.err.println("Connection lost");
            log.error("Connection lost");
        }
    }

    private List<String> getFiles(String dir) {
        String[] list = new File(dir).list();
        assert list != null;
        return Arrays.asList(list);
    }

    public void btnExitAction() throws IOException {
        network.write(new RemoveLogin(forRemovePathLogin));
        Platform.exit();
    }

    public void update2(Path path, TableView<FileInfo> tableView, TextField textField) {
        updateFileList2(path, tableView, textField);
    }

    private void updateFileList2(Path path, TableView<FileInfo> tableview, TextField textField) {
        try {
            textField.setText(path.normalize().toAbsolutePath().toString());
            tableview.getItems().clear();
            tableview.getItems().addAll(Files.list(path).map(FileInfo::new).toList());
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "can't refresh lit files", ButtonType.OK);
            alert.showAndWait();
            tableview.sort();
            throw new RuntimeException(e);
        }
    }

    public void btnUpload() throws IOException {
        downloadAndUpload(server_is_list_table, client_text_field, server_text_field);
    }

    public void btnDownload(ActionEvent actionEvent) throws IOException {
        downloadAndUpload(client_is_list_table, server_text_field, client_text_field);
    }

    private void downloadAndUpload(TableView<FileInfo> client_is_list_table, TextField
            server_text_field, TextField client_text_field) throws IOException {
        String file2 = client_is_list_table.getSelectionModel().getSelectedItem().getFilename();
        Path path2 = Paths.get(server_text_field.getText()).normalize().resolve(file2);
        String forCopy = String.valueOf(path2);
        network.write(new CurrentDirectorForSaveFile(forCopy));
        if (client_is_list_table.isFocused()) {
            String toFolder = client_is_list_table.getSelectionModel().getSelectedItem().getFilename();
            Path path = Paths.get(client_text_field.getText()).resolve(toFolder);
            String file = String.valueOf(path);
            if (file != null) {
                System.out.println(file + " " + "file");
                network.write(new FileInfo(Path.of(file)));
                network.write(new FileRequest(file));
            }
        } else {
            log.debug("choose file from server side");
        }
    }

    private void downloadAndUploadMultiple(TableView<FileInfo> tableView, TextField
            currentDirectoryTextField, TextField whereWillBeCopiedText) throws IOException {
        MultipleSelectionModel<FileInfo> lvSelModel =
                tableView.getSelectionModel();
        lvSelModel.setSelectionMode(SelectionMode.MULTIPLE);
        for (FileInfo selectedItem : lvSelModel.getSelectedItems()) {
            Path path2 = Paths.get(currentDirectoryTextField.getText()).normalize().resolve(selectedItem.getFilename());
            String forCopy = String.valueOf(path2);
            network.write(new CurrentDirectorForSaveFile(forCopy));
            if (tableView.isFocused()) {
                Path path = Paths.get(whereWillBeCopiedText.getText()).resolve(selectedItem.getFilename());
                String file = String.valueOf(path);
                if (file != null) {
                    if (!Files.isDirectory(path)) {
                        System.out.println(file + " " + "file");
                        log.info(file + " " + "file");
                        network.write(new FileInfo(Path.of(file)));
                        network.write(new FileRequest(file));
                    }
                }
            } else {
                log.debug("choose file from server side");
            }
        }
    }

    public void btnMultipleUpload() throws IOException {
        if (server_is_list_table.isFocused()) {
            downloadAndUploadMultiple(server_is_list_table, client_text_field, server_text_field);
        } else {
            downloadAndUploadMultiple(client_is_list_table, server_text_field, client_text_field);
        }
    }

    public void backPathUpRequestMethodClientTable(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            String toFolder = client_is_list_table.getSelectionModel().getSelectedItem().getFilename();
            Path path = Paths.get(client_text_field.getText()).resolve(toFolder);
            if (Files.isDirectory(path)) {
                updateFileList2(path, client_is_list_table, client_text_field);
                log.debug("is directory");
            } else {
                log.debug("is not directory can't go inside");
            }
        }
    }

    public void backPathUpRequestMethodServerTable(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            if (server_is_list_table.getSelectionModel().getSelectedItem() != null) {
                String toFolder = server_is_list_table.getSelectionModel().getSelectedItem().getFilename();
                Path path = Paths.get(server_text_field.getText()).resolve(toFolder);
                if (Files.isDirectory(path)) {
                    updateFileList2(path, server_is_list_table, server_text_field);
                    System.out.println("is directory");
                    log.info("is directory");
                } else {
                    System.out.println("is not");
                    log.info("is not directory");
                }
            } else {
                System.out.println("nothing selected");
                log.info("nothing selected");
            }
        }
    }


    public void goBackDirectoryServer() {
        Path path = Paths.get(server_text_field.getText()).getParent();
        if (path != null) {
            updateFileList2(path, server_is_list_table, server_text_field);
        }
    }

    public void goBackDirectoryClient() {
        Path path = Paths.get(client_text_field.getText()).getParent();
        if (path != null) {
            updateFileList2(path, client_is_list_table, client_text_field);
        }
    }

    public void menuContextExit() throws IOException {
        btnExitAction();
    }

    public void menuContextUpload() throws IOException {
        btnUpload();
    }

    public void menuContextDownload(ActionEvent actionEvent) throws IOException {
        btnDownload(actionEvent);
    }

    public void menuContextDeleteFile() throws IOException {
        if (server_is_list_table.isFocused()) {
            network.write(new FileForDelete(fileForDelete(server_is_list_table, server_text_field)));
            System.out.println(fileForDelete(server_is_list_table, server_text_field) + " " + "server");
        } else {
            network.write(new FileForDelete(fileForDelete(client_is_list_table, client_text_field)));
            System.out.println(fileForDelete(client_is_list_table, client_text_field) + " " + "client");
        }
    }

    public String fileForDelete(TableView<FileInfo> tableView, TextField textField) {
        String file = tableView.getSelectionModel().getSelectedItem().getFilename();
        return String.valueOf(Paths.get(textField.getText()).resolve(file));
    }

    public void refreshMethod() {
        update2(Paths.get(dir), server_is_list_table, server_text_field);
        update2(Paths.get(homeDir), client_is_list_table, client_text_field);
    }

    public void menuContextOpen() {
        try {
            if (server_is_list_table.isFocused()) {
                dataSingleton.setUserName(forPassData(server_is_list_table, server_text_field));
                setScene(server_text_field);
            } else {
                dataSingleton.setUserName(forPassData(client_is_list_table, client_text_field));
                setScene(client_text_field);
            }
        } catch (Exception e) {
            System.out.println("can't load new window");
        }
    }

    public String forPassData(TableView<FileInfo> tableview, TextField text_field) {
        String toFolder = tableview.getSelectionModel().getSelectedItem().getFilename();
        Path path = Paths.get(text_field.getText()).resolve(toFolder);
        return String.valueOf(path);
    }

    public void setScene(TextField text_field) throws IOException {
        Stage stage = (Stage) text_field.getScene().getWindow();
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("secondforopenfile.fxml"))));
        root.getStylesheets().add(Objects.requireNonNull(CloudController.class.getResource("Background.css")).toExternalForm());
        stage.setTitle(text_field.getText());
        stage.setScene(new Scene(root));

    }

    public void menuQuitMethod() throws IOException {
        btnExitAction();
    }

    public void helpMenuClicked(ActionEvent event) {
        helpMenu.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                Label secondLabel = new Label("Created in august 2022");
                StackPane secondaryLayout = new StackPane();
                secondaryLayout.getChildren().add(secondLabel);
                Scene secondScene = new Scene(secondaryLayout, 400, 400);
                // New window (Stage)
                Stage newWindow = new Stage();
                newWindow.setTitle("About app");
                newWindow.setScene(secondScene);
                // Set position of second window, related to primary window.
                newWindow.setX(100);
                newWindow.setY(100);
                newWindow.show();
            }
        });
    }

    public void shareToUser() {
        if (listViewForOnlineUsers.isFocused()) {
            String s = listViewForOnlineUsers.getSelectionModel().getSelectedItem();
            System.out.println("s = " + s);
        }
    }
}