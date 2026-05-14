package main.java.negocio;

import main.java.exception.EstoqueInsuficienteException;
import main.java.model.Componente;
import main.java.model.Carrinho;
import main.java.model.ItemCarrinho;

public class CarrinhoService {

    private Carrinho carrinho;

    public CarrinhoService() {
        carrinho = new Carrinho();
    }

    public void adicionarItem(Componente componente, int quantidade) {

        if (componente.getEstoque() < quantidade) {

            throw new EstoqueInsuficienteException(
                    "Estoque insuficiente"
            );
        }

        ItemCarrinho item =
                new ItemCarrinho(componente, quantidade);

        carrinho.getItens().add(item);

        componente.setEstoque(
                componente.getEstoque() - quantidade
        );

        System.out.println("Item adicionado");
    }

    public void removerItem(String nome) {

        for (int i = 0; i < carrinho.getItens().size(); i++) {

            ItemCarrinho item =
                    carrinho.getItens().get(i);

            if (item.getComponente()
                    .getNome()
                    .equalsIgnoreCase(nome)) {

                carrinho.getItens().remove(i);

                System.out.println("Item removido");
                return;
            }
        }

        System.out.println("Item não encontrado");
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
}