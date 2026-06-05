import gui.ClienteController;
import gui.HomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Cliente;
import negocio.ClienteService;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {


        FXMLLoader loaderHome = new FXMLLoader(
                getClass().getResource("/Home.fxml")
        );

        Parent telaHome = loaderHome.load();

        HomeController controllerHome = loaderHome.getController();
        controllerHome.setUsuario(ClienteService.getInstance().buscarPorId(1));
        Scene scene = new Scene(telaHome, 1200, 800);

        stage.setTitle("CustomPC - Home");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}