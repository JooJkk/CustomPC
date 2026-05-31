import gui.ClienteController;
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
        FXMLLoader loaderPrincipal = new FXMLLoader(getClass().getResource("/cliente-view.fxml"));
        Parent raiz = loaderPrincipal.load();

        // controller do menu lateral
        ClienteController controllerPrincipal = loaderPrincipal.getController();

        // carrega o cadastro
        String[] caminhosPossiveis = {
                "/cadastro-view.fxml", "cadastro-view.fxml",
                "/negocio/cadastro-view.fxml", "negocio/cadastro-view.fxml"
        };
        java.net.URL recursoCadastro = null;

        for (String caminho : caminhosPossiveis) {
            recursoCadastro = getClass().getResource(caminho);
            if (recursoCadastro != null) {
                System.out.println("Sucesso! cadastro.fxml encontrado usando o caminho: " + caminho);
                break;
            }
        }

        if (recursoCadastro != null) {
            FXMLLoader loaderCadastro = new FXMLLoader(recursoCadastro);
            Parent telaCadastro = loaderCadastro.load();

            // controller do javafx para fzr o cadastro
            ClienteController controllerCadastro = loaderCadastro.getController();

            // passa o painel principal pra o cadastro
            if (controllerPrincipal != null && controllerCadastro != null) {
                controllerCadastro.painelPrincipal = controllerPrincipal.painelPrincipal;
            }


            if (controllerPrincipal != null && controllerPrincipal.painelPrincipal != null) {
                controllerPrincipal.painelPrincipal.setCenter(telaCadastro);
            }
        } else {
            System.out.println("Aviso: O arquivo cadastro-view.fxml nao foi encontrado de jeito nenhum!");
        }


        Scene scene = new Scene(raiz, 1200, 800);
        stage.setTitle("Meu Sistema JavaFX");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}