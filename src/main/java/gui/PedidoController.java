package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;
import java.util.List;

import model.Cliente;
import model.Pedido;
import negocio.PedidoService;

public class PedidoController {

    private Cliente usuarioLogado;
    private ClienteController clienteControllerPai;

    @FXML
    private VBox containerPedidos;

    public void setClienteControllerPai(ClienteController pai) {
        this.clienteControllerPai = pai;
    }

    public void setUsuario(Cliente usuario) {
        this.usuarioLogado = usuario;
        carregarPedidos();
    }

    private void carregarPedidos() {
        containerPedidos.getChildren().clear();

        List<Pedido> meusPedidos = PedidoService.getInstance().listarTodos().stream().filter(p -> p.getCliente() != null && p.getCliente().getId() == usuarioLogado.getId()).toList();

        if (meusPedidos == null || meusPedidos.isEmpty()) {
            containerPedidos.getChildren().add(new Label("Nenhum pedido encontrado."));
            return;
        }

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Pedido p : meusPedidos) {
            VBox cardPedido = new VBox(5);
            cardPedido.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 10; -fx-background-color: #f9f9f9;");

            Label lblId = new Label("Pedido #" + p.getId());
            lblId.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            String dataStr = (p.getData() != null) ? p.getData().format(formatador) : "--/--/----";
            Label lblData = new Label("Data: " + dataStr);

            Label lblTotal = new Label(String.format("Total: R$ %.2f", p.getValorTotal()));
            lblTotal.setStyle("-fx-font-weight: bold;");

            Label lblStatus = new Label("Status: " + p.getStatus());
            lblStatus.setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");

            ProgressBar barra = new ProgressBar(0);
            barra.setPrefWidth(250);
            if ("PENDENTE".equalsIgnoreCase(p.getStatus()) || "AGUARDANDO_PAGAMENTO".equalsIgnoreCase(p.getStatus())) barra.setProgress(0.25);
            else if ("PREPARANDO_COMPONENTE 🛠️".equalsIgnoreCase(p.getStatus())) barra.setProgress(0.50);
            else if ("ENVIADO".equalsIgnoreCase(p.getStatus())) barra.setProgress(1.0);

            Label lblTituloItens = new Label("Itens Comprados:");
            lblTituloItens.setStyle("-fx-font-weight: bold; -fx-padding: 10 0 2 0;");

            VBox caixaDeItens = new VBox(2);
            caixaDeItens.setStyle("-fx-padding: 0 0 10 10;");

            if (p.getItens() != null && !p.getItens().isEmpty()) {
                for (var item : p.getItens()) {
                    var comp = item.getComponente();

                    String textoItem = String.format("• %s - R$ %.2f",
                            comp.getNome(),
                            comp.getPreco());

                    Label lblItem = new Label(textoItem);
                    lblItem.setStyle("-fx-text-fill: #555555; -fx-font-size: 12px;");
                    caixaDeItens.getChildren().add(lblItem);
                }
            } else {
                Label lblVazio = new Label("Nenhum item detalhado.");
                lblVazio.setStyle("-fx-text-fill: #999999; -fx-font-size: 12px;");
                caixaDeItens.getChildren().add(lblVazio);
            }

            Button btnCancelar = new Button("Cancelar Pedido");
            btnCancelar.setStyle("-fx-background-color: #ff4c4c; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

            if ("CANCELADO".equalsIgnoreCase(p.getStatus()) ||
                    "PREPARANDO_COMPONENTE 🛠️".equalsIgnoreCase(p.getStatus()) ||
                    "ENVIADO".equalsIgnoreCase(p.getStatus())) {
                btnCancelar.setDisable(true);
            }

            btnCancelar.setOnAction(event -> {
                System.out.println("Clicou em cancelar no pedido #" + p.getId());

                p.setStatus("CANCELADO");

                PedidoService.getInstance().atualizarPedido(p);

                carregarPedidos();
            });

            cardPedido.getChildren().addAll(lblId, lblData, lblStatus, barra, lblTituloItens, caixaDeItens, lblTotal, btnCancelar);

            containerPedidos.getChildren().add(cardPedido);
        }
    }

    @FXML
    public void onBotaoVoltarClick(ActionEvent event) {
        NavegacaoController.trocarTela("/cliente-view.fxml", event, usuarioLogado);
    }
}
