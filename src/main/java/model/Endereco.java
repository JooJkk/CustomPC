package main.java.model;

public class Endereco {
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String cep;
    private String estado;


    public Endereco(String rua, String numero, String bairro, String cidade, String cep, String estado) {
        setRua(rua);
        setNumero(numero);
        setBairro(bairro);
        setCidade(cidade);
        setCep(cep);
        setEstado(estado);
    }

    public String getRua() {
        return rua;
    }
    public void setRua(String rua) {
        if (rua == null || rua.isBlank()) {
            throw new IllegalArgumentException("rua não pode ser nula ou vazia.");
        }
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("número não pode ser nulo ou vazio.");
        }
        int num = Integer.parseInt(numero);
        if (num <= 0) {
            throw new IllegalArgumentException("número deve ser maior que zero.");
        }

        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }
    public void setBairro(String bairro) {
        if (bairro == null || bairro.isBlank()) {
            throw new IllegalArgumentException("bairro não pode ser nulo ou vazio.");
        }
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }
    public void setCidade(String cidade) {
        if (cidade == null || cidade.isBlank()) {
            throw new IllegalArgumentException("cidade não pode ser nula ou vazia.");
        }
        this.cidade = cidade;
    }

    public String getCep() {
        return cep;
    }
    public void setCep(String cep) {
        if (cep == null || cep.trim().isEmpty()) {
            throw new IllegalArgumentException("CEP não pode ser nulo ou vazio.");
        }
        if (!cep.matches("\\d{8}")) {
            throw new IllegalArgumentException("CEP deve conter exatamente 8 dígitos.");
        }
        this.cep = cep;
    }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new IllegalArgumentException("estado não pode ser nulo ou vazio.");
        }

        this.estado = estado;
        }
    }
