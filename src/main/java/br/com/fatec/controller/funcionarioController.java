package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.Messenger;
import br.com.fatec.dao.FuncionarioDAO;
import br.com.fatec.data.FuncionarioHolder;
import br.com.fatec.model.Funcionario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.cell.PropertyValueFactory;

public class funcionarioController implements Initializable {

    @FXML
    public Label date;
    @FXML
    private TableView<Funcionario> workersTable;

    @FXML
    private TableColumn<Funcionario, String> colNome;

    @FXML
    private TableColumn<Funcionario, String> colCpf;

    @FXML
    private TableColumn<Funcionario, String> colEmail;

    @FXML
    private TableColumn<Funcionario, String> colCargo;

    @FXML
    private TableColumn<Funcionario, String> colStatus;

    @FXML
    private AnchorPane profilePane;

    @FXML
    private AnchorPane profileBack;

    @FXML
    private ImageView profile;

    @FXML
    private ImageView filter;

    // Método para carregar a tela de "Adicionar Produto"
    @FXML
    private void carregarDashboard() throws IOException {
        App.setRoot("dashboard");
    }

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        date.setText(LocalDate.now(
                ZoneId.of( "America/Sao_Paulo" )
        ).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Inicializa o TableView
        configurarTabela();

        // Lógica do ContextMenu para o perfil
        configurarProfileMenu();

        // Lógica do filtro de funcionários
        configurarFilterMenu();
    }


    private void editarFuncionario(Funcionario funcionario) {
        System.out.println("Editando: " + funcionario.getName());
        FuncionarioHolder.setF(funcionario);

        try {
            App.setRoot("adicionarFuncionario");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Implementar lógica de edição
    }

    private void deletarFuncionario(Funcionario funcionario) {
        // Exibe uma caixa de diálogo de confirmação
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Exclusão");
        alert.setHeaderText("Você tem certeza que deseja excluir o funcionário?");
        alert.setContentText("Nome: " + funcionario.getName());

        // Captura a resposta do usuário
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    FuncionarioDAO f =new FuncionarioDAO();
                    f.delete(funcionario.getCpf());
                    Messenger.info("Successo","O funcionario"+funcionario.getName()+" foi excluido");

                    // Remove o funcionário do ObservableList
                    ObservableList<Funcionario> funcionarios = workersTable.getItems();
                    funcionarios.remove(funcionario);

                    // Atualiza o TableView
                    workersTable.setItems(funcionarios);

                }catch (SQLException e){
                    Messenger.error("Erro no banco","O funcionario não foi deletado");
                }
            }
        });
    }


    private void configurarTabela() {
        // Configuração das colunas
        colNome.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCargo.setCellValueFactory(new PropertyValueFactory<>("occupation"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        
        // Adicionando dados ao TableView
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        ObservableList<Funcionario> funcionarios = FXCollections.observableArrayList(funcionarioDAO.getTable());
        workersTable.setItems(funcionarios); // Adicionando funcionários à tabela

        
        
        // Configurar a coluna de Ações com largura fixa e ícones
        TableColumn<Funcionario, Void> actionColumn = new TableColumn<>("Ações");
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
                    Funcionario funcionario = getTableView().getItems().get(getIndex());
                    editarFuncionario(funcionario);
                });

                // Ação de deletar
                deleteButton.setOnAction(event -> {
                    Funcionario funcionario = getTableView().getItems().get(getIndex());
                    deletarFuncionario(funcionario);
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

        workersTable.getColumns().add(actionColumn); // Adiciona apenas uma vez a coluna de Ações
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
    }

    private void configurarFilterMenu() {
        ContextMenu filterMenu = new ContextMenu();

        MenuItem cargoMenuItem = new MenuItem("Cargo");
        MenuItem statusMenuItem = new MenuItem("Status");
        MenuItem semFiltro = new MenuItem("Sem filtro");

        filterMenu.getItems().addAll(cargoMenuItem, statusMenuItem, semFiltro);

        filter.setOnMouseClicked(event -> {
            double xPos = filter.localToScreen(filter.getLayoutX(), filter.getLayoutY()).getX() - 125;
            double yPos = filter.localToScreen(filter.getLayoutX(), filter.getLayoutY()).getY() - 86;
            filterMenu.show(filter, xPos, yPos);
        });
        
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
    }

    /*public static class Funcionario {
        private String nome;
        private String cpf;
        private String usuario;
        private String cargo;
        private String status;

        public Funcionario(String nome, String cpf, String usuario, String cargo, String status) {
            this.nome = nome;
            this.cpf = cpf;
            this.usuario = usuario;
            this.cargo = cargo;
            this.status = status;
        }

        public String getNome() {
            return nome;
        }

        public String getCpf() {
            return cpf;
        }

        public String getUsuario() {
            return usuario;
        }

        public String getCargo() {
            return cargo;
        }

        public String getStatus() {
            return status;
        }
    }*/
}
