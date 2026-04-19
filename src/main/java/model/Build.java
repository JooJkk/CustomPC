package main.java.model;

import java.util.ArrayList;
import java.util.List;

public class Build {
    private long id;
    private String nome;
    private int consumoTotalWatts;
    private boolean compativel;
    private Processador processador;
    private PlacaMae placaMae;
    private Fonte fonte;
    private List<MemoriaRAM> memorias = new ArrayList<>();
    private PlacaVideo gpu;

    public void addMemoria(MemoriaRAM memoria) {
        memorias.add(memoria);
    }

    public void setFonte(Fonte fonte) {
        this.fonte = fonte;
    }

    public void setGpu(PlacaVideo gpu) {
        this.gpu = gpu;
    }

    public void setMemorias(List<MemoriaRAM> memorias) {
        this.memorias = memorias;
    }

    public void setPlacaMae(PlacaMae placaMae) {
        this.placaMae = placaMae;
    }

    public void setProcessador(Processador processador) {
        this.processador = processador;
    }

    public Fonte getFonte() {
        return fonte;
    }

    public List<MemoriaRAM> getMemorias() {
        return memorias;
    }

    public PlacaMae getPlacaMae() {
        return placaMae;
    }

    public Processador getProcessador() {
        return processador;
    }

    public PlacaVideo getGpu() {
        return gpu;
    }

    public int calcularConsumoTotal() {
        int total = 0;
        if (processador != null) {
            total += processador.getConsumoWatts();
        }
        if (placaMae != null) {
            total += placaMae.getConsumoWatts();
        }
        if (gpu != null) {
            total += gpu.getConsumoWatts();
        }

        for (MemoriaRAM memoria : memorias) {
            total += memoria.getConsumoWatts();
        }

        this.consumoTotalWatts = total;
        return total;
    }

    public boolean validarCompatibilidade() {
        // ta faltando implementar a regra dos componentes aqui
        this.compativel = true;
        return compativel;
    }


    public double calcularPrecoTotal() {
        double total = 0.0;
        if (processador != null) {
            total += processador.getPreco();
        }
        if (placaMae != null) {
            total += placaMae.getPreco();
        }
        if (fonte != null) {
            total += fonte.getPreco();
        }
        if (gpu != null) {
            total += gpu.getPreco();
        }

        for (MemoriaRAM memoria : memorias) {
            total += memoria.getPreco();
        }

        return total;
    }


    public void setId (long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }

    public void setNome(String nome){
        this.nome = nome;
    }
    public String getNome() {
        return nome;
    }

    public void setConsumoTotalWatts(int consumoTotalWatts) {
        this.consumoTotalWatts  = consumoTotalWatts;
    }
    public int getConsumoTotalWatts(){
        return consumoTotalWatts;
    }

    public void setCompativel(boolean compativel){
        this.compativel = compativel;
    }
    public boolean isCompativel() {
        return compativel;
    }

    public Build() {

    }

}
