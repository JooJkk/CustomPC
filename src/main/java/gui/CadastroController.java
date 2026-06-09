package gui;

import exception.ClienteJaExisteException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Cliente;
import negocio.ClienteService;

public class CadastroController {

    @FXML
    private TextField campoNome;

    @FXML
    private TextField campoEmail;

    @FXML
    private PasswordField campoSenha;

    @FXML
    private PasswordField campoConfirmarSenha;

    @FXML
    private Label lblMensagem;

    @FXML
    private Button btnCadastrar;

    @FXML
    void fazerCadastro(ActionEvent event) {
        String nome = campoNome.getText().trim();
        String email = campoEmail.getText().trim();
        String senha = campoSenha.getText();
        String confirmarSenha = campoConfirmarSenha.getText();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            mostrarErro("Preencha todos os campos.");
            return;
        }

        if (!email.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.-]+$")) {
            mostrarErro("Digite um e-mail válido.");
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            mostrarErro("As senhas não coincidem.");
            campoSenha.clear();
            campoConfirmarSenha.clear();
            return;
        }

        try {
            Cliente cliente = new Cliente(nome, email, senha);
            ClienteService.getInstance().cadastrar(cliente);

            lblMensagem.setStyle("-fx-text-fill: #27ae60;");
            lblMensagem.setText("Conta criada com sucesso! Redirecionando...");

            NavegacaoController.trocarTela("/Login.fxml", event, null);

        } catch (ClienteJaExisteException e) {
            mostrarErro("Já existe uma conta com esse e-mail.");
        } catch (IllegalArgumentException e) {
            mostrarErro(e.getMessage());
        }
    }

    @FXML
    void irParaLogin(ActionEvent event) {
        NavegacaoController.trocarTela("/Login.fxml", event, null);
    }

    @FXML
    void irParaHome(ActionEvent event) {
        NavegacaoController.trocarTela("/Home.fxml", event, null);
    }

    private void mostrarErro(String mensagem) {
        lblMensagem.setStyle("-fx-text-fill: #e74c3c;");
        lblMensagem.setText(mensagem);
    }
}
