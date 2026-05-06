package main.java.dados;

import main.java.model.Build;

import java.util.List;

public class RepositorioBuild implements IRepositorioBuild{

    private List<Build> builds;

    @Override
    public void cadastrar(Build build){
        //TODO
    }

    @Override
    public Build buscar(Long id){
        for(Build b : builds) {
            if(b.getId() == id) {
                return b;
            }
        }
        return null;
    }

    @Override
    public List<Build> getBuilds(){
        return builds;
    }

    @Override
    public void atualizar(Build build){
        //TODO
    }

    @Override
    public void remover(Long id){
        //TODO
    }
}
