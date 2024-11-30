package br.com.fatec.controller;

import br.com.fatec.App;
import javafx.fxml.FXML;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class pedidoController implements Initializable {

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

    
    // Método para buscar as categorias no banco de dados e adicionar na ComboBox
    private void carregarCategorias() {
        /**ObservableList<String> categorias = FXCollections.observableArrayList();  // Lista observável para as categorias

        // Conectando ao banco de dados
        try (Connection conn = FuncionarioDAO.getConnection()) {
            String sql = "SELECT DISTINCT categoria FROM produtos";  // Query SQL para pegar as categorias únicas

            // Executando a consulta
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                
                // Adicionando as categorias na lista
                while (rs.next()) {
                    categorias.add(rs.getString("categoria"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Adicionando as categorias na ComboBox
        productCategory.setItems(categorias);
    */}
    
    public void initialize(URL url, ResourceBundle rb) {
        
        carregarCategorias();

        
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
