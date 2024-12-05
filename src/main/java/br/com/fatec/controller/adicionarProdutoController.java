package br.com.fatec.controller;

import br.com.fatec.App;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.fatec.Messenger;
import br.com.fatec.dao.FuncionarioDAO;
import br.com.fatec.dao.ProdutoDAO;
import br.com.fatec.data.Database;
import br.com.fatec.data.FuncionarioHolder;
import br.com.fatec.data.ProdutoHolder;
import br.com.fatec.model.Produto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    public CheckBox dispCheck;

    @FXML
    public ToggleGroup categoria;

    @FXML
    public Label date;

    @FXML
    private Label profilePaneName;

    @FXML
    private Label profilePaneType;

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
    private ImageView btn_confirm;

    // Método para desabilitar o campo CPF
    public void botaoEditar() {
        // Caminho da nova imagem
        String imagePath = getClass().getResource("/br/com/fatec/Imagens/icons/funcionarios/atualizar.png").toExternalForm();

        // Defina a nova imagem
        Image newImage = new Image(imagePath);
        btn_confirm.setImage(newImage);
        btn_confirm.getStyleClass().add("botao-atualizar");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.profilePaneName.setText(FuncionarioHolder.getUser().getName());
        this.profilePaneType.setText(FuncionarioHolder.getUser().getOccupation());

        // Exibe a data atual
        date.setText(LocalDate.now(ZoneId.of("America/Sao_Paulo")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Configurações adicionais de interface
        configureContextMenu();
        configureProfileImage();
        configureProductImage();

        // Lógica para determinar o próximo código do produto
        try {
            Database.connect();

            // Consulta para contar quantos produtos já existem
            String sql = "SELECT COUNT(*) FROM produtos";
            PreparedStatement ps = Database.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            int nextCodigoProduto = 1; // Padrão se não houver produtos na tabela
            if (rs.next()) {
                int count = rs.getInt(1); // Retorna o número de produtos na tabela
                nextCodigoProduto = count + 1; // Próximo código será o número de produtos + 1
            }

            // Atribui o próximo código ao campo 'codigoProduto'
            codigoProduto.setText(String.valueOf(nextCodigoProduto));

            // Verifica se o ProdutoHolder contém um produto e preenche os campos, se necessário
            if (!ProdutoHolder.isEmpty()) {
                Produto p = ProdutoHolder.getP();

                // Preenche os campos com os dados do produto
                nomeProduto.setText(p.getNome());
                codigoProduto.setText(String.valueOf(p.getCod()));
                valorProduto.setText("R$ " + String.format("%.2f", p.getPreco()).replace('.', ','));

                dispCheck.setSelected(p.isDisp());

                // Seleciona o RadioButton correspondente à categoria
                categoria.getToggles().stream()
                        .filter(toggle -> toggle instanceof RadioButton) // Garante que seja um RadioButton
                        .map(toggle -> (RadioButton) toggle) // Faz o cast
                        .filter(radioButton -> radioButton.getText().equals(p.getCat())) // Verifica o texto
                        .findFirst()
                        .ifPresent(radioButton -> categoria.selectToggle(radioButton)); // Seleciona o botão

                // Limpa o holder após a carga dos dados
                ProdutoHolder.clear();
            }

            Database.close(); // Fecha a conexão com o banco de dados

        } catch (SQLException e) {
            Messenger.error("Erro ao acessar o banco de dados", e.getMessage());
        }

        configureValorProdutoMask();

    }

    private boolean verifyForm() {
        ArrayList<String> msg = new ArrayList<>();
        if (nomeProduto.getText().isEmpty()) {
            msg.add("Nome  ");
        }
        if (codigoProduto.getText().isEmpty()) {
            msg.add("Código do produto  ");
        }
        if (valorProduto.getText().replaceAll("R\\$ ", "").isEmpty()) {
            msg.add("Valor do produto  ");
        }
        if (categoria.getSelectedToggle() == null) {
            msg.add("Categoria do produto");
        }

        if (!msg.isEmpty()) {
            Messenger.error("Campos obrigatorios não preenchidos", new String[]{String.join(",", msg)});
            return false;
        }
        return true;
    }

    @FXML
    private void addProduto() {
        if (verifyForm()) {
            System.out.println(valorProduto.getText());

            RadioButton selectedRadioButton = (RadioButton) categoria.getSelectedToggle();
            String categoriaSelecionada = selectedRadioButton.getText();
            System.out.println(categoriaSelecionada);

            try {
                ProdutoDAO produtoDAO = new ProdutoDAO();
                Database.connect();
                PreparedStatement ps = Database.getConnection().prepareStatement(
                        "SELECT * FROM produtos WHERE codprod = ?;"
                );
                ps.setInt(1, Integer.parseInt(codigoProduto.getText()));
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    // Se o produto existe no banco, perguntar ao usuário se deseja atualizar
                    boolean userWantsToUpdate = Messenger.confirm(
                            "Produto já cadastrado",
                            "Deseja atualizar os dados do produto existente?"
                    );

                    if (userWantsToUpdate) {
                        // Cria um novo objeto Produto com os dados atualizados
                        Produto updatedProduto = new Produto(
                                nomeProduto.getText(),
                                Integer.parseInt(codigoProduto.getText()),
                                Float.parseFloat(valorProduto.getText().replaceAll(",", ".").replaceAll("R\\$ ", "")),
                                categoriaSelecionada,
                                dispCheck.isSelected()
                        );

                        // Atualiza no banco de dados
                        produtoDAO.update(updatedProduto, codigoProduto.getText());
                        Messenger.info("Concluído", "Dados do produto atualizados com sucesso!");
                        App.setRoot("produto");
                    }
                } else {
                    // Se o produto não existe, insere um novo registro
                    Produto newProduto = new Produto(
                            nomeProduto.getText(),
                            Integer.parseInt(codigoProduto.getText()),
                            Float.parseFloat(valorProduto.getText().replaceAll(",", ".").replaceAll("R\\$ ", "")),
                            categoriaSelecionada,
                            dispCheck.isSelected()
                    );

                    produtoDAO.insert(newProduto);
                    Messenger.info("Concluído", "Produto inserido com sucesso!");
                }

                Database.close();
            } catch (SQLException e) {
                Messenger.error("Erro no banco de dados", e.getMessage());
            } catch (IOException e) {
                Messenger.error("Erro ao carregar tela", e.getMessage());
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
        // Adiciona um listener para mudanças no grupo de radio buttons 'categoria'
        categoria.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            // Verifica se um radio button foi selecionado
            RadioButton selectedRadioButton = (RadioButton) newValue;
            String categoriaSelecionada = selectedRadioButton != null ? selectedRadioButton.getText() : "";

            // Defina o caminho da imagem com base na categoria selecionada
            String imagePath = "";

            // Determine o caminho da imagem com base na categoria
            switch (categoriaSelecionada) {
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
                product.setImage(newImage);

                // Configura bordas arredondadas para a imagem
                SnapshotParameters parameters = new SnapshotParameters();
                parameters.setFill(Color.TRANSPARENT);
                WritableImage image = product.snapshot(parameters, null);

                // Configura o recorte arredondado da imagem
                Rectangle clip = new Rectangle(product.getFitWidth(), product.getFitHeight());
                clip.setArcWidth(25);
                clip.setArcHeight(25);
                product.setClip(clip);

                // Atualiza a imagem com o recorte
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
        if (valorProduto.getText() == null || valorProduto.getText().isEmpty()) {
            valorProduto.setText("R$ ");
        }
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
