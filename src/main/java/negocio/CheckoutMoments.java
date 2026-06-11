package negocio;

import java.util.HashMap;
import java.util.Map;

public class CheckoutMoments {
    private static CheckoutMoments instance;


    private final Map<String, String> dadosTemporarios = new HashMap<>();

    private CheckoutMoments() {}

    public static CheckoutMoments getInstance() {
        if (instance == null) {
            instance = new CheckoutMoments();
        }
        return instance;
    }

    public void salvarCampo(String idCampo, String valor) {
        dadosTemporarios.put(idCampo, valor);
    }

    public String pegarCampo(String idCampo) {
        return dadosTemporarios.getOrDefault(idCampo, "");
    }

    public void limparDados() {
        dadosTemporarios.clear();
    }
}