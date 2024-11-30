
package br.com.fatec.controller;

import br.com.fatec.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Pedro
 */
public class funcionarioController implements Initializable {
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
    private ImageView filter;
  
    public void initialize(URL url, ResourceBundle rb) {
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
    
        
        // Adiciona bordas arredondadas ao ImageView.
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
    
        //FILTRO FUNCIONÁRIOS
    
        // Criar o ContextMenu principal para o "filter"
        ContextMenu filterMenu = new ContextMenu();

        // Criar os itens principais do menu
        MenuItem cargoMenuItem = new MenuItem("Cargo");
        MenuItem statusMenuItem = new MenuItem("Status");
        MenuItem semFiltro = new MenuItem("Sem filtro");


        // Submenu para "Cargo"
        ContextMenu cargoSubMenu = new ContextMenu();
        MenuItem adminItem = new MenuItem("Administrador");
        MenuItem supervisorItem = new MenuItem("Supervisor");
        MenuItem atendenteItem = new MenuItem("Atendente");
        cargoSubMenu.getItems().addAll(adminItem, supervisorItem, atendenteItem);

        // Submenu para "Status"
        ContextMenu statusSubMenu = new ContextMenu();
        MenuItem ativoItem = new MenuItem("Ativo");
        MenuItem desativadoItem = new MenuItem("Desativado");
        statusSubMenu.getItems().addAll(ativoItem, desativadoItem);

        // Configurar eventos para abrir os submenus
        cargoMenuItem.setOnAction(event -> {
            double xPos = filter.localToScreen(filter.getLayoutX(), filter.getLayoutY()).getX()- 70;
            double yPos = filter.localToScreen(filter.getLayoutX(), filter.getLayoutY()).getY() - 92;
            cargoSubMenu.show(filter, xPos, yPos);
        });

        statusMenuItem.setOnAction(event -> {
            double xPos = filter.localToScreen(filter.getLayoutX(), filter.getLayoutY()).getX() - 70;
            double yPos = filter.localToScreen(filter.getLayoutX(), filter.getLayoutY()).getY() - 92;
            statusSubMenu.show(filter, xPos, yPos);
        });
        
        semFiltro.setOnAction(event -> {
            System.out.println("TÁ SEM FILTRO CONFIA");
            
            //TO DO
        });

        
        
        // Adicionar os itens principais ao menu do "filter"
        filterMenu.getItems().addAll(cargoMenuItem, statusMenuItem, semFiltro);

        // Configurar o evento de clique no "filter" para abrir o menu
        filter.setOnMouseClicked(event -> {
            filterMenu.hide();
            statusSubMenu.hide();
            cargoSubMenu.hide();
            
            double xPos = filter.localToScreen(filter.getLayoutX(), filter.getLayoutY()).getX() - 125;
            double yPos = filter.localToScreen(filter.getLayoutX(), filter.getLayoutY()).getY() - 86;
            filterMenu.show(filter, xPos, yPos);
        });
    }
}
   
