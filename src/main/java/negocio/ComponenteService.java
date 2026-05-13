package main.java.negocio;

import main.java.dados.IRepositorioComponente;
import main.java.model.Componente;
import main.java.exception.*;
import java.util.List;

public class ComponenteService {
    IRepositorioComponente repositorio;
    public ComponenteService(IRepositorioComponente repositorio) {
        if(repositorio != null) {
            this.repositorio = repositorio;
        }
        else {
            throw new IllegalArgumentException("Repositorio nulo.");
        }
    }

    public void cadastrar(Componente componente) {
        if(componente == null) {
            throw new ComponenteInvalidoException("Componente nulo.");
        }
        validarDadosBasicos(componente);
        repositorio.cadastrar(componente);
    }

    public Componente buscar(long id) {
        Componente comp = repositorio.buscar(id);
        if(comp == null) {
            throw new ComponenteNotFoundException("Componente nao encontrado.");
        }
        return comp;
    }

    public List<Componente> listar() {
        return repositorio.listar();
    }

    public void atualizar(Componente componente) {
        if(componente == null) {
            throw new ComponenteInvalidoException("Componente nulo.");
        }
        Componente comp = repositorio.buscar(componente.getId());
        validarDadosBasicos(comp);
        repositorio.atualizar(componente);
    }

    public void remover(long id) {
        Componente comp = repositorio.buscar(id);
        if(comp == null) {
            throw new ComponenteNotFoundException("Componente nao encontrado.");
        }
        repositorio.remover(id);
    }

    private void validarDadosBasicos(Componente componente) {
        if (componente.getNome() == null || componente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do componente é obrigatório.");
        }
        if (componente.getPreco() <= 0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero.");
        }
        if (componente.getEstoque() < 0) {
            throw new IllegalArgumentException("O estoque físico não pode ser negativo.");
        }
    }
}
