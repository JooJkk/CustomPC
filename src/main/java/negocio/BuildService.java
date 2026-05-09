package main.java.negocio;
import main.java.dados.IRepositorioBuild;
import main.java.model.Build;

import java.util.List;

public class BuildService {
    IRepositorioBuild repositorio;
    public BuildService(IRepositorioBuild repositorio){
        if(repositorio != null){
            this.repositorio = repositorio;
        }
        else{
            throw new IllegalArgumentException("Repositorio não pode ser nulo");
        }
    }
    public void cadastrar(Build build) {
        if (build == null) {
            throw new IllegalArgumentException("Build não pode ser nula.");
        }

        if (build.getNome() == null || build.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome da build não pode ser vazio");
        }

        repositorio.cadastrar(build);
    }
    public Build buscar(long id) {
        Build build = repositorio.buscar(id);

        if (build == null) {
            throw new IllegalArgumentException("Build não encontrada.");
        }

        return build;
    }

    public List<Build> listar() {
        return repositorio.listar();
    }

    public void atualizar(Build build) {
        if (build == null) {
            throw new IllegalArgumentException("Build não pode ser nula.");
        }

        Build existente = repositorio.buscar(build.getId());

        if (existente == null) {
            throw new IllegalArgumentException("Build não encontrada.");
        }

        repositorio.atualizar(build);
    }

    public void remover(long id) {
        Build existente = repositorio.buscar(id);

        if (existente == null) {
            throw new IllegalArgumentException("Build não encontrada.");
        }

        repositorio.remover(id);
    }
}
