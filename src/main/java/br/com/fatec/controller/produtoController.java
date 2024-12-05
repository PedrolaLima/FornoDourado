package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.Messenger;
import br.com.fatec.dao.ProdutoDAO;
import br.com.fatec.data.FuncionarioHolder;
import br.com.fatec.data.ProdutoHolder;
import br.com.fatec.model.Produto;
import javafx.fxml.FXML;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class produtoController implements Initializable {

    @FXML
    public Label date;

    @FXML
    private Label profilePaneName;

    @FXML
    private Label profilePaneType;

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
    private ImageView filter;

    @FXML
    private TextField searchBar;

    @FXML
    private TableView<Produto> productsTable;

    @FXML
    private TableColumn<Produto, String> colProduto;

    @FXML
    private TableColumn<Produto, String> colCodigo;

    @FXML
    private TableColumn<Produto, String> colCategoria;

    @FXML
    private TableColumn<Produto, String> colPreco;

    @FXML
    private TableColumn<Produto, String> colDisp;

    private void configurarCliqueNaColunaProduto() {
        colProduto.setCellFactory(tc -> {
            TableCell<Produto, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item);
                    }
                }
            };

            // Adiciona suporte ao duplo clique
            cell.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !cell.isEmpty()) { // Verifica duplo clique e célula válida
                    Produto produto = productsTable.getItems().get(cell.getIndex());
                    visualizarProduto(produto);
                }
            });

            return cell;
        });
    }

    private void visualizarProduto(Produto produto) {
        System.out.println("Visualizando: " + produto.getNome());
        ProdutoHolder.setP(produto);

        try {
            App.setRoot("visualizarProduto");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.profilePaneName.setText(FuncionarioHolder.getUser().getName());
        this.profilePaneType.setText(FuncionarioHolder.getUser().getOccupation());

        date.setText(LocalDate.now(
                ZoneId.of("America/Sao_Paulo")
        ).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Inicializa o TableView
        configurarTabela();

        // Lógica do ContextMenu para o perfil
        configurarProfileMenu();

        // Lógica do filtro de funcionários
        configurarFilterMenu();

        // Configurar a barra de pesquisa
        configurarBarraDePesquisa();

        configurarCliqueNaColunaProduto();
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
            productsTable.setItems(todosProdutos); // Mostra todos os funcionários se não houver consulta
            return;
        }

        String filtro = query.toLowerCase();
        ObservableList<Produto> filtrados = todosProdutos.filtered(produto
                -> produto.getNome().toLowerCase().contains(filtro)
                || String.valueOf(produto.getCod()).contains(filtro)
                || // Converte código para String
                produto.getCat().toLowerCase().contains(filtro)
                || String.valueOf(produto.getPreco()).contains(filtro)
                || // Converte preço para String
                (produto.isDisp() ? "Ativo" : "Desativado").toLowerCase().contains(filtro) // Garante que "Ativo" ou "Desativado" estão em minúsculas
        );

        productsTable.setItems(filtrados); // Atualiza a tabela com os resultados filtrados
    }

    private void editarProduto(Produto produto) {
        System.out.println("Editando: " + produto.getNome());
        ProdutoHolder.setP(produto);

        try {
            App.setRoot("adicionarProduto");

            adicionarProdutoController controller = App.getController("adicionarProduto");
            if (controller != null) {
                controller.botaoEditar();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deletarProduto(Produto produto) {
        // Exibe uma caixa de diálogo de confirmação
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Exclusão");
        alert.setHeaderText("Você tem certeza que deseja excluir o produto?");
        alert.setContentText("Nome: " + produto.getNome());

        // Captura a resposta do usuário
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    ProdutoDAO f = new ProdutoDAO();
                    f.delete(String.valueOf(produto.getCod()));
                    Messenger.info("Successo", "O produto " + produto.getNome() + " foi excluido");

                    // Remove o funcionário do ObservableList
                    ObservableList<Produto> produtos = productsTable.getItems();
                    produtos.remove(produto);

                    // Atualiza o TableView
                    productsTable.setItems(produtos);

                } catch (SQLException e) {
                    Messenger.error("Erro no banco", "O funcionario não foi deletado");
                }
            }
        });
    }

    private void configurarTabela() {
        // Configuração das colunas
        colProduto.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("cod"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("cat"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colDisp.setCellValueFactory(new PropertyValueFactory<>("disp"));

        // Configurando o status com base no valor booleano
        colDisp.setCellValueFactory(cellData -> {
            Produto produto = cellData.getValue();
            String status = produto.isDisp() ? "Ativo" : "Desativado"; // Lógica condicional
            return new SimpleStringProperty(status); // Retorna o valor como uma propriedade observável
        });

        // Adicionando dados ao TableView
        ProdutoDAO produtoDAO = new ProdutoDAO();
        ObservableList<Produto> produtos = FXCollections.observableArrayList(produtoDAO.getAll());
        productsTable.setItems(produtos); // Adicionando funcionários à tabela

        // Configurar a coluna de Ações com largura fixa e ícones
        TableColumn<Produto, Void> actionColumn = new TableColumn<>("Ações");
        actionColumn.setPrefWidth(80); // Definir a largura da coluna

        // A célula da coluna de ações agora terá os botões com ícones
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button();
            private final Button deleteButton = new Button();

            {
                // Configurando os ícones para os botões de editar e excluir
                ImageView editIcon = new ImageView("br/com/fatec/Imagens/icons/pedidos/edit.png");
                editIcon.setFitWidth(16);
                editIcon.setFitHeight(16);
                editButton.setGraphic(editIcon);

                ImageView deleteIcon = new ImageView("br/com/fatec/Imagens/icons/pedidos/trash.png");
                deleteIcon.setFitWidth(16);
                deleteIcon.setFitHeight(16);
                deleteButton.setGraphic(deleteIcon);

                // Ação de editar
                editButton.setOnAction(event -> {
                    Produto produto = getTableView().getItems().get(getIndex());
                    editarProduto(produto);

                });

                // Ação de deletar
                deleteButton.setOnAction(event -> {
                    Produto produto = getTableView().getItems().get(getIndex());
                    deletarProduto(produto);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    // Alinhando os botões no painel
                    AnchorPane container = new AnchorPane(editButton, deleteButton);
                    AnchorPane.setLeftAnchor(editButton, 5.0);
                    AnchorPane.setLeftAnchor(deleteButton, 35.0);
                    setGraphic(container);
                }
            }
        });

        productsTable.getColumns().add(actionColumn); // Adiciona apenas uma vez a coluna de Ações
    }

    private void configurarProfileMenu() {
        // Criação do menu de contexto
        ContextMenu contextMenu = new ContextMenu();

        MenuItem menuItem1 = new MenuItem("Meu Perfil");
        MenuItem menuItem2 = new MenuItem("Sair");

        menuItem1.setOnAction(event -> {
            try {
                App.setRoot("visualizarFuncionario");
            } catch (IOException ex) {
                Logger.getLogger(funcionarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        menuItem2.setOnAction(event -> {
            try {
                App.setRoot("menu");
            } catch (IOException ex) {
                Logger.getLogger(funcionarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

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

        // Configuração de bordas arredondadas no profile
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

    private void configurarFilterMenu() {
        // Criar o ContextMenu principal para o "filter"
        ContextMenu filterMenu = new ContextMenu();

        // Criar os itens principais do menu
        MenuItem categoriaMenuItem = new MenuItem("Categoria");
        MenuItem statusMenuItem = new MenuItem("Disponibilidade");
        MenuItem semFiltro = new MenuItem("Sem filtro");

        // Submenu para "Categoria"
        ContextMenu categoriaSubMenu = new ContextMenu();
        MenuItem breadItem = new MenuItem("Pães");
        MenuItem dessertItem = new MenuItem("Doces");
        MenuItem saltyFoodItem = new MenuItem("Salgados");
        categoriaSubMenu.getItems().addAll(breadItem, dessertItem, saltyFoodItem);

        // Submenu para "Disponibilidade"
        ContextMenu statusSubMenu = new ContextMenu();
        MenuItem ativoItem = new MenuItem("Ativo");
        MenuItem desativadoItem = new MenuItem("Desativado");
        statusSubMenu.getItems().addAll(ativoItem, desativadoItem);

        // Configurar eventos para abrir os submenus
        categoriaMenuItem.setOnAction(event -> {
            double xPos = filter.localToScreen(filter.getLayoutX(), filter.getLayoutY()).getX() - 70;
            double yPos = filter.localToScreen(filter.getLayoutX(), filter.getLayoutY()).getY() - 92;
            categoriaSubMenu.show(filter, xPos, yPos);
        });

        statusMenuItem.setOnAction(event -> {
            double xPos = filter.localToScreen(filter.getLayoutX(), filter.getLayoutY()).getX() - 70;
            double yPos = filter.localToScreen(filter.getLayoutX(), filter.getLayoutY()).getY() - 92;
            statusSubMenu.show(filter, xPos, yPos);
        });

        // Remover filtro
        semFiltro.setOnAction(event -> carregarProdutosSemFiltro());

        // Eventos para filtrar por Categoria
        breadItem.setOnAction(event -> aplicarFiltroPorCategoria("Pães"));
        dessertItem.setOnAction(event -> aplicarFiltroPorCategoria("Doces"));
        saltyFoodItem.setOnAction(event -> aplicarFiltroPorCategoria("Salgados"));

        // Eventos para filtrar por Disponibilidade
        ativoItem.setOnAction(event -> aplicarFiltroPorDisponibilidade("Ativo"));
        desativadoItem.setOnAction(event -> aplicarFiltroPorDisponibilidade("Desativado"));

        // Mostrar o menu principal ao clicar
        filter.setOnMouseClicked(event -> {
            double xPos = filter.localToScreen(filter.getLayoutX(), filter.getLayoutY()).getX() - 125;
            double yPos = filter.localToScreen(filter.getLayoutX(), filter.getLayoutY()).getY() - 86;
            filterMenu.show(filter, xPos, yPos);
            categoriaSubMenu.hide(); // Fecha o submenu de categoria
            statusSubMenu.hide();
        });

        // Adicionar os itens principais ao menu
        filterMenu.getItems().addAll(categoriaMenuItem, statusMenuItem, semFiltro);
    }

// Método para aplicar o filtro por Categoria
    private void aplicarFiltroPorCategoria(String categoria) {
        ObservableList<Produto> todosProdutos = FXCollections.observableArrayList(new ProdutoDAO().getAll());
        ObservableList<Produto> filtrados = todosProdutos.filtered(produto -> categoria.equals(produto.getCat()));
        productsTable.setItems(filtrados);
    }

// Método para aplicar o filtro por Disponibilidade
    private void aplicarFiltroPorDisponibilidade(String disponibilidade) {
        ObservableList<Produto> todosProdutos = FXCollections.observableArrayList(new ProdutoDAO().getAll());
        ObservableList<Produto> filtrados = todosProdutos.filtered(produto -> {
            String produtoStatus = produto.isDisp() ? "Ativo" : "Desativado";
            return disponibilidade.equals(produtoStatus);
        });
        productsTable.setItems(filtrados);
    }

// Método para carregar todos os produtos sem filtro
    private void carregarProdutosSemFiltro() {
        ObservableList<Produto> todosProdutos = FXCollections.observableArrayList(new ProdutoDAO().getAll());
        productsTable.setItems(todosProdutos);
    }
}