package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Cliente;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    private Cliente usuarioLogado;
    public void setUsuario(Cliente usuario) {
        this.usuarioLogado = usuario;
    }

    public BorderPane painelPrincipal;

    @FXML
    private Button btnCatalogo;

    @FXML
    private Button btnCarrinho;
    
    @FXML
    private Button btnAreaCliente;

    @FXML
    private Button btnLogin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boolean usuarioEstaLogado = false;
        if (usuarioEstaLogado) {
            btnLogin.setText("Minha Conta");
        } else {
            btnLogin.setText("Login");
        }
    }

    @FXML
    void irParaCarrinho(ActionEvent event) {
        NavegacaoController.trocarTela("/Carrinho.fxml", event, usuarioLogado);
    }

    @FXML
    void irParaCatalogo(ActionEvent event) {
        NavegacaoController.trocarTela("/catalogo-view.fxml", event, usuarioLogado);
    }

    @FXML
    public void irParaAreaCliente(ActionEvent event) {
        NavegacaoController.trocarTela("/cliente-view.fxml", event, usuarioLogado);
    }
    @FXML
    void irParaLogin(ActionEvent event) {

    }
}
