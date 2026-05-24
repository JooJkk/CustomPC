package dados;
import java.util.List;

import exception.PedidoNaoEncontradoException;
import model.Pedido;
import exception.*;

public interface IRepositorioPedido {

    void salvar(Pedido pedido);
    void deletar(int id) throws PedidoNaoEncontradoException;
    Pedido buscarPorId(int id) throws PedidoNaoEncontradoException;
    List<Pedido> listarTodos();

}
