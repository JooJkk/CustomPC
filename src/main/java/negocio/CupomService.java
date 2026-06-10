package negocio;

import model.Carrinho;
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

        if (verificarBuildCompleta(itens))
            return gerarCupom(TipoCupom.FULL_BUILD, 10.0, 0.0);
        if (verificarMesmaMarca(itens))
            return gerarCupom(TipoCupom.SAME_BRAND, 7.0, 0.0);
        if (verificarValorMinimo(itens, 5000.0))
            return gerarCupom(TipoCupom.MIN_VALUE, 5.0, 5000.0);

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

        return tiposPresentes.containsAll(Set.of(Processador.class, MemoriaRam.class, PlacaMae.class, Fonte.class));
    }

    private boolean verificarValorMinimo(List<ItemCarrinho> itens, double valorMinimo) {
        double total = itens.stream().mapToDouble(item -> item.getComponente().getPreco() * item.getQuantidade()).sum();
        return total >= valorMinimo;
    }

    //Desconto em 2 itens da mesma marca
    private boolean verificarMesmaMarca(List<ItemCarrinho> itens) {
        Map<String, Long> contagemPorMarca = itens.stream().collect(Collectors.groupingBy(item -> item.getComponente().getMarca(), Collectors.counting()));
        return contagemPorMarca.values().stream().anyMatch(quantidade -> quantidade >= 2);
    }

    public List<CupomDesconto> listarCuponsAtivos() {
        return cuponsGerados.stream()
                .filter(CupomDesconto::isAtivo)
                .collect(Collectors.toList());
    }

    public CupomDesconto buscarPorCodigo(String codigo) {
        return cuponsGerados.stream().filter(c -> c.getCodigo().equals(codigo)).findFirst().orElseThrow(() -> new IllegalArgumentException("Cupom não encontrado: " + codigo));
    }

    public String verificarProgressoDesconto(List<ItemCarrinho> itens) {

        String componentesFaltando = calcularComponentesFaltando(itens);

        if (componentesFaltando != null) {
            return "Faltam apenas " + componentesFaltando + " componentes para ganhar 10% de desconto!";
        }

        int itensMesmaMarca = maiorQuantidadeMesmaMarca(itens);

        if (itensMesmaMarca >= 2 && itensMesmaMarca < 4) {
            if(4 - itensMesmaMarca == 1){
                return "Compre mais 1 item da mesma marca para ganhar 7% de desconto!";
            }
            return "Compre mais " + (4 - itensMesmaMarca) + " item(ns) da mesma marca para ganhar 7% de desconto!";
        }
        double total = itens.stream().mapToDouble(item -> item.getComponente().getPreco() * item.getQuantidade()).sum();

        double valorMeta = 5000.0;
        if (total >= valorMeta * 0.75 && total < valorMeta) {
            return String.format("Faltam apenas R$ %.2f para ganhar 5%% de desconto!", valorMeta - total);
        }

        return null;
    }

    private String calcularComponentesFaltando(List<ItemCarrinho> itens) {

        Set<Class<?>> tiposPresentes = itens.stream().map(item -> item.getComponente().getClass()).collect(Collectors.toSet());

        int faltando = 0;
        List<String> faltantes = new ArrayList<>();

        if (!tiposPresentes.contains(Processador.class))
            faltantes.add("Processador");
        if (!tiposPresentes.contains(MemoriaRam.class))
            faltantes.add("Memória RAM");
        if (!tiposPresentes.contains(PlacaMae.class))
            faltantes.add("Placa-mãe");
        if (!tiposPresentes.contains(Fonte.class))
            faltantes.add("Fonte");
        if (faltantes.size() <= 2 && !faltantes.isEmpty()) {
            return "Você está perto de ganhar 10% de desconto! Adicione: " + String.join(" e ", faltantes) + " para completar uma build.";
        }
        return null;
    }

    private int maiorQuantidadeMesmaMarca(List<ItemCarrinho> itens) {
        return itens.stream().collect(Collectors.groupingBy(item -> item.getComponente().getMarca(), Collectors.counting())).values().stream().mapToInt(Long::intValue).max().orElse(0);
    }
}