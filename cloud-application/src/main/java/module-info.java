module com.example.cloudapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    requires io.netty.codec;
    requires cloud.model;
    requires lombok;
    requires org.slf4j;
    requires io.netty.transport;


    opens com.example.cloudapplication_2022 to javafx.fxml;
    exports com.example.cloudapplication_2022;
}