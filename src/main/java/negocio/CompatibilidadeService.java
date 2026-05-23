package main.java.negocio;

import main.java.exception.BuildIncompativelException;
import main.java.model.componentes.Build;
import main.java.model.componentes.MemoriaRam;
import main.java.model.componentes.PlacaMae;

public class CompatibilidadeService {

    public void validarBuildCompleta(Build build) {
        validarComponentesObrigatorios(build);
        validarSocket(build);
        validarSlotsETipoMemoria(build);
        validarPotenciaFonte(build);
        validarGargalo(build);
    }


    private void validarComponentesObrigatorios(Build build) {
        if (build.getProcessador() == null || build.getPlacaMae() == null ||
                build.getMemorias().isEmpty() || build.getFonte() == null) {
            throw new BuildIncompativelException("A build deve ter obrigatoriamente: Processador, Placa-mãe, RAM e Fonte.");
        }
    }


    private void validarSocket(Build build) {
        if (!build.getProcessador().getSocket().equals(build.getPlacaMae().getSocket())) {
            throw new BuildIncompativelException("Incompatibilidade de Socket! Processador: " +
                    build.getProcessador().getSocket() + " vs Placa-mãe: " + build.getPlacaMae().getSocket());
        }
    }

    private void validarSlotsETipoMemoria(Build build) {
        PlacaMae mobo = build.getPlacaMae();


        if (build.getMemorias().size() > mobo.getSlotsRam()) {
            throw new BuildIncompativelException("Quantidade de memórias (" + build.getMemorias().size() +
                    ") excede os slots da placa-mãe (" + mobo.getSlotsRam() + ").");
        }


        for (MemoriaRam ram : build.getMemorias()) {
            if (!ram.getTipoRam().equals(mobo.getTipoRamSuportada())) {
                throw new BuildIncompativelException("Tipo de RAM incompatível! A placa suporta " +
                        mobo.getTipoRamSuportada() + " mas a memória é " + ram.getTipoRam());
            }
        }
    }


    private void validarPotenciaFonte(Build build) {
        int consumoTotal = build.calcularConsumoTotal();

        double consumoRecomendado = consumoTotal * 1.2;

        if (build.getFonte().getPotenciaWatts() < consumoRecomendado) {
            throw new BuildIncompativelException("Fonte insuficiente! Consumo total com margem: " +
                    consumoRecomendado + "W. Fonte atual: " + build.getFonte().getPotenciaWatts() + "W.");
        }
    }


    private void validarGargalo(Build build) {
        if (build.getGpu() != null) {
            int cpuPower = build.getProcessador().getNivelDesempenho();
            int gpuPower = build.getGpu().getNivelDesempenho();


            if (Math.abs(cpuPower - gpuPower) > 3) {
                String pecaFraca = (cpuPower < gpuPower) ? "Processador" : "Placa de Vídeo";
                throw new BuildIncompativelException("Aviso de Gargalo Crítico: O " + pecaFraca +
                        " limitará muito o desempenho do conjunto.");
            }
        }
    }
}