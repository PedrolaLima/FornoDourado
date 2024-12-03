package br.com.fatec;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    private static Object currentController;

    @Override
    public void start(Stage stage) throws IOException {
        //scene = new Scene(loadFXML("menu"));
        scene = new Scene(loadFXML("dashboard"));
        scene.getStylesheets().add(getClass().getResource("/br/com/fatec/Imagens/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        // Carrega o novo FXML
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent root = loader.load();

        // Atualiza a cena com o novo root
        scene.setRoot(root);

        // Armazena o controlador da nova tela
        currentController = loader.getController();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    // Método para recuperar o controlador da tela atual
    public static <T> T getController(String fxmlName) {
        if (currentController != null) {
            return (T) currentController; // Retorna o controlador armazenado
        }
        return null;
    }

}
