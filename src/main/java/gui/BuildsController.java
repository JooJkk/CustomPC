package gui;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import model.componentes.Build;
import model.componentes.Fonte;
import model.componentes.MemoriaRam;
import model.componentes.PlacaMae;
import model.componentes.Processador;
import negocio.CompatibilidadeService;
import exception.BuildIncompativelException;

public class BuildsController {

    @FXML private ComboBox<Processador> comboProcessador;
    @FXML private ComboBox<PlacaMae> comboPlacaMae;
    @FXML private ComboBox<MemoriaRam> comboRam;
    @FXML private ComboBox<Fonte> comboFonte;
    @FXML private Label lblResultado;

    private CompatibilidadeService compatibilidadeService = new CompatibilidadeService();

    @FXML
    public void initialize() {
        // CRIANDO PEÇAS FALSAS APENAS PARA TESTE VISUAL NA TELA
        // No futuro, você vai puxar isso do Banco de Dados ou de uma Lista do seu Service

        Processador cpuTeste = new Processador("Ryzen 5 5600", "AMD", 1000.0, 0.5, 10, 65, "AM4", 65);
        PlacaMae moboTeste = new PlacaMae("B550M", "Asus", 800.0, 1.0, 10, 30, "AM4", 4, "DDR4", "Micro-ATX");
        MemoriaRam ramTeste = new MemoriaRam("8GB Fury", "HyperX", 200.0, 0.1, 20, 5, "DDR4", 8, 3200);
        Fonte fonteForte = new Fonte("Corsair 650W", "Corsair", 400.0, 2.0, 10, 0, 650, "80 Plus Bronze");
        Fonte fonteFraca = new Fonte("Fonte Genérica 200W", "Genérica", 50.0, 1.0, 10, 0, 200, "Nenhuma");

        // Adicionando as peças nas caixinhas (ComboBox)
        comboProcessador.getItems().add(cpuTeste);
        comboPlacaMae.getItems().add(moboTeste);
        comboRam.getItems().add(ramTeste);
        comboFonte.getItems().addAll(fonteForte, fonteFraca);
    }

    @FXML
    public void validarBuild() {
        // 1. Pega o que o usuário escolheu nas caixas
        Processador cpu = comboProcessador.getValue();
        PlacaMae mobo = comboPlacaMae.getValue();
        MemoriaRam ram = comboRam.getValue();
        Fonte fonte = comboFonte.getValue();

        // 2. Verifica se o usuário esqueceu de selecionar alguma coisa
        if (cpu == null || mobo == null || ram == null || fonte == null) {
            lblResultado.setText("⚠️ Por favor, selecione todas as peças antes de verificar.");
            lblResultado.setStyle("-fx-text-fill: #ffa500;"); // Laranja
            return;
        }

        // 3. Monta a Build
        Build novaBuild = new Build();
        novaBuild.setProcessador(cpu);
        novaBuild.setPlacaMae(mobo);
        novaBuild.adicionarMemoria(ram);
        novaBuild.setFonte(fonte);

        // 4. Passa pelo Motor de Compatibilidade!
        try {
            compatibilidadeService.validarBuildCompleta(novaBuild);

            // Se passar direto, deu tudo certo!
            lblResultado.setText("✅ Sucesso! Todas as peças são 100% compatíveis. A Build está pronta para ir ao Carrinho!");
            lblResultado.setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;"); // Verde

        } catch (BuildIncompativelException e) {
            // Se o motor lançar a exceção, capturamos a mensagem e mostramos na tela
            lblResultado.setText("❌ Erro de Compatibilidade: " + e.getMessage());
            lblResultado.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;"); // Vermelho
        }
    }
}
