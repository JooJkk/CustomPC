
import gui.CheckoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

import model.Cliente;
import model.componentes.PlacaVideo;
import model.componentes.Processador;
import negocio.CarrinhoService;

public class AppCheckoutTeste extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Service
        CarrinhoService carrinhoService = CarrinhoService.getInstance();

        // Produtos fake
        PlacaVideo gpu = new PlacaVideo(
                "RTX 4070",
                "NVIDIA",
                4200.0,
                1.2,
                10,
                220,
                300,
                12
        );

        Processador cpu = new Processador(
                "Ryzen 7 7800X3D",
                "AMD",
                2500.0,
                0.3,
                5,
                120,
                "AM5",
                120
        );

        // Adiciona ao carrinho
        carrinhoService.adicionarItem(gpu, 1);

        carrinhoService.adicionarItem(cpu, 2);

        // Carrega FXML
        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/Checkout.fxml"
                        )
                );

        Parent root = loader.load();

        // Pega controller
        CheckoutController controller = loader.getController();
        Cliente cliente = new Cliente("Nome qualquer", "email@teste.com", "123");
        // Injeta service
        controller.setUsuario(cliente);

        // Cena
        Scene scene = new Scene(root, 1200, 800);

        stage.setScene(scene);

        stage.setTitle("Teste Checkout");

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}