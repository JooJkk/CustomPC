package main.java.dados;
import java.util.List;

import main.java.exception.PedidoNaoEncontradoException;
import main.java.model.Pedido;
import main.java.exception.*;

public interface IRepositorioPedido {

    void salvar(Pedido pedido);
    void deletar(int id) throws PedidoNaoEncontradoException;
    Pedido buscarPorId(int id) throws PedidoNaoEncontradoException;
    List<Pedido> listarTodos();

}
