package dados;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.Cliente;
import model.Pedido;
import exception.*;
import model.componentes.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RepositorioPedido implements IRepositorioPedido {

    private static final String ARQUIVO = "pedidos.json";
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Componente.class, new JsonDeserializer<Componente>() {
        @Override public Componente deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) {JsonObject obj = json.getAsJsonObject();
            if (obj.has("tipoRam")) {return context.deserialize(obj, MemoriaRam.class);}
            if (obj.has("formato")) {return context.deserialize(obj, PlacaMae.class);}
            if (obj.has("comprimentoMM")) {return context.deserialize(obj, PlacaVideo.class);}
            if (obj.has("certificacao")) {return context.deserialize(obj, Fonte.class);}
            if (obj.has("comprimentoMaxGpuMM")) {return context.deserialize(obj, Gabinete.class);}
            if (obj.has("socket")) {return context.deserialize(obj, Processador.class);}
            throw new JsonParseException(
                    "Tipo de componente desconhecido");
            }}).setPrettyPrinting().create();
    private List<Pedido> pedidos;

    public RepositorioPedido() {
        pedidos = carregar();
    }

    private List<Pedido> carregar() {
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(arquivo)) {
            Type tipo = new TypeToken<List<Pedido>>() {}.getType();
            List<Pedido> lista = gson.fromJson(reader, tipo);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar pedidos: " + e.getMessage());
        }
    }

    private void salvarDados() {
        try (Writer writer = new FileWriter(ARQUIVO)) {
            gson.toJson(pedidos, writer);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar pedidos: " + e.getMessage());
        }
    }

    @Override
    public void salvar(Pedido pedido) {
        pedidos.add(pedido);
        salvarDados();
    }

    @Override
    public Pedido buscarPorId(int id) throws PedidoNaoEncontradoException {
        for (Pedido p : pedidos) {
            if (p.getId() == id) return p;
        }
        throw new PedidoNaoEncontradoException();
    }

    @Override
    public void deletar(int id) throws PedidoNaoEncontradoException {
        Pedido p = buscarPorId(id);
        pedidos.remove(p);
        salvarDados();
    }

    @Override
    public List<Pedido> listarTodos() {
        return new ArrayList<>(pedidos);
    }

    @Override
    public List<Pedido> buscarPorCliente(Cliente cliente) {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            if (pedido.getCliente().equals(cliente)) {
                resultado.add(pedido);
            }
        }
        return resultado;
    }
}