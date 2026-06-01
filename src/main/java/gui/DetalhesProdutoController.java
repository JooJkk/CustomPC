package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.*;
import model.componentes.Componente;
import negocio.*;

public class DetalhesProdutoController {
    private CarrinhoService carrinhoService = CarrinhoService.getInstance();
    private Componente produto;
    private Cliente usuarioLogado;
    public void setUsuario(Cliente usuario) {
        this.usuarioLogado = usuario;
    }
    public void setComponente(Componente produto) {
        this.produto = produto;
        atualizarTela();
    }
    private void atualizarTela() {

        if (produto == null) {
            return;
        }

        txtQntProduto.setText(String.valueOf(produto.getEstoque()));
        txtConsumoWatt.setText(String.valueOf(produto.getConsumoWatts()));
        txtNivelDesem.setText(String.valueOf(produto.getNivelDesempenho()));
        txtMarcaProduto.setText(produto.getMarca());
        txtNomeProduto.setText(produto.getNome());
        txtPrecoProduto.setText("R$ " + produto.getPreco());
    }
    //Buttons
    @FXML
    private Button btnVoltar;

    @FXML
    private Button btnLogar;

    @FXML
    private Button btnCarrinho;

    //Labels e Img
    @FXML
    private ImageView imgProduto;

    @FXML
    private Label txtQntProduto;

    @FXML
    private Label txtConsumoWatt;

    @FXML
    private Label txtNivelDesem;

    @FXML
    private Label txtMarcaProduto;

    @FXML
    private Label txtNomeProduto;

    @FXML
    private Label txtInfoProduto;

    @FXML
    private Label txtPrecoProduto;

    @FXML
    public void initialize() {
        atualizarTela();
    }

    @FXML
    private void adicionarCarrinho() {
        try {
            carrinhoService.adicionarItem(produto, 1);
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/Checkout.fxml"));
            Parent root = loader.load();
            CheckoutController controller =
                    loader.getController();
            controller.setUsuario(usuarioLogado);
            Stage stage = (Stage) btnCarrinho.getScene().getWindow();
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
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();
            HomeController controller = loader.getController();
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
