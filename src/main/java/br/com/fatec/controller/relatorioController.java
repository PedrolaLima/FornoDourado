package br.com.fatec.controller;

import br.com.fatec.App;
import javafx.fxml.FXML;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class relatorioController implements Initializable {

    @FXML
    public Label date;
    // Referência global para o Stage da janela de quantidade
    private Stage quantidadeStage;

    
    // Método para carregar a tela de "Adicionar Produto"
    @FXML
    private void carregarProdutos() throws IOException {
        App.setRoot("produto");
    }

    // Método para carregar a tela de "Pedidos"
    @FXML
    private void carregarPedidos() throws IOException {
        App.setRoot("pedido");
    }
    
    @FXML
    private void carregarFuncionarios() throws IOException {
        App.setRoot("funcionarios");
    }  
    
    @FXML
    private void carregarDashboard() throws IOException {
        App.setRoot("dashboard");
    }
    
    @FXML
    private void carregarRelatorios() throws IOException {
        App.setRoot("relatorio");
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
        
        
        //FILTRO RELATÓRIOS
        int[] quantidadeSelecionada = new int[1];  // Array é usado para capturar o valor dentro do evento do slider

        // Criar o ContextMenu principal para o "filter"
        ContextMenu filterMenu = new ContextMenu();

        // Criar os itens principais do menu
        MenuItem paymentMenuItem = new MenuItem("Forma de Pagamento");
        MenuItem quantidadeMenuItem = new MenuItem("Quantidade");
        MenuItem semFiltro = new MenuItem("Sem filtro");

        // Submenu para "Forma de Pagamento"
        ContextMenu paymentSubMenu = new ContextMenu();
        MenuItem pixItem = new MenuItem("PIX");
        MenuItem creditItem = new MenuItem("Cartão de Crédito");
        MenuItem debitItem = new MenuItem("Cartão de Débito");
        MenuItem  moneyItem = new MenuItem("Dinheiro");
        paymentSubMenu.getItems().addAll(pixItem, creditItem, debitItem, moneyItem);

        // Configurar eventos para abrir os submenus
        paymentMenuItem.setOnAction(event -> {
            double xPos = filter.localToScreen(filter.getLayoutX(), filter.getLayoutY()).getX() + 100;
            double yPos = filter.localToScreen(filter.getLayoutX(), filter.getLayoutY()).getY() + 21;
            paymentSubMenu.show(filter, xPos, yPos);
        });
        
        
        quantidadeMenuItem.setOnAction(event -> {
            carregarQuantidadeFiltro();
        });

        semFiltro.setOnAction(event -> {
            System.out.println("Sem filtro");
            // Lógica para remover os filtros
        });

        

        //Adicionar os itens principais ao menu do "filter"
        filterMenu.getItems().addAll(paymentMenuItem, quantidadeMenuItem, semFiltro);

        // Configurar o evento de clique no "filter" para abrir o menu
        filter.setOnMouseClicked(event -> {
            filterMenu.hide();
            paymentSubMenu.hide();
            if (quantidadeStage != null) {
                quantidadeStage.close();
            }
            
            double xPos = filter.localToScreen(filter.getLayoutX(), filter.getLayoutY()).getX() - 15;
            double yPos = filter.localToScreen(filter.getLayoutX(), filter.getLayoutY()).getY() + 21;
            filterMenu.show(filter, xPos, yPos);
        });
    }
    
        @FXML
        private void carregarQuantidadeFiltro() {
        // Criar uma nova janela para mostrar o filtro
        Stage quantidadeStage = new Stage();
        quantidadeStage.setTitle("Filtro de Quantidade");

        // Criar um VBox para layout
        VBox quantidadeLayout = new VBox(10);
        quantidadeLayout.setStyle("-fx-padding: 20px; -fx-alignment: center;");

        // Criar o slider com intervalos reduzidos (entre 1 e 20)
        Slider quantidadeSlider = new Slider(1, 20, 1);
        quantidadeSlider.setBlockIncrement(1);
        quantidadeSlider.setShowTickMarks(true);
        quantidadeSlider.setMajorTickUnit(5);
        quantidadeSlider.setMinorTickCount(0);

        // Label para mostrar o valor do slider
        Label quantidadeLabel = new Label("Quantidade: " + (int)quantidadeSlider.getValue());

        // Atualiza o valor do label conforme o slider é movido
        quantidadeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            quantidadeLabel.setText("Quantidade: " + newValue.intValue());
        });

        // Variável para armazenar a quantidade selecionada
        final int[] quantidadeSelecionada = new int[1];

        // Botão OK para salvar a seleção
        Button okButton = new Button("OK");
        okButton.setOnAction(event -> {
            quantidadeSelecionada[0] = (int)quantidadeSlider.getValue(); // Armazena o valor quando OK for clicado
            System.out.println("Quantidade selecionada: " + quantidadeSelecionada[0]);

            // Fechar a janela após selecionar
            quantidadeStage.close();
        });

        // Adicionar o slider, o label e o botão no layout
        quantidadeLayout.getChildren().addAll(quantidadeSlider, quantidadeLabel, okButton);
        
        // Criar a cena e configurar a janela
        Scene quantidadeScene = new Scene(quantidadeLayout, 300, 100);
        quantidadeStage.setScene(quantidadeScene);
        quantidadeStage.show();
    }
}
   
