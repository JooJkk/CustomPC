package dados;
import java.util.List;

import exception.PedidoNaoEncontradoException;
import model.Cliente;
import model.Pedido;
import exception.*;

public interface IRepositorioPedido {

    void salvar(Pedido pedido);
    void atualizar(Pedido pedido);
    void deletar(int id) throws PedidoNaoEncontradoException;
    Pedido buscarPorId(long id) throws PedidoNaoEncontradoException;
    List<Pedido> listarTodos();
    List<Pedido> buscarPorCliente(Cliente cliente);

}
