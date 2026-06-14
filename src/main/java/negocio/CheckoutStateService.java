package negocio;

import java.util.HashMap;
import java.util.Map;

public class CheckoutStateService {
    private static CheckoutStateService instance;


    private final Map<String, String> dadosTemporarios = new HashMap<>();

    private CheckoutStateService() {}

    public static synchronized CheckoutStateService getInstance() {
        if (instance == null) {
            instance = new CheckoutStateService();
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