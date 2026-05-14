package main.java.model;

public class PlacaVideo extends Componente{
    private int comprimentoMM;
    private int memoriaGB;
    public PlacaVideo(String nome, String marca, double preco, double peso, int estoque, int consumoWatts, int comprimentoMM, int memoriaGB) {

        super(nome, marca, preco, peso, estoque, consumoWatts);

        setComprimentoMM(comprimentoMM);
        setMemoriaGB(memoriaGB);
    }

    public PlacaVideo() {
        super();
    }

    public int getComprimentoMM() {
        return comprimentoMM;
    }

    public void setComprimentoMM(int comprimentoMM) {

        if(comprimentoMM <= 0){
            throw new IllegalArgumentException(
                    "comprimento deve ser maior que zero"
            );
        }

        this.comprimentoMM = comprimentoMM;
    }

    public int getMemoriaGB() {
        return memoriaGB;
    }

    public void setMemoriaGB(int memoriaGB) {

        if(memoriaGB <= 0){
            throw new IllegalArgumentException(
                    "memória deve ser maior que zero"
            );
        }

        this.memoriaGB = memoriaGB;
    }
}