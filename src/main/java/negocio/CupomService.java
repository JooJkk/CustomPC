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

        return tiposPresentes.containsAll(Set.of(
                Processador.class,
                MemoriaRam.class,
                PlacaMae.class,
                Fonte.class
        ));
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

        int componentesFaltando = calcularComponentesFaltando(itens);

        if (componentesFaltando > 0 && componentesFaltando <= 2) {
            return "Faltam apenas " + componentesFaltando +
                    " componentes para ganhar 10% de desconto!";
        }

        int itensMesmaMarca = maiorQuantidadeMesmaMarca(itens);

        if (itensMesmaMarca >= 2 && itensMesmaMarca < 4) {
            return "Compre mais " + (4 - itensMesmaMarca) +
                    " item(ns) da mesma marca para ganhar 7% de desconto!";
        }

        return null;
    }

    private int calcularComponentesFaltando(List<ItemCarrinho> itens) {

        Set<Class<?>> tiposPresentes = itens.stream().map(item -> item.getComponente().getClass()).collect(Collectors.toSet());

        int faltando = 0;

        if (!tiposPresentes.contains(Processador.class)) faltando++;
        if (!tiposPresentes.contains(MemoriaRam.class)) faltando++;
        if (!tiposPresentes.contains(PlacaMae.class)) faltando++;
        if (!tiposPresentes.contains(Fonte.class)) faltando++;

        return faltando;
    }

    private int maiorQuantidadeMesmaMarca(List<ItemCarrinho> itens) {
        return itens.stream().collect(Collectors.groupingBy(item -> item.getComponente().getMarca(), Collectors.counting())).values().stream().mapToInt(Long::intValue).max().orElse(0);
    }
}