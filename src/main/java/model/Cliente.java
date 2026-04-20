package main.java.model;

public class Cliente {
    private long id;
    private String nome;
    private String email;
    private String senha;
    private Endereco endereco;
    private String cpf;
    private static long proximoId = 1;

    public String getCpf() {
        return cpf;
    }

    public long getId() {
        return id;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        if (nome == null || nome.isEmpty() ) {
            throw new IllegalArgumentException("nome nao pode ser nulo");
        }
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail (String email) {
        this.email = email;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Cliente() {
        this.id = proximoId;
        proximoId++;
    }
    public Cliente(String nome, String email, String senha) {
        this.id = proximoId;
        proximoId++;
        setNome(nome);
        setEmail(email);
        setSenha(senha);
    }

}