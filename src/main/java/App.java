import gui.HomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        // 1. Carrega APENAS a Home.fxml
        FXMLLoader loaderHome = new FXMLLoader(
                getClass().getResource("/Home.fxml")
        );

        Parent telaHome = loaderHome.load();

        // 2. Pega o controller dela (caso precise usar depois)
        HomeController controllerHome = loaderHome.getController();

        // 3. Cria a cena PURA com a tela da Home
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