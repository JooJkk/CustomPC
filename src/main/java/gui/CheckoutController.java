package gui;

import dados.IRepositorioPedido;
import dados.RepositorioPedido;
import exception.CarrinhoVazioException;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;
import negocio.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CheckoutController {
/*Para a tela que vem antes, quando for abrir a tela de checkout (para que o carrinho seja registrado:
 FXMLLoader loader =
        new FXMLLoader(
                getClass().getResource(
                        "/fxml/checkout.fxml"
                )
        );

Parent root = loader.load();

CheckoutController controller =
        loader.getController();

controller.setCarrinhoService(carrinhoService); */
    private CarrinhoService carrinhoService;
    private PedidoService pedidoService;
    private Cliente usuarioLogado;
    public void setUsuario(Cliente usuario) {
        this.usuarioLogado = usuario;
    }
    public void setCarrinhoService(CarrinhoService carrinhoService
    ) {
        this.carrinhoService = carrinhoService;
        carregarDados();
    }


    //tabela com dados dos produtos comprados
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

    //Endereço
    @FXML
    private TextField txtRua;

    @FXML
    private TextField txtNumero;

    @FXML
    private TextField txtCidade;

    @FXML
    private TextField txtCep;

    @FXML
    private TextField txtEstado;

    @FXML
    private TextField txtBairro;


    //Forma de pagamento
    @FXML
    private RadioButton btnPix;

    @FXML
    private RadioButton btnCartao;

    @FXML
    private RadioButton btnBoleto;

    @FXML
    private ToggleGroup grupoPagamento;

    // Finalizar (e dados da compra finais)
    @FXML
    private Button btnConfirmar;

    @FXML
    private Label txtSubtotal;

    @FXML
    private Label txtFrete;

    @FXML
    private Label txtTotal;

    private void carregarDados() {

        ObservableList<ItemCarrinho> itens = FXCollections.observableArrayList(carrinhoService.getCarrinho().getItens());

        tabelaCheckout.setItems(itens);

        txtSubtotal.setText("R$: " + carrinhoService.calcularTotal());

    }

    @FXML
    public void initialize() {
        IRepositorioPedido repositorio = new RepositorioPedido();
        pedidoService = new PedidoService(repositorio);
        //Tabela
        colunaTotal.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getSubtotal()).asObject());

        colunaProduto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getComponente().getNome()));
        colunaQuantidade.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantidade()).asObject());
        colunaPreco.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrecoUnitario()).asObject());

    }

    @FXML
    private void finalizarCompra() {


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
            Pagamento pagamento = new Pagamento();
            pagamento.setFormaPagamento(formaPagamento);
            pagamento.setValor(carrinhoService.calcularTotal());

            Endereco endereco = new Endereco(rua, numero, bairro, cidade, cep, estado);
            Pedido pedido = pedidoService.finalizarCompra(carrinhoService.getCarrinho(),  endereco, pagamento);
            //Carrega tela de pagamento, passando o cliente logado e o pedido feito
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ConfirmacaoPagamento.fxml"));

            Parent root = loader.load();
            ConfirmacaoController controller =
                    loader.getController();
            controller.setPedido(pedido);
            Stage stage = (Stage) btnConfirmar.getScene().getWindow();

            stage.setScene(new Scene(root));
        } catch (CarrinhoVazioException e) {
            alert("Carrinho não pode estar vazio!");
        }
        catch (IllegalArgumentException e){
            alert(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            alert("Erro: " + e.getMessage());
        }

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

            txtSubtotal.setText(
                    String.format("R$ %.2f", subtotal)
            );

            // só calcula frete se endereço válido
            Endereco endereco = new Endereco(
                    rua,
                    numero,
                    bairro,
                    cidade,
                    cep,
                    estado
            );

            double frete =
                    pedidoService.calcularFrete(endereco);

            double total = subtotal + frete;

            txtFrete.setText(
                    String.format("R$ %.2f", frete)
            );

            txtTotal.setText(
                    String.format("R$ %.2f", total)
            );

        } catch (Exception e) {

            // enquanto usuário digita,
            // evita popup de erro
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
