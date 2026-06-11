package negocio;

import model.OrdemMontagem;
import model.Pedido;
import model.componentes.Componente;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import model.ItemPedido;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class RelatoriosService {
    public void gerarRelatorioBaixoEstoque(List<Componente> componentes, int limiteEstoque, String caminho)
            throws IOException {

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Baixo Estoque");

            CellStyle headerStyle = criarEstiloHeader(wb, new byte[]{(byte)198, (byte)224, (byte)180});
            CellStyle alertaStyle = criarEstiloAlerta(wb);

            Row header = sheet.createRow(0);
            String[] colunas = {"ID", "Componente", "Qtd. Atual", "Qtd. Mínima", "Situação"};
            for (int i = 0; i < colunas.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(colunas[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (Componente c : componentes) {
                if (c.getEstoque() <= limiteEstoque) {
                    Row row = sheet.createRow(rowIdx++);
                    row.createCell(0).setCellValue(c.getId());
                    row.createCell(1).setCellValue(c.getNome());
                    row.createCell(2).setCellValue(c.getEstoque());
                    row.createCell(3).setCellValue(limiteEstoque);

                    Cell situacao = row.createCell(4);
                    boolean critico = c.getEstoque() == 0;
                    situacao.setCellValue(critico ? "SEM ESTOQUE" : "CRÍTICO");
                    if (critico) situacao.setCellStyle(alertaStyle);
                }
            }

            for (int i = 0; i < colunas.length; i++) sheet.autoSizeColumn(i);

            try (FileOutputStream fos = new FileOutputStream(caminho)) {
                wb.write(fos);
            }
        }
    }

    public void gerarFaturamentoMensal(List<Pedido> pedidos, int mes, int ano, String caminho)
            throws IOException {

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Faturamento " + mes + "-" + ano);

            CellStyle headerStyle = criarEstiloHeader(wb, new byte[]{(byte)189, (byte)215, (byte)238});
            CellStyle moedaStyle  = criarEstiloMoeda(wb);
            CellStyle percentStyle = criarEstiloPercent(wb);

            String[] colunas = {
                    "Pedido", "Data", "Cliente", "Produto",
                    "Qtd", "Custo Unit.", "Preço Unit.",
                    "Subtotal", "Custo Total", "Lucro", "Margem %"
            };
            Row header = sheet.createRow(0);
            for (int i = 0; i < colunas.length; i++) {
                Cell c = header.createCell(i);
                c.setCellValue(colunas[i]);
                c.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (Pedido pedido : pedidos) {
                // SEGURANÇA: Ignora se o pedido ou a data estiverem nulos
                if (pedido == null || pedido.getData() == null) continue;

                // FILTRO REAL: Só adiciona se o pedido for do mês e ano selecionados
                int mesPedido = pedido.getData().getMonthValue();
                int anoPedido = pedido.getData().getYear();
                if (mesPedido != mes || anoPedido != ano) continue;

                // SEGURANÇA: Ignora se a lista de itens estiver vazia
                if (pedido.getItens() == null) continue;

                for (ItemPedido item : pedido.getItens()) {
                    if (item == null || item.getComponente() == null) continue;

                    Row row = sheet.createRow(rowIdx);

                    row.createCell(0).setCellValue(pedido.getId());
                    row.createCell(1).setCellValue(pedido.getData().toLocalDate().toString());

                    // Evita NullPointerException no Cliente
                    String nomeCliente = (pedido.getCliente() != null) ? pedido.getCliente().getNome() : "Não informado";
                    row.createCell(2).setCellValue(nomeCliente);

                    row.createCell(3).setCellValue(item.getComponente().getNome());
                    row.createCell(4).setCellValue(item.getQuantidade());

                    Cell custoUnit = row.createCell(5);
                    custoUnit.setCellValue(item.getComponente().getPreco());
                    custoUnit.setCellStyle(moedaStyle);

                    Cell precoUnit = row.createCell(6);
                    precoUnit.setCellValue(item.getPrecoUnitario());
                    precoUnit.setCellStyle(moedaStyle);

                    int r = rowIdx + 1;
                    Cell subtotal = row.createCell(7);
                    subtotal.setCellFormula("E" + r + "*G" + r);
                    subtotal.setCellStyle(moedaStyle);

                    Cell custoTotal = row.createCell(8);
                    custoTotal.setCellFormula("E" + r + "*F" + r);
                    custoTotal.setCellStyle(moedaStyle);

                    Cell lucro = row.createCell(9);
                    lucro.setCellFormula("H" + r + "-I" + r);
                    lucro.setCellStyle(moedaStyle);

                    Cell margem = row.createCell(10);
                    margem.setCellFormula("IF(H" + r + "=0,0,J" + r + "/H" + r + ")");
                    margem.setCellStyle(percentStyle);

                    rowIdx++;
                }
            }

            // Apenas cria a linha de totais se houveram dados populados (rowIdx > 1)
            if (rowIdx > 1) {
                Row totais = sheet.createRow(rowIdx + 1);
                totais.createCell(0).setCellValue("TOTAL");
                Cell totalSubtotal = totais.createCell(7);
                totalSubtotal.setCellFormula("SUM(H2:H" + rowIdx + ")");
                totalSubtotal.setCellStyle(moedaStyle);

                Cell totalLucro = totais.createCell(9);
                totalLucro.setCellFormula("SUM(J2:J" + rowIdx + ")");
                totalLucro.setCellStyle(moedaStyle);

                Cell margemGeral = totais.createCell(10);
                margemGeral.setCellFormula("IF(H" + (rowIdx+2) + "=0,0,J" + (rowIdx+2) + "/H" + (rowIdx+2) + ")");
                margemGeral.setCellStyle(percentStyle);
            }

            for (int i = 0; i < colunas.length; i++) sheet.autoSizeColumn(i);

            try (FileOutputStream fos = new FileOutputStream(caminho)) {
                wb.write(fos);
            }
        }
    }
    public void gerarOrdensPendentes(List<Pedido> pedidos, String caminho) throws IOException {
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Ordens Pendentes");
            sheet.createFreezePane(0, 1);
            sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, 4));

            CellStyle headerStyle = criarEstiloHeader(wb, new byte[]{(byte)255, (byte)230, (byte)153});
            CellStyle moedaStyle  = criarEstiloMoeda(wb);

            Row header = sheet.createRow(0);
            String[] colunas = {"ID Pedido", "Cliente", "Data Pedido", "Valor Total", "Status Ordem"};
            for (int i = 0; i < colunas.length; i++) {
                Cell c = header.createCell(i);
                c.setCellValue(colunas[i]);
                c.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            LocalDate hoje = LocalDate.now();

            for (Pedido pedido : pedidos) {
                OrdemMontagem ordem = pedido.getOrdemMontagem();
                if (ordem == null) continue;
                if (!"PENDENTE".equalsIgnoreCase(ordem.getStatus())) continue;

                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(pedido.getId());
                row.createCell(1).setCellValue(pedido.getCliente().getNome());
                row.createCell(2).setCellValue(pedido.getData().toLocalDate().toString());

                Cell total = row.createCell(3);
                total.setCellValue(pedido.getValorTotal());
                total.setCellStyle(moedaStyle);

                long diasEmAberto = ChronoUnit.DAYS.between(ordem.getDataCriacao(), hoje);
                Cell statusCell = row.createCell(4);
                statusCell.setCellValue("PENDENTE (" + diasEmAberto + " dias)");

                // destaca em laranja se atrasado
                if (diasEmAberto > 7) {
                    CellStyle atrasado = wb.createCellStyle();
                    atrasado.cloneStyleFrom(moedaStyle);
                    atrasado.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
                    atrasado.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    statusCell.setCellStyle(atrasado);
                }
            }

            for (int i = 0; i < colunas.length; i++) sheet.autoSizeColumn(i);

            try (FileOutputStream fos = new FileOutputStream(caminho)) {
                wb.write(fos);
            }
        }
    }
    private CellStyle criarEstiloHeader(Workbook wb, byte[] rgb) {
        CellStyle style = wb.createCellStyle();
        XSSFCellStyle xs = (XSSFCellStyle) style;
        xs.setFillForegroundColor(new XSSFColor(rgb, null));
        xs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = wb.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }

    private CellStyle criarEstiloMoeda(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        DataFormat fmt = wb.createDataFormat();
        style.setDataFormat(fmt.getFormat("R$ #,##0.00"));
        return style;
    }

    private CellStyle criarEstiloPercent(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        DataFormat fmt = wb.createDataFormat();
        style.setDataFormat(fmt.getFormat("0.00%"));
        return style;
    }

    private CellStyle criarEstiloAlerta(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(IndexedColors.RED.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = wb.createFont();
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setBold(true);
        style.setFont(font);
        return style;
        }
}
