package model;

public class Pagamento {
    private long id;
    private double valor;
    private String formaPagamento;
    private String status;
    private static long proximoId = 1;
    private  CupomDesconto cupom;
    public Pagamento(){
        this.id = proximoId++;
    }

    public Pagamento(double valor,
                     String formaPagamento,
                     String status) {

        this.id = proximoId++;

        setValor(valor);
        setFormaPagamento(formaPagamento);
        setStatus(status);
    }

    public CupomDesconto getCupom(){
        return cupom;
    }

    public void setCupom(CupomDesconto cupom) {
        this.cupom = cupom;
    }

    public long getId() {
        return id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        if(valor < 0){
            throw new IllegalArgumentException("valor não pode ser negativo");
        }
        this.valor = valor;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        if(formaPagamento == null || formaPagamento.isBlank()){
            throw new IllegalArgumentException("forma de pagamento inválida");
        }
        this.formaPagamento = formaPagamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {

        if(status == null || status.isBlank()){
            throw new IllegalArgumentException("status não pode ser nulo");
        }
        this.status = status;
    }

}