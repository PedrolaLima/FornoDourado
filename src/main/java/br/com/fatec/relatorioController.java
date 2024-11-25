package br.com.fatec;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

public class relatorioController {

    // Método para carregar a tela de "Adicionar Produto"
    @FXML
    private void carregarProdutos() throws IOException {
        App.setRoot("produto");
    }

    // Método para carregar a tela de "Pedidos"
    @FXML
    private void carregarPedidos() throws IOException {
        App.setRoot("pedido");
    }
    
    @FXML
    private void carregarFuncionarios() throws IOException {
        App.setRoot("");
    }  
    
    @FXML
    private void carregarDashboard() throws IOException {
        App.setRoot("dashboard");
    }
    
    @FXML
    private void carregarRelatorios() throws IOException {
        App.setRoot("relatorio");
    }
}
