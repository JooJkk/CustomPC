package negocio;
import dados.IRepositorioPedido;
import model.*;
import exception.*;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import model.ItemPedido;
import model.Pedido;

import java.nio.file.Files;
import java.nio.file.Paths;

public class PedidoService {
    private IRepositorioPedido repositorio;

    public PedidoService(IRepositorioPedido repositorio) {
        this.repositorio = repositorio;
    }

    public void cancelarPedido(int id) throws PedidoNaoEncontradoException, PedidoEnviadoException {
        Pedido p = repositorio.buscarPorId(id);

        if (p == null) {
            throw new PedidoNaoEncontradoException("ID " + id + " não encontrado.");
        }

        if ("ENVIADO".equalsIgnoreCase(p.getStatus())) {
            throw new PedidoEnviadoException();
        }

        repositorio.deletar(id);
        System.out.println("Pedido " + id + " foi cancelado com sucesso.");
    }
    public static void gerarPDF(Pedido pedido, Cliente usuario) {

        try {
            Files.createDirectories(Paths.get("notas"));
            String destino = "notas/nota_pedido_" + pedido.getId() + ".pdf";
            PdfWriter writer = new PdfWriter(destino);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            document.add(new Paragraph("NOTA FISCAL"));
            document.add(new Paragraph("Pedido #" + pedido.getId()));
            document.add(new Paragraph("Cliente: " + pedido.getCliente().getNome()));
            document.add(new Paragraph("Valor Total: R$ " + pedido.getValorTotal()));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Itens:"));
            for(ItemPedido item : pedido.getItens()) {
                document.add(new Paragraph(item.getComponente().getNome() + " - Qtd: " + item.getQuantidade()));
            }
            document.close();
            System.out.println("PDF gerado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public double calcularFrete(Endereco endereco) {
        return 25.0;
    }
    public Pedido finalizarCompra(Carrinho carrinho, Endereco endereco, Pagamento pagamento, Cliente cliente) throws CarrinhoVazioException {
        if (carrinho.getItens() == null || carrinho.getItens().isEmpty()) {
            throw new CarrinhoVazioException();
        }

        Pedido novoPedido = new Pedido();
        novoPedido.setCliente(cliente);
        novoPedido.setId(carrinho.getId());
        novoPedido.setEndereco(endereco);
        double subtotal = carrinho.getValorTotal();
        double frete = calcularFrete(endereco);
        double total = subtotal + frete;
        novoPedido.setFrete(frete);

        novoPedido.setValorTotal(total);

        pagamento.setValor(total);
        novoPedido.setPagamento(pagamento);
        novoPedido.getPagamento().setStatus("PENDENTE");
        novoPedido.setStatus("PENDENTE");
        for (ItemCarrinho itemC : carrinho.getItens()) {

            ItemPedido itemP = new ItemPedido();
            itemP.setQuantidade(itemC.getQuantidade());
            itemP.setPrecoUnitario(itemC.getPrecoUnitario());

            itemP.setComponente(itemC.getComponente());
            novoPedido.adicionarItem(itemP);
        }

        repositorio.salvar(novoPedido);

        carrinho.limpar();

        return novoPedido;
    }
}