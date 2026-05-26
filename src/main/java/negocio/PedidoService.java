package negocio;
import dados.IRepositorioPedido;
import model.*;
import exception.*;


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

    public Pedido finalizarCompra(Carrinho carrinho, Endereco endereco, Pagamento pagamento) throws CarrinhoVazioException {
        if (carrinho.getItens() == null || carrinho.getItens().isEmpty()) {
            throw new CarrinhoVazioException();
        }

        Pedido novoPedido = new Pedido();
        novoPedido.setId(carrinho.getId());
        novoPedido.setEndereco(endereco);
        novoPedido.setPagamento(pagamento);
        novoPedido.getPagamento().setStatus("PENDENTE");
        novoPedido.setStatus("PENDENTE");
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