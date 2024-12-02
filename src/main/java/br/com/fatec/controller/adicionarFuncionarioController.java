package br.com.fatec.controller;

import br.com.fatec.App;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.fatec.Messenger;
import br.com.fatec.dao.FuncionarioDAO;
import br.com.fatec.data.Database;
import br.com.fatec.model.Funcionario;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.util.converter.IntegerStringConverter;

public class adicionarFuncionarioController implements Initializable {

    @FXML
    public DatePicker birthDate;
    @FXML
    private AnchorPane profilePane;
    @FXML
    private AnchorPane profileBack;
    @FXML
    private ImageView profile;
    @FXML
    private ImageView employee;
    @FXML
    private TextField cepField;
    @FXML
    private TextField cpfField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField nameField;
    @FXML
    public TextField passwordField;
    @FXML
    public TextField addressField;
    @FXML
    public ComboBox<String> occupationCombo;
    @FXML
    public ComboBox<String> stateCombo;
    @FXML
    public ComboBox<String> cityCombo;
    @FXML
    public ComboBox<String> statusCombo;

    public String img;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

        // Configuração de imagem para o employee
        employee.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagens", "*.jpg", "*.jpeg", "*.png"));
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                img = selectedFile.toURI().toString();
                Image newImage = new Image(img);
                employee.setImage(newImage);

                SnapshotParameters newParams = new SnapshotParameters();
                newParams.setFill(Color.TRANSPARENT);
                WritableImage newWritableImage = employee.snapshot(newParams, null);
                employee.setClip(null);
                employee.setImage(newWritableImage);

