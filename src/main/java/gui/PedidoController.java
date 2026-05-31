package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import java.time.format.DateTimeFormatter;
import java.io.IOException;

// Importando as classes do seu modelo e negócio
import model.Pedido;
import negocio.PedidoService;

public class PedidoController {

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

    // Guarda a referência da Área do Cliente
    private ClienteController clienteControllerPai;

    // Para o ClienteController se passar para cá
    public void setClienteControllerPai(ClienteController pai) {
        this.clienteControllerPai = pai;
    }

    @FXML
    public void initialize() {
        // Puxa o pedido que o seu amigo acabou de guardar no Service após o checkout
        Pedido pedidoReal = PedidoService.getPedidoAtual();

        // Segurança: Se a tela abrir sem nenhum pedido feito, evita estourar erro
        if (pedidoReal != null) {

            // 1. Preenche o número do Pedido
            lblNumeroPedido.setText("Número do Pedido: #" + pedidoReal.getId());

            // 2. Formata e preenche a data de forma segura
            if (pedidoReal.getData() != null) {
                DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                String dataFormatada = pedidoReal.getData().format(formatador);
                lblDataPedido.setText("Data do Pedido: " + dataFormatada);
            } else {
                lblDataPedido.setText("Data do Pedido: --/--/----");
            }

            // 3. Preenche o Status
            lblStatusPedido.setText("Status: " + pedidoReal.getStatus());

            // 4. Preenche o Valor Total
            lblTotalPedido.setText(String.format("Valor Total: R$ %.2f", pedidoReal.getValorTotal()));

            // 5. Controla a barra de progresso baseado no status do pedido
            if (barraProgresso != null) {
                if ("PENDENTE".equalsIgnoreCase(pedidoReal.getStatus())) {
                    barraProgresso.setProgress(0.25);
                } else if ("PREPARANDO_COMPONENTE 🛠️".equalsIgnoreCase(pedidoReal.getStatus())) {
                    barraProgresso.setProgress(0.50);
                } else if ("ENVIADO".equalsIgnoreCase(pedidoReal.getStatus())) {
                    barraProgresso.setProgress(1.0);
                }
            }
        } else {
            lblNumeroPedido.setText("Nenhum pedido ativo encontrado.");
            lblTotalPedido.setText("Valor Total: R$ 0,00");
            if (barraProgresso != null) {
                barraProgresso.setProgress(0.0);
            }
        }
    }

    @FXML
    public void onBotaoVoltarClick(ActionEvent event) {
        System.out.println("🤖 Clique detectado! Redirecionando para a área do cliente...");

        try {
            // Buscando o arquivo da tela principal do cliente
            java.net.URL recursoMenu = getClass().getResource("/cliente-view.fxml");
            if (recursoMenu == null) {
                recursoMenu = getClass().getResource("cliente-view.fxml");
            }

            if (recursoMenu == null) {
                System.out.println("❌ ERRO: O arquivo cliente-view.fxml NÃO foi encontrado!");
                return;
            }

            FXMLLoader loaderMenu = new FXMLLoader(recursoMenu);
            Parent rootMenu = loaderMenu.load();
            ClienteController clienteController = loaderMenu.getController();

            // Buscando a tela inicial de cadastro/boas-vindas para embutir no centro
            java.net.URL recursoBoasVindas = getClass().getResource("/cadastro-view.fxml");
            if (recursoBoasVindas == null) {
                recursoBoasVindas = getClass().getResource("cadastro-view.fxml");
            }

            if (recursoBoasVindas == null) {
                System.out.println("❌ ERRO: O arquivo cadastro-view.fxml NÃO foi encontrado!");
                return;
            }

            Parent rootBoasVindas = FXMLLoader.load(recursoBoasVindas);

            // Se veio pelo painel lateral (Cenário A), podemos usar a referência existente ou reinjetar
            if (clienteControllerPai != null && clienteControllerPai.painelPrincipal != null) {
                clienteControllerPai.painelPrincipal.setCenter(rootBoasVindas);
                System.out.println("✅ Centro atualizado no painelPrincipal existente!");
            } else if (clienteController != null && clienteController.painelPrincipal != null) {
                clienteController.painelPrincipal.setCenter(rootBoasVindas);
                System.out.println("✅ Centro injetado com sucesso no novo painelPrincipal!");
            }

            // Realiza a troca da cena na janela atual de forma suave
            javafx.scene.Scene novaCena = new javafx.scene.Scene(rootMenu);
            javafx.scene.Node componente = (javafx.scene.Node) event.getSource();
            javafx.stage.Stage janelaAtual = (javafx.stage.Stage) componente.getScene().getWindow();

            janelaAtual.setScene(novaCena);
            janelaAtual.setTitle("Área do Cliente - Custom PC");
            janelaAtual.centerOnScreen();
            System.out.println("🚀 Transição de tela concluída com sucesso!");

        } catch (Exception e) {
            System.out.println("💥 ERRO no carregamento da transição de telas:");
            e.printStackTrace();
        }
    }
}