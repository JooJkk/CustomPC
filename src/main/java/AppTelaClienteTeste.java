import gui.ClienteController;
import model.Cliente;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class AppTelaClienteTeste extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // menu lateral
        FXMLLoader loaderPrincipal =
                new FXMLLoader(
                        getClass().getResource("/cliente-view.fxml")
                );

        Parent raiz = loaderPrincipal.load();

        ClienteController controllerPrincipal =
                loaderPrincipal.getController();

        Cliente cliente =
                new Cliente(
                        "Nome teste",
                        "email@teste.com",
                        "123"
                );

        controllerPrincipal.setUsuario(cliente);

        String[] caminhosPossiveis = {
                "/cadastro-view.fxml",
                "cadastro-view.fxml",
                "/negocio/cadastro-view.fxml",
                "negocio/cadastro-view.fxml"
        };

        java.net.URL recursoCadastro = null;

        for (String caminho : caminhosPossiveis) {

            recursoCadastro =
                    getClass().getResource(caminho);

            if (recursoCadastro != null) {
                break;
            }
        }

        if (recursoCadastro != null) {

            FXMLLoader loaderCadastro =
                    new FXMLLoader(recursoCadastro);

            Parent telaCadastro =
                    loaderCadastro.load();

            ClienteController controllerCadastro =
                    loaderCadastro.getController();

            // PASSA O USUÁRIO PARA A TELA INTERNA
            controllerCadastro.setUsuario(cliente);

            controllerCadastro.painelPrincipal =
                    controllerPrincipal.painelPrincipal;

            controllerPrincipal
                    .painelPrincipal
                    .setCenter(telaCadastro);
        }

        Scene scene = new Scene(raiz, 1200, 800);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}