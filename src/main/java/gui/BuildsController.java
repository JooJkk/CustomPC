package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Cliente;
import model.componentes.*;
import negocio.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BuildsController {
    private Cliente usuarioLogado;
    public void setUsuario(Cliente usuario) {
        this.usuarioLogado = usuario;
    }

    @FXML private ComboBox<Processador> comboProcessador;
    @FXML private ComboBox<PlacaMae> comboPlacaMae;
    @FXML private ComboBox<MemoriaRam> comboRam;
    @FXML private ComboBox<PlacaVideo> comboPlacaVideo;
    @FXML private ComboBox<Fonte> comboFonte;
    @FXML private Label lblResultado;
// infomação generica para arrumar commit
    private BuildService buildService = BuildService.getInstance();
    private CarrinhoService carrinhoService = CarrinhoService.getInstance();
    private ComponenteService componenteService = ComponenteService.getInstance();

    private List<Processador> todosProcessadores = new ArrayList<>();
    private List<PlacaMae> todasPlacasMae = new ArrayList<>();
    private List<MemoriaRam> todasMemorias = new ArrayList<>();
    private List<PlacaVideo> todasPlacasVideo = new ArrayList<>();
    private List<Fonte> todasFontes = new ArrayList<>();

    @FXML
    public void initialize() {
        carregarPecas();

        configurarCombo(comboProcessador);
        configurarCombo(comboPlacaMae);
        configurarCombo(comboRam);
        configurarCombo(comboPlacaVideo);
        configurarCombo(comboFonte);

        comboProcessador.getItems().addAll(todosProcessadores);
        comboPlacaMae.setDisable(true);
        comboRam.setDisable(true);
        comboPlacaVideo.setDisable(true);
        comboFonte.setDisable(true);

        comboProcessador.setOnAction(e -> filtrarEExibirPlacasMae());
        comboPlacaMae.setOnAction(e -> filtrarEExibirMemoriasRam());
        comboRam.setOnAction(e -> filtrarEExibirPlacasVideo());
        comboPlacaVideo.setOnAction(e -> filtrarEExibirFontes());

        processarComponentesDoCatalogo();
    }

    private void filtrarEExibirPlacasMae() {
        Processador cpuSelecionado = comboProcessador.getValue();

        comboPlacaMae.setValue(null);
        comboPlacaMae.getItems().clear();
        comboRam.setValue(null);
        comboRam.getItems().clear();
        comboRam.setDisable(true);
        comboPlacaVideo.setValue(null);
        comboPlacaVideo.getItems().clear();
        comboPlacaVideo.setDisable(true);
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
        comboPlacaVideo.setValue(null);
        comboPlacaVideo.getItems().clear();
        comboPlacaVideo.setDisable(true);
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

    private void filtrarEExibirPlacasVideo() {
        MemoriaRam ramSelecionada = comboRam.getValue();

        comboPlacaVideo.setValue(null);
        comboPlacaVideo.getItems().clear();
        comboFonte.setValue(null);
        comboFonte.getItems().clear();
        comboFonte.setDisable(true);

        if (ramSelecionada != null) {
            comboPlacaVideo.getItems().addAll(todasPlacasVideo);
            comboPlacaVideo.setDisable(false);
            lblResultado.setText("⚡ Memória selecionada. Escolha a sua Placa de Vídeo.");
            lblResultado.setStyle("-fx-text-fill: #2ecc71;");
        } else {
            comboPlacaVideo.setDisable(true);
        }
    }

    private void filtrarEExibirFontes() {
        Processador cpu = comboProcessador.getValue();
        PlacaMae mobo = comboPlacaMae.getValue();
        MemoriaRam ram = comboRam.getValue();
        PlacaVideo gpu = comboPlacaVideo.getValue();

        comboFonte.setValue(null);
        comboFonte.getItems().clear();

        if (cpu != null && mobo != null && ram != null && gpu != null) {
            // SOMA ENERGÉTICA COMPLETA DE TODOS OS HARDWARES SELECIONADOS NA CASCATA
            int consumoWatts = cpu.getConsumoWatts() + mobo.getConsumoWatts() + ram.getConsumoWatts() + gpu.getConsumoWatts();
            double consumoRecomendado = consumoWatts * 1.2;

            for (Fonte f : todasFontes) {
                if (f.getPotenciaWatts() >= consumoRecomendado) {
                    comboFonte.getItems().add(f);
                }
            }
            comboFonte.setDisable(false);
            lblResultado.setText("⚡ Fontes filtradas. Consumo total (com GPU): " + consumoWatts + "W (Recomendado: " + (int)consumoRecomendado + "W).");
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
    public void adicionarEIrParaCarrinho(ActionEvent event) {
        Processador cpu = comboProcessador.getValue();
        PlacaMae mobo = comboPlacaMae.getValue();
        MemoriaRam ram = comboRam.getValue();
        PlacaVideo gpu = comboPlacaVideo.getValue();
        Fonte fonte = comboFonte.getValue();

        if (cpu == null || mobo == null || ram == null || gpu == null || fonte == null) {
            lblResultado.setText("⚠️ Complete todos os passos da build antes de enviar ao carrinho.");
            lblResultado.setStyle("-fx-text-fill: #ffa500;");
            return;
        }

        adicionarComEstoque(cpu);
        adicionarComEstoque(mobo);
        adicionarComEstoque(ram);
        adicionarComEstoque(gpu);
        adicionarComEstoque(fonte);

        exibirProgressoCupom(event);
        NavegacaoController.trocarTela("/Carrinho.fxml", event, usuarioLogado);
    }

    private void exibirProgressoCupom(ActionEvent event) {
        String mensagem = CupomService.getInstance().verificarProgressoDesconto(carrinhoService.getCarrinho().getItens());
        if (mensagem == null) {
            NavegacaoController.trocarTela("/Carrinho.fxml", event, usuarioLogado);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quase lá!");
        alert.setHeaderText("🎁 Você está perto de um desconto");
        alert.setContentText(mensagem);

        ButtonType continuarComprando = new ButtonType("Continuar comprando");
        ButtonType irCarrinho = new ButtonType("Ir para o carrinho");

        alert.getButtonTypes().setAll(continuarComprando, irCarrinho);
        Optional<ButtonType> resultado = alert.showAndWait();

        if (resultado.isPresent()) {
            if (resultado.get() == irCarrinho) {
                NavegacaoController.trocarTela("/Carrinho.fxml", event, usuarioLogado);
            } else if (resultado.get() == continuarComprando) {
                voltarCatalogo(event);
            }
        }
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
        comboPlacaVideo.setValue(null);
        comboFonte.setValue(null);

        comboPlacaMae.getItems().clear();
        comboRam.getItems().clear();
        comboPlacaVideo.getItems().clear();
        comboFonte.getItems().clear();

        comboPlacaMae.setDisable(true);
        comboRam.setDisable(true);
        comboPlacaVideo.setDisable(true);
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
        if (compartilhados == null || compartilhados.isEmpty()) return;

        Processador cpuParaSetar = null;
        PlacaMae moboParaSetar = null;
        MemoriaRam ramParaSetar = null;
        PlacaVideo gpuParaSetar = null;
        Fonte fonteParaSetar = null;

        for (Componente comp : compartilhados) {
            if (comp == null || comp.getEstoque() <= 0) continue;
            if (comp instanceof Processador)  cpuParaSetar   = (Processador) comp;
            else if (comp instanceof PlacaMae)     moboParaSetar  = (PlacaMae) comp;
            else if (comp instanceof MemoriaRam)   ramParaSetar   = (MemoriaRam) comp;
            else if (comp instanceof PlacaVideo)   gpuParaSetar   = (PlacaVideo) comp;
            else if (comp instanceof Fonte)        fonteParaSetar = (Fonte) comp;
        }

        // Respeita a cascata: cada setValue dispara o listener que filtra o próximo combo
        if (cpuParaSetar != null) {
            comboProcessador.setValue(cpuParaSetar);
            filtrarEExibirPlacasMae(); // dispara manualmente pois setValue não aciona setOnAction
        }
        if (moboParaSetar != null) {
            comboPlacaMae.setValue(moboParaSetar);
            filtrarEExibirMemoriasRam();
        }
        if (ramParaSetar != null) {
            comboRam.setValue(ramParaSetar);
            filtrarEExibirPlacasVideo();
        }
        if (gpuParaSetar != null) {
            comboPlacaVideo.setValue(gpuParaSetar);
            filtrarEExibirFontes();
        }
        if (fonteParaSetar != null) {
            comboFonte.setValue(fonteParaSetar);
        }
    }

    private void carregarPecas() {
        todosProcessadores.clear();
        todasPlacasMae.clear();
        todasMemorias.clear();
        todasPlacasVideo.clear();
        todasFontes.clear();
        for (Componente c : componenteService.listar()) {
            if (c == null || c.getEstoque() <= 0) {
                continue;
            }
            if (c instanceof Processador) {
                todosProcessadores.add((Processador) c);
            } else if (c instanceof PlacaMae) {
                todasPlacasMae.add((PlacaMae) c);
            } else if (c instanceof MemoriaRam) {
                todasMemorias.add((MemoriaRam) c);
            } else if (c instanceof Fonte) {
                todasFontes.add((Fonte) c);
            }
            else if (c instanceof PlacaVideo) {
                todasPlacasVideo.add((PlacaVideo) c);
            }
        }
        System.out.println("GPUs carregadas: " + todasPlacasVideo.size());
    }

    private void exibirErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}