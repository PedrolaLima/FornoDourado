package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.data.ProdutoHolder;
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
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class visualizarProdutoController implements Initializable {

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
    private void carregarFuncionarios() throws IOException {
        App.setRoot("funcionarios");
    }

    @FXML
    private void carregarRelatorios() throws IOException {
        App.setRoot("relatorio");
    }

    @FXML
    private void carregarAddProduct() throws IOException {
        App.setRoot("adicionarProduto");
    }

    @FXML
    private void cancelarProduto() throws IOException {
        App.setRoot("produto");
    }

    @FXML
    private AnchorPane profilePane;

    @FXML
    private AnchorPane profileBack;

    @FXML
    private ImageView profile;

    @FXML
    private ImageView productview;

    @FXML
    private Label produtoLabel;

    @FXML
    private Label precoLabel;

    @FXML
    private Label codigoLabel;

    @FXML
    private Label categoriaLabel;

    @FXML
    private Label statusLabel;
    
    @FXML
    private Label date;

    // Configura a imagem do produto com bordas arredondadas e troca de imagem
    private void configureProductImage(String categoriaLabel) {
        // Determine o caminho da imagem com base no valor da labelCategoria
        String imagePath = "";

        switch (categoriaLabel) {
            case "Pães":
                imagePath = "/br/com/fatec/Imagens/cadastros/Produtos/pao-brioche.png"; // Caminho correto para a imagem de pães
                break;
            case "Doces":
                imagePath = "/br/com/fatec/Imagens/cadastros/Produtos/donuts.png"; // Caminho correto para a imagem de doces
                break;
            case "Salgados":
                imagePath = "/br/com/fatec/Imagens/cadastros/Produtos/croissant.png"; // Caminho correto para a imagem de salgados
                break;
            default:
                break;
        }

        // Crie a nova imagem com o caminho determinado
        if (!imagePath.isEmpty()) {
            Image newImage = new Image(getClass().getResource(imagePath).toExternalForm());

            // Defina a nova imagem no ImageView
            productview.setImage(newImage);

            // Configura bordas arredondadas para a imagem
            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            WritableImage image = productview.snapshot(parameters, null);

            // Configura o recorte arredondado da imagem
            Rectangle clip = new Rectangle(productview.getFitWidth(), productview.getFitHeight());
            clip.setArcWidth(25);
            clip.setArcHeight(25);
            productview.setClip(clip);

            // Atualiza a imagem com o recorte
            productview.setImage(image);
        }
    }

    public void initialize(URL url, ResourceBundle rb) {
        // Exibe a data atual
        date.setText(LocalDate.now(ZoneId.of("America/Sao_Paulo")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        
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

        // Verifica se o ProdutoHolder contém um produto e preenche os campos, se necessário
        if (!ProdutoHolder.isEmpty()) {
            Produto p = ProdutoHolder.getP();

            // Preenche os campos com os dados do produto
            produtoLabel.setText(p.getNome());
            codigoLabel.setText(String.valueOf(p.getCod()));
            precoLabel.setText("R$ " + String.format("%.2f", p.getPreco()).replace('.', ','));

            statusLabel.setText(p.isDisp() ? "Ativo" : "Desativado");

            categoriaLabel.setText(p.getCat());

            // Limpa o holder após a carga dos dados
            ProdutoHolder.clear();
        }
        configureProductImage(categoriaLabel.getText());
    }
}
