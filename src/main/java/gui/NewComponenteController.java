package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Cliente;
import model.componentes.Componente;
import model.componentes.*;
import negocio.ComponenteService;

public class NewComponenteController {
    private Cliente usuarioLogado;
    private ComponenteService componenteService = ComponenteService.getInstance();

    @FXML private ComboBox<String> comboTipo;

    // Elementos mapeados a partir do seu arquivo NewComponente.fxml
    @FXML private TextField txtNome;
    @FXML private TextField txtMarca;
    @FXML private TextField txtPreco;
    @FXML private TextField txtPeso;
    @FXML private TextField txtVolume;
    @FXML private TextField txtEstoque;
    @FXML private TextField txtConsumo;
    @FXML private TextField txtNivel;

    public void setUsuario(Cliente usuario) {
        this.usuarioLogado = usuario;
    }

    @FXML
    public void initialize() {
        // Inicializa o ComboBox com as opções exigidas
        comboTipo.getItems().addAll("Fonte", "Placa Mãe", "Placa de Vídeo", "Processador", "Memória Ram");
    }

    @FXML
    public void onBtnSalvar(ActionEvent event) {
        String tipoSelecionado = comboTipo.getSelectionModel().getSelectedItem();

        // Validação inicial da seleção do combo
        if (tipoSelecionado == null) {
            exibirAlerta("Aviso", "Por favor, selecione o tipo de componente antes de salvar.");
            return;
        }

        // Validação básica de campos vazios obrigatórios
        if (txtNome.getText().trim().isEmpty() || txtMarca.getText().trim().isEmpty()) {
            exibirAlerta("Erro de Validação", "Nome e Marca são campos obrigatórios.");
            return;
        }

        try {
            // 1. Polimorfismo: Instancia a classe correta baseada na seleção do ComboBox
            Componente novoComponente = switch (tipoSelecionado) {
                case "Fonte" -> new Fonte();
                case "Placa Mãe" -> new PlacaMae();
                case "Placa de Vídeo" -> new PlacaVideo(); // Certifique-se de que o nome da classe em seu modelo é exatamente este
                case "Processador" -> new Processador();
                case "Memória Ram" -> new MemoriaRam();
                default -> null;
            };

            if (novoComponente == null) return;

            // 2. Define os atributos genéricos extraídos dos inputs textuais
            novoComponente.setNome(txtNome.getText().trim());
            novoComponente.setMarca(txtMarca.getText().trim());

            // Tratamento e conversão dos dados numéricos
            novoComponente.setPreco(Double.parseDouble(txtPreco.getText().trim()));
            novoComponente.setEstoque(Integer.parseInt(txtEstoque.getText().trim()));

            // Nota: Se a sua classe Componente possuir os setters abaixo para peso, volume, consumo e nível,
            // eles serão populados corretamente. Caso contrário, verifique os nomes exatos no seu modelo.
            novoComponente.setPeso(Double.parseDouble(txtPeso.getText().trim()));
            novoComponente.setVolume(Double.parseDouble(txtVolume.getText().trim()));
            novoComponente.setConsumoWatts(Integer.parseInt(txtConsumo.getText().trim()));

            // 3. Envia para a camada de negócio salvar no repositório
            componenteService.cadastrar(novoComponente);

            exibirAlerta("Sucesso", tipoSelecionado + " cadastrado(a) com sucesso no sistema!");
            limparFormulario();
            NavegacaoController.trocarTela("/AdminView.fxml", event, usuarioLogado);

        } catch (NumberFormatException e) {
            exibirAlerta("Erro de Validação", "Verifique os campos numéricos. Preço, Estoque e Consumo devem conter apenas valores válidos.");
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
        txtNivel.clear();
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