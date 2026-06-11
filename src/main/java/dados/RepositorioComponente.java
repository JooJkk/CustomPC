package dados;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.componentes.*;
import exception.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RepositorioComponente implements IRepositorioComponente {

    private static final String ARQUIVO = "componentes.json";
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Componente.class, new JsonDeserializer<Componente>() {
                @Override
                public Componente deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) {
                    JsonObject obj = json.getAsJsonObject();
                    if (obj.has("tipoRamSuportada"))     return context.deserialize(obj, PlacaMae.class);
                    if (obj.has("tipoRam"))              return context.deserialize(obj, MemoriaRam.class);
                    if (obj.has("certificacao"))         return context.deserialize(obj, Fonte.class);
                    if (obj.has("comprimentoMaxGpuMM")) return context.deserialize(obj, Gabinete.class);

                    // 2. PLACA DE VÍDEO: Se tiver qualquer um dos campos exclusivos de GPU
                    if (obj.has("comprimentoMM") || obj.has("memoriaGB")) {
                        return context.deserialize(obj, PlacaVideo.class);
                    }

                    // 3. PROCESSADOR: Se tiver tdp ou socket (e não caiu nos filtros anteriores)
                    if (obj.has("tdp") || obj.has("socket")) {
                        return context.deserialize(obj, Processador.class);
                    }

                    throw new JsonParseException("Tipo de componente desconhecido");
                }
            })
            .serializeNulls()           // ← serializa nulls
            .enableComplexMapKeySerialization()
            .setPrettyPrinting()
            .create();
    private List<Componente> componentes;

    public RepositorioComponente() {
        componentes = carregar();
    }

    private List<Componente> carregar() {
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(arquivo)) {
            Type tipo = new TypeToken<List<Componente>>() {}.getType();
            List<Componente> lista = gson.fromJson(reader, tipo);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar componentes: " + e.getMessage());
        }
    }

    private void salvarArquivo() {
        try (Writer writer = new FileWriter(ARQUIVO)) {
            gson.toJson(componentes, writer);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar componentes: " + e.getMessage());
        }
    }

    @Override
    public void cadastrar(Componente componente) {
        componentes.add(componente);
        salvarArquivo();
    }

    @Override
    public Componente buscar(long id) {
        for (Componente c : componentes) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    @Override
    public List<Componente> listar() {
        return new ArrayList<>(componentes);
    }

    @Override
    public void atualizar(Componente componente) {
        for (int i = 0; i < componentes.size(); i++) {
            if (componentes.get(i).getId() == componente.getId()) {
                componentes.set(i, componente);
                salvarArquivo();
                return;
            }
        }
    }

    @Override
    public void remover(Long id) {
        componentes.removeIf(c -> c.getId() == id);
        salvarArquivo();
    }
}