package br.com.fatec;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

public class pedidoController {

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
        App.setRoot("");
    }  
    
    @FXML
    private void carregarRelatorios() throws IOException {
        App.setRoot("relatorio");
    }
    
    @FXML
    private void carregarPedidos() throws IOException {
        App.setRoot("pedidos");
    }
    
}
