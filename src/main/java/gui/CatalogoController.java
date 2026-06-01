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
import java.io.IOException;

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

        ObservableList<Componente> pecas = FXCollections.observableArrayList();

        pecas.add(new Processador("Ryzen 5 5600", "AMD", 1000.0, 0.5, 10, 65, "AM4", 65));
        pecas.add(new Processador("Intel i7-12700K", "Intel", 2100.0, 0.6, 5, 125, "LGA1700", 125));
        pecas.add(new Processador("Ryzen 9 7950X", "AMD", 3500.0, 0.5, 3, 170, "AM5", 170));
        pecas.add(new Processador("Core i5-13600K", "Intel", 1900.0, 0.6, 8, 125, "LGA1700", 125));
        pecas.add(new Processador("Core i3-12100F", "Intel", 650.0, 0.4, 20, 58, "LGA1700", 60));

        pecas.add(new PlacaMae("B550M Aorus Elite", "Gigabyte", 950.0, 1.0, 8, 30, "AM4", 4, "DDR4", "Micro-ATX"));
        pecas.add(new PlacaMae("X670E ASUS ROG", "ASUS", 2800.0, 1.2, 4, 40, "AM5", 4, "DDR5", "ATX"));
        pecas.add(new PlacaMae("H610M-E", "MSI", 620.0, 0.8, 12, 25, "LGA1700", 2, "DDR4", "Micro-ATX"));
        pecas.add(new PlacaMae("A520M-K", "ASUS", 480.0, 0.7, 15, 20, "AM4", 2, "DDR4", "Micro-ATX"));
        pecas.add(new PlacaMae("Z790 MSI Pro", "MSI", 2100.0, 1.1, 6, 45, "LGA1700", 4, "DDR5", "ATX"));

        pecas.add(new MemoriaRam("16GB Corsair Vengeance", "Corsair", 380.0, 0.1, 30, 5, "DDR4", 16, 3200));
        pecas.add(new MemoriaRam("32GB G.Skill Trident", "G.Skill", 1250.0, 0.2, 10, 10, "DDR5", 32, 6000));
        pecas.add(new MemoriaRam("8GB Kingston Fury", "Kingston", 210.0, 0.1, 50, 5, "DDR4", 8, 2666));
        pecas.add(new MemoriaRam("16GB XPG Spectrix", "XPG", 440.0, 0.1, 25, 8, "DDR4", 16, 3600));

        pecas.add(new Fonte("650W Corsair CV", "Corsair", 460.0, 2.0, 15, 0, 650, "80 Plus Bronze"));
        pecas.add(new Fonte("850W EVGA SuperNova", "EVGA", 850.0, 2.5, 10, 0, 850, "80 Plus Gold"));
        pecas.add(new Fonte("500W Redragon RGPS", "Redragon", 290.0, 1.5, 22, 0, 500, "80 Plus Bronze"));
        pecas.add(new Fonte("1000W Seasonic Prime", "Seasonic", 1600.0, 3.0, 5, 0, 1000, "80 Plus Platinum"));

        pecas.add(new Processador("RTX 4060 Ti", "NVIDIA", 2600.0, 1.2, 7, 0, "PCIe", 160));
        pecas.add(new Processador("RX 6750 XT", "AMD", 2300.0, 1.3, 5, 0, "PCIe", 250));
        pecas.add(new Processador("RTX 3060", "MSI", 1850.0, 1.1, 12, 0, "PCIe", 170));

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