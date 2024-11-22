module br.com.fatec {
    requires javafx.controls;
    requires javafx.fxml;

    opens br.com.fatec to javafx.fxml;
    exports br.com.fatec;
}
