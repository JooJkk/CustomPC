package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import model.Cliente;
import negocio.ComponenteService;
import negocio.RelatoriosService;

import java.io.IOException;

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
        RelatoriosService relatoriosService = new RelatoriosService();
        try {
            relatoriosService.gerarRelatorioBaixoEstoque(ComponenteService.getInstance().listar(), 10, "relatorios/relatorio_baixo_estoque.xlsx");
            alert("Relatorio baixado com sucesso!");
        } catch (IOException e) {
            alert("Não foi possivel baixar o relatorio, erro: " + e.getMessage());
        }
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
