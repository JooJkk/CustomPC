package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import java.time.format.DateTimeFormatter;

// Importando as classes do seu modelo e negócio
import javafx.stage.Stage;
import model.Cliente;
import model.Pedido;
import negocio.PedidoService;

public class PedidoController {

    private Cliente usuarioLogado;
    public void setUsuario(Cliente usuario) {
        this.usuarioLogado = usuario;
    }

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cliente-view.fxml"));
            Parent root = loader.load();

            ClienteController controller = loader.getController();
            controller.setUsuario(usuarioLogado);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}