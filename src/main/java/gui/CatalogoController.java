package gui;

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

        gerarBancoDeDadosCatalogo();
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

    private void gerarBancoDeDadosCatalogo() {
        todasPecas.add(new Processador("Ryzen 5 5600", "AMD", 1000.0, 0.5, 0.3, 10, 65, "AM4", 65));
        todasPecas.add(new Processador("Intel i7-12700K", "Intel", 2100.0, 0.6, 0.4, 5, 125, "LGA1700", 125));
        todasPecas.add(new Processador("Ryzen 9 7950X", "AMD", 3500.0, 0.5, 0.5, 3, 170, "AM5", 170));
        todasPecas.add(new Processador("Core i5-13600K", "Intel", 1900.0, 0.6, 0.4, 8, 125, "LGA1700", 125));
        todasPecas.add(new Processador("Core i3-12100F", "Intel", 650.0, 0.4, 0.3, 20, 58, "LGA1700", 60));

        todasPecas.add(new PlacaMae("B550M Aorus Elite", "Gigabyte", 950.0, 1.0, 0.6, 8, 30, "AM4", 4, "DDR4", "Micro-ATX"));
        todasPecas.add(new PlacaMae("X670E ASUS ROG", "ASUS", 2800.0, 1.2, 0.8, 4, 40, "AM5", 4, "DDR5", "ATX"));
        todasPecas.add(new PlacaMae("H610M-E", "MSI", 620.0, 0.8, 0.5, 12, 25, "LGA1700", 2, "DDR4", "Micro-ATX"));
        todasPecas.add(new PlacaMae("A520M-K", "ASUS", 480.0, 0.7, 0.4, 15, 20, "AM4", 2, "DDR4", "Micro-ATX"));
        todasPecas.add(new PlacaMae("Z790 MSI Pro", "MSI", 2100.0, 1.1, 0.7, 6, 45, "LGA1700", 4, "DDR5", "ATX"));

        todasPecas.add(new MemoriaRam("16GB Corsair Vengeance", "Corsair", 380.0, 0.1, 0.1, 30, 5, "DDR4", 16, 3200));
        todasPecas.add(new MemoriaRam("32GB G.Skill Trident", "G.Skill", 1250.0, 0.2, 0.15, 10, 10, "DDR5", 32, 6000));
        todasPecas.add(new MemoriaRam("8GB Kingston Fury", "Kingston", 210.0, 0.1, 0.08, 50, 5, "DDR4", 8, 2666));
        todasPecas.add(new MemoriaRam("16GB XPG Spectrix", "XPG", 440.0, 0.1, 0.1, 25, 8, "DDR4", 16, 3600));

        todasPecas.add(new Fonte("650W Corsair CV", "Corsair", 460.0, 2.0, 1.5, 15, 0, 650, "80 Plus Bronze"));
        todasPecas.add(new Fonte("850W EVGA SuperNova", "EVGA", 850.0, 2.5, 2.0, 10, 0, 850, "80 Plus Gold"));
        todasPecas.add(new Fonte("500W Redragon RGPS", "Redragon", 290.0, 1.5, 1.2, 22, 0, 500, "80 Plus Bronze"));
        todasPecas.add(new Fonte("1000W Seasonic Prime", "Seasonic", 1600.0, 3.0, 2.5, 5, 0, 1000, "80 Plus Platinum"));

        todasPecas.add(new Processador("RTX 4060 Ti", "NVIDIA", 2600.0, 1.2, 0.9, 7, 160, "PCIe", 160));
        todasPecas.add(new Processador("RX 6750 XT", "AMD", 2300.0, 1.3, 0.95, 5, 250, "PCIe", 250));
        todasPecas.add(new Processador("RTX 3060", "MSI", 1850.0, 1.1, 0.8, 12, 170, "PCIe", 170));
    }

    @FXML private void filtrarTodos() { tabelaComponentes.setItems(FXCollections.observableArrayList(todasPecas)); }
    @FXML private void filtrarProcessadores() { List<Componente> f = new ArrayList<>(); for (Componente c : todasPecas) { if (c instanceof Processador && !c.getNome().toUpperCase().contains("RTX") && !c.getNome().toUpperCase().contains("RX ")) f.add(c); } tabelaComponentes.setItems(FXCollections.observableArrayList(f)); }
    @FXML private void filtrarPlacasMae() { List<Componente> f = new ArrayList<>(); for (Componente c : todasPecas) { if (c instanceof PlacaMae) f.add(c); } tabelaComponentes.setItems(FXCollections.observableArrayList(f)); }
    @FXML private void filtrarMemorias() { List<Componente> f = new ArrayList<>(); for (Componente c : todasPecas) { if (c instanceof MemoriaRam) f.add(c); } tabelaComponentes.setItems(FXCollections.observableArrayList(f)); }
    @FXML private void filtrarPlacasDeVideo() { List<Componente> f = new ArrayList<>(); for (Componente c : todasPecas) { String n = c.getNome().toUpperCase(); if (n.contains("RTX") || n.contains("RX ") || n.contains("GTX")) f.add(c); } tabelaComponentes.setItems(FXCollections.observableArrayList(f)); }
    @FXML private void filtrarFontes() { List<Componente> f = new ArrayList<>(); for (Componente c : todasPecas) { if (c instanceof Fonte) f.add(c); } tabelaComponentes.setItems(FXCollections.observableArrayList(f)); }

    @FXML private void voltarHome(ActionEvent event) { trocarTela("/home.fxml", event); }
    @FXML private void enviarParaBuild(ActionEvent event) { Componente s = tabelaComponentes.getSelectionModel().getSelectedItem(); if (s != null) buildService.adicionarComponenteParaMontagem(s); trocarTela("/builds-view.fxml", event); }
    @FXML private void irParaCarrinho(ActionEvent event) { try { FXMLLoader l = new FXMLLoader(getClass().getResource("/Carrinho.fxml")); Parent r = l.load(); CarrinhoController c = l.getController(); c.setUsuario(usuarioLogado); Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow(); s.setScene(new Scene(r)); s.show(); } catch (IOException e) { e.printStackTrace(); alert("Erro ao abrir a tela de carrinho."); } }

    private void trocarTela(String fxml, ActionEvent event) { try { FXMLLoader l = new FXMLLoader(getClass().getResource(fxml)); Parent r = l.load(); Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow(); s.setScene(new Scene(r)); s.show(); } catch (IOException e) { e.printStackTrace(); alert("Erro na navegação: Não foi possível carregar " + fxml); } }
    private void alert(String message) { Alert a = new Alert(Alert.AlertType.INFORMATION); a.setHeaderText(null); a.setContentText(message); a.showAndWait(); }
}