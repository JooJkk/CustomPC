package model;

public class CupomDesconto {
    private String codigo;
    private double percentual;
    private double valorMinimo;
    private boolean ativo;

    public CupomDesconto(String codigo, double percentual, double valorMinimo) {

        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Código inválido");
        }

        if (percentual <= 0 || percentual > 100) {
            throw new IllegalArgumentException("Percentual inválido");
        }

        if (valorMinimo < 0) {
            throw new IllegalArgumentException("Valor mínimo inválido");
        }

        this.codigo = codigo;
        this.percentual = percentual;
        this.valorMinimo = valorMinimo;
        this.ativo = true;
    }

    public String getCodigo() {
        return codigo;
    }

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
}
