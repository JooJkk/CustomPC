package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

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

    private void trocarTela(ActionEvent event, String caminhoFxml) {
        try {
            Parent novaTela = FXMLLoader.load(getClass().getResource(caminhoFxml));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(novaTela));
            window.show();
        } catch (IOException e) {
            System.out.println("Erro ao carregar a tela: " + caminhoFxml);
            e.printStackTrace();
        }
    }

    @FXML
    void irParaCatalogo(ActionEvent event) {
        trocarTela(event, "/Catalogo.fxml");
    }

    @FXML
    void irParaCarrinho(ActionEvent event) {
        trocarTela(event, "/Carrinho.fxml");
    }

    @FXML
    void irParaLogin(ActionEvent event) {
        trocarTela(event, "/Login.fxml");
    }
}