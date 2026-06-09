package gui;

import exception.ClienteNaoEncontradoException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Cliente;
import negocio.ClienteService;

public class LoginController {

    private Cliente usuarioAnterior;

    public void setUsuarioAnterior(Cliente usuario) {
        this.usuarioAnterior = usuario;
    }

    @FXML
    private TextField campoEmail;

    @FXML
    private PasswordField campoSenha;

    @FXML
    private Label lblErro;

    @FXML
    private Button btnEntrar;

    @FXML
    void fazerLogin(ActionEvent event) {
        String email = campoEmail.getText().trim();
        String senha = campoSenha.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            mostrarErro("Preencha o e-mail e a senha.");
            return;
        }

        try {
            Cliente cliente = ClienteService.getInstance().autenticar(email, senha);
            NavegacaoController.trocarTela("/cliente-view.fxml", event, cliente);
        } catch (ClienteNaoEncontradoException e) {
            mostrarErro("E-mail ou senha incorretos. Tente novamente.");
            campoSenha.clear();
        }
    }

    @FXML
    void irParaHome(ActionEvent event) {
        NavegacaoController.trocarTela("/Home.fxml", event, null);
    }

    @FXML
    void irParaCadastro(ActionEvent event) {
        NavegacaoController.trocarTela("/Cadastro.fxml", event, null);
    }

    private void mostrarErro(String mensagem) {
        lblErro.setText(mensagem);
    }
}
