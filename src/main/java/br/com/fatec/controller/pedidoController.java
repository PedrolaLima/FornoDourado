package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.dao.ProdutoDAO;
import br.com.fatec.model.Produto;
import javafx.fxml.FXML;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.cell.PropertyValueFactory;



public class pedidoController implements Initializable {

    @FXML
    public Label date;
    
    @FXML
    private TableView<Produto> ordersTable;
    
    @FXML
    private TableColumn<Produto, String> colProduto;

    @FXML
    private TableColumn<Produto, String> colCodigo;

    @FXML
    private TableColumn<Produto, String> colCategoria;

    @FXML
    private TableColumn<Produto, String> colPreco;

    @FXML
    private void carregarProdutos() throws IOException {
        App.setRoot("produto");
    }

    @FXML
    private void carregarDashboard() throws IOException {
        App.setRoot("dashboard");
    }
    
    @FXML
    private void carregarFuncionarios() throws IOException {
        App.setRoot("funcionarios");
    }  
    
    @FXML
    private void carregarRelatorios() throws IOException {
        App.setRoot("relatorio");
    }
    
    @FXML
    private void carregarPedidos() throws IOException {
        App.setRoot("pedido");
    }
     
    @FXML
    private void carregarFimPedido() throws IOException {
        App.setRoot("finalizarPedido");
    }
      
    @FXML
    private ComboBox<String> productCategory;  // A ComboBox para categorias de produtos

    @FXML
    private AnchorPane profilePane;
    
    @FXML
    private AnchorPane profileBack; 
    
    @FXML
    private ImageView profile; 

    
    /**private void configurarTabela() {
        // Configuração das colunas
        colProduto.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("cod"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("cat"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));

        // Adicionando dados ao TableView
        ProdutoDAO produtoDAO = new ProdutoDAO();
        ObservableList<Produto> produtos = FXCollections.observableArrayList(ProdutoDAO.getTable());

        ordersTable.setItems(produtos); // Adicionando produtos à tabela

        // Configurar a coluna de Ações
        TableColumn<Produto, Void> actionColumn = new TableColumn<>("Quantidade");
        actionColumn.setPrefWidth(120); // Define a largura para caber os botões e o valor de qtd

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button plusButton = new Button();
            private final Button minusButton = new Button();
            private final Label qtdLabel = new Label("0"); // Label para exibir o valor da quantidade

            {
                // Configurando os ícones para os botões
                ImageView plusIcon = new ImageView("br/com/fatec/Imagens/icons/pedidos/mais.png");
                plusIcon.setFitWidth(16);
                plusIcon.setFitHeight(16);
                plusButton.setGraphic(plusIcon);

                ImageView minusIcon = new ImageView("br/com/fatec/Imagens/icons/pedidos/menos.png");
                minusIcon.setFitWidth(16);
                minusIcon.setFitHeight(16);
                minusButton.setGraphic(minusIcon);

                // Configuração do estilo dos componentes
                qtdLabel.setStyle("-fx-alignment: center; -fx-font-size: 14px;"); // Centraliza o texto e ajusta o tamanho

                // Ação do botão mais
                plusButton.setOnAction(event -> {
                    Produto produto = getTableView().getItems().get(getIndex());
                    produto.setQtd(produto.getQtd() + 1); // Incrementar a quantidade
                    qtdLabel.setText(String.valueOf(produto.getQtd())); // Atualizar o valor no rótulo
                });

                // Ação do botão menos
                minusButton.setOnAction(event -> {
                    Produto produto = getTableView().getItems().get(getIndex());
                    if (produto.getQtd() > 0) { // Verifica para evitar valor negativo
                        produto.setQtd(produto.getQtd() - 1); // Decrementar a quantidade
                        qtdLabel.setText(String.valueOf(produto.getQtd())); // Atualizar o valor no rótulo
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    // Configurar layout dos botões e do valor de qtd
                    HBox container = new HBox(5); // Espaçamento entre os elementos
                    container.setAlignment(Pos.CENTER); // Centraliza o conteúdo
                    container.getChildren().addAll(minusButton, qtdLabel, plusButton); // Adiciona os componentes ao painel
                    setGraphic(container);
                }
            }
        });

        ordersTable.getColumns().add(actionColumn); // Adiciona a coluna de ações
    }
    */
    
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
    }
}
