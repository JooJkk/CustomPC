package negocio;
import dados.IRepositorioBuild;
import model.componentes.Build;
import exception.*;

import java.util.List;

public class BuildService {
    IRepositorioBuild repositorio;
    private CompatibilidadeService compatibilidadeService;
    public BuildService(IRepositorioBuild repositorio, CompatibilidadeService compatibilidadeService) {

        if(repositorio == null){
            throw new IllegalArgumentException("Repositorio não pode ser nulo");
        }

        if(compatibilidadeService == null){
            throw new IllegalArgumentException("CompatibilidadeService não pode ser nulo");
        }

        this.repositorio = repositorio;
        this.compatibilidadeService = compatibilidadeService;
    }
    public void cadastrar(Build build){
        if (build == null) {
            throw new BuildInvalidaException("Build não pode ser nula.");
        }

        if (build.getNome() == null || build.getNome().isBlank()) {
            throw new BuildInvalidaException("Nome da build não pode ser vazio");
        }
        compatibilidadeService.validarBuildCompleta(build);
        repositorio.cadastrar(build);
    }
    public Build buscar(long id){
        if(id <= 0){
            throw new IllegalArgumentException("id inválido");
        }
        Build build = repositorio.buscar(id);

        if (build == null) {
            throw new BuildNaoEncontradaException("Build não encontrada.");
        }

        return build;
    }

    public List<Build> listar() {
        return repositorio.listar();
    }

    public void atualizar(Build build){
        if (build == null) {
            throw new BuildInvalidaException("Build não pode ser nula.");
        }

        Build existente = repositorio.buscar(build.getId());

        if (existente == null) {
            throw new BuildNaoEncontradaException("Build não encontrada.");
        }
        compatibilidadeService.validarBuildCompleta(build);
        repositorio.atualizar(build);
    }

    public void remover(long id){
        if(id <= 0){
            throw new IllegalArgumentException("id inválido");
        }
        Build existente = repositorio.buscar(id);

        if (existente == null) {
            throw new BuildNaoEncontradaException("Build não encontrada.");
        }

        repositorio.remover(id);
    }
}
