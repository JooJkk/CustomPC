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
        inicializarBancoDeDadosPecas();

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

        if (cpu != null) carrinhoService.adicionarItem(cpu, 1);
        if (mobo != null) carrinhoService.adicionarItem(mobo, 1);
        if (ram != null) carrinhoService.adicionarItem(ram, 1);
        if (fonte != null) carrinhoService.adicionarItem(fonte, 1);

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
            exibirErro("Erro ao abrir a tela de carrinho: " + e.getMessage());
        }
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home.fxml"));
            Parent root = loader.load();
            HomeController controller = loader.getController();
            controller.setUsuario(usuarioLogado);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            exibirErro("Erro ao ir para a Home: " + e.getMessage());
        }
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

    private void inicializarBancoDeDadosPecas() {
        todosProcessadores.add(new Processador("Ryzen 5 5600", "AMD", 1000.0, 0.5, 0.3, 10, 65, "AM4", 65));
        todosProcessadores.add(new Processador("Intel i7-12700K", "Intel", 2100.0, 0.6, 0.4, 5, 125, "LGA1700", 125));
        todosProcessadores.add(new Processador("Ryzen 9 7950X", "AMD", 3500.0, 0.5, 0.5, 3, 170, "AM5", 170));
        todosProcessadores.add(new Processador("Core i5-13600K", "Intel", 1900.0, 0.6, 0.4, 8, 125, "LGA1700", 125));
        todosProcessadores.add(new Processador("Core i3-12100F", "Intel", 650.0, 0.4, 0.3, 20, 58, "LGA1700", 60));

        todasPlacasMae.add(new PlacaMae("B550M Aorus Elite", "Gigabyte", 950.0, 1.0, 0.6, 8, 30, "AM4", 4, "DDR4", "Micro-ATX"));
        todasPlacasMae.add(new PlacaMae("X670E ASUS ROG", "ASUS", 2800.0, 1.2, 0.8, 4, 40, "AM5", 4, "DDR5", "ATX"));
        todasPlacasMae.add(new PlacaMae("H610M-E", "MSI", 620.0, 0.8, 0.5, 12, 25, "LGA1700", 2, "DDR4", "Micro-ATX"));
        todasPlacasMae.add(new PlacaMae("A520M-K", "ASUS", 480.0, 0.7, 0.4, 15, 20, "AM4", 2, "DDR4", "Micro-ATX"));
        todasPlacasMae.add(new PlacaMae("Z790 MSI Pro", "MSI", 2100.0, 1.1, 0.7, 6, 45, "LGA1700", 4, "DDR5", "ATX"));

        todasMemorias.add(new MemoriaRam("16GB Corsair Vengeance", "Corsair", 380.0, 0.1, 0.1, 30, 5, "DDR4", 16, 3200));
        todasMemorias.add(new MemoriaRam("32GB G.Skill Trident", "G.Skill", 1250.0, 0.2, 0.15, 10, 10, "DDR5", 32, 6000));
        todasMemorias.add(new MemoriaRam("8GB Kingston Fury", "Kingston", 210.0, 0.1, 0.08, 50, 5, "DDR4", 8, 2666));
        todasMemorias.add(new MemoriaRam("16GB XPG Spectrix", "XPG", 440.0, 0.1, 0.1, 25, 8, "DDR4", 16, 3600));

        todasFontes.add(new Fonte("650W Corsair CV", "Corsair", 460.0, 2.0, 1.5, 15, 0, 650, "80 Plus Bronze"));
        todasFontes.add(new Fonte("850W EVGA SuperNova", "EVGA", 850.0, 2.5, 2.0, 10, 0, 850, "80 Plus Gold"));
        todasFontes.add(new Fonte("500W Redragon RGPS", "Redragon", 290.0, 1.5, 1.2, 22, 0, 500, "80 Plus Bronze"));
        todasFontes.add(new Fonte("1000W Seasonic Prime", "Seasonic", 1600.0, 3.0, 2.5, 5, 0, 1000, "80 Plus Platinum"));
    }

    private void exibirErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}