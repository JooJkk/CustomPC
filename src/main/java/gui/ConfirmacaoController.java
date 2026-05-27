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

public class ConfirmacaoController {
    private Pedido pedido;
    private Cliente usuarioLogado;
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;

        carregarDados();
    }
    private void carregarDados() {

        lblIdPedido.setText("Pedido #" + pedido.getId());
        lblPagamento.setText(pedido.getPagamento().getFormaPagamento());
        lblValor.setText(String.valueOf(pedido.getValorTotal()));
        lblEndereco.setText(pedido.getEndereco().toString());
        lblStatus.setText(pedido.getStatus());
        ObservableList<ItemPedido> itens = FXCollections.observableArrayList(pedido.getItens());

        tabelaCheckout.setItems(itens);
    }

    @FXML
    private Label lblStatus;
    @FXML
    private Label lblIdPedido;

    @FXML
    private Label lblEndereco;

    @FXML
    private Label lblPagamento;

    @FXML
    private Label lblValor;

    //tabela com dados dos produtos comprados
    @FXML
    private TableView<ItemPedido> tabelaCheckout;

    @FXML
    private TableColumn<ItemPedido, String> colunaProduto;

    @FXML
    private TableColumn<ItemPedido, Integer> colunaQuantidade;

    @FXML
    private TableColumn<ItemPedido, Double> colunaPreco;

    @FXML
    private TableColumn<ItemPedido, Double> colunaTotal;


    public void initialize() {

        //Tabela
        colunaTotal.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getSubtotal()).asObject());

        colunaProduto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getComponente().getNome()));
        colunaQuantidade.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantidade()).asObject());
        colunaPreco.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrecoUnitario()).asObject());

    }
}

