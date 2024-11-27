module FornoDourado {
    requires javafx.controls; 
    requires javafx.fxml;    
    requires javafx.base;
    requires java.sql;
    //requires java.util.HexFormat;
    
    opens br.com.fatec to javafx.fxml;

    exports br.com.fatec;
}