                Rectangle newClip = new Rectangle(employee.getFitWidth(), employee.getFitHeight());
                newClip.setArcWidth(25);
                newClip.setArcHeight(25);
                employee.setClip(newClip);
            }
        });

       // Máscara para o campo de CEP (formatação dinâmica)
            UnaryOperator<TextFormatter.Change> cepFilter = change -> {
          if (!change.isContentChange()) {
              return change; // Mantém mudanças de estilo, etc.
          }

          String text = change.getControlNewText().replaceAll("[^\\d]", ""); // Remove qualquer não numérico

          // Limita o texto ao máximo de 8 dígitos
          if (text.length() > 8) {
              text = text.substring(0, 8);
          }

          // Formata como "00000-000"
          StringBuilder formatted = new StringBuilder();
          if (text.length() > 5) {
              formatted.append(text.substring(0, 5)).append("-");
              formatted.append(text.substring(5));
          } else {
              formatted.append(text);
          }

          int caretPosition = formatted.length(); // Posiciona o cursor no final
          change.setText(formatted.toString());
          change.setRange(0, change.getControlText().length()); // Substitui tudo
          change.selectRange(caretPosition, caretPosition); // Posiciona o cursor

          return change;
      };

      TextFormatter<String> textFormatterCep = new TextFormatter<>(cepFilter);
      cepField.setTextFormatter(textFormatterCep);

       // Máscara para o campo de CPF (formatação dinâmica)
                    UnaryOperator<TextFormatter.Change> cpfFilter = change -> {
          if (!change.isContentChange()) {
              return change; // Mantém mudanças de estilo, etc.
          }

          String text = change.getControlNewText().replaceAll("[^\\d]", ""); // Remove qualquer não numérico

          // Limita o texto ao máximo de 11 dígitos
          if (text.length() > 11) {
              text = text.substring(0, 11);
          }

          // Formata como "000.000.000-00"
          StringBuilder formatted = new StringBuilder();
          if (text.length() > 3) {
              formatted.append(text.substring(0, 3)).append(".");
              if (text.length() > 6) {
                  formatted.append(text.substring(3, 6)).append(".");
                  if (text.length() > 9) {
                      formatted.append(text.substring(6, 9)).append("-");
                      formatted.append(text.substring(9));
                  } else {
                      formatted.append(text.substring(6));
                  }
              } else {
                  formatted.append(text.substring(3));
              }
          } else {
              formatted.append(text);
          }

          int caretPosition = formatted.length(); // Posiciona o cursor no final
          change.setText(formatted.toString());
          change.setRange(0, change.getControlText().length()); // Substitui tudo
          change.selectRange(caretPosition, caretPosition); // Posiciona o cursor

          return change;
      };

      TextFormatter<String> textFormatterCpf = new TextFormatter<>(cpfFilter);
      cpfField.setTextFormatter(textFormatterCpf);

        cpfField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue){
                    if(cpfField.getText().isEmpty()) {
                        cpfField.getStyleClass().add("errorInput");
                    }
                }else{
                        if(!cpfField.getText().isEmpty()){
                            cpfField.getStyleClass().remove("errorInput");
                        }
                }
            }
        });

        cepField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue){//Lost focus
                    if(cepField.getText().isEmpty()){
                        cepField.getStyleClass().add("errorInput");
                    }else if(cepField.getText().length()==9){
                        String sql="SELECT DESCRICAO,COMPLEMENTO,DESCRICAO_CIDADE,UF FROM logradouro WHERE CEP = "+cepField.getText().replaceAll("-","")+";";
                        try {
                            Database.connect();
                            PreparedStatement ps = Database.getConnection().prepareStatement(sql);
                            ResultSet rs = ps.executeQuery();
                            String e ="";
                            String c = "";
                            String u = "";
                            if(rs.next()) {
                                e = rs.getString(1) + " " + rs.getString(2);
                                c = rs.getString(3);
                                u = rs.getString(4);
                                addressField.setText(e);
                                stateCombo.setValue(u);

                                populateCity();
                                cityCombo.setValue(c);
                                cityCombo.setDisable(false);
                            }else {
                                Messenger.warn("CEP não encontrado",new String[]{cepField.getText()});
                            }
                            Database.close();

                        }catch (SQLException e){
                            //Messenger.error("Erro de banco",new String[]{String.valueOf(e.getErrorCode())});
                            Messenger.error("SQLe",new String[]{e.toString()});
                        }
                    }
                }else{
                    if(!cepField.getText().isEmpty()){
                        cepField.getStyleClass().remove("errorInput");
                    }
                }
            }
        });

        nameField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue){
                    if(nameField.getText().isEmpty()){
                        nameField.getStyleClass().add("errorInput");
                    }
                }else{
                    if(!nameField.getText().isEmpty()){
                        nameField.getStyleClass().remove("errorInput");
                    }
                }
            }
        });

        emailField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue){
                    if(emailField.getText().isEmpty()){
                        //Credit to www.regular-expressions.info
                        if(!emailField.getText().matches("^(?=[A-Za-z0-9][A-Za-z0-9@._%+-]{5,253}+$)[A-Za-z0-9._%+-]{1,64}+@" +
                                "(?:(?=[A-Za-z0-9-]{1,63}+\\.)[A-Za-z0-9]++(?:-[A-Za-z0-9]++)*+\\.){1,8}+[A-Za-z]{2,63}+$")) {
                            emailField.getStyleClass().add("errorInput");
                        }
                    }
                }else{
                    if(!emailField.getText().isEmpty()){
                        emailField.getStyleClass().remove("errorInput");
                    }
                }
            }
        });

        passwordField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue){
                    if(passwordField.getText().isEmpty()){
                        passwordField.getStyleClass().add("errorInput");
                    }
                }else{
                    if(!passwordField.getText().isEmpty()){
                        passwordField.getStyleClass().remove("errorInput");
                    }
                }
            }
        });

        addressField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue){
                    if(addressField.getText().isEmpty()){
                        addressField.getStyleClass().add("errorInput");
                    }
                }else{
                    if(!addressField.getText().isEmpty()){
                        addressField.getStyleClass().remove("errorInput");
                    }
                }
            }
        });

        statusCombo.setItems(FXCollections.observableArrayList("Ativo","Desativado"));

        occupationCombo.setItems(FXCollections.observableArrayList("Administrador","Supervisor","Atendente"));

        ObservableList<String> uf =  FXCollections.observableArrayList("AC","AL","AM","AP","BA","CE","DF",
                "ES","GO","MA","MG","MS","MT","PA","PB","PE","PI","PR","RJ","RN","RO","RR","RS","SC","SE","SP","TO");

        stateCombo.setItems(uf);
        stateCombo.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue){//Lost focus
                    if((stateCombo.getValue() == null)){
                        stateCombo.getStyleClass().add("errorInput");
                    }else {
                        stateCombo.getStyleClass().remove("errorInput");
                        populateCity();
                    }
                }
            }
        });

    }

    private boolean verify(){
        ArrayList<String> msg = new ArrayList<>();
        ArrayList<String> context = new ArrayList<>();
        if (cepField.getText().isEmpty()) {
            msg.add("CEP");
        }
        if(cpfField.getText().isEmpty()) {
            msg.add("CPF");
        } else if (cpfField.getText().length() != 14) {
            context.add("CPF inválido");
        }
        if (nameField.getText().isEmpty()) {
            msg.add("Nome");
        }
        if (emailField.getText().isEmpty()) {
            msg.add("Email");

        } else if (!emailField.getText().matches("^(?=[A-Za-z0-9][A-Za-z0-9@._%+-]{5,253}+$)[A-Za-z0-9._%+-]{1,64}+@" +
                "(?:(?=[A-Za-z0-9-]{1,63}+\\.)[A-Za-z0-9]++(?:-[A-Za-z0-9]++)*+\\.){1,8}+[A-Za-z]{2,63}+$")) {
            context.add("Email inválido");
        }
        if (passwordField.getText().isEmpty()){
            msg.add("Senha");
            
        }
        if (addressField.getText().isEmpty()){
            msg.add("Endereço");
        }
        if (statusCombo.getValue() == null){
            msg.add("Status");
        }
        if (stateCombo.getValue() == null){
            msg.add("Estado");
        }
        if (cityCombo.getValue()  == null){
            msg.add("Cidade");
        }
        if (occupationCombo.getValue()  == null){
            msg.add("Cargo");
        }
        if (birthDate.getValue()  == null){
            msg.add("Data de nascimento");
        }
        if(!msg.isEmpty()){
            
            Messenger.error("Campos obrigatorios não preenchidos",new String[]{String.join(",",msg),String.join("\n",context)});
            return false;
        }
        return true;
    }

    private void populateCity(){
        try{
            Database.connect();
            PreparedStatement ps = Database.getConnection().prepareStatement("SELECT DISTINCT DESCRICAO_CIDADE FROM logradouro WHERE UF = '"+stateCombo.getValue()+"' ORDER BY DESCRICAO_CIDADE ASC;");
            ResultSet rs = ps.executeQuery();
            ArrayList<String> u = new ArrayList<String>();
            while (rs.next()){
                u.add(rs.getString(1));
            }
            Database.close();
            ObservableList<String> o = FXCollections.observableArrayList(u);
            cityCombo.setItems(o);
            cityCombo.setDisable(false);

        }catch (SQLException e){
            //Messenger.error("Erro de banco",new String[]{String.valueOf(e.getErrorCode())});
            Messenger.error("SQLe",new String[]{e.toString()});
        }
    }

    @FXML
    private void addFuncionario(){
        if(verify()) {  //cpf name birth occu email cep endereco cidade uf status img
            Funcionario f = new Funcionario(cpfField.getText(),nameField.getText(),
                    birthDate.getValue(),occupationCombo.getValue()
                    ,emailField.getText(),cepField.getText(),addressField.getText(),cityCombo.getValue(),
                    stateCombo.getValue(),
                    statusCombo.getValue().equals("Ativo"));
            try {
                FuncionarioDAO fu =new FuncionarioDAO();
                fu.insert(f);
                Messenger.info("Concluido","Funcionario inserido no banco");
            }catch (SQLException e){
                Messenger.error("Erro de banco",e.getMessage());
            }

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
}
