package negocio;

import gui.HelloController;
import gui.HomeController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    public void start(Stage stage) throws IOException {

        FXMLLoader loaderPrincipal =
                new FXMLLoader(
                        getClass().getResource("/hello-view.fxml")
                );

        Parent raiz = loaderPrincipal.load();

        HelloController controllerPrincipal =
                loaderPrincipal.getController();

        FXMLLoader loaderHome =
                new FXMLLoader(
                        getClass().getResource("/Home.fxml")
                );

        Parent telaHome = loaderHome.load();

        HomeController controllerHome =
                loaderHome.getController();

        controllerHome.painelPrincipal =
                controllerPrincipal.painelPrincipal;

        controllerPrincipal.painelPrincipal
                .setCenter(telaHome);

        Scene scene = new Scene(raiz, 1200, 800);

        stage.setTitle("CustomPC");

        stage.setScene(scene);

        stage.setResizable(false);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}