package main.java.dados;

import main.java.model.componentes.Build;

import java.util.List;

public class RepositorioBuild implements IRepositorioBuild{

    private List<Build> builds;

    @Override
    public void cadastrar(Build build){
        builds.add(build);
    }

    @Override
    public Build buscar(long id){
        for(Build b : builds) {
            if(b.getId() == id) {
                return b;
            }
        }
        return null;
    }

    @Override
    public List<Build> listar(){
        return builds;
    }

    @Override
    public void atualizar(Build build){
        for(int i = 0; i < builds.size(); i++) {
            if(builds.get(i).getId() == build.getId()) {
                builds.set(i, build);
                return;
            }
        }
    }

    @Override
    public void remover(long id){
        for(int i = 0; i < builds.size(); i++) {
            if(builds.get(i).getId() == id) {
                builds.remove(i);
                return;
            }
        }
    }
}
