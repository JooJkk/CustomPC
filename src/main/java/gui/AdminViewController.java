package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import model.Cliente;

public class AdminViewController {
    private Cliente usuarioLogado;
    public void setUsuario(Cliente usuario) {
        this.usuarioLogado = usuario;
    }

    @FXML
    public void onBtnGerEstoque(ActionEvent event){
        NavegacaoController.trocarTela("/GerenciadorEstoque.fxml", event, usuarioLogado);
    }
    @FXML
    public void onBtnGerPedidos(ActionEvent event){

    }
    @FXML
    public void onBtnRelatorio(ActionEvent event){

    }
    @FXML
    public void onBtnSair(ActionEvent event){
        usuarioLogado = null;
        NavegacaoController.trocarTela("/Home.fxml", event, usuarioLogado);
    }

    @FXML
    public void initialize(){System.out.println("Controller inicializado com sucesso!");}

    private void alert(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
