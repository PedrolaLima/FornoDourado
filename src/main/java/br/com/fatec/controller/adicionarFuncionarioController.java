package br.com.fatec.controller;

import br.com.fatec.App;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.util.converter.IntegerStringConverter;

public class adicionarFuncionarioController implements Initializable {

    @FXML
    private AnchorPane profilePane;
    @FXML
    private AnchorPane profileBack;
    @FXML
    private ImageView profile;
    @FXML
    private ImageView employee;
    @FXML
    private TextField cepField;
    @FXML
    private TextField cpfField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Criar o menu
        ContextMenu contextMenu = new ContextMenu();

        // Criar itens do menu
        MenuItem menuItem1 = new MenuItem("Meu Perfil");
        MenuItem menuItem2 = new MenuItem("Sair");

        // Ações dos itens do menu
        menuItem1.setOnAction(event -> {
            try {
                App.setRoot("visualizarFuncionario");
            } catch (IOException ex) {
                Logger.getLogger(dashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        menuItem2.setOnAction(event -> {
            try {
                App.setRoot("menu");
            } catch (IOException ex) {
                Logger.getLogger(dashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Adicionar itens ao menu
        contextMenu.getItems().addAll(menuItem1, menuItem2);

        profilePane.setOnMouseClicked(event -> {
            javafx.application.Platform.runLater(() -> {
                double xPos = profilePane.localToScreen(profilePane.getLayoutX(), profilePane.getLayoutY()).getX() - 880;
                double yPos = profilePane.localToScreen(profilePane.getLayoutX(), profilePane.getLayoutY()).getY() - 3;
                contextMenu.show(profilePane, xPos, yPos);
            });
        });

        profilePane.setOnMouseEntered(event -> profileBack.getStyleClass().add("image-view-hover"));
        profilePane.setOnMouseExited(event -> profileBack.getStyleClass().remove("image-view-hover"));

        // Configuração de bordas arredondadas no profile
        Rectangle clip = new Rectangle(profile.getFitWidth(), profile.getFitHeight());
        clip.setArcWidth(25);
        clip.setArcHeight(25);
        profile.setClip(clip);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = profile.snapshot(parameters, null);
        profile.setClip(null);
        profile.setImage(image);

        // Configuração de imagem para o employee
        employee.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagens", "*.jpg", "*.jpeg", "*.png"));
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                Image newImage = new Image(selectedFile.toURI().toString());
                employee.setImage(newImage);

                SnapshotParameters newParams = new SnapshotParameters();
                newParams.setFill(Color.TRANSPARENT);
                WritableImage newWritableImage = employee.snapshot(newParams, null);
                employee.setClip(null);
                employee.setImage(newWritableImage);

                Rectangle newClip = new Rectangle(employee.getFitWidth(), employee.getFitHeight());
                newClip.setArcWidth(25);
                newClip.setArcHeight(25);
                employee.setClip(newClip);
            }
        });

       // Máscara para o campo de CEP (formatação dinâmica)
UnaryOperator<TextFormatter.Change> cepFilter = change -> {
    String text = change.getControlNewText().replaceAll("[^\\d]", ""); // Remove qualquer não numérico
    StringBuilder formatted = new StringBuilder();

    // Formata como "00000-000"
    if (text.length() > 5) {
        formatted.append(text.substring(0, 5)).append("-");
        formatted.append(text.substring(5, Math.min(text.length(), 8)));
    } else {
        formatted.append(text);
    }

    // Atualiza o texto
    change.setText(formatted.toString());
    change.setRange(0, change.getControlText().length()); // Substitui tudo
    return change;
};

TextFormatter<String> textFormatterCep = new TextFormatter<>(cepFilter);
cepField.setTextFormatter(textFormatterCep);

    // Máscara para o campo de CPF (formatação dinâmica)
    UnaryOperator<TextFormatter.Change> cpfFilter = change -> {
        String text = change.getControlNewText().replaceAll("[^\\d]", ""); // Remove qualquer não numérico
        StringBuilder formatted = new StringBuilder();

        // Formata como "000.000.000-00"
        if (text.length() > 3) {
            formatted.append(text.substring(0, 3)).append(".");
            if (text.length() > 6) {
                formatted.append(text.substring(3, 6)).append(".");
                if (text.length() > 9) {
                    formatted.append(text.substring(6, 9)).append("-");
                    formatted.append(text.substring(9, Math.min(text.length(), 11)));
                } else {
                    formatted.append(text.substring(6));
                }
            } else {
                formatted.append(text.substring(3));
            }
        } else {
            formatted.append(text);
        }

            // Atualiza o texto
            change.setText(formatted.toString());
            change.setRange(0, change.getControlText().length()); // Substitui tudo
            return change;
    };

    TextFormatter<String> textFormatterCpf = new TextFormatter<>(cpfFilter);
    cpfField.setTextFormatter(textFormatterCpf);

}

    // Métodos para navegação entre telas
    @FXML
    private void carregarDashboard() throws IOException {
        App.setRoot("dashboard");
    }

    @FXML
    private void carregarPedidos() throws IOException {
        App.setRoot("pedido");
    }

    @FXML
    private void carregarRelatorios() throws IOException {
        App.setRoot("relatorio");
    }

    @FXML
    private void carregarProdutos() throws IOException {
        App.setRoot("produto");
    }

    @FXML
    private void adicionarFuncionario() throws IOException {
        App.setRoot("adicionarFuncionario");
    }

    @FXML
    private void cancelarFuncionario() throws IOException {
        App.setRoot("funcionarios");
    }
}
