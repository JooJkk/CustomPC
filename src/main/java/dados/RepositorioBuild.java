package dados;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.componentes.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RepositorioBuild implements IRepositorioBuild {

    private static final String ARQUIVO = "builds.json";
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
    private List<Build> carregar() {
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(arquivo)) {
            Type tipo = new TypeToken<List<Build>>() {}.getType();
            List<Build> lista = gson.fromJson(reader, tipo);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar builds: " + e.getMessage());
        }
    }

    private void salvar(List<Build> builds) {
        try (Writer writer = new FileWriter(ARQUIVO)) {
            gson.toJson(builds, writer);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar builds: " + e.getMessage());
        }
    }

    @Override
    public void cadastrar(Build build) {
        List<Build> builds = carregar();
        builds.add(build);
        salvar(builds);
    }

    @Override
    public Build buscar(long id) {
        for (Build b : carregar()) {
            if (b.getId() == id) return b;
        }
        return null;
    }

    @Override
    public List<Build> listar() {
        return carregar();
    }

    @Override
    public void atualizar(Build build) {
        List<Build> builds = carregar();
        for (int i = 0; i < builds.size(); i++) {
            if (builds.get(i).getId() == build.getId()) {
                builds.set(i, build);
                salvar(builds);
                return;
            }
        }
    }

    @Override
    public void remover(long id) {
        List<Build> builds = carregar();
        for (int i = 0; i < builds.size(); i++) {
            if (builds.get(i).getId() == id) {
                builds.remove(i);
                salvar(builds);
                return;
            }
        }
    }
}