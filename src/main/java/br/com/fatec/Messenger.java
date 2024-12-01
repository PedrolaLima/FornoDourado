package br.com.fatec;

import javafx.scene.control.Alert;

public class Messenger {
    public static void info(String title, String[] msg){
        String s = String.join("\n",msg);
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText("");
        a.setContentText(s);
        a.showAndWait();
        a.showAndWait();
    }

    public static void warn(String title, String[] msg){
        String s = String.join("\n",msg);
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(title);
        a.setHeaderText("");
        a.setContentText(s);
        a.showAndWait();
    }

    public static void error(String title, String[] msg){
        String s = String.join("\n",msg);
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText("");
        a.setContentText(s);
        a.showAndWait();
    }
}
