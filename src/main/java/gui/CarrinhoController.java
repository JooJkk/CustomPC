package gui;

import dados.IRepositorioPedido;
import dados.RepositorioPedido;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.*;
import model.componentes.Componente;
import negocio.*;
import java.io.IOException;

public class CarrinhoController {
    private CarrinhoService carrinhoService;
    private PedidoService pedidoService;
    private Cliente usuarioLogado;
    public void setUsuario(Cliente usuario) {
        this.usuarioLogado = usuario;
    }
    //Tabela de itens
    @FXML
    private TableView<ItemCarrinho> tabelaCarrinho;

    @FXML
    private TableColumn<ItemCarrinho, String> colunaItem;

    @FXML
    private TableColumn<ItemCarrinho, Integer> colunaQnt;

    @FXML
    private TableColumn<ItemCarrinho, Double> colunaUnd;

    @FXML
    private TableColumn<ItemCarrinho, Double> colunaTotal;

    //Resumo
    @FXML
    private Button btnVoltar;

    @FXML
    private Button btnContinuar;

    @FXML
    private Label txtValorTotal;

    @FXML
    private TableColumn<ItemCarrinho, Void> colunaRemove;

    private void carregarDados() {
        carrinhoService = CarrinhoService.getInstance();
        ObservableList<ItemCarrinho> itens =
                FXCollections.observableArrayList(carrinhoService.getCarrinho().getItens());
        tabelaCarrinho.setItems(itens);
        txtValorTotal.setText("R$: " + carrinhoService.calcularTotal());
    }

    @FXML
    public void initialize() {

        colunaTotal.setCellValueFactory(cellData
                -> new SimpleDoubleProperty(cellData.getValue().getSubtotal()).asObject());
        colunaItem.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getComponente().getNome()));
        colunaQnt.setCellValueFactory(cellData
                -> new SimpleIntegerProperty(cellData.getValue().getQuantidade()).asObject());
        colunaUnd.setCellValueFactory(cellData
                -> new SimpleDoubleProperty(cellData.getValue().getPrecoUnitario()).asObject());
        configurarColunaRemover();
        carregarDados();
        pedidoService = PedidoService.getInstance();
    }
    private void configurarColunaRemover() {
        colunaRemove.setCellFactory(param -> new TableCell<>() {
            private final Button btnRemover = new Button("Remover 🗑️");

            {
                btnRemover.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " + "-fx-background-radius: 5; -fx-font-weight: bold; -fx-font-size: 12px;");
                btnRemover.setCursor(javafx.scene.Cursor.HAND);
                btnRemover.setOnAction(event -> {
                    ItemCarrinho item = getTableView().getItems().get(getIndex());
                    removerItemDoCarrinho(item);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnRemover);
                }
            }
        });
    }
    private void removerItemDoCarrinho(ItemCarrinho item) {
        Componente componente = item.getComponente();
        int quantidadeParaDevolver = item.getQuantidade();
        carrinhoService.getCarrinho().removerItem(item);
        int estoqueAtual = componente.getEstoque();
        componente.setEstoque(estoqueAtual + quantidadeParaDevolver);
        ComponenteService.getInstance().atualizar(componente);
        carregarDados();
    }
    @FXML
    private void continuarCheckout(ActionEvent event) {
        NavegacaoController.trocarTela("/Checkout.fxml", event, usuarioLogado);
    }

    @FXML
    private void voltarHome(ActionEvent event) {
        NavegacaoController.trocarTela("/Home.fxml", event, usuarioLogado);
    }

    private void alert(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    @FXML
    void irParaLogin(ActionEvent event) {
        NavegacaoController.trocarTela("/Login.fxml", event, usuarioLogado);
    }
    @FXML
    private void irParaAreaCliente(ActionEvent event) {
        try {
            // 1. Carrega a "casca" da Área do Cliente
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cliente-view.fxml"));
            Parent rootCliente = loader.load();

            // 2. Pega o controller dela para injetar o usuário de volta
            ClienteController clienteController = loader.getController();

            // 3. Passa o usuário atual. Isso faz o ClienteController renderizar a Home interna automaticamente
            clienteController.setUsuario(this.usuarioLogado);
            clienteController.atualizarTela();

            // 4. Cria a cena respeitando rigidamente o tamanho padrão de 1200x800
            Scene cenaAreaCliente = new Scene(rootCliente, 1200.0, 800.0);

            // 5. Redireciona na mesma janela sem alterar as proporções do monitor
            Stage window = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            window.setScene(cenaAreaCliente);
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro crítico ao tentar voltar para a Área do Cliente a partir do Carrinho.");
        }
    }
}
