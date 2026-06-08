package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Cliente;
import model.componentes.Build;
import model.componentes.Componente;
import model.componentes.Fonte;
import model.componentes.MemoriaRam;
import model.componentes.PlacaMae;
import model.componentes.Processador;
import negocio.BuildService;
import negocio.CarrinhoService;
import negocio.CompatibilidadeService;
import exception.BuildIncompativelException;
import negocio.ComponenteService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BuildsController {
    private Cliente usuarioLogado;
    public void setUsuario(Cliente usuario) {
        this.usuarioLogado = usuario;
    }

    @FXML private ComboBox<Processador> comboProcessador;
    @FXML private ComboBox<PlacaMae> comboPlacaMae;
    @FXML private ComboBox<MemoriaRam> comboRam;
    @FXML private ComboBox<Fonte> comboFonte;
    @FXML private Label lblResultado;

    private CompatibilidadeService compatibilidadeService = new CompatibilidadeService();
    private BuildService buildService = BuildService.getInstance();
    private CarrinhoService carrinhoService = CarrinhoService.getInstance();

    private List<Processador> todosProcessadores = new ArrayList<>();
    private List<PlacaMae> todasPlacasMae = new ArrayList<>();
    private List<MemoriaRam> todasMemorias = new ArrayList<>();
    private List<Fonte> todasFontes = new ArrayList<>();

    @FXML
    public void initialize() {
        carregarPecas();

        configurarCombo(comboProcessador);
        configurarCombo(comboPlacaMae);
        configurarCombo(comboRam);
        configurarCombo(comboFonte);

        comboProcessador.getItems().addAll(todosProcessadores);
        comboPlacaMae.setDisable(true);
        comboRam.setDisable(true);
        comboFonte.setDisable(true);

        comboProcessador.setOnAction(e -> filtrarEExibirPlacasMae());
        comboPlacaMae.setOnAction(e -> filtrarEExibirMemoriasRam());
        comboRam.setOnAction(e -> filtrarEExibirFontes());

        processarComponentesDoCatalogo();
    }

    private void filtrarEExibirPlacasMae() {
        Processador cpuSelecionado = comboProcessador.getValue();

        comboPlacaMae.setValue(null);
        comboPlacaMae.getItems().clear();
        comboRam.setValue(null);
        comboRam.getItems().clear();
        comboRam.setDisable(true);
        comboFonte.setValue(null);
        comboFonte.getItems().clear();
        comboFonte.setDisable(true);

        if (cpuSelecionado != null) {
            for (PlacaMae mobo : todasPlacasMae) {
                if (mobo.getSocket().equals(cpuSelecionado.getSocket())) {
                    comboPlacaMae.getItems().add(mobo);
                }
            }
            comboPlacaMae.setDisable(false);
            lblResultado.setText("⚡ Mostrando placas-mãe compatíveis com o socket " + cpuSelecionado.getSocket());
            lblResultado.setStyle("-fx-text-fill: #2ecc71;");
        } else {
            comboPlacaMae.setDisable(true);
        }
    }

    private void filtrarEExibirMemoriasRam() {
        PlacaMae moboSelecionada = comboPlacaMae.getValue();

        comboRam.setValue(null);
        comboRam.getItems().clear();
        comboFonte.setValue(null);
        comboFonte.getItems().clear();
        comboFonte.setDisable(true);

        if (moboSelecionada != null) {
            for (MemoriaRam ram : todasMemorias) {
                if (ram.getTipoRam().equals(moboSelecionada.getTipoRamSuportada())) {
                    comboRam.getItems().add(ram);
                }
            }
            comboRam.setDisable(false);
            lblResultado.setText("⚡ Mostrando memórias compatíveis do tipo " + moboSelecionada.getTipoRamSuportada());
            lblResultado.setStyle("-fx-text-fill: #2ecc71;");
        } else {
            comboRam.setDisable(true);
        }
    }

    private void filtrarEExibirFontes() {
        Processador cpu = comboProcessador.getValue();
        PlacaMae mobo = comboPlacaMae.getValue();
        MemoriaRam ram = comboRam.getValue();

        comboFonte.setValue(null);
        comboFonte.getItems().clear();

        if (cpu != null && mobo != null && ram != null) {
            Build buildTemp = new Build();
            buildTemp.setProcessador(cpu);
            buildTemp.setPlacaMae(mobo);
            buildTemp.adicionarMemoria(ram);

            int consumoWatts = buildTemp.calcularConsumoTotal();
            double consumoRecomendado = consumoWatts * 1.2;

            for (Fonte f : todasFontes) {
                if (f.getPotenciaWatts() >= consumoRecomendado) {
                    comboFonte.getItems().add(f);
                }
            }
            comboFonte.setDisable(false);
            lblResultado.setText("⚡ Fontes filtradas. Consumo mínimo estimado: " + (int)consumoRecomendado + "W.");
            lblResultado.setStyle("-fx-text-fill: #2ecc71;");
        } else {
            comboFonte.setDisable(true);
        }
    }

    private <T extends Componente> void configurarCombo(ComboBox<T> combo) {
        combo.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome());
            }
        });
        combo.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNome());
            }
        });
    }

    @FXML
    public void validarBuild() {
        Processador cpu = comboProcessador.getValue();
        PlacaMae mobo = comboPlacaMae.getValue();
        MemoriaRam ram = comboRam.getValue();
        Fonte fonte = comboFonte.getValue();

        if (cpu == null || mobo == null || ram == null || fonte == null) {
            lblResultado.setText("⚠️ Selecione todos os componentes antes de validar.");
            lblResultado.setStyle("-fx-text-fill: #ffa500;");
            return;
        }

        Build novaBuild = new Build();
        novaBuild.setNome("Build Pronta");
        novaBuild.setProcessador(cpu);
        novaBuild.setPlacaMae(mobo);
        novaBuild.adicionarMemoria(ram);
        novaBuild.setFonte(fonte);

        try {
            compatibilidadeService.validarBuildCompleta(novaBuild);
            lblResultado.setText("✅ Sucesso! A build passou em 100% dos testes de compatibilidade.");
            lblResultado.setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");
        } catch (BuildIncompativelException e) {
            lblResultado.setText("❌ Erro: " + e.getMessage());
            lblResultado.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        }
    }

    @FXML
    public void adicionarEIrParaCarrinho(ActionEvent event) {
        Processador cpu = comboProcessador.getValue();
        PlacaMae mobo = comboPlacaMae.getValue();
        MemoriaRam ram = comboRam.getValue();
        Fonte fonte = comboFonte.getValue();

        if (cpu == null && mobo == null && ram == null && fonte == null) {
            lblResultado.setText("⚠️ Selecione pelo menos uma peça para mandar ao carrinho.");
            lblResultado.setStyle("-fx-text-fill: #ffa500;");
            return;
        }

        adicionarComEstoque(cpu);
        adicionarComEstoque(mobo);
        adicionarComEstoque(ram);
        adicionarComEstoque(fonte);

        NavegacaoController.trocarTela("/Carrinho.fxml", event, usuarioLogado);
    }
    private void adicionarComEstoque(Componente comp) {
        if (comp == null) return;
        if (comp.getEstoque() <= 0) {
            lblResultado.setText("⚠️ " + comp.getNome() + " está sem estoque!");
            return;
        }
        carrinhoService.adicionarItem(comp, 1);
        comp.setEstoque(comp.getEstoque() - 1);
        componenteService.atualizar(comp);
    }
    @FXML
    public void limparBancada() {
        buildService.limparComponentesMontagem();

        comboProcessador.setValue(null);
        comboPlacaMae.setValue(null);
        comboRam.setValue(null);
        comboFonte.setValue(null);

        comboPlacaMae.getItems().clear();
        comboRam.getItems().clear();
        comboFonte.getItems().clear();

        comboPlacaMae.setDisable(true);
        comboRam.setDisable(true);
        comboFonte.setDisable(true);

        lblResultado.setText("Bancada limpa. Escolha um Processador para reiniciar.");
        lblResultado.setStyle("-fx-text-fill: #b0b0b0;");
    }

    @FXML
    public void voltarHome(ActionEvent event) {
        NavegacaoController.trocarTela("/Home.fxml", event, usuarioLogado);
    }

    @FXML
    public void voltarCatalogo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/catalogo-view.fxml"));
            Parent root = loader.load();
            CatalogoController controller = loader.getController();
            controller.setUsuario(usuarioLogado);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            exibirErro("Erro ao voltar para o Catálogo: " + e.getMessage());
        }
    }

    private void processarComponentesDoCatalogo() {
        List<Componente> compartilhados = buildService.getComponentesSelecionadosParaMontagem();
        for (Componente comp : compartilhados) {
            if (comp instanceof Processador) {
                if (!comp.getNome().toUpperCase().contains("RTX") && !comp.getNome().toUpperCase().contains("RX ")) {
                    Processador p = (Processador) comp;
                    comboProcessador.setValue(p);
                    filtrarEExibirPlacasMae();
                }
            } else if (comp instanceof PlacaMae) {
                PlacaMae pm = (PlacaMae) comp;
                comboPlacaMae.setValue(pm);
                filtrarEExibirMemoriasRam();
            } else if (comp instanceof MemoriaRam) {
                MemoriaRam ram = (MemoriaRam) comp;
                comboRam.setValue(ram);
                filtrarEExibirFontes();
            } else if (comp instanceof Fonte) {
                Fonte f = (Fonte) comp;
                comboFonte.setValue(f);
            }
        }
    }
    private ComponenteService componenteService = ComponenteService.getInstance();
    private void carregarPecas() {
        for (Componente c : componenteService.listar()) {
            if (c instanceof Processador) todosProcessadores.add((Processador) c);
            else if (c instanceof PlacaMae) todasPlacasMae.add((PlacaMae) c);
            else if (c instanceof MemoriaRam) todasMemorias.add((MemoriaRam) c);
            else if (c instanceof Fonte) todasFontes.add((Fonte) c);
        }
    }

    private void exibirErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}