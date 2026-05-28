package negocio;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

// importando a classe do meu pedido real
import main.java.model.Pedido;

public class HelloController {

    @FXML
    public BorderPane painelPrincipal;

    // Elementos da tela de pedidos
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
        if (lblBoasVindas != null) {
            String nomeUsuario = "Brenna";
            lblBoasVindas.setText("SEJA BEM-VINDO(A), " + nomeUsuario.toUpperCase() + "!");
        }

        // logica de pedido  e usando a minha classe real, algo TEMPORARIO até substituir por outro
        if (lblNumeroPedido != null) {

            // pedido TEMPORARIO
            Pedido pedidoExemplo = new Pedido(); //criando um objeto
            pedidoExemplo.setId(45219); // ID
            pedidoExemplo.setStatus("PREPARANDO_COMPONENTE 🛠️"); // muda o status

            // depois vai ficar assim:
            // Pedido pedidoExemplo = CarrinhoService.getPedidoAtual();


            // usando localdatetime para ficar mais formal
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
        System.out.println("aguardandoo");   //depois eu vou trocar esse por carregarTela(aqui eu vou colocar o fxml da tela q eu quero que apareça)
    }

    @FXML
    public void onBotaoCatalogoClick(ActionEvent event) {
        carregarTela("catalogo-view.fxml");
    }

    @FXML
    public void onBotaoPedidoClick(ActionEvent event) {
        carregarTela("pedido-view.fxml");
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

            if (!fxml.contains("cadastro")) {
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