package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Cliente;
import model.componentes.*;
import negocio.ComponenteService;

public class NewComponenteController {
    private Cliente usuarioLogado;
    private ComponenteService componenteService = ComponenteService.getInstance();

    @FXML private ComboBox<String> comboTipo;

    // Campos padrões / comuns da classe abstrata Componente
    @FXML private TextField txtNome;
    @FXML private TextField txtMarca;
    @FXML private TextField txtPreco;
    @FXML private TextField txtPeso;
    @FXML private TextField txtVolume;
    @FXML private TextField txtEstoque;
    @FXML private TextField txtConsumo;

    // Primeiro campo específico e seu respectivo Label mutável
    @FXML private Label lblNivel;
    @FXML private TextField txtNivel;

    // Segundo campo específico e seu respectivo Label mutável
    @FXML private Label lblMutavel;
    @FXML private TextField txtEspecifico2;

    public void setUsuario(Cliente usuario) {
        this.usuarioLogado = usuario;
    }

    @FXML
    public void initialize() {
        // Popula as opções do ComboBox
        comboTipo.getItems().addAll("Fonte", "Placa Mãe", "Placa de Vídeo", "Processador", "Memória Ram");

        // Define exemplos fixos (PromptText) nas caixas de texto genéricas
        txtNome.setPromptText("ex: Intel Core i5-12400F ou HyperX Fury 8GB");
        txtMarca.setPromptText("ex: Intel, AMD, Corsair, ASUS, Gigabyte");
        txtPreco.setPromptText("ex: 1250.50");
        txtPeso.setPromptText("ex: 0.45 (em kg - opcional)");
        txtVolume.setPromptText("ex: 0.02 (opcional)");
        txtEstoque.setPromptText("ex: 15 (quantidade em unidades)");
        txtConsumo.setPromptText("ex: 65 (em Watts - opcional)");

        // Ouvinte para detectar a troca de tipo no ComboBox e adaptar o formulário
        comboTipo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            atualizarCamposEspecificos(newValue);
        });

        // Estado visual inicial desativado até que o usuário escolha um Tipo
        configurarEstadoCamposEspecificos(true, "Especificação 1:", "Especificação 2:", "", "");
    }

    /**
     * Altera dinamicamente os Textos dos Labels e Prompts de acordo com o componente escolhido
     */
    private void atualizarCamposEspecificos(String tipoSelecionado) {
        if (tipoSelecionado == null) {
            configurarEstadoCamposEspecificos(true, "Especificação 1:", "Especificação 2:", "", "");
            return;
        }

        switch (tipoSelecionado) {
            case "Fonte" -> configurarEstadoCamposEspecificos(
                    false,
                    "Certificação:",
                    "Potência (Watts):",
                    "ex: 80 Plus Gold, 80 Plus Bronze",
                    "ex: 650, 750, 850"
            );
            case "Placa Mãe" -> configurarEstadoCamposEspecificos(
                    false,
                    "Formato / Tamanho:",
                    "Slots de RAM:",
                    "ex: ATX, Micro-ATX, Mini-ITX",
                    "ex: 2, 4"
            );
            case "Placa de Vídeo" -> configurarEstadoCamposEspecificos(
                    false,
                    "Comprimento (MM):",
                    "Memória de Vídeo (GB):",
                    "ex: 240, 280, 320 (apenas números)",
                    "ex: 8, 12, 16, 24"
            );
            case "Processador" -> configurarEstadoCamposEspecificos(
                    false,
                    "Socket:",
                    "TDP (Watts):",
                    "ex: AM4, LGA1700, AM5",
                    "ex: 65, 105, 125"
            );
            case "Memória Ram" -> configurarEstadoCamposEspecificos(
                    false,
                    "Tipo de RAM:",
                    "Capacidade (GB):",
                    "ex: DDR4, DDR5",
                    "ex: 8, 16, 32"
            );
        }
    }

    /**
     * Função auxiliar para limpar, ativar/desativar e definir os textos dinâmicos de uma vez só
     */
    private void configurarEstadoCamposEspecificos(boolean desativado, String textLabel1, String textLabel2, String prompt1, String prompt2) {
        lblNivel.setText(textLabel1);
        lblMutavel.setText(textLabel2);

        txtNivel.setDisable(desativado);
        txtEspecifico2.setDisable(desativado);

        txtNivel.clear();
        txtEspecifico2.clear();

        txtNivel.setPromptText(prompt1);
        txtEspecifico2.setPromptText(prompt2);
    }

    @FXML
    public void onBtnSalvar(ActionEvent event) {
        String tipoSelecionado = comboTipo.getSelectionModel().getSelectedItem();

        if (tipoSelecionado == null) {
            exibirAlerta("Aviso", "Por favor, selecione o tipo de componente antes de salvar.");
            return;
        }

        // Validação estrita de preenchimento dos campos essenciais
        if (txtNome.getText().trim().isEmpty() || txtMarca.getText().trim().isEmpty() ||
                txtPreco.getText().trim().isEmpty() || txtEstoque.getText().trim().isEmpty() ||
                txtNivel.getText().trim().isEmpty() || txtEspecifico2.getText().trim().isEmpty()) {
            exibirAlerta("Erro de Validação", "Por favor, preencha todos os campos obrigatórios e de especificação.");
            return;
        }

        try {
            Componente novoComponente;
            switch (tipoSelecionado) {
                case "Fonte" -> {
                    Fonte f = new Fonte();
                    f.setCertificacao(txtNivel.getText().trim());
                    f.setPotenciaWatts(Integer.parseInt(txtEspecifico2.getText().trim()));
                    novoComponente = f;
                }
                case "Placa Mãe" -> {
                    PlacaMae pm = new PlacaMae();
                    pm.setFormato(txtNivel.getText().trim());
                    pm.setSlotsRam(Integer.parseInt(txtEspecifico2.getText().trim()));

                    // Definindo variáveis adicionais necessárias que o modelo PlacaMae possui
                    pm.setSocket("AM4"); // Valor padrão ou expansível
                    pm.setTipoRamSuportada("DDR4");
                    novoComponente = pm;
                }
                case "Placa de Vídeo" -> {
                    PlacaVideo pv = new PlacaVideo();
                    pv.setComprimentoMM(Integer.parseInt(txtNivel.getText().trim()));
                    pv.setMemoriaGB(Integer.parseInt(txtEspecifico2.getText().trim()));
                    novoComponente = pv;
                }
                case "Processador" -> {
                    Processador p = new Processador();
                    p.setSocket(txtNivel.getText().trim());
                    p.setTdp(Integer.parseInt(txtEspecifico2.getText().trim()));
                    novoComponente = p;
                }
                case "Memória Ram" -> {
                    MemoriaRam ram = new MemoriaRam();
                    ram.setTipoRam(txtNivel.getText().trim());
                    ram.setCapacidadeGB(Integer.parseInt(txtEspecifico2.getText().trim()));
                    novoComponente = ram;
                }
                default -> throw new IllegalArgumentException("Tipo inválido selecionado.");
            };

            // Atributos genéricos herdados da classe abstrata Componente
            novoComponente.setNome(txtNome.getText().trim());
            novoComponente.setMarca(txtMarca.getText().trim());
            novoComponente.setPreco(Double.parseDouble(txtPreco.getText().trim()));
            novoComponente.setEstoque(Integer.parseInt(txtEstoque.getText().trim()));

            // Tratamento seguro para campos numéricos opcionais (evita quebra por string vazia)
            if (!txtPeso.getText().trim().isEmpty()) novoComponente.setPeso(Double.parseDouble(txtPeso.getText().trim()));
            if (!txtVolume.getText().trim().isEmpty()) novoComponente.setVolume(Double.parseDouble(txtVolume.getText().trim()));
            if (!txtConsumo.getText().trim().isEmpty()) novoComponente.setConsumoWatts(Integer.parseInt(txtConsumo.getText().trim()));

            // 3. Envia para a camada de negócio salvar de fato no arquivo componentes.json
            componenteService.cadastrar(novoComponente);

            exibirAlerta("Sucesso", tipoSelecionado + " cadastrado(a) e salvo(a) com sucesso!");
            limparFormulario();

            // Retorna à tela anterior de Gerenciamento/Administração
            NavegacaoController.trocarTela("/GerenciadorEstoque.fxml", event, usuarioLogado);

        } catch (NumberFormatException e) {
            exibirAlerta("Erro de Validação", "Verifique as entradas numéricas. Preço, Estoque e campos numéricos específicos (Potência, Slots, Comprimento, TDP, Capacidade) precisam de valores numéricos válidos.");
        } catch (Exception e) {
            exibirAlerta("Erro ao Salvar", "Não foi possível cadastrar o componente: " + e.getMessage());
        }
    }

    private void limparFormulario() {
        comboTipo.getSelectionModel().clearSelection();
        txtNome.clear();
        txtMarca.clear();
        txtPreco.clear();
        txtPeso.clear();
        txtVolume.clear();
        txtEstoque.clear();
        txtConsumo.clear();
        atualizarCamposEspecificos(null);
    }

    private void exibirAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    public void onBtnVoltar(ActionEvent event) {
        NavegacaoController.trocarTela("/GerenciadorEstoque.fxml", event, usuarioLogado);
    }
}