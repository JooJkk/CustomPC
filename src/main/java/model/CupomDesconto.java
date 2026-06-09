package model;

import java.util.UUID;

public class CupomDesconto {
    private TipoCupom tipo;
    private String codigo;
    private double percentual;
    private double valorMinimo;
    private boolean ativo;

    public CupomDesconto(TipoCupom tipo, double percentual, double valorMinimo) {
        if (tipo == null)
            throw new IllegalArgumentException("Tipo inválido");

        if (percentual <= 0 || percentual > 100)
            throw new IllegalArgumentException("Percentual inválido");

        if (valorMinimo < 0)
            throw new IllegalArgumentException("Valor mínimo inválido");

        this.tipo = tipo;
        this.codigo = gerarCodigo(tipo);
        this.percentual = percentual;
        this.valorMinimo = valorMinimo;
        this.ativo = true;
    }

    private String gerarCodigo(TipoCupom tipo) {
        String prefixo = switch (tipo) {
            case FULL_BUILD -> "BUILD";
            case SAME_BRAND -> "BRAND";
            case MIN_VALUE  -> "VALUE";
        };
        return prefixo + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
    public TipoCupom getTipo() { return tipo; }
    public String getCodigo() { return codigo; }
    public double getPercentual() {
        return percentual;
    }

    public double getValorMinimo() {
        return valorMinimo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void ativar() {
        ativo = true;
    }

    public void desativar() {
        ativo = false;
    }

    public boolean validarCupom(double valorCompra) {
        return ativo && valorCompra >= valorMinimo;
    }
    public double calcularDesconto(double valorCompra) {
        if (!validarCupom(valorCompra)) return 0.0;
        return valorCompra * (percentual / 100.0);
    }
}
