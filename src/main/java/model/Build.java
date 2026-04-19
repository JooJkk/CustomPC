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
    private List<MemoriaRam> memorias = new ArrayList<>();
    private PlacaVideo gpu;
    private static long proximoId = 1;

    public void adicionarMemoria(MemoriaRam memoria) {
        memorias.add(memoria);
    }

    public void setFonte(Fonte fonte) {
        this.fonte = fonte;
    }

    public void setGpu(PlacaVideo gpu) {
        this.gpu = gpu;
    }

    public void setMemorias(List<MemoriaRam> memorias) {
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

    public List<MemoriaRam> getMemorias() {
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

        for (MemoriaRam memoria : memorias) {
            total += memoria.getConsumoWatts();
        }

        this.consumoTotalWatts = total;
        return total;
    }

    public boolean validarCompatibilidade() {
        if (processador == null || placaMae == null) {
            this.compativel = false;
            return false;
        }
        if (fonte != null && calcularConsumoTotal() > fonte.getPotenciaWatts()) {
            this.compativel = false;
            return false;
        }
        if (memorias.isEmpty()) {
            this.compativel = false;
            return false;
        }
        if (!processador.getSocket().equals(placaMae.getSocket())) {
            this.compativel = false;
            return false;
        }
        for (MemoriaRam memoria : memorias) {
            if (!memoria.getTipoRam().equals(placaMae.getTipoRamSuportada())) {
                this.compativel = false;
                return false;
            }
        }
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

        for (MemoriaRam memoria : memorias) {
            total += memoria.getPreco();
        }

        return total;
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
        this.id = proximoId;
        proximoId++;
    }

}
