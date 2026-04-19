import java.util.ArrayList;

public class Build {
    private long id;
    private String nome;
    private int consumoTotalWatts;
    private Boolean compativel;

/*
    private List<Componente> componentes = new ArrayList<>();

    public void adicionarComponente(Componente componente) {
        componentes.add(componente);
    }

    public double calcularConsumoTotal() {
        int total = 0;
        for (Componente c : componentes) {
            total = total + c.getConsumoWatts();
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
        for (Componente c : componentes) {
            total = total + c.getPreco();
        }
        return total;
    }

 */


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

    public void setCompativel(Boolean compativel){
        this.compativel = compativel;
    }
    public Boolean getCompativel() {
        return compativel;
    }

    public Build() {

    }






}
