package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

public class ClienteController {

    @FXML
    public BorderPane painelPrincipal;

    // frase que vai aparecer na minha tela home
    @FXML
    private Label lblBoasVindas;

    @FXML
    public void initialize() {
        // 1. Lógica da Home (Mantida)
        if (lblBoasVindas != null) {
            String nomeUsuario = "Brenna";
            lblBoasVindas.setText("SEJA BEM-VINDO(A), " + nomeUsuario.toUpperCase() + "!");
        }

        // 🌟 Removemos o bloco do pedido temporário daqui,
        // porque agora quem cuida dele é o PedidoController!
    }

    @FXML
    public void onBotaoHomeClick(ActionEvent event) {
        System.out.println("aguardandoo");   //depois você coloca o carregarTela aqui se quiser
    }

    @FXML
    public void onBotaoCatalogoClick(ActionEvent event) {
        carregarTela("catalogo-view.fxml");
    }

    @FXML
    public void onBotaoPedidoClick(ActionEvent event) {
        try {
            String fxml = "pedido-view.fxml";
            String[] caminhosPossiveis = {"/" + fxml, fxml, "/negocio/" + fxml, "negocio/" + fxml};
            java.net.URL recurso = null;

            for (String caminho : caminhosPossiveis) {
                recurso = getClass().getResource(caminho);
                if (recurso != null) break;
            }

            if (recurso == null) return;

            FXMLLoader loader = new FXMLLoader(recurso);
            Parent novaTela = loader.load();

            // 🌟 Conecta o PedidoController com este ClienteController
            PedidoController pedidoCtrl = loader.getController();
            if (pedidoCtrl != null) {
                pedidoCtrl.setClienteControllerPai(this);
            }

            if (painelPrincipal != null) {
                painelPrincipal.setCenter(novaTela);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void voltarParaAreaCliente() {
        try {
            // Carrega as boas-vindas usando o nome correto do seu arquivo! 🌟
            java.net.URL recurso = getClass().getResource("/cadastro-view.fxml");
            if (recurso == null) {
                recurso = getClass().getResource("cadastro-view.fxml");
            }

            if (recurso != null) {
                FXMLLoader loader = new FXMLLoader(recurso);
                Parent root = loader.load();

                // Coloca a tela de boas-vindas de volta no centro do seu menu lateral cinza
                if (painelPrincipal != null) {
                    painelPrincipal.setCenter(root);
                    System.out.println("✅ Voltou para a tela de boas-vindas com sucesso!");
                }
            } else {
                System.out.println(" ERRO: Não encontrou o arquivo cadastro-view.fxml dentro do ClienteController!");
            }
        } catch (IOException e) {
            System.out.println(" ERRO ao tentar voltar para a Área do Cliente:");
            e.printStackTrace();
        }
    }

    @FXML
    public void onBotaoCarrinhoClick(ActionEvent event) {
        carregarTela("carrinho-view.fxml");
    }

    @FXML
    public void onBotaoVoltarClick(ActionEvent event) {
        carregarTela("cadastro-view.fxml");
    }

    private void carregarTela(String fxml) {
        try {
            String[] caminhosPossiveis = {"/" + fxml, fxml, "/negocio/" + fxml, "negocio/" + fxml};
            java.net.URL recurso = null;

            for (String caminho : caminhosPossiveis) {
                recurso = getClass().getResource(caminho);
                if (recurso != null) break;
            }

            if (recurso == null) return;

            FXMLLoader loader = new FXMLLoader(recurso);

            // Apenas um IF limpo que protege as telas que têm controllers próprios
            if (!fxml.contains("cadastro") && !fxml.contains("pedido")) {
                loader.setController(this);
            }

            Parent novaTela = loader.load();

            if (painelPrincipal != null) {
                painelPrincipal.setCenter(novaTela);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}