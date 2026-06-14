package gui;

import dados.IRepositorioPedido;
import dados.RepositorioPedido;
import exception.CarrinhoVazioException;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.*;
import negocio.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CheckoutController {

    private CarrinhoService carrinhoService = CarrinhoService.getInstance();
    private PedidoService pedidoService;
    private Cliente usuarioLogado;

    public void setUsuario(Cliente usuario) {
        this.usuarioLogado = usuario;
    }

    @FXML
    private Button btnVoltar;

    @FXML
    private TableView<ItemCarrinho> tabelaCheckout;

    @FXML
    private TableColumn<ItemCarrinho, String> colunaProduto;

    @FXML
    private TableColumn<ItemCarrinho, Integer> colunaQuantidade;

    @FXML
    private TableColumn<ItemCarrinho, Double> colunaPreco;

    @FXML
    private TableColumn<ItemCarrinho, Double> colunaTotal;

    // Endereço
    @FXML private TextField txtRua;
    @FXML private TextField txtNumero;
    @FXML private TextField txtCidade;
    @FXML private TextField txtCep;
    @FXML private TextField txtEstado;
    @FXML private TextField txtBairro;

    // Forma de pagamento
    @FXML private RadioButton btnPix;
    @FXML private RadioButton btnCartao;
    @FXML private RadioButton btnBoleto;
    @FXML private ToggleGroup grupoPagamento;
    @FXML private VBox painelPagamento;

    // Finalizar
    @FXML private Button btnConfirmar;
    @FXML private Label txtSubtotal;
    @FXML private Label txtFrete;
    @FXML private Label txtDesconto;
    @FXML private Label txtTotal;

    private void carregarDados() {
        ObservableList<ItemCarrinho> itens = FXCollections.observableArrayList(carrinhoService.getCarrinho().getItens());
        tabelaCheckout.setItems(itens);
        txtSubtotal.setText("R$: " + carrinhoService.calcularTotal());
    }

    @FXML
    public void initialize() {

        CheckoutStateService momentos = CheckoutStateService.getInstance();
        txtRua.setText(momentos.pegarCampo("rua"));
        txtNumero.setText(momentos.pegarCampo("numero"));
        txtCidade.setText(momentos.pegarCampo("cidade"));
        txtCep.setText(momentos.pegarCampo("cep"));
        txtEstado.setText(momentos.pegarCampo("estado"));
        txtBairro.setText(momentos.pegarCampo("bairro"));

        // calcula o frete automaticamente se o usuario tiver digitado o cep
        if (!txtCep.getText().isEmpty()) {
            atualizarFrete();
        }


        carregarDados();
        IRepositorioPedido repositorio = new RepositorioPedido();
        pedidoService = PedidoService.getInstance();

        // Tabela
        colunaTotal.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getSubtotal()).asObject());
        colunaProduto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getComponente().getNome()));
        colunaQuantidade.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantidade()).asObject());
        colunaPreco.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrecoUnitario()).asObject());

        grupoPagamento.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            painelPagamento.getChildren().clear();
            if (newToggle == btnPix) {
                mostrarPix();
            } else if (newToggle == btnCartao) {
                mostrarCartao();
            } else if (newToggle == btnBoleto) {
                mostrarBoleto();
            }
        });
    }

    // metodo que vai salvar os dados de forma TEMPORARIAA
    private void salvarDadosAtuais() {
        CheckoutStateService momentos = CheckoutStateService.getInstance();
        momentos.salvarCampo("rua", txtRua.getText());
        momentos.salvarCampo("numero", txtNumero.getText());
        momentos.salvarCampo("cidade", txtCidade.getText());
        momentos.salvarCampo("cep", txtCep.getText());
        momentos.salvarCampo("estado", txtEstado.getText());
        momentos.salvarCampo("bairro", txtBairro.getText());
    }

    private void mostrarPix() {
        ImageView qrCode = new ImageView(new Image(getClass().getResourceAsStream("/imagens/PIX.png")));
        qrCode.setFitWidth(200);
        qrCode.setFitHeight(200);
        Label lblQr = new Label("QR CODE");
        lblQr.setFont(new Font(20));
        painelPagamento.getChildren().addAll(qrCode, lblQr);
    }

    private void mostrarCartao() {
        TextField numeroCartao = new TextField();
        numeroCartao.setPromptText("Número do cartão");

        TextField nome = new TextField();
        nome.setPromptText("Nome no cartão");

        TextField cvv = new TextField();
        cvv.setPromptText("CVV");

        painelPagamento.getChildren().addAll(numeroCartao, nome, cvv);
    }

    private void mostrarBoleto() {
        Label lblBoleto = new Label("O boleto será gerado após confirmar a compra.");
        lblBoleto.setFont(new Font(20));
        lblBoleto.setWrapText(true);
        painelPagamento.getChildren().add(lblBoleto);
    }

    @FXML
    private void finalizarCompra(ActionEvent event) {
        String rua = txtRua.getText();
        String numero = txtNumero.getText();
        String cidade = txtCidade.getText();
        String cep = txtCep.getText();
        String estado = txtEstado.getText();
        String bairro = txtBairro.getText();

        Toggle selecionado = grupoPagamento.getSelectedToggle();

        if (selecionado == null) {
            alert("Selecione uma forma de pagamento!");
            return;
        }

        String formaPagamento = ((RadioButton) selecionado).getText();

        try {
            if(usuarioLogado != null) {
                Pagamento pagamento = new Pagamento();
                pagamento.setFormaPagamento(formaPagamento);
                pagamento.setValor(carrinhoService.calcularTotal());

                Endereco endereco = new Endereco(rua, numero, bairro, cidade, cep, estado);
                Pedido pedido = pedidoService.finalizarCompra(carrinhoService.getCarrinho(), endereco, pagamento, usuarioLogado);

                NavegacaoController.trocarTelaConfirmacao("/ConfirmacaoPagamento.fxml", event, usuarioLogado, pedido);

                // ponto em que vai excluir depois que a compra ja tiver sido feita
                CheckoutStateService.getInstance().limparDados();
            }
            else {
                alert("Faça o Login antes de finalizar o pedido");
            }
        } catch (CarrinhoVazioException e) {
            alert("Carrinho não pode estar vazio!");
        } catch (IllegalArgumentException e){
            alert(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            alert("Erro: " + e.getMessage());
        }
    }

    @FXML
    private void voltar(ActionEvent event){
        NavegacaoController.trocarTela("/Carrinho.fxml", event, usuarioLogado);
    }

    @FXML
    void irParaLogin(ActionEvent event) {

        salvarDadosAtuais();
        NavegacaoController.trocarTela("/Login.fxml", event, usuarioLogado);
    }

    @FXML
    private void atualizarFrete() {
        try {
            String rua = txtRua.getText();
            String numero = txtNumero.getText();
            String cidade = txtCidade.getText();
            String cep = txtCep.getText();
            String estado = txtEstado.getText();
            String bairro = txtBairro.getText();

            double subtotal = carrinhoService.calcularTotal();

            Endereco endereco = new Endereco(rua, numero, bairro, cidade, cep, estado);
            double frete = pedidoService.calcularFrete(carrinhoService.getCarrinho());
            CupomDesconto cupom = CupomService.getInstance().verificarEGerarCupom(carrinhoService.getCarrinho().getItens());
            double desconto = 0;
            if (cupom != null) {
                desconto = cupom.calcularDesconto(subtotal);
            }
            double total = subtotal + frete - desconto;

            txtSubtotal.setText(String.format("+ R$ %.2f", subtotal));
            txtDesconto.setText(String.format("- R$ %.2f", desconto));
            txtFrete.setText(String.format("+ R$ %.2f", frete));
            txtTotal.setText(String.format("R$ %.2f", total));
        } catch (Exception e) {
            txtFrete.setText("—");
            txtTotal.setText("—");
        }
    }

    private void alert(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}