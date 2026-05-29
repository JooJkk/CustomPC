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


        FXMLLoader loaderHome = new FXMLLoader(
                getClass().getResource("/Home.fxml")
        );

        Parent telaHome = loaderHome.load();

        HomeController controllerHome = loaderHome.getController();

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