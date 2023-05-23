module com.example.tcpserver {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.tcpserver to javafx.fxml;
    exports com.example.tcpserver;
}