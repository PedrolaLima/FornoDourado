package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.dao.FuncionarioDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author alberto
 */
public class LoginController {

    @FXML
    private TextField userInput;
    
    @FXML
    private TextField passwInput;

    // Método chamado automaticamente após o carregamento do controlador
    @FXML
    private void initialize() {
        // Adiciona o ChangeListener para detectar mudanças no texto do TextField
        userInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Se houver alteração no texto, remove a classe 'errorInput'
                userInput.getStyleClass().remove("errorInput");
            }
        });
        
        passwInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Se houver alteração no texto, remove a classe 'errorInput'
                passwInput.getStyleClass().remove("errorInput");
            }
        });
    }

    public void login() throws IOException {
        FuncionarioDAO fd = new FuncionarioDAO();
        try {
            if (fd.login(userInput.getText(), passwInput.getText())) {
                carregarDashboard();
            } else{
                // Caso ocorra erro no login, adiciona a classe 'errorInput' ao campo
                userInput.getStyleClass().add("errorInput");
                passwInput.getStyleClass().add("errorInput");
            }
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    @FXML
    private void carregarDashboard() throws IOException {
        App.setRoot("dashboard");
    }
}
