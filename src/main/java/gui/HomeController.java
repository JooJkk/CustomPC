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
        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource("/Carrinho.fxml")
                    );

            Parent root = loader.load();

            CarrinhoController controller =
                    loader.getController();

            controller.setUsuario(usuarioLogado);

            Stage stage =
                    (Stage) btnCarrinho.getScene().getWindow();

            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void irParaCatalogo(ActionEvent event) {
        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource("/catalogo-view.fxml")
                    );

            Parent root = loader.load();

            CatalogoController controller =
                    loader.getController();

            controller.setUsuario(usuarioLogado);

            Stage stage =
                    (Stage) btnCatalogo.getScene().getWindow();

            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void irParaLogin(ActionEvent event) {

    }
}
