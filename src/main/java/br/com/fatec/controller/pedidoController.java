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

public class pedidoController implements Initializable {

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
    private TableColumn<Produto, String> colCodigo;

    @FXML
    private TableColumn<Produto, String> colCategoria;

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
    private void carregarFimPedido() throws IOException {
        PedidoHolder.setPedido(p);
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

    @FXML
    private ImageView btn_confirmar;

    @FXML
    private ImageView btn_cancelar;
    
    //Pedido a ser feito
    private Pedido p=new Pedido();

    private void configurarTabela() {
        // Configuração das colunas
        colProduto.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("cod"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("cat"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));

        // Adicionando dados ao TableView
        ProdutoDAO produtoDAO = new ProdutoDAO();
        ObservableList<Produto> todosProdutos = FXCollections.observableArrayList(produtoDAO.getAll());

        // Filtrar apenas os produtos disponíveis
        ObservableList<Produto> produtosDisponiveis = todosProdutos.filtered(Produto::isDisp);

        ordersTable.setItems(produtosDisponiveis); // Adicionando produtos disponíveis à tabela

        // Variável global para armazenar a soma das quantidades
        final int[] totalQuantidades = {0}; // Usando um array para modificar o valor dentro das funções anônimas

        // Configurar a coluna de Ações com largura fixa e ícones
        TableColumn<Produto, Void> actionColumn = new TableColumn<>("Ações");
        actionColumn.setPrefWidth(120);
        
        // A célula da coluna de ações agora terá os botões com ícones e o número entre eles
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button menosButton = new Button();
            private final Button maisButton = new Button();
            private final Label quantidadeLabel = new Label("0");
            private int quantidade = 0;

            {
                // Configurando os ícones para os botões de menos e mais
                ImageView menosIcon = new ImageView("br/com/fatec/Imagens/icons/pedidos/menos.png");
                menosIcon.setFitWidth(16);
                menosIcon.setFitHeight(16);
                menosButton.setGraphic(menosIcon);

                ImageView maisIcon = new ImageView("br/com/fatec/Imagens/icons/pedidos/mais.png");
                maisIcon.setFitWidth(16);
                maisIcon.setFitHeight(16);
                maisButton.setGraphic(maisIcon);

                quantidadeLabel.setStyle("-fx-font-weight: bold;");

                // Ação do botão de "menos"
                menosButton.setOnAction(event -> {
                    if (quantidade > 0) {
                        quantidade--;
                        //p.setItem(item, quantidade); <-- Pegar o produto da linha
                        quantidadeLabel.setText(String.valueOf(quantidade));
                        totalQuantidades[0]--; // Decrementa a quantidade total
                        verificarBotaoConfirmacao(totalQuantidades); // Verifica o estado do botão de confirmação
                    }
                });

                // Ação do botão de "mais"
                maisButton.setOnAction(event -> {
                    quantidade++;
                    //p.setItem(item, quantidade); <-- Pegar o produto da linha
                    quantidadeLabel.setText(String.valueOf(quantidade));
                    totalQuantidades[0]++; // Incrementa a quantidade total
                    verificarBotaoConfirmacao(totalQuantidades); // Verifica o estado do botão de confirmação
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    AnchorPane container = new AnchorPane(menosButton, quantidadeLabel, maisButton);
                    AnchorPane.setLeftAnchor(menosButton, 5.0);
                    AnchorPane.setLeftAnchor(quantidadeLabel, 55.0);
                    AnchorPane.setLeftAnchor(maisButton, 75.0);
                    setGraphic(container);
                }
            }
        });

        ordersTable.getColumns().add(actionColumn);
    }

    @FXML
    private void onCancelarClicked() throws IOException {
        App.setRoot("dashboard");
        App.setRoot("pedido");
    }

    private void verificarBotaoConfirmacao(int[] totalQuantidades) {
        // Verifica se a soma das quantidades é 0
        if (totalQuantidades[0] == 0) {
            btn_confirmar.setDisable(true); // Desabilita o botão de confirmação
            btn_confirmar.setOpacity(0.5);
        } else {
            btn_confirmar.setDisable(false); // Habilita o botão de confirmação
            btn_confirmar.setOpacity(1);
        }
    }

    private void configurarBarraDePesquisa() {
        // Configurar evento de digitação na barra de pesquisa
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarProdutos(newValue);
        });
    }

    private void filtrarProdutos(String query) {
        ObservableList<Produto> todosProdutos = FXCollections.observableArrayList(new ProdutoDAO().getAll());

        if (query == null || query.isBlank()) {
            ordersTable.setItems(todosProdutos); // Mostra todos os produtos se não houver consulta
            return;
        }

        String filtro = query.toLowerCase();
        ObservableList<Produto> filtrados = todosProdutos.filtered(produto
                -> produto.getNome().toLowerCase().contains(filtro)
                || String.valueOf(produto.getCod()).contains(filtro)
                || // Converte código para String
                produto.getCat().toLowerCase().contains(filtro)
                || String.valueOf(produto.getPreco()).contains(filtro));

        ordersTable.setItems(filtrados); // Atualiza a tabela com os resultados filtrados
    }

    private void filtrarPorCategoria(String categoria) {
        ProdutoDAO produtoDAO = new ProdutoDAO();
        ObservableList<Produto> todosProdutos = FXCollections.observableArrayList(produtoDAO.getAll());

        if (categoria == null || categoria.isBlank() || categoria.equals("Todos")) {
            ordersTable.setItems(todosProdutos); // Mostra todos os produtos se a categoria for "Todos" ou estiver vazia
            return;
        }

        // Filtra os produtos pela categoria
        ObservableList<Produto> filtrados = todosProdutos.filtered(produto
                -> categoria.equalsIgnoreCase(produto.getCat())
        );

        ordersTable.setItems(filtrados); // Atualiza a tabela com os produtos filtrados
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

        configurarTabela();

        // Populando a ComboBox com categorias de produtos
        ObservableList<String> categorias = FXCollections.observableArrayList("Todos", "Pães", "Doces", "Salgados");
        productCategory.setItems(categorias); // Adiciona os itens à ComboBox
        productCategory.setValue("Todos"); // Define "Todos" como valor inicial

        //Listener para ComboBox de Categoria
        productCategory.valueProperty().addListener((observable, oldValue, newValue) -> {
            filtrarPorCategoria(newValue); // Chama o método de filtragem quando o valor muda
        });

        configurarBarraDePesquisa();
    }
}
