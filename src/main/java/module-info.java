module br.com.fatec {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    //requires java.util.HexFormat;
    
    opens br.com.fatec to javafx.fxml;
    exports br.com.fatec;
}
