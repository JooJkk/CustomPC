package dados;
import java.util.ArrayList;
import java.util.List;
import model.Pedido;
import exception.*;


public class RepositorioPedido implements IRepositorioPedido {

    private List<Pedido> pedidos = new ArrayList<>();

    @Override
    public void salvar(Pedido pedido) {
        pedidos.add(pedido);
        System.out.println("o pedido foi salvo com sucesso");
    }

    @Override
    public Pedido buscarPorId(int id) throws PedidoNaoEncontradoException {
        for(Pedido p: pedidos) {
            if(p.getId() == id) {
                return p;
            }
        }
        throw new PedidoNaoEncontradoException();
    }

    public void deletar(int id) throws PedidoNaoEncontradoException{
        Pedido p = buscarPorId(id);
        pedidos.remove(p);
        System.out.println("pedido" + id + "removido com sucesso");
    }

    @Override
    public List<Pedido> listarTodos() {
        return new ArrayList<>(pedidos);
    }




}
