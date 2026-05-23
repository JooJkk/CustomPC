package main.java.dados;
import main.java.model.componentes.Build;
import java.util.List;

public interface IRepositorioBuild {
    void cadastrar(Build build);
    Build buscar(long id);
    List<Build> listar();
    void atualizar(Build build);
    void remover(long id);
}
