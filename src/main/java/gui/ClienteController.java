package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

// importando a classe do meu pedido real
import javafx.stage.Stage;
import model.Cliente;
import model.Pedido;

public class ClienteController {

    private Cliente usuarioLogado;
    public void setUsuario(Cliente usuario) {
        this.usuarioLogado = usuario;

        // Agora sim carrega a sub-tela, pois já temos o usuário
        carregarTela("hello-view.fxml");
    }

    private void atualizarTela() {
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
    private Label lblNumeroPedido;
    @FXML
    private Label lblDataPedido;
    @FXML
    private Label lblTotalPedido;
    @FXML
    private Label lblStatusPedido;
    @FXML
    private ProgressBar barraProgresso;

    // frase que vai aparecer na minha tela home
    @FXML
    private Label lblBoasVindas;

    @FXML
    public void initialize() {
        // 1. Lógica da Home (Mantida)

        // logica de pedido e usando a minha classe real, algo TEMPORARIO até substituir por outro
        if (lblNumeroPedido != null) {

            // pedido TEMPORARIO
            Pedido pedidoExemplo = new Pedido(); //criando um objeto
            pedidoExemplo.setId(45219); // ID
            pedidoExemplo.setStatus("PREPARANDO_COMPONENTE 🛠️"); // muda o status

            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String dataFormatada = pedidoExemplo.getData().format(formatador);

            // colocando dados no meu objeto
            lblNumeroPedido.setText("Número do Pedido: #" + pedidoExemplo.getId());
            lblDataPedido.setText("Data do Pedido: " + dataFormatada);
            lblStatusPedido.setText("Status: " + pedidoExemplo.getStatus());

            // calcula o valor total
            lblTotalPedido.setText(String.format("Valor Total: R$ %.2f", pedidoExemplo.getValorTotal()));

            if (barraProgresso != null) {
                barraProgresso.setProgress(0.5);
            }
        }
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
        carregarTela("pedido-view.fxml");
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
                System.out.println("❌ ERRO: Não foi possível encontrar o arquivo FXML: " + fxml);
                return;
            }

            FXMLLoader loader = new FXMLLoader(recurso);
            Parent novaTela = loader.load();

            Object controller = loader.getController();
            if (controller instanceof ClienteController subController) {
                subController.usuarioLogado = this.usuarioLogado;
                subController.atualizarTela(); // chama direto pois lblBoasVindas já foi injetado
            }
            // 🌟 Se for a tela de Pedido, passa a referência deste ClienteController (Pai) para o botão voltar funcionar!
            if ("pedido-view.fxml".equals(fxml)) {
                PedidoController pc = loader.getController();
                if (pc != null) {
                    pc.setUsuario(usuarioLogado);
                    pc.setClienteControllerPai(this);
                }
            }

            if (painelPrincipal != null) {
                painelPrincipal.setCenter(novaTela);
                System.out.println("✅ Tela " + fxml + " carregada com sucesso no centro!");
            }

        } catch (IOException e) {
            System.out.println("💥 ERRO crítico ao dar .load() na tela " + fxml + ":");
            e.printStackTrace();
        }
    }
}