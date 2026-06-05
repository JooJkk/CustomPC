package dados;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Cliente;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RepositorioCliente implements IRepositorioCliente {

    private static final String ARQUIVO = "clientes.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private List<Cliente> carregar() {
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(arquivo)) {
            Type tipo = new TypeToken<List<Cliente>>() {}.getType();
            List<Cliente> lista = gson.fromJson(reader, tipo);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar clientes: " + e.getMessage());
        }
    }

    private void salvar(List<Cliente> clientes) {
        try (Writer writer = new FileWriter(ARQUIVO)) {
            gson.toJson(clientes, writer);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar clientes: " + e.getMessage());
        }
    }

    private int gerarProximoId(List<Cliente> clientes) {
        return clientes.stream()
                .mapToInt(Cliente::getId)
                .max()
                .orElse(0) + 1;
    }

    @Override
    public void cadastrar(Cliente cliente) {
        List<Cliente> clientes = carregar();
        cliente.setId(gerarProximoId(clientes));
        clientes.add(cliente);
        salvar(clientes);
    }

    @Override
    public Cliente buscar(long id) {
        for (Cliente c : carregar()) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    @Override
    public Cliente buscarPorEmail(String email) {
        for (Cliente c : carregar()) {
            if (c.getEmail().equalsIgnoreCase(email)) return c;
        }
        return null;
    }

    @Override
    public List<Cliente> listar() {
        return carregar();
    }

    @Override
    public void atualizar(Cliente cliente) {
        List<Cliente> clientes = carregar();
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getId() == cliente.getId()) {
                clientes.set(i, cliente);
                salvar(clientes);
                return;
            }
        }
    }

    @Override
    public void remover(long id) {
        List<Cliente> clientes = carregar();
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getId() == id) {
                clientes.remove(i);
                salvar(clientes);
                return;
            }
        }
    }
}