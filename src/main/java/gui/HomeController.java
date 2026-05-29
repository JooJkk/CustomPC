package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    public BorderPane painelPrincipal;

    @FXML
    private Button btnCatalogo;

    @FXML
    private Button btnCarrinho;

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

    private void trocarTela(String caminhoFxml) {
        try {
            Parent novaTela = FXMLLoader.load(getClass().getResource(caminhoFxml));
            if (painelPrincipal != null) {
                painelPrincipal.setCenter(novaTela);
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar a tela: " + caminhoFxml);
            e.printStackTrace();
        }
    }

    @FXML
    void irParaCatalogo(ActionEvent event) {
        trocarTela("/Catalogo.fxml");
    }

    @FXML
    void irParaCarrinho(ActionEvent event) {
        trocarTela("/Carrinho.fxml");
    }

    @FXML
    void irParaLogin(ActionEvent event) {
        trocarTela("/Login.fxml");
    }
}
