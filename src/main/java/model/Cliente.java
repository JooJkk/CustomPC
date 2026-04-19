public class Cliente {
    private int id;
    private String nome;
    private String email;
    private String senha;



    public void setId(int id) {
        this.id = id;

    }
    public int getId() {
        return id;
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

    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Cliente() {

    }

}