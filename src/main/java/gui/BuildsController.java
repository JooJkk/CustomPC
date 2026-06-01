package gui;

import javafx.event.ActionEvent; // IMPORTANTE: Para capturar o clique do botão com segurança
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
import negocio.CompatibilidadeService;
import exception.BuildIncompativelException;
import java.io.IOException;
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

    @FXML
    public void initialize() {
        // 1. CARREGA TODOS OS MESMOS ITENS DO CATÁLOGO NAS CAIXINHAS (21 ITENS)

        // --- PROCESSADORES ---
        comboProcessador.getItems().add(new Processador("Ryzen 5 5600", "AMD", 1000.0, 0.5, 10, 65, "AM4", 65));
        comboProcessador.getItems().add(new Processador("Intel i7-12700K", "Intel", 2100.0, 0.6, 5, 125, "LGA1700", 125));
        comboProcessador.getItems().add(new Processador("Ryzen 9 7950X", "AMD", 3500.0, 0.5, 3, 170, "AM5", 170));
        comboProcessador.getItems().add(new Processador("Core i5-13600K", "Intel", 1900.0, 0.6, 8, 125, "LGA1700", 125));
        comboProcessador.getItems().add(new Processador("Core i3-12100F", "Intel", 650.0, 0.4, 20, 58, "LGA1700", 60));

        // --- PLACAS-MÃE ---
        comboPlacaMae.getItems().add(new PlacaMae("B550M Aorus Elite", "Gigabyte", 950.0, 1.0, 8, 30, "AM4", 4, "DDR4", "Micro-ATX"));
        comboPlacaMae.getItems().add(new PlacaMae("X670E ASUS ROG", "ASUS", 2800.0, 1.2, 4, 40, "AM5", 4, "DDR5", "ATX"));
        comboPlacaMae.getItems().add(new PlacaMae("H610M-E", "MSI", 620.0, 0.8, 12, 25, "LGA1700", 2, "DDR4", "Micro-ATX"));
        comboPlacaMae.getItems().add(new PlacaMae("A520M-K", "ASUS", 480.0, 0.7, 15, 20, "AM4", 2, "DDR4", "Micro-ATX"));
        comboPlacaMae.getItems().add(new PlacaMae("Z790 MSI Pro", "MSI", 2100.0, 1.1, 6, 45, "LGA1700", 4, "DDR5", "ATX"));

        // --- MEMÓRIAS RAM ---
        comboRam.getItems().add(new MemoriaRam("16GB Corsair Vengeance", "Corsair", 380.0, 0.1, 30, 5, "DDR4", 16, 3200));
        comboRam.getItems().add(new MemoriaRam("32GB G.Skill Trident", "G.Skill", 1250.0, 0.2, 10, 10, "DDR5", 32, 6000));
        comboRam.getItems().add(new MemoriaRam("8GB Kingston Fury", "Kingston", 210.0, 0.1, 50, 5, "DDR4", 8, 2666));
        comboRam.getItems().add(new MemoriaRam("16GB XPG Spectrix", "XPG", 440.0, 0.1, 25, 8, "DDR4", 16, 3600));

        // --- FONTES ---
        comboFonte.getItems().add(new Fonte("650W Corsair CV", "Corsair", 460.0, 2.0, 15, 0, 650, "80 Plus Bronze"));
        comboFonte.getItems().add(new Fonte("850W EVGA SuperNova", "EVGA", 850.0, 2.5, 10, 0, 850, "80 Plus Gold"));
        comboFonte.getItems().add(new Fonte("500W Redragon RGPS", "Redragon", 290.0, 1.5, 22, 0, 500, "80 Plus Bronze"));
        comboFonte.getItems().add(new Fonte("1000W Seasonic Prime", "Seasonic", 1600.0, 3.0, 5, 0, 1000, "80 Plus Platinum"));

        // 2. SELEÇÃO AUTOMÁTICA EM TEMPO REAL (Vindo do catálogo)
        List<Componente> compartilhados = buildService.getComponentesSelecionadosParaMontagem();

        for (Componente comp : compartilhados) {
            if (comp instanceof Processador) {
                if (!comp.getNome().toUpperCase().contains("RTX") && !comp.getNome().toUpperCase().contains("RX ")) {
                    Processador p = (Processador) comp;
                    if (!comboProcessador.getItems().contains(p)) comboProcessador.getItems().add(p);
                    comboProcessador.setValue(p);
                }
            } else if (comp instanceof PlacaMae) {
                PlacaMae pm = (PlacaMae) comp;
                if (!comboPlacaMae.getItems().contains(pm)) comboPlacaMae.getItems().add(pm);
                comboPlacaMae.setValue(pm);
            } else if (comp instanceof MemoriaRam) {
                MemoriaRam ram = (MemoriaRam) comp;
                if (!comboRam.getItems().contains(ram)) comboRam.getItems().add(ram);
                comboRam.setValue(ram);
            } else if (comp instanceof Fonte) {
                Fonte f = (Fonte) comp;
                if (!comboFonte.getItems().contains(f)) comboFonte.getItems().add(f);
                comboFonte.setValue(f);
            }
        }
    }

    @FXML
    public void validarBuild() {
        Processador cpu = comboProcessador.getValue();
        PlacaMae mobo = comboPlacaMae.getValue();
        MemoriaRam ram = comboRam.getValue();
        Fonte fonte = comboFonte.getValue();

        if (cpu == null || mobo == null || ram == null || fonte == null) {
            lblResultado.setText("⚠️ Por favor, selecione todas as peças antes de verificar.");
            lblResultado.setStyle("-fx-text-fill: #ffa500;");
            return;
        }

        Build novaBuild = new Build();
        novaBuild.setNome("Build Compartilhada");
        novaBuild.setProcessador(cpu);
        novaBuild.setPlacaMae(mobo);
        novaBuild.adicionarMemoria(ram);
        novaBuild.setFonte(fonte);

        try {
            compatibilidadeService.validarBuildCompleta(novaBuild);
            lblResultado.setText("✅ Sucesso! Todas as peças são 100% compatíveis. A Build está pronta!");
            lblResultado.setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");

        } catch (BuildIncompativelException e) {
            lblResultado.setText("❌ Erro de Compatibilidade: " + e.getMessage());
            lblResultado.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        }
    }

    @FXML
    public void limparBancada() {
        buildService.limparComponentesMontagem();
        comboProcessador.setValue(null);
        comboPlacaMae.setValue(null);
        comboRam.setValue(null);
        comboFonte.setValue(null);
        lblResultado.setText("Bancada limpa! Selecione novas peças ou adicione pelo catálogo.");
        lblResultado.setStyle("-fx-text-fill: #b0b0b0;");
    }

    // --- MÉTODOS DE NAVEGAÇÃO ADICIONADOS ---

    // Ação para ir direto para a tela do Menu Principal (Home)
    @FXML
    public void voltarHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            exibirErro("Erro ao ir para a Home: " + e.getMessage());
        }
    }

    // Ação para voltar para a tela de Catálogo de Peças
    @FXML
    public void voltarCatalogo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/catalogo-view.fxml"));
            Parent root = loader.load();

            // Repassa o usuário logado para manter a sessão ativa no catálogo se necessário
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

    private void exibirErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}