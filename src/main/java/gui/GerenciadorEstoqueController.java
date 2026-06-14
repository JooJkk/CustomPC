package gui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Cliente;
import model.componentes.Componente;
import model.componentes.*;
import negocio.CarrinhoService;
import negocio.ComponenteService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.scene.control.Alert;
import dados.RepositorioComponente;


public class GerenciadorEstoqueController {
    private Cliente usuarioLogado;
    public void setUsuario(Cliente usuario) {
        this.usuarioLogado = usuario;
    }
    private CarrinhoService carrinhoService = CarrinhoService.getInstance();
    private List<Componente> todasPecas = new ArrayList<>();
    private ComponenteService componenteService = ComponenteService.getInstance();
    private void carregarCatalogo() {
        todasPecas = componenteService.listar();
    }
    private RepositorioComponente repositorio = new RepositorioComponente();

    @FXML
    private TableView<Componente> tabelaComponentes;
    @FXML
    private TableColumn<Componente, Componente> colunaImagem;
    @FXML
    private TableColumn<Componente, String> colunaNome;
    @FXML
    private TableColumn<Componente, String> colunaMarca;
    @FXML
    private TableColumn<Componente, Double> colunaPreco;
    @FXML
    private TableColumn<Componente, String> colunaConsumo;

    @FXML
    private ImageView imgDetalheGrande;
    @FXML
    private Label lblDetalheNome;
    @FXML
    private Label lblDetalheMarca;
    @FXML
    private Label lblDetalhePreco;
    @FXML
    private Label lblDetalheConsumo;
    @FXML
    private Label lblDetalheEstoque;
    @FXML
    private TextField txtEstoqueNovo;

    @FXML
    public void onBtnVoltar(ActionEvent event){
        NavegacaoController.trocarTela("/AdminView.fxml", event, usuarioLogado);
    }

    @FXML
    public void onBtnAdicionarNovo(ActionEvent event){
        NavegacaoController.trocarTela("/NewComponente.fxml", event, usuarioLogado);
    }

    @FXML
    public void onBtnAtualizarEstoque(ActionEvent event){
        Componente selecionado = tabelaComponentes.getSelectionModel().getSelectedItem();
        if(selecionado != null){
            try {
                int qntNova = Integer.parseInt(txtEstoqueNovo.getText());
                selecionado.setEstoque(qntNova);
                componenteService.atualizar(selecionado);
                carregarCatalogo();
                exibirDetalhesProduto(selecionado);
                alert("Estoque atualizado com sucesso!");
            } catch (NumberFormatException e) {
                alert("⚠️ Por favor, digite um número.");
            }
        }

    }
    @FXML
    public void onBtnExcluir(ActionEvent event){
        Componente selecionado = tabelaComponentes.getSelectionModel().getSelectedItem();
        if(selecionado != null){
            try {
                ButtonType btnConfirmar = new ButtonType("Excluir do estoque");
                ButtonType btnCancelar = new ButtonType("Cancelar");

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Exclusão");
                alert.setHeaderText("Você selecionou: " + selecionado.getNome());
                alert.setContentText("Continuar com a exclusão?");

                alert.getButtonTypes().setAll(btnConfirmar, btnCancelar);

                Optional<ButtonType> resultado = alert.showAndWait();
                if (resultado.isPresent() && resultado.get() == btnConfirmar) {
                    long idComp = selecionado.getId();

                    // 1. Usa o service para remover (Boa prática arquitetural)
                    componenteService.remover(idComp);
                    alert("Componente removido com sucesso!");
                    // 2. Limpa os detalhes da tela já que o item não existe mais
                    exibirDetalhesProduto(null);
                    // 3. Atualiza a lista interna 'todasPecas' buscando do service
                    carregarCatalogo();
                    // 4. Atualiza a TableView com a lista nova
                    filtrarTodos();
                }
            } catch (Exception e) {
                alert("⚠️ Erro ao remover o componente: " + e.getMessage());
            }
        }
    }

