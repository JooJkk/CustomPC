package dados;

import model.componentes.Componente;
import java.util.List;

public class RepositorioComponente implements IRepositorioComponente {
    private List<Componente> componentes;

    @Override
    public void cadastrar(Componente componente) {
        componentes.add(componente);
    }

    @Override
    public Componente buscar(long id) {
        for (Componente componente : componentes) {
            if (componente.getId() == id) {
                return componente;
            }
        }
        return null;
    }

    @Override
    public List<Componente> listar() {
        return componentes;
    }

    @Override
    public void atualizar(Componente componente) {
        for(int i=0; i<componentes.size(); i++){
            if(componente.getId() == componentes.get(i).getId()){
                componentes.set(i, componente);
                return;
            }
        }
    }

    @Override
    public void remover(Long id) {
        for(int i=0; i<componentes.size(); i++){
            if(componentes.get(i).getId() == id){
                componentes.remove(i);
                return;
            }
        }
    }
}
