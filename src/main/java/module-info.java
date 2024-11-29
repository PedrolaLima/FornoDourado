module FornoDourado {
    requires javafx.controls; 
    requires javafx.fxml;    
    requires javafx.base;
    requires java.sql;

    opens br.com.fatec to javafx.fxml;
    opens br.com.fatec.controller to javafx.fxml;

    exports br.com.fatec;
    exports br.com.fatec.controller;
}
