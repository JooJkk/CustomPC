package dados;

import model.componentes.Componente;
import java.util.List;

public interface IRepositorioComponente {
    void cadastrar(Componente componente);
    Componente buscar(long id);
    List<Componente> listar();
    void atualizar(Componente componente);
    void remover(Long id);
}
