package gui;

import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Cliente;
import model.componentes.Componente;
import model.componentes.*;
import negocio.CarrinhoService;
import negocio.BuildService;
import negocio.ComponenteService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CatalogoController {

    private Cliente usuarioLogado;

    public void setUsuario(Cliente usuario) {
        this.usuarioLogado = usuario;
    }

    @FXML private TableView<Componente> tabelaComponentes;
    @FXML private TableColumn<Componente, Componente> colunaImagem;
    @FXML private TableColumn<Componente, String> colunaNome;
    @FXML private TableColumn<Componente, String> colunaMarca;
    @FXML private TableColumn<Componente, Double> colunaPreco;
    @FXML private TableColumn<Componente, String> colunaConsumo;

    @FXML private VBox painelDetalhes;
    @FXML private ImageView imgDetalheGrande;
    @FXML private Label lblDetalheNome;
    @FXML private Label lblDetalheMarca;
    @FXML private Label lblDetalhePreco;
    @FXML private Label lblDetalheConsumo;
    @FXML private Label lblDetalheEstoque;
    @FXML private Spinner<Integer> spinQuantidade;

    @FXML private Button btnAdicionar;
    @FXML private Button btnVoltar;
    @FXML private Button btnIrCarrinho;
    @FXML private Button btnEnviarBuild;

    private CarrinhoService carrinhoService = CarrinhoService.getInstance();
    private BuildService buildService = BuildService.getInstance();

    private List<Componente> todasPecas = new ArrayList<>();

    private Image imgFonte;
    private Image imgRam;
    private Image imgGpu;
    private Image imgCpu;

    @FXML
    public void initialize() {
        carregarImagensSystem();

        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));

        colunaConsumo.setCellValueFactory(cellData -> {
            Componente comp = cellData.getValue();
            if (comp instanceof Fonte) {
                return new SimpleStringProperty("0W (Fornece Energia)");
            } else if (comp instanceof Processador) {
                Processador p = (Processador) comp;
                return new SimpleStringProperty(p.getConsumoWatts() + "W");
            } else if (comp instanceof PlacaMae) {
                PlacaMae pm = (PlacaMae) comp;
                return new SimpleStringProperty(pm.getConsumoWatts() + "W");
            } else if (comp instanceof MemoriaRam) {
                MemoriaRam ram = (MemoriaRam) comp;
                return new SimpleStringProperty(ram.getConsumoWatts() + "W");
            }
            return new SimpleStringProperty("0W");
        });

        colunaImagem.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
        colunaImagem.setCellFactory(param -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            @Override
            protected void updateItem(Componente item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(descobrirImagemPeca(item));
                    imageView.setFitHeight(40);
                    imageView.setFitWidth(40);
                    imageView.setPreserveRatio(true);
                    setGraphic(imageView);
                }
            }
        });

        spinQuantidade.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1, 1));
        spinQuantidade.setDisable(true);

        tabelaComponentes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> exibirDetalhesProduto(newValue));

        carregarCatalogo();
        filtrarTodos();

        btnAdicionar.setOnAction(event -> {
            Componente selecionado = tabelaComponentes.getSelectionModel().getSelectedItem();
            if (selecionado != null) {
                int qtdDesejada = spinQuantidade.getValue();

                // ALTERADO PARA UTILIAR .getEstoque()
                int estoqueAtual = selecionado.getEstoque();

                if (qtdDesejada <= estoqueAtual) {
                    carrinhoService.adicionarItem(selecionado, qtdDesejada);
                    // ALTERADO PARA UTILIAR .setEstoque()
                    selecionado.setEstoque(estoqueAtual - qtdDesejada);
                    componenteService.atualizar(selecionado);
                    exibirDetalhesProduto(selecionado);

                    alert("✅ " + qtdDesejada + " unidade(s) de \"" + selecionado.getNome() + "\" adicionada(s) ao carrinho!");
                } else {
                    alert("⚠️ Quantidade indisponível! Estoque máximo: " + estoqueAtual);
                }
            } else {
                alert("⚠️ Por favor, selecione um produto na tabela primeiro.");
            }
        });
    }

    private void exibirDetalhesProduto(Componente comp) {
        if (comp == null) {
            lblDetalheNome.setText("Selecione um item...");
            lblDetalheMarca.setText("Marca: -");
            lblDetalhePreco.setText("Preço: R$ 0.00");
            lblDetalheConsumo.setText("Consumo: -");
            lblDetalheEstoque.setText("Disponível em Estoque: -");
            imgDetalheGrande.setImage(null);
            spinQuantidade.setDisable(true);
            return;
        }

        // ALTERADO PARA UTILIAR .getEstoque()
        int estoqueDisponivel = comp.getEstoque();

        lblDetalheNome.setText(comp.getNome());
        lblDetalheMarca.setText("Marca: " + comp.getMarca());
        lblDetalhePreco.setText(String.format("Preço: R$ %.2f", comp.getPreco()));
        lblDetalheEstoque.setText("Disponível em Estoque: " + estoqueDisponivel + " un.");

        if (comp instanceof Fonte) {
            lblDetalheConsumo.setText("Fornece energia do sistema.");
        } else if (comp instanceof Processador) {
            lblDetalheConsumo.setText("Consumo: " + ((Processador) comp).getConsumoWatts() + "W");
        } else if (comp instanceof PlacaMae) {
            lblDetalheConsumo.setText("Consumo: " + ((PlacaMae) comp).getConsumoWatts() + "W");
        } else if (comp instanceof MemoriaRam) {
            lblDetalheConsumo.setText("Consumo: " + ((MemoriaRam) comp).getConsumoWatts() + "W");
        }

        imgDetalheGrande.setImage(descobrirImagemPeca(comp));

        if (estoqueDisponivel > 0) {
            spinQuantidade.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, estoqueDisponivel, 1));
            spinQuantidade.setDisable(false);
        } else {
            spinQuantidade.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0));
            spinQuantidade.setDisable(true);
            lblDetalheEstoque.setText("⚠️ PRODUTO ESGOTADO NO ESTOQUE!");
        }
    }

    private Image descobrirImagemPeca(Componente item) {
        String nomeUpper = item.getNome().toUpperCase();
        if (nomeUpper.contains("RTX") || nomeUpper.contains("RX ") || nomeUpper.contains("GTX")) {
            return imgGpu;
        } else if (item instanceof Fonte) {
            return imgFonte;
        } else if (item instanceof MemoriaRam) {
            return imgRam;
        } else {
            return imgCpu;
        }
    }

    private void carregarImagensSystem() {
        try {
            imgFonte = new Image(getClass().getResourceAsStream("/imagens/fonte.png"));
            imgRam = new Image(getClass().getResourceAsStream("/imagens/memoriaram.png"));
            imgGpu = new Image(getClass().getResourceAsStream("/imagens/placa de video.png"));
            imgCpu = new Image(getClass().getResourceAsStream("/imagens/Processador.jpg"));
        } catch (Exception e) {
            System.out.println("Aviso: Arquivos de imagem não encontrados em resources/imagens/.");
        }
    }
    private ComponenteService componenteService = ComponenteService.getInstance();
    private void carregarCatalogo() {
        todasPecas = componenteService.listar();
        }

    @FXML private void filtrarTodos() { tabelaComponentes.setItems(FXCollections.observableArrayList(todasPecas)); }
    @FXML private void filtrarProcessadores() { List<Componente> f = new ArrayList<>(); for (Componente c : todasPecas) { if (c instanceof Processador && !c.getNome().toUpperCase().contains("RTX") && !c.getNome().toUpperCase().contains("RX ")) f.add(c); } tabelaComponentes.setItems(FXCollections.observableArrayList(f)); }
    @FXML private void filtrarPlacasMae() { List<Componente> f = new ArrayList<>(); for (Componente c : todasPecas) { if (c instanceof PlacaMae) f.add(c); } tabelaComponentes.setItems(FXCollections.observableArrayList(f)); }
    @FXML private void filtrarMemorias() { List<Componente> f = new ArrayList<>(); for (Componente c : todasPecas) { if (c instanceof MemoriaRam) f.add(c); } tabelaComponentes.setItems(FXCollections.observableArrayList(f)); }
    @FXML private void filtrarPlacasDeVideo() { List<Componente> f = new ArrayList<>(); for (Componente c : todasPecas) { String n = c.getNome().toUpperCase(); if (n.contains("RTX") || n.contains("RX ") || n.contains("GTX")) f.add(c); } tabelaComponentes.setItems(FXCollections.observableArrayList(f)); }
    @FXML private void filtrarFontes() { List<Componente> f = new ArrayList<>(); for (Componente c : todasPecas) { if (c instanceof Fonte) f.add(c); } tabelaComponentes.setItems(FXCollections.observableArrayList(f)); }

    @FXML private void voltarHome(ActionEvent event) { NavegacaoController.trocarTela("/home.fxml", event, usuarioLogado); }
    @FXML private void enviarParaBuild(ActionEvent event) { Componente s = tabelaComponentes.getSelectionModel().getSelectedItem(); if (s != null) buildService.adicionarComponenteParaMontagem(s); NavegacaoController.trocarTela("/builds-view.fxml", event, usuarioLogado); }
    @FXML private void irParaCarrinho(ActionEvent event) {NavegacaoController.trocarTela("/Carrinho.fxml", event, usuarioLogado);}

    private void alert(String message) { Alert a = new Alert(Alert.AlertType.INFORMATION); a.setHeaderText(null); a.setContentText(message); a.showAndWait(); }
    @FXML
    private void irParaAreaCliente(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cliente-view.fxml"));
            Parent rootCliente = loader.load();

            // Pega o controller que acabamos de ajustar acima
            ClienteController clienteController = loader.getController();

            // Passa o usuário de volta para ele. Isso vai disparar o 'carregarTela("hello-view.fxml")'
            // automaticamente lá de dentro, montando o centro do layout!
            clienteController.setUsuario(this.usuarioLogado);
            clienteController.atualizarTela();

            // Aplica a cena restaurada perfeitamente no tamanho original do seu sistema
            Scene cenaAreaCliente = new Scene(rootCliente, 1200.0, 800.0);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(cenaAreaCliente);
            window.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}