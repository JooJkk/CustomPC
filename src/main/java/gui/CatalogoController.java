package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Cliente;
import model.componentes.Componente;
import model.componentes.*;
import negocio.CarrinhoService;
import negocio.BuildService;
import negocio.ComponenteService;

import java.io.IOException;
import java.util.List;

public class CatalogoController {

    private Cliente usuarioLogado;

    public void setUsuario(Cliente usuario) {
        this.usuarioLogado = usuario;
    }

    @FXML private TableView<Componente> tabelaComponentes;
    @FXML private TableColumn<Componente, String> colunaNome;
    @FXML private TableColumn<Componente, String> colunaMarca;
    @FXML private TableColumn<Componente, Double> colunaPreco;

    @FXML private Button btnAdicionar;
    @FXML private Button btnVoltar;
    @FXML private Button btnIrCarrinho;
    @FXML private Button btnEnviarBuild;
    @FXML private Button btnDetalhes;
    private CarrinhoService carrinhoService = CarrinhoService.getInstance();
    private BuildService buildService = BuildService.getInstance();

    @FXML
    public void initialize() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        ObservableList<Componente> pecas = FXCollections.observableArrayList(ComponenteService.getInstance().listar());

        tabelaComponentes.setItems(pecas);

        btnAdicionar.setOnAction(event -> {
            Componente selecionado = tabelaComponentes.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                carrinhoService.adicionarItem(selecionado, 1);
                alert("✅ " + selecionado.getNome() + " adicionado ao carrinho!");
            } else {
                alert("⚠️ Selecione um componente primeiro!");
            }
        });
    }

    @FXML
    private void voltarHome(ActionEvent event) {
        trocarTela("/home.fxml", event);
    }

    // CORRIGIDO: Agora você pode clicar no botão mesmo sem selecionar nada na tabela!
    @FXML
    private void enviarParaBuild(ActionEvent event) {
        Componente selecionado = tabelaComponentes.getSelectionModel().getSelectedItem();

        // Se houver uma peça selecionada, nós adicionamos. Se não houver, ele apenas ignora o IF e abre a tela limpa!
        if (selecionado != null) {
            buildService.adicionarComponenteParaMontagem(selecionado);
        }

        // Abre a tela direto de qualquer forma
        trocarTela("/builds-view.fxml", event);
    }
    @FXML
    private void abrirDetalhes(ActionEvent event) {
        Componente selecionado = tabelaComponentes.getSelectionModel().getSelectedItem();



        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetalhesProduto.fxml"));
            Parent root = loader.load();

            DetalhesProdutoController controller = loader.getController();
            controller.setUsuario(usuarioLogado);
            controller.setComponente(selecionado);
            Stage stage = (Stage) btnDetalhes.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            alert("Erro ao abrir detalhes: " + e.getMessage());
        }
    }


    @FXML
    private void irParaCarrinho(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Carrinho.fxml"));
            Parent root = loader.load();

            CarrinhoController controller = loader.getController();
            controller.setUsuario(usuarioLogado);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            alert("Erro ao abrir a tela de carrinho.");
        }
    }

    private void trocarTela(String fxml, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Object controller = loader.getController();
            if (controller instanceof BuildsController c) c.setUsuario(usuarioLogado);
            if (controller instanceof ClienteController c) c.setUsuario(usuarioLogado);
            if (controller instanceof HomeController c)     c.setUsuario(usuarioLogado);
            if (controller instanceof CatalogoController c) c.setUsuario(usuarioLogado);
            if (controller instanceof CarrinhoController c) c.setUsuario(usuarioLogado);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            alert("Erro na navegação: Não foi possível carregar " + fxml);
        }
    }

    private void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}