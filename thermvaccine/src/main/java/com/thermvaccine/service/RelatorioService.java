package com.thermvaccine.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.thermvaccine.model.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class RelatorioService {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DeviceRgb GREEN_DARK  = new DeviceRgb(34, 105, 66);
    private static final DeviceRgb GREEN_LIGHT = new DeviceRgb(142, 199, 146);
    private static final DeviceRgb GRAY        = new DeviceRgb(245, 245, 245);
    private static final DeviceRgb RED         = new DeviceRgb(192, 57, 43);

    private final CaixaService    caixaService    = new CaixaService();
    private final ComandaService  comandaService  = new ComandaService();

    public void gerarRelatorio(String placa, UserLogistica usuario, String caminho) {
        try (PdfWriter writer = new PdfWriter(caminho);
             PdfDocument pdf = new PdfDocument(writer);
             Document doc = new Document(pdf)) {

            doc.setMargins(40, 50, 40, 50);

            // ── CABEÇALHO ─────────────────────────────────────
            Paragraph cabecalho = new Paragraph("ThermVaccine")
                    .setFontSize(22)
                    .setBold()
                    .setFontColor(GREEN_DARK)
                    .setTextAlignment(TextAlignment.CENTER);
            doc.add(cabecalho);

            Paragraph subtitulo = new Paragraph("Relatório de Conformidade do Transporte")
                    .setFontSize(13)
                    .setFontColor(ColorConstants.DARK_GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(4);
            doc.add(subtitulo);

            Paragraph dataGeracao = new Paragraph("Gerado em: " + java.time.LocalDateTime.now().format(FMT))
                    .setFontSize(10)
                    .setFontColor(ColorConstants.GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            doc.add(dataGeracao);

            linhaHorizontal(doc);

            // ── DADOS DO TRANSPORTE ────────────────────────────
            doc.add(secao("Transporte"));

            Table tTransporte = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                    .useAllAvailableWidth()
                    .setMarginBottom(16);

            addLinha(tTransporte, "Placa", placa);

            List<Caixa> caixas = caixaService.caixasPorTransporte(placa);

            // pega data saida da primeira comanda disponivel
            String dataSaida = "—";
            String dataChegada = "—";
            for (Caixa c : caixas) {
                List<Comanda> cmds = comandaService.comandaPorCaixa(c.getId());
                for (Comanda cmd : cmds) {
                    if (cmd.getData_saida() != null && dataSaida.equals("—"))
                        dataSaida = cmd.getData_saida().format(FMT);
                    if (cmd.getData_Chegada() != null && dataChegada.equals("—"))
                        dataChegada = cmd.getData_Chegada().format(FMT);
                }
            }

            addLinha(tTransporte, "Data de Saída",   dataSaida);
            addLinha(tTransporte, "Data de Chegada", dataChegada);
            addLinha(tTransporte, "Total de Caixas", String.valueOf(caixas.size()));
            doc.add(tTransporte);

            // ── POR CAIXA ──────────────────────────────────────
            int numCaixa = 1;
            int totalLotes = 0;
            int totalAprovados = 0;

            for (Caixa caixa : caixas) {
                doc.add(secao("Caixa #" + numCaixa++));

                // datalogger
                DataLogger dl = caixaService.dataLoggerDaCaixa(caixa.getId());
                if (dl != null) {
                    Table tDl = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                            .useAllAvailableWidth()
                            .setMarginBottom(8);
                    addLinha(tDl, "Datalogger", dl.getModelo() + " — " + dl.getId());

                    List<RegistroDatalogger> registros = dl.getRegistroDatalogger();
                    if (registros != null && !registros.isEmpty()) {
                        double min  = registros.stream().mapToDouble(RegistroDatalogger::getTemperatura).min().orElse(0);
                        double max  = registros.stream().mapToDouble(RegistroDatalogger::getTemperatura).max().orElse(0);
                        double media = registros.stream().mapToDouble(RegistroDatalogger::getTemperatura).average().orElse(0);
                        addLinha(tDl, "Temp. Mínima",  String.format("%.2f °C", min));
                        addLinha(tDl, "Temp. Máxima",  String.format("%.2f °C", max));
                        addLinha(tDl, "Temp. Média",   String.format("%.2f °C", media));
                        addLinha(tDl, "Total Registros", String.valueOf(registros.size()));
                    } else {
                        addLinha(tDl, "Registros", "Nenhum registro disponível");
                    }
                    doc.add(tDl);
                }

                // comandas
                List<Comanda> comandas = comandaService.comandaPorCaixa(caixa.getId());
                for (Comanda comanda : comandas) {
                    doc.add(new Paragraph("Comanda — CEP: " + comanda.getCep() + ", Nº " + comanda.getNumEndereco())
                            .setFontSize(11)
                            .setBold()
                            .setFontColor(GREEN_DARK)
                            .setMarginTop(8)
                            .setMarginBottom(4));

                    // tabela de lotes
                    Table tLotes = new Table(UnitValue.createPercentArray(new float[]{2, 1, 1, 1, 1}))
                            .useAllAvailableWidth()
                            .setMarginBottom(8);

                    // header tabela
                    for (String h : new String[]{"Vacina", "Qtd", "mRNA Final (%)", "Threshold (%)", "Resultado"}) {
                        tLotes.addHeaderCell(new Cell()
                                .add(new Paragraph(h).setFontSize(10).setBold().setFontColor(ColorConstants.WHITE))
                                .setBackgroundColor(GREEN_DARK)
                                .setPadding(6));
                    }

                    for (Lote_coman lc : comanda.getLote_coman()) {
                        Vacina vacina    = lc.getLote() != null ? lc.getLote().getVacina() : null;
                        double mrna      = lc.getMRNA_Disponivel();
                        double threshold = vacina != null ? vacina.getThreshold() : 0;
                        boolean aprovado = mrna >= threshold;

                        totalLotes++;
                        if (aprovado) totalAprovados++;

                        tLotes.addCell(celula(vacina != null ? vacina.getNome() : "—", false));
                        tLotes.addCell(celula(String.valueOf(lc.getQtd()), false));
                        tLotes.addCell(celula(String.format("%.4f", mrna), false));
                        tLotes.addCell(celula(String.format("%.1f", threshold), false));

                        Cell resultCell = new Cell()
                                .add(new Paragraph(aprovado ? "✓ APROVADO" : "✗ REPROVADO")
                                        .setFontSize(10)
                                        .setBold()
                                        .setFontColor(aprovado ? GREEN_DARK : RED))
                                .setPadding(6);
                        tLotes.addCell(resultCell);
                    }

                    doc.add(tLotes);
                }
            }

            // ── RESULTADO GERAL ────────────────────────────────
            linhaHorizontal(doc);
            doc.add(secao("Resultado Geral"));

            int reprovados = totalLotes - totalAprovados;
            boolean transporteAprovado = reprovados == 0;

            Table tResultado = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                    .useAllAvailableWidth()
                    .setMarginBottom(16);

            addLinha(tResultado, "Total de Lotes",      String.valueOf(totalLotes));
            addLinha(tResultado, "Aprovados",           String.valueOf(totalAprovados));
            addLinha(tResultado, "Reprovados",          String.valueOf(reprovados));
            doc.add(tResultado);

            Paragraph resultado = new Paragraph(transporteAprovado
                    ? "✓ TRANSPORTE APROVADO — Todos os lotes dentro do padrão de qualidade."
                    : "✗ TRANSPORTE REPROVADO — " + reprovados + " lote(s) fora do padrão.")
                    .setFontSize(13)
                    .setBold()
                    .setFontColor(transporteAprovado ? GREEN_DARK : RED)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(24);
            doc.add(resultado);

            // ── ASSINATURA ────────────────────────────────────
            linhaHorizontal(doc);
            doc.add(new Paragraph("Responsável: " + usuario.getNome() + " — " + usuario.getCargo())
                    .setFontSize(10)
                    .setFontColor(ColorConstants.GRAY)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginTop(8));

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório: " + e.getMessage(), e);
        }
    }

    // ── UTILITÁRIOS ───────────────────────────────────────────

    private Paragraph secao(String texto) {
        return new Paragraph(texto)
                .setFontSize(12)
                .setBold()
                .setFontColor(GREEN_DARK)
                .setMarginTop(12)
                .setMarginBottom(6);
    }

    private void linhaHorizontal(Document doc) {
        doc.add(new LineSeparator(new com.itextpdf.kernel.pdf.canvas.draw.SolidLine(0.5f))
                .setMarginTop(4)
                .setMarginBottom(12));
    }

    private void addLinha(Table table, String chave, String valor) {
        table.addCell(new Cell()
                .add(new Paragraph(chave).setFontSize(10).setBold())
                .setBackgroundColor(GRAY)
                .setPadding(5)
                .setBorder(new com.itextpdf.layout.borders.SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f)));
        table.addCell(new Cell()
                .add(new Paragraph(valor).setFontSize(10))
                .setPadding(5)
                .setBorder(new com.itextpdf.layout.borders.SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f)));
    }

    private Cell celula(String texto, boolean header) {
        return new Cell()
                .add(new Paragraph(texto).setFontSize(10))
                .setPadding(5)
                .setBorder(new com.itextpdf.layout.borders.SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f));
    }
}