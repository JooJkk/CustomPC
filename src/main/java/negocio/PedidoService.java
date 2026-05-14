package main.java.negocio;
import main.java.dados.IRepositorioPedido;
import main.java.model.Carrinho;
import main.java.model.ItemCarrinho;
import main.java.model.ItemPedido;
import main.java.model.Pedido;
import main.java.exception.*;


public class PedidoService {
    private IRepositorioPedido repositorio;

    public PedidoService(IRepositorioPedido repositorio) {
        this.repositorio = repositorio;
    }

    public void cancelarPedido(int id) throws PedidoNaoEncontradoException, PedidoEnviadoException {
        Pedido p = repositorio.buscarPorId(id);

        if (p == null) {
            throw new PedidoNaoEncontradoException("ID " + id + " não encontrado.");
        }

        if ("ENVIADO".equalsIgnoreCase(p.getStatus())) {
            throw new PedidoEnviadoException();
        }

        repositorio.deletar(id);
        System.out.println("Pedido " + id + " foi cancelado com sucesso.");
    }

    public Pedido finalizarCompra(Carrinho carrinho) throws CarrinhoVazioException {
        if (carrinho.getItens() == null || carrinho.getItens().isEmpty()) {
            throw new CarrinhoVazioException();
        }

        Pedido novoPedido = new Pedido();
        novoPedido.setId(carrinho.getId());

        for (ItemCarrinho itemC : carrinho.getItens()) {

            ItemPedido itemP = new ItemPedido();
            itemP.setQuantidade(itemC.getQuantidade());
            itemP.setPrecoUnitario(itemC.getPrecoUnitario());

            itemP.setComponente(itemC.getComponente());
            novoPedido.adicionarItem(itemP);
        }

        repositorio.salvar(novoPedido);

        carrinho.limpar();

        return novoPedido;
    }
}