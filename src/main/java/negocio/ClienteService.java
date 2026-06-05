package negocio;

import dados.RepositorioCliente;
import exception.ClienteJaExisteException;
import exception.ClienteNaoEncontradoException;
import model.Cliente;
import dados.IRepositorioCliente;
import java.util.List;

public class ClienteService {
    private static ClienteService instance;

    private final IRepositorioCliente repositorio;

    private ClienteService() {
        this.repositorio = new RepositorioCliente();
    }

    public static ClienteService getInstance() {
        if (instance == null) {
            instance = new ClienteService();
        }
        return instance;
    }

    public void cadastrar(Cliente cliente) throws ClienteJaExisteException {

        if (repositorio.buscarPorEmail(cliente.getEmail()) != null) {
            throw new ClienteJaExisteException();
        }

        repositorio.cadastrar(cliente);
    }

    public Cliente buscarPorId(long id) throws ClienteNaoEncontradoException {

        Cliente cliente = repositorio.buscar(id);

        if (cliente == null) {
            throw new ClienteNaoEncontradoException();
        }

        return cliente;
    }

    public Cliente autenticar(String email, String senha) throws ClienteNaoEncontradoException {

        Cliente cliente = repositorio.buscarPorEmail(email);

        if (cliente == null || !cliente.getSenha().equals(senha)) {
            throw new ClienteNaoEncontradoException();
        }

        return cliente;
    }

    public List<Cliente> listar() {
        return repositorio.listar();
    }

    public void remover(long id) {
        repositorio.remover(id);
    }
    public Cliente login(String email, String senha) throws ClienteNaoEncontradoException {
        Cliente cliente = repositorio.buscarPorEmail(email);
        if (cliente == null) {
            throw new ClienteNaoEncontradoException();
        }

        if (!cliente.getSenha().equals(senha)) {
            throw new ClienteNaoEncontradoException();
        }

        return cliente;
    }

}
