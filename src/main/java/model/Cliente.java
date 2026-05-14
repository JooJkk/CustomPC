package main.java.model;

public class Cliente {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private static int proximoId = 1;



    public int getId() {
        return id;
    }


    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        if (nome == null || nome.isBlank() ) {
            throw new IllegalArgumentException("nome nao pode ser nulo");
        }
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail (String email) {
        if (email == null || email.isBlank() ) {
            throw new IllegalArgumentException("email nao pode ser nulo");
        }
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        if (senha == null || senha.isBlank() ) {
            throw new IllegalArgumentException("senha nao pode ser nulo");
        }
        this.senha = senha;
    }

    public Cliente(String nome, String email, String senha) {
        this.id = proximoId++;

        setNome(nome);
        setEmail(email);
        setSenha(senha);
    }

}