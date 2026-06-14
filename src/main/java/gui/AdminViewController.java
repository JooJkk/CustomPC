package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import model.Cliente;
import negocio.ComponenteService;
import negocio.PedidoService;
import negocio.RelatoriosService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

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
        NavegacaoController.trocarTela("/GerenciadorPedidos.fxml", event, usuarioLogado);
    }

    // Método utilitário para garantir que a pasta de relatórios exista no sistema de arquivos
    private void garantirDiretorioExistente(String caminhoArquivo) {
        File arquivo = new File(caminhoArquivo);
        File diretorio = arquivo.getParentFile();
        if (diretorio != null && !diretorio.exists()) {
            diretorio.mkdirs(); // Cria a pasta "relatorios" se ela não existir
        }
    }

    @FXML
    public void onBtnRelatorioBE(ActionEvent event){
        RelatoriosService relatoriosService = new RelatoriosService();
        String caminho = "relatorios/relatorio_baixo_estoque.xlsx";
        try {
            garantirDiretorioExistente(caminho);
            relatoriosService.gerarRelatorioBaixoEstoque(ComponenteService.getInstance().listar(), 10, caminho);
            alert("Relatório de Baixo Estoque baixado com sucesso!");
        } catch (IOException e) {
            alert("Não foi possível baixar o relatório. Erro: " + e.getMessage());
        }
    }

    @FXML
    public void onBtnRelatorioF(ActionEvent event){
        RelatoriosService relatoriosService = new RelatoriosService();
        String caminho = "relatorios/relatorio_faturamento.xlsx";

        // Pega dinamicamente o mês e ano atuais
        LocalDate dataAtual = LocalDate.now();
        int mesAtual = dataAtual.getMonthValue();
        int anoAtual = dataAtual.getYear();

        try {
            garantirDiretorioExistente(caminho);
            // Agora passa a lista de pedidos real obtida do Service, junto com o mês e ano dinâmicos
            relatoriosService.gerarFaturamentoMensal(PedidoService.getInstance().listarTodos(), mesAtual, anoAtual, caminho);
            alert("Relatório de Faturamento (" + mesAtual + "/" + anoAtual + ") baixado com sucesso!");
        } catch (IOException e) {
            alert("Não foi possível baixar o relatório. Erro: " + e.getMessage());
        }
    }

    @FXML
    public void onBtnRelatorioOP(ActionEvent event){
        RelatoriosService relatoriosService = new RelatoriosService();
        String caminho = "relatorios/relatorio_ordens_pendentes.xlsx";
        try {
            garantirDiretorioExistente(caminho);
            // Passa a lista de todos os pedidos para filtrar as ordens pendentes
            relatoriosService.gerarOrdensPendentes(PedidoService.getInstance().listarTodos(), caminho);
            alert("Relatório de Ordens Pendentes baixado com sucesso!");
        } catch (IOException e) {
            alert("Não foi possível baixar o relatório. Erro: " + e.getMessage());
        }
    }

    @FXML
    public void onBtnSair(ActionEvent event){
        usuarioLogado = null;
        NavegacaoController.trocarTela("/Home.fxml", event, usuarioLogado);
    }

    @FXML
    public void initialize(){
    }

    private void alert(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Gerador de Relatórios");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}