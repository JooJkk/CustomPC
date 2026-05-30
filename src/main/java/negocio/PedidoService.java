package negocio;
import dados.IRepositorioPedido;
import model.*;
import exception.*;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import com.itextpdf.layout.properties.TextAlignment;
import model.ItemPedido;
import model.Pedido;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
            // FONTES
            PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // TÍTULO
            Paragraph titulo = new Paragraph("NOTA FISCAL").setFont(bold).setFontSize(20).setTextAlignment(TextAlignment.CENTER);

            document.add(titulo);

            document.add(new Paragraph("\n"));

            // DADOS DO PEDIDO
            document.add(new Paragraph("Pedido #" + pedido.getId()).setFont(bold));

            document.add(new Paragraph("Cliente: " + usuario.getNome()).setFont(normal));

            document.add(new Paragraph("Data: " + pedido.getData()).setFont(normal));

            document.add(new Paragraph("Endereço: " + pedido.getEndereco()).setFont(normal));

            document.add(new Paragraph("\n"));

            // TABELA DE ITENS
            float[] colunas = {300f, 100f, 100f};
            Table tabela = new Table(colunas);

            tabela.addHeaderCell(new Cell().add(new Paragraph("Produto").setFont(bold)));
            tabela.addHeaderCell(new Cell().add(new Paragraph("Qtd").setFont(bold)));
            tabela.addHeaderCell(new Cell().add(new Paragraph("Preço").setFont(bold)));

            for(ItemPedido item : pedido.getItens()) {

                tabela.addCell(item.getComponente().getNome());

                tabela.addCell(String.valueOf(item.getQuantidade()));

                tabela.addCell(String.format(
                        "R$ %.2f",
                        item.getComponente().getPreco()
                ));
            }

            document.add(tabela);

            document.add(new Paragraph("\n"));

            // TOTAL
            Paragraph total = new Paragraph(String.format("TOTAL: R$ %.2f", pedido.getValorTotal())).setFont(bold).setFontSize(16).setTextAlignment(TextAlignment.RIGHT);
            document.add(total);

            document.add(new Paragraph("\n"));

            // RODAPÉ
            Paragraph rodape = new Paragraph("Obrigado pela preferência!").setTextAlignment(TextAlignment.CENTER);
            document.add(rodape);

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

    public List<Pedido> listarPedidosDoCliente(Cliente cliente) {
        return repositorio.buscarPorCliente(cliente);
    }
}