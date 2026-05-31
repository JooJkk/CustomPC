package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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

    private void trocarTela(
            String caminhoFxml,
            Button botaoOrigem
    ) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(caminhoFxml));
            Stage stage = (Stage) botaoOrigem.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void irParaCarrinho(ActionEvent event) {
        trocarTela("/Carrinho.fxml", btnCarrinho);
    }

    @FXML
    void irParaCatalogo(ActionEvent event) {
        trocarTela("/catalogo-view.fxml", btnCatalogo);
    }

    @FXML
    void irParaLogin(ActionEvent event) {
        trocarTela("/Login.fxml", btnLogin);
    }
}
