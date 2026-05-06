package main.java.dados;
import main.java.model.Build;
import java.util.List;

public interface IRepositorioBuild {
    void cadastrar(Build build);
    Build buscar(Long id);
    List<Build> getBuilds();
    void atualizar(Build build);
    void remover(Long id);
}
