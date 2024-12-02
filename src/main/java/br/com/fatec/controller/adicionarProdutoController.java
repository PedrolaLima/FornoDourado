package br.com.fatec.controller;

import br.com.fatec.App;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.fatec.Messenger;
import br.com.fatec.dao.FuncionarioDAO;
import br.com.fatec.dao.ProdutoDAO;
import br.com.fatec.model.Produto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

public class adicionarProdutoController implements Initializable {


    @FXML
    private AnchorPane profilePane;

    @FXML
    private AnchorPane profileBack;

    @FXML
    private ImageView profile;

    @FXML
    private ImageView product;

    @FXML
    private TextField valorProduto;

    @FXML
    public TextField nomeProduto;

    @FXML
    public TextField codigoProduto;

    @FXML
    public ComboBox<String> dispoProduto;

    @FXML
    public ComboBox<String> categoriaProduto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configureContextMenu();
        configureProfileImage();
        configureProductImage();
        configureValorProdutoMask();

        dispoProduto.setItems(FXCollections.observableArrayList("Disponível","Não disponível"));
        categoriaProduto.setItems(FXCollections.observableArrayList("Pães","Doces","Salgados"));
    }

    private boolean verifyForm(){
        ArrayList<String> msg = new ArrayList<>();
        if(nomeProduto.getText().isEmpty()){
            msg.add("Nome  ");
        }
        if (codigoProduto.getText().isEmpty()){
            msg.add("Código do produto  ");
        }
        if (valorProduto.getText().isEmpty()){
            msg.add("Valor do produto  ");
        }
        if(dispoProduto.getValue() == null){
            msg.add("Disponibilidade  ");
        }
        if (categoriaProduto.getValue() == null){
            msg.add("Categoria do produto");
        }

        if(!msg.isEmpty()){
            Messenger.error("Campos obrigatorios não preenchidos",new String[]{String.join(",",msg)});
            return false;
        }
        return true;
    }

    @FXML
    private void addProduto(){
        if(verifyForm()){
            System.out.println(valorProduto.getText());
            Produto p = new Produto(nomeProduto.getText(),
                    Integer.parseInt(codigoProduto.getText()),
                    Float.parseFloat(valorProduto.getText().replaceAll(",",".").replaceAll("R\\$ ","")),
                    dispoProduto.getValue().equals("Disponível"));
            try {
                ProdutoDAO po =new ProdutoDAO();
                po.insert(p);
                Messenger.info("Concluido","Produto inserido no banco");
            }catch (SQLException e){
                Messenger.error("Erro de banco",e.getMessage());
            }
        }
    }

    // Configura o menu de contexto
    private void configureContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem menuItem1 = new MenuItem("Meu Perfil");
        MenuItem menuItem2 = new MenuItem("Sair");

        menuItem1.setOnAction(event -> loadScene("visualizarFuncionario"));
        menuItem2.setOnAction(event -> loadScene("menu"));

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
    }

    // Configura a imagem do perfil com bordas arredondadas
    private void configureProfileImage() {
        Rectangle clip = new Rectangle(profile.getFitWidth(), profile.getFitHeight());
        clip.setArcWidth(25);
        clip.setArcHeight(25);
        profile.setClip(clip);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = profile.snapshot(parameters, null);

        profile.setClip(null);
        profile.setImage(image);
    }

    // Configura a imagem do produto com bordas arredondadas e troca de imagem
    private void configureProductImage() {
        product.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagens", "*.jpg", "*.jpeg", "*.png"));
            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                Image newImage = new Image(selectedFile.toURI().toString());
                product.setImage(newImage);

                SnapshotParameters parameters = new SnapshotParameters();
                parameters.setFill(Color.TRANSPARENT);
                WritableImage image = product.snapshot(parameters, null);

                Rectangle clip = new Rectangle(product.getFitWidth(), product.getFitHeight());
                clip.setArcWidth(25);
                clip.setArcHeight(25);
                product.setClip(clip);

                product.setImage(image);
            }
        });
    }

    private void configureValorProdutoMask() {
    // Máscara para o campo "valorProduto"
    UnaryOperator<TextFormatter.Change> filter = change -> {
        String oldText = change.getControlText(); // Texto atual do campo
        String newText = change.getControlNewText(); // Novo texto sugerido

        // Bloqueia Backspace se o texto for exatamente "R$ " ou "R$" (sem o espaço) ou estiver vazio
        if (change.isDeleted() && (oldText.equals("R$ ") || oldText.equals("R$") || oldText.isEmpty())) {
            return null; // Bloqueia o Backspace
        }

        // Garante que o texto sempre comece com "R$ "
        if (!newText.startsWith("R$ ")) {
            // Previne a alteração do prefixo "R$ "
            if (!oldText.startsWith("R$ ")) {
                change.setText("R$ " + newText.replaceAll("[^\\d,]", ""));
                change.setCaretPosition(change.getText().length());
                change.setAnchor(change.getText().length());
                return change;
            }
        }

        // Remove o prefixo "R$ " temporariamente para facilitar a validação
        String valueWithoutPrefix = newText.substring(3);

        // Verifica se o novo texto está no formato válido (números + opcionalmente uma vírgula + dois números)
        if (!valueWithoutPrefix.matches("\\d*(,\\d{0,2})?")) {
            return null; // Bloqueia mudanças inválidas
        }

        return change; // Aceita mudanças válidas
    };

    // Aplica a máscara ao campo de texto
    TextFormatter<String> formatter = new TextFormatter<>(filter);
    valorProduto.setTextFormatter(formatter);

    // Define o texto inicial do campo
    valorProduto.setText("R$ ");
}


        
        // Método auxiliar para carregar cenas
        private void loadScene(String sceneName) {
            try {
                App.setRoot(sceneName);
            } catch (IOException ex) {
                Logger.getLogger(adicionarProdutoController.class.getName()).log(Level.SEVERE, null, ex);
            }
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
}
