
package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.dao.ProdutoDAO;
import br.com.fatec.data.PedidoHolder;
import br.com.fatec.model.Pedido;
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
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.cell.PropertyValueFactory;


public class finalizarPedidoController implements Initializable {

    @FXML
    private Label date;

    @FXML
    private Label profilePaneName;

    @FXML
    private Label profilePaneType;

    @FXML
    private TableView<Produto> ordersTable;

    @FXML
    private TableColumn<Produto, String> colProduto;

    @FXML
    private TableColumn<Produto, String> colQtd ;

    @FXML
    private TableColumn<Produto, String> colSubtotal;

    @FXML
    private TableColumn<Produto, String> colPreco;

    @FXML
    private TextField searchBar;

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
    private AnchorPane profilePane;

    @FXML
    private AnchorPane profileBack;

    @FXML
    private ImageView profile;

    @FXML
    private ImageView btn_confirmar;

    @FXML
    private ImageView btn_cancelar;
    
    @FXML
    private Label subtotalLabel;
    
    @FXML
    private Label discountLabel;
    
    @FXML
    private Label totalLabel;
    
    @FXML
    private Label percentLabel;
    
    @FXML
    private Label orderNumberLabel;
    

    @FXML
    private void onCancelarClicked() throws IOException {
        App.setRoot("pedido");
    }

    private void configurarTabela() {
        // Configuração das colunas
        colProduto.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colQtd.setCellValueFactory(new PropertyValueFactory<>("cod"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("cat"));

        // Adicionando dados ao TableView
        ProdutoDAO produtoDAO = new ProdutoDAO();
        ObservableList<Produto> todosProdutos = FXCollections.observableArrayList(produtoDAO.getAll());
    }

    public void initialize(URL url, ResourceBundle rb) {

        this.profilePaneName.setText("admin");
        this.profilePaneType.setText("Administração");
        //this.profilePaneName.setText(FuncionarioHolder.getUser().getName());
        //this.profilePaneType.setText(FuncionarioHolder.getUser().getOccupation());

        date.setText(LocalDate.now(
                ZoneId.of("America/Sao_Paulo")
        ).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

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
