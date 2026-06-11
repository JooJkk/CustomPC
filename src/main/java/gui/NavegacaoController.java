package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Cliente;
import model.Pedido;

import java.io.IOException;

public class NavegacaoController {

    public static void trocarTela(String fxml, ActionEvent event, Cliente usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(NavegacaoController.class.getResource(fxml));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof HomeController)
                ((HomeController) controller).setUsuario(usuario);
            else if (controller instanceof CatalogoController)
                ((CatalogoController) controller).setUsuario(usuario);
            else if (controller instanceof BuildsController)
                ((BuildsController) controller).setUsuario(usuario);
            else if (controller instanceof CarrinhoController)
                ((CarrinhoController) controller).setUsuario(usuario);
            else if (controller instanceof PedidoController)
                ((PedidoController) controller).setUsuario(usuario);
            else if (controller instanceof CheckoutController)
                ((CheckoutController) controller).setUsuario(usuario);
            else if (controller instanceof ClienteController)
                ((ClienteController) controller).setUsuario(usuario);
            else if (controller instanceof LoginController)
                ((LoginController) controller).setUsuarioAnterior(usuario);
            else if (controller instanceof AdminViewController)
                ((AdminViewController) controller).setUsuario(usuario);
            else if (controller instanceof GerenciadorEstoqueController)
                ((GerenciadorEstoqueController) controller).setUsuario(usuario);
            else if (controller instanceof NewComponenteController)
                ((NewComponenteController) controller).setUsuario(usuario);


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void trocarTelaConfirmacao(String fxml, ActionEvent event, Cliente usuario, Pedido pedido) {
        try {
            FXMLLoader loader = new FXMLLoader(NavegacaoController.class.getResource(fxml));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof ConfirmacaoController) {
                ((ConfirmacaoController) controller).setUsuario(usuario);
                ((ConfirmacaoController)controller).setPedido(pedido);
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
