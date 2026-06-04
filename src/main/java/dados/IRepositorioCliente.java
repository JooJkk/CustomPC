package dados;

import model.Cliente;

import java.util.List;

public interface IRepositorioCliente {
    void cadastrar(Cliente cliente);
    Cliente buscar(int id);
    Cliente buscarPorEmail(String email);
    List<Cliente> listar();
    void atualizar(Cliente cliente);
    void remover(int id);
}
