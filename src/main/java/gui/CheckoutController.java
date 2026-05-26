package gui;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
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

    public void setCarrinhoService(
            CarrinhoService carrinhoService
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
    private Button btnFinalizar;

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
        Endereco endereco = new Endereco(rua, numero, bairro, cidade, cep, estado);


    }

}
