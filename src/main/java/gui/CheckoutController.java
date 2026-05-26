package gui;

import model.*;
import negocio.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CheckoutController {
    @FXML
    private TableView tabelaCheckout;

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

    @FXML
    private RadioButton btnPix;

    @FXML
    private RadioButton btnCartao;

    @FXML
    private RadioButton btnBoleto;

    @FXML
    private Button btnFinalizar;

    @FXML
    private Label txtSubtotal;

    @FXML
    private Label txtFrete;

    @FXML
    private Label txtTotal;

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
