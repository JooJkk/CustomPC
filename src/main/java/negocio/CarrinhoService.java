package negocio;

import exception.EstoqueInsuficienteException;
import model.componentes.Componente;
import model.Carrinho;
import model.ItemCarrinho;

public class CarrinhoService {

    private Carrinho carrinho;

    private static CarrinhoService instancia;

    private CarrinhoService() {
        carrinho = new Carrinho();
    }

    public static synchronized CarrinhoService getInstance() {
        if (instancia == null) {
            instancia = new CarrinhoService();
        }
        return instancia;
    }
    public void adicionarItem(Componente componente, int quantidade) {

        if (componente.getEstoque() < quantidade) {

            throw new EstoqueInsuficienteException(
                    "Estoque insuficiente"
            );
        }
        for (ItemCarrinho item : carrinho.getItens()) {
            if (item.getComponente().equals(componente)) {
                item.setQuantidade(item.getQuantidade() + quantidade);
                componente.setEstoque(componente.getEstoque() - quantidade);
                return;
            }
        }
        ItemCarrinho item =
                new ItemCarrinho(componente, quantidade);

        carrinho.adicionarItem(item);

        componente.setEstoque(
                componente.getEstoque() - quantidade
        );

    }

    public void removerItem(String nome) {

        for (int i = 0; i < carrinho.getItens().size(); i++) {

            ItemCarrinho item =
                    carrinho.getItens().get(i);

            if (item.getComponente()
                    .getNome()
                    .equalsIgnoreCase(nome)) {

                carrinho.removerItem(item);

                return;
            }
        }

    }

    public void listarItens() {

        if (carrinho.getItens().isEmpty()) {

            System.out.println("Carrinho vazio");
            return;
        }

        for (ItemCarrinho item : carrinho.getItens()) {

            System.out.println(
                    item.getComponente().getNome()
                            + " Quantidade:"
                            + item.getQuantidade()
                            + " Preço:"
                            + item.getComponente().getPreco()
            );
        }
    }

    public void atualizarQuantidade(
            String nome,
            int novaQuantidade
    ) {

        for (ItemCarrinho item : carrinho.getItens()) {

            if (item.getComponente()
                    .getNome()
                    .equalsIgnoreCase(nome)) {

                item.setQuantidade(novaQuantidade);

                System.out.println(
                        "Quantidade atualizada"
                );

                return;
            }
        }

        System.out.println("Item não encontrado");
    }

    public double calcularTotal() {

        double total = 0;

        for (ItemCarrinho item : carrinho.getItens()) {

            total += item.getComponente()
                    .getPreco()
                    * item.getQuantidade();
        }

        return total;
    }
    public Carrinho getCarrinho() {
        return carrinho;
    }
}