package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import model.Cliente;


public class ClienteController {

    private Cliente usuarioLogado;

    public void setUsuario(Cliente usuario) {
        this.usuarioLogado = usuario;

        carregarTela("hello-view.fxml");
    }

    public void atualizarTela() {
        if (lblBoasVindas == null) return;

        if (usuarioLogado == null) {
            lblBoasVindas.setText("SEJA BEM-VINDO(A)!");
        } else {
            lblBoasVindas.setText("SEJA BEM-VINDO(A), " + usuarioLogado.getNome().toUpperCase() + "!");
        }
    }

    @FXML
    public BorderPane painelPrincipal;

    @FXML
    private Label lblBoasVindas;

    @FXML
    public void initialize() {
    }

    @FXML
    public void onBotaoHomeClick(ActionEvent event) {
        NavegacaoController.trocarTela("/Home.fxml", event, usuarioLogado);
    }

    @FXML
    public void onBotaoCatalogoClick(ActionEvent event) {
        NavegacaoController.trocarTela("/catalogo-view.fxml", event, usuarioLogado);
    }

    @FXML
    public void onBotaoPedidoClick(ActionEvent event) {
        if(usuarioLogado == null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Acesso Restrito");
            alert.setHeaderText("Você precisa estar logado para ver seus pedidos.");
            alert.setContentText("Deseja ir para a tela de login?");

            ButtonType botaoLogin = new ButtonType("Fazer Login");
            ButtonType botaoCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(botaoLogin, botaoCancelar);

            alert.showAndWait().ifPresent(resposta -> {
                if (resposta == botaoLogin) {
                    NavegacaoController.trocarTela("/Login.fxml", event, null);
                }
            });
        }
        else {
            carregarTela("pedido-view.fxml");
        }
    }

    @FXML
    public void onBotaoCarrinhoClick(ActionEvent event) {
        NavegacaoController.trocarTela("/Carrinho.fxml", event, usuarioLogado);
    }

    @FXML
    public void onBotaoVoltarClick(ActionEvent event) {
        carregarTela("hello-view.fxml");
    }

    private void carregarTela(String fxml) {
        try {
            String[] caminhosPossiveis = {"/" + fxml, fxml, "/negocio/" + fxml, "negocio/" + fxml};
            java.net.URL recurso = null;

            for (String caminho : caminhosPossiveis) {
                recurso = getClass().getResource(caminho);
                if (recurso != null) break;
            }

            if (recurso == null) {
                return;
            }

            FXMLLoader loader = new FXMLLoader(recurso);
            Parent novaTela = loader.load();

            Object controller = loader.getController();
            if (controller instanceof ClienteController subController) {
                subController.usuarioLogado = this.usuarioLogado;
                subController.atualizarTela();
            }

            // Tratamento da sub-tela de Pedidos
            if ("pedido-view.fxml".equals(fxml)) {
                PedidoController pc = loader.getController();
                if (pc != null) {
                    pc.setUsuario(usuarioLogado);
                    pc.setClienteControllerPai(this);
                }
            }

            if (painelPrincipal != null) {
                painelPrincipal.setCenter(novaTela);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}