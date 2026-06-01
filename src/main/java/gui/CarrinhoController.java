package gui;

import dados.IRepositorioPedido;
import dados.RepositorioPedido;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.*;
import negocio.*;

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
        carregarDados();
        pedidoService = PedidoService.getInstance();
    }

    @FXML
    private void continuarCheckout() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Checkout.fxml"));
            Parent root = loader.load();
            CheckoutController controller = loader.getController();
            controller.setUsuario(usuarioLogado);
            Stage stage = (Stage) btnContinuar.getScene().getWindow();
            stage.setScene(new Scene(root));
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
    private void voltarHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();
            HomeController controller = loader.getController();
            controller.setUsuario(usuarioLogado);
            Stage stage = (Stage) btnVoltar.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            alert("Erro: " + e.getMessage());
        }
    }

    private void alert(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
