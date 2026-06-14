package negocio;

import model.CupomDesconto;
import model.ItemCarrinho;
import model.TipoCupom;
import model.componentes.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CupomService {
    private CupomService() {
    }
    private static CupomService instancia;
    public static synchronized CupomService getInstance() {
        if (instancia == null) {
            instancia = new CupomService();
        }
        return instancia;
    }
    private final List<CupomDesconto> cuponsGerados = new ArrayList<>();

    public CupomDesconto verificarEGerarCupom(List<ItemCarrinho> itens) {
        double totalCarrinho = calcularTotalCarrinho(itens);
        if (totalCarrinho >= 8000.0) {
            return gerarCupom(TipoCupom.MIN_VALUE, 15.0, 8000.0);
        }
        if (totalCarrinho >= 5000.0) {
            return gerarCupom(TipoCupom.MIN_VALUE, 10.0, 5000.0);
        }
        if (totalCarrinho >= 3000.0) {
            return gerarCupom(TipoCupom.MIN_VALUE, 5.0, 3000.0);
        }
        if (verificarBuildCompleta(itens)) {
            return gerarCupom(TipoCupom.FULL_BUILD, 10.0, 3000.0);
        }
        if (verificarMesmaMarca(itens)) {
            return gerarCupom(TipoCupom.SAME_BRAND, 7.0, 1500.0);
        }
        return null;
    }

    private CupomDesconto gerarCupom(TipoCupom tipo, double percentual, double valorMinimo) {
        CupomDesconto cupom = new CupomDesconto(tipo, percentual, valorMinimo);
        cuponsGerados.add(cupom);
        return cupom;
    }

    // Desconto em build completa
    private boolean verificarBuildCompleta(List<ItemCarrinho> itens) {
        Set<Class<?>> tiposPresentes = itens.stream().map(item -> item.getComponente().getClass()).collect(Collectors.toSet());
        return tiposPresentes.containsAll(Set.of(Processador.class, MemoriaRam.class, PlacaMae.class, Fonte.class, Gabinete.class, PlacaVideo.class));
    }

    //Desconto em 3 itens da mesma marca
    private boolean verificarMesmaMarca(List<ItemCarrinho> itens) {
        Map<String, Long> contagemPorMarca = itens.stream().collect(Collectors.groupingBy(item -> item.getComponente().getMarca(), Collectors.counting()));
        return contagemPorMarca.values().stream().anyMatch(quantidade -> quantidade >= 3);
    }

    public List<CupomDesconto> listarCuponsAtivos() {
        return cuponsGerados.stream().filter(CupomDesconto::isAtivo).collect(Collectors.toList());
    }

    public CupomDesconto buscarPorCodigo(String codigo) {
        return cuponsGerados.stream().filter(c -> c.getCodigo().equals(codigo)).findFirst().orElseThrow(() -> new IllegalArgumentException("Cupom não encontrado: " + codigo));
    }
    private double calcularTotalCarrinho(List<ItemCarrinho> itens) {
        return itens.stream().mapToDouble(item -> item.getComponente().getPreco() * item.getQuantidade()).sum();
    }
    public String verificarProgressoDesconto(List<ItemCarrinho> itens) {
        String componentesFaltando = calcularComponentesFaltando(itens);
        if (componentesFaltando != null) {
            return componentesFaltando;
        }
        int itensMesmaMarca = maiorQuantidadeMesmaMarca(itens);
        if (itensMesmaMarca == 2) {
            return "Compre mais 1 item da mesma marca para ganhar 7% de desconto!";
        }
        double total = calcularTotalCarrinho(itens);
        if (total > 1750 && total < 3000.0) {
            return String.format("Faltam R$ %.2f para ganhar 5%% de desconto por valor!", 3000.0 - total);
        } else if (total < 5000.0) {
            return String.format("Você já tem 5%%! Faltam R$ %.2f para subir para 10%% de desconto!", 5000.0 - total);
        } else if (total < 8000.0) {
            return String.format("Você já tem 10%%! Faltam R$ %.2f para atingir o desconto máximo de 15%%!", 8000.0 - total);
        }
        return null;
    }

    private String calcularComponentesFaltando(List<ItemCarrinho> itens) {

        Set<Class<?>> tiposPresentes = itens.stream().map(item -> item.getComponente().getClass()).collect(Collectors.toSet());
        List<String> faltantes = new ArrayList<>();

        if (!tiposPresentes.contains(Processador.class)) {
            faltantes.add("Processador");
        }
        if (!tiposPresentes.contains(MemoriaRam.class)) {
            faltantes.add("Memória RAM");
        }
        if (!tiposPresentes.contains(PlacaMae.class)) {
            faltantes.add("Placa-mãe");
        }
        if (!tiposPresentes.contains(Fonte.class)) {
            faltantes.add("Fonte");
        }
        if (!tiposPresentes.contains(Gabinete.class)) {
            faltantes.add("Gabinete");
        }
        if (!tiposPresentes.contains(PlacaVideo.class)) {
            faltantes.add("Placa de video");
        }
        if (faltantes.size() <= 2 && !faltantes.isEmpty()) {
            return "Você está perto de ganhar 10% de desconto! Adicione: " + String.join(" e ", faltantes) + " para adquirir o desconto.";
        }
        return null;
    }

    private int maiorQuantidadeMesmaMarca(List<ItemCarrinho> itens) {
        return itens.stream().collect(Collectors.groupingBy(item -> item.getComponente().getMarca(),
                Collectors.counting())).values().stream().mapToInt(Long::intValue).max().orElse(0);
    }
}