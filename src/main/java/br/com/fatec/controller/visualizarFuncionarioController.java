package br.com.fatec.controller;

import br.com.fatec.App;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

public class visualizarFuncionarioController implements Initializable {
     // Método para carregar a tela de "Adicionar Produto"
    @FXML
    private void carregarDashboard() throws IOException {
        App.setRoot("dashboard");
    }

    // Método para carregar a tela de "Pedidos"
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
    
    
    @FXML
    private AnchorPane profilePane; 
    
    @FXML
    private AnchorPane profileBack; 
    
    @FXML
    private ImageView profile;
        
    @FXML
    private ImageView employeeview;
    
    @FXML
    private Label date;
  
    public void initialize(URL url, ResourceBundle rb) {
        
         date.setText(LocalDate.now(
                ZoneId.of( "America/Sao_Paulo" )
        ).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        // Criar o menu
        ContextMenu contextMenu = new ContextMenu();

        // Criar itens do menu
        MenuItem menuItem1 = new MenuItem("Meu Perfil");
        MenuItem menuItem2 = new MenuItem("Sair");

        // Ações dos itens do menu
        menuItem1.setOnAction(event ->  {
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
            // Chama o código dentro do thread JavaFX
            javafx.application.Platform.runLater(() -> {
                // Obtém a posição do profilePane na tela
                double xPos = profilePane.localToScreen(profilePane.getLayoutX(), profilePane.getLayoutY()).getX() - 880;
                double yPos = profilePane.localToScreen(profilePane.getLayoutX(), profilePane.getLayoutY()).getY() - 3; // 70 pixels abaixo

                // Exibe o menu na nova posição, em coordenadas absolutas
                contextMenu.show(profilePane, xPos, yPos);
            });
        });
        
        profilePane.setOnMouseEntered(event -> {
            profileBack.getStyleClass().add("image-view-hover");
        });

        // Remove a classe CSS de hover ao sair do profilePane
        profilePane.setOnMouseExited(event -> {
            profileBack.getStyleClass().remove("image-view-hover");
        });
    
        
        // Adiciona bordas arredondadas ao ImageView do profile.
        Rectangle clip = new Rectangle(
         profile.getFitWidth(), profile.getFitHeight()
        );
        clip.setArcWidth(25); // Ajuste o raio para bordas mais ou menos arredondadas.
        clip.setArcHeight(25);
        profile.setClip(clip);

        // Cria uma imagem arredondada.
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT); // Fundo transparente.
        WritableImage image = profile.snapshot(parameters, null);

        // Remove o clipe para exibir os efeitos.
        profile.setClip(null);

        // Define a imagem arredondada no ImageView.
        profile.setImage(image);
       
        
        //FOTO DO EMPREGADO
        
        // Adiciona bordas arredondadas ao ImageView.
        Rectangle clip2 = new Rectangle(
            employeeview.getFitWidth(), employeeview.getFitHeight()
        );
        clip2.setArcWidth(25); // Ajuste o raio para bordas mais ou menos arredondadas.
        clip2.setArcHeight(25);
        employeeview.setClip(clip2);

        // Cria uma imagem arredondada.
        SnapshotParameters parameters2 = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT); // Fundo transparente.
        WritableImage image2 = employeeview.snapshot(parameters2, null);

        // Remove o clipe para exibir os efeitos.
        employeeview.setClip(null);

        // Define a imagem arredondada no ImageView.
        employeeview.setImage(image2);
    }
}

