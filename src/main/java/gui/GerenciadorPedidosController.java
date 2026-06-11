package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Cliente;
import model.Pedido;
import negocio.PedidoService;
import exception.PedidoNaoEncontradoException;
import exception.PedidoEnviadoException;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class GerenciadorPedidosController {

    @FXML
    private VBox containerPedidos;

    private Cliente usuarioLogado;
    private final PedidoService pedidoService = PedidoService.getInstance();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Método essencial para receber o usuário logado vindo da navegação do Admin
    public void setUsuario(Cliente usuario) {
        this.usuarioLogado = usuario;
    }

    @FXML
    public void initialize() {
        carregarPedidos();
    }

    private void carregarPedidos() {
        containerPedidos.getChildren().clear();
        List<Pedido> listaPedidos = pedidoService.listarTodos();

        if (listaPedidos == null || listaPedidos.isEmpty()) {
            Label semPedidos = new Label("Nenhum pedido encontrado no sistema.");
            semPedidos.setFont(Font.font("System", 16));
            containerPedidos.getChildren().add(semPedidos);
            return;
        }

        for (Pedido pedido : listaPedidos) {
            containerPedidos.getChildren().add(criarCardPedido(pedido));
        }
    }

    private VBox criarCardPedido(Pedido pedido) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 15;");

        // Informações básicas do Pedido
        String nomeCliente = (pedido.getCliente() != null) ? pedido.getCliente().getNome() : "Desconhecido";
        Label lblInfo = new Label(String.format("Pedido #%d | Cliente: %s | Data: %s",
                pedido.getId(), nomeCliente, pedido.getData().format(formatter)));
        lblInfo.setFont(Font.font("System", FontWeight.BOLD, 14));

        Label lblValor = new Label(String.format("Valor Total: R$ %.2f (Frete: R$ %.2f)", pedido.getValorTotal(), pedido.getFrete()));
        lblValor.setFont(Font.font("System", 13));

        // Label de Status com destaque
        Label lblStatus = new Label("Status atual: " + pedido.getStatus());
        lblStatus.setFont(Font.font("System", FontWeight.BOLD, 13));
        lblStatus.setStyle("-fx-text-fill: " + obterCorStatus(pedido.getStatus()) + ";");

        // Espaçador para empurrar os botões para a direita
        HBox containerBotoes = new HBox(10);
        containerBotoes.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(containerBotoes, Priority.ALWAYS);

        // Botão para Avançar Status
        Button btnProximoStatus = new Button();
        configurarBotaoStatus(btnProximoStatus, pedido);

        // Botão Cancelar
        Button btnCancelar = new Button("Cancelar Pedido ❌");
        btnCancelar.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");

        // Desativa o cancelamento se já estiver enviado
        if ("ENVIADO".equalsIgnoreCase(pedido.getStatus())) {
            btnCancelar.setDisable(true);
            btnCancelar.setStyle("-fx-background-color: #e0a8ad; -fx-text-fill: white; -fx-background-radius: 5;");
        }

        // Ações dos botões do Card
        btnCancelar.setOnAction(e -> handleCancelar(pedido.getId()));
        btnProximoStatus.setOnAction(e -> handleProximoStatus(pedido));

        containerBotoes.getChildren().addAll(btnProximoStatus, btnCancelar);

        card.getChildren().addAll(lblInfo, lblValor, lblStatus, containerBotoes);
        return card;
    }

    private void configurarBotaoStatus(Button btn, Pedido pedido) {
        String statusAtual = pedido.getStatus();

        switch (statusAtual.toUpperCase()) {
            case "PENDENTE":
                btn.setText("Avançar para: AGUARDANDO_PAGAMENTO ⏳");
                btn.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black; -fx-font-weight: bold; -fx-background-radius: 5;");
                break;
            case "AGUARDANDO_PAGAMENTO":
                btn.setText("Avançar para: PREPARANDO_COMPONENTE 🛠️");
                btn.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
                break;
            case "PREPARANDO_COMPONENTE 🛠️":
            case "PREPARANDO_COMPONENTE":
                btn.setText("Avançar para: ENVIADO 🚚");
                btn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
                break;
            case "ENVIADO":
                btn.setText("Pedido Concluído (ENVIADO) ✓");
                btn.setDisable(true);
                btn.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-background-radius: 5;");
                break;
            default:
                btn.setText("Mudar Status 🔄");
                btn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
                break;
        }
    }

    private String obterCorStatus(String status) {
        switch (status.toUpperCase()) {
            case "PENDENTE": return "#b8860b";
            case "AGUARDANDO_PAGAMENTO": return "#007bff";
            case "PREPARANDO_COMPONENTE 🛠️":
            case "PREPARANDO_COMPONENTE": return "#17a2b8";
            case "ENVIADO": return "#28a745";
            default: return "#000000";
        }
    }

    private void handleProximoStatus(Pedido pedido) {
        String statusAtual = pedido.getStatus().toUpperCase();
        String novoStatus = statusAtual;

        if (statusAtual.equals("PENDENTE")) {
            novoStatus = "AGUARDANDO_PAGAMENTO";
        } else if (statusAtual.equals("AGUARDANDO_PAGAMENTO")) {
            novoStatus = "PREPARANDO_COMPONENTE 🛠️";
        } else if (statusAtual.equals("PREPARANDO_COMPONENTE 🛠️") || statusAtual.equals("PREPARANDO_COMPONENTE")) {
            novoStatus = "ENVIADO";
        }

        if (!novoStatus.equals(statusAtual)) {
            pedido.setStatus(novoStatus);
            pedidoService.atualizarPedido(pedido);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Status do pedido #" + pedido.getId() + " atualizado para " + novoStatus);
            carregarPedidos(); // Recarrega a lista visualmente
        }
    }

    private void handleCancelar(long id) {
        try {
            // Conversão de long para int necessária para se adequar à assinatura do PedidoService fornecido
            pedidoService.cancelarPedido((int) id);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Pedido Cancelado", "O pedido #" + id + " foi cancelado com sucesso.");
            carregarPedidos();
        } catch (PedidoNaoEncontradoException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Pedido não encontrado.");
        } catch (PedidoEnviadoException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Não é possível cancelar um pedido que já foi ENVIADO.");
        }
    }

    @FXML
    public void onBotaoVoltarClick(ActionEvent event) {
        // Retorna para a tela de administração fornecendo o usuário que estava logado
        NavegacaoController.trocarTela("/AdminView.fxml", event, usuarioLogado);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}