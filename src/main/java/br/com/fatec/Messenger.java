package br.com.fatec;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class Messenger {

    public static void info(String title, String[] msg) {
        String s = String.join("\n", msg);
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText("");
        a.setContentText(s);
        a.showAndWait();
    }

    public static void info(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText("");
        a.setContentText(msg);
        a.showAndWait();
    }

    public static void warn(String title, String[] msg) {
        String s = String.join("\n", msg);
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(title);
        a.setHeaderText("");
        a.setContentText(s);
        a.showAndWait();
    }

    public static void error(String title, String[] msg) {
        String s = String.join("\n", msg);
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText("");
        a.setContentText(s);
        a.showAndWait();
    }

    public static void error(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText("");
        a.setContentText(msg);
        a.showAndWait();
    }

    public static boolean confirm(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType buttonYes = new ButtonType("Sim", ButtonBar.ButtonData.YES);
        ButtonType buttonNo = new ButtonType("Não", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        return alert.showAndWait().orElse(buttonNo) == buttonYes;
    }

}