    @FXML
    private void filtrarTodos() { tabelaComponentes.setItems(FXCollections.observableArrayList(todasPecas)); }
    @FXML
    private void filtrarProcessadores() { List<Componente> f = new ArrayList<>(); for (Componente c : todasPecas) {
        if (c instanceof Processador && !c.getNome().toUpperCase().contains("RTX") && !c.getNome().toUpperCase().contains("RX ")) f.add(c); } tabelaComponentes.setItems(FXCollections.observableArrayList(f)); }
    @FXML
    private void filtrarPlacasMae() { List<Componente> f = new ArrayList<>(); for (Componente c : todasPecas) {
        if (c instanceof PlacaMae) f.add(c); } tabelaComponentes.setItems(FXCollections.observableArrayList(f)); }
    @FXML
    private void filtrarMemorias() { List<Componente> f = new ArrayList<>(); for (Componente c : todasPecas) {
        if (c instanceof MemoriaRam) f.add(c); } tabelaComponentes.setItems(FXCollections.observableArrayList(f)); }
    @FXML
    private void filtrarPlacasDeVideo() { List<Componente> f = new ArrayList<>(); for (Componente c : todasPecas) { String n = c.getNome().toUpperCase();
        if (n.contains("RTX") || n.contains("RX ") || n.contains("GTX")) f.add(c); } tabelaComponentes.setItems(FXCollections.observableArrayList(f)); }
    @FXML
    private void filtrarFontes() { List<Componente> f = new ArrayList<>(); for (Componente c : todasPecas) {
        if (c instanceof Fonte) f.add(c); } tabelaComponentes.setItems(FXCollections.observableArrayList(f)); }


    private Image imgFonte;
    private Image imgRam;
    private Image imgGpu;
    private Image imgCpu;
    private Image imgGabinete;

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


        tabelaComponentes.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> exibirDetalhesProduto(newValue));

        carregarCatalogo();
        filtrarTodos();
    }

    private Image descobrirImagemPeca(Componente item) {
        String nomeUpper = item.getNome().toUpperCase();
        if (nomeUpper.contains("RTX") || nomeUpper.contains("RX ") || nomeUpper.contains("GTX")) {
            return imgGpu;
        } else if (item instanceof Fonte) {
            return imgFonte;
        } else if (item instanceof MemoriaRam) {
            return imgRam;
        }
        else if (item instanceof Gabinete) {
            return imgGabinete;
        }
        else {
            return imgCpu;
        }
    }

    private void carregarImagensSystem() {
        try {
            imgFonte = new Image(getClass().getResourceAsStream("/imagens/fonte.png"));
            imgRam = new Image(getClass().getResourceAsStream("/imagens/memoriaram.png"));
            imgGpu = new Image(getClass().getResourceAsStream("/imagens/placa de video.png"));
            imgCpu = new Image(getClass().getResourceAsStream("/imagens/Processador.jpg"));
            imgGabinete = new Image(getClass().getResourceAsStream("/imagens/gabinete.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exibirDetalhesProduto(Componente comp) {
        if (comp == null) {
            lblDetalheNome.setText("Selecione um item...");
            lblDetalheMarca.setText("Marca: -");
            lblDetalhePreco.setText("Preço: R$ 0.00");
            lblDetalheConsumo.setText("Consumo: -");
            lblDetalheEstoque.setText("Disponível em Estoque: -");
            imgDetalheGrande.setImage(null);
            txtEstoqueNovo.setDisable(true);
            return;
        }

        // ALTERADO PARA UTILIAR .getEstoque()
        int estoqueDisponivel = comp.getEstoque();
        txtEstoqueNovo.setDisable(false);

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
    }
    private void alert(String message) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setHeaderText(null);
        alerta.setContentText(message);
        alerta.showAndWait(); }

}
