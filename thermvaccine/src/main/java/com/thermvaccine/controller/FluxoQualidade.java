package com.thermvaccine.controller;

import com.thermvaccine.model.*;
import com.thermvaccine.service.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller do fluxo de qualidade.
 * Contém toda a lógica — a view só chama métodos daqui.
 */
public class FluxoQualidade {

    private final TransporteService transporteService = new TransporteService();
    private final CaixaService      caixaService      = new CaixaService();

    private FluxoQualidadeWindow window;

    public void start(UserQualidade user) {
        SwingUtilities.invokeLater(() -> {
            window = new FluxoQualidadeWindow(user, this);
            mostrarTransportes();
            window.setVisible(true);
        });
    }

    // ── TELA 1 ────────────────────────────────────────────────

    public void mostrarTransportes() {
        List<Transporte> lista = transporteService.listarEmTransito();
        
        window.limparConteudo();
        window.setTitulo("Transportes em trânsito");

        if (lista.isEmpty()) {
            window.adicionarMensagem("Nenhum transporte em trânsito.");
            return;
        }

        for (Transporte t : lista) {
            String titulo = "Placa: " + t.getPlaca();
            LocalDateTime dataSaida = caixaService.dataSaidaPorTransporte(t.getPlaca());
            String sub = "Capacidade: " + t.getCapacidade() + " | Saída: " + (dataSaida != null ? dataSaida.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "—");
            window.adicionarCard(titulo, sub, () -> mostrarCaixas(t.getPlaca()));
        }

        window.semBotaoVoltar();
        window.redimensionar(460, 300);
    }

    // ── TELA 2 ────────────────────────────────────────────────

    public void mostrarCaixas(String placa) {
        List<Caixa> lista = caixaService.caixasPorTransporte(placa);
        window.limparConteudo();
        window.setTitulo("Caixas — " + placa);

        if (lista.isEmpty()) {
            window.adicionarMensagem("Nenhuma caixa para este transporte.");
        } else {
            for (Caixa c : lista) {
                String idCurto = abreviar(c.getId());
                String titulo  = "Caixa — " + idCurto;
                String sub     = "Capacidade: " + c.getQtd_max_vac() + " vacinas";
                window.adicionarCard(titulo, sub, () -> mostrarCaixa(c, placa));
            }
        }

        window.setBotaoVoltar(() -> mostrarTransportes());
        window.redimensionar(460, 400);
    }

    // ── TELA 3 ────────────────────────────────────────────────

    public void mostrarCaixa(Caixa caixa, String placa) {
        window.limparConteudo();
        window.setTitulo("CAIXA — " + abreviar(caixa.getId()));

        // datalogger
        window.adicionarSecao("Datalogger");
        DataLogger dl = caixaService.dataLoggerDaCaixa(caixa.getId());
        
        if (dl == null) {
            window.adicionarMensagem("Nenhum datalogger vinculado.");
        } else {
            String titulo = dl.getModelo() + " — " + abreviar(dl.getId());
            String sub    = "Disponível: " + (dl.isDisponivel() ? "Sim" : "Não (em uso)");
            window.adicionarInfoCard(titulo, sub);
            window.adicionarBotaoAcao("Ver Gráfico", () -> abrirGrafico(dl));
        }

        window.adicionarEspacador();

        // comandas
        window.adicionarSecao("Comandas");
        List<Comanda> comandas = caixaService.comandasDaCaixa(caixa.getId());

        if (comandas == null || comandas.isEmpty()) {
            window.adicionarMensagem("Nenhuma comanda nesta caixa.");
        } else {
            for (Comanda comanda : comandas) {
                boolean entregue = comanda.getStatus() == Comanda.StatusComanda.ENTREGUE;
                String idC    = abreviar(comanda.getId());
                String titulo = "Comanda — " + idC;
                String sub    = "CEP: " + comanda.getCep() + " | Nº " + comanda.getNumEndereco()
                    + (entregue ? "   ✓ ENTREGUE" : "");
                window.adicionarCard(titulo, sub, () -> mostrarComanda(comanda, caixa, placa));
            }
        }

        window.setBotaoVoltar(() -> mostrarCaixas(placa));
        window.redimensionar(460, 560);
    }

    // ── TELA 4 ────────────────────────────────────────────────

    public void mostrarComanda(Comanda comanda, Caixa caixa, String placa) {
        window.limparConteudo();
        window.setTitulo("Lotes da Comanda");

        List<Lote_coman> lotes = comanda.getLote_coman();

        if (lotes == null || lotes.isEmpty()) {
            window.adicionarMensagem("Nenhum lote nesta comanda.");
        } else {
            for (Lote_coman lc : lotes) {
                Lote   lote   = lc.getLote();
                Vacina vacina = lote != null ? lote.getVacina() : null;
                double mrna   = lc.getMRNA_Disponivel();

                String idL    = lote != null ? abreviar(lote.getId()) : "s/id";
                String titulo = "Lote — " + idL;
                String sub    = "Vacina: " + (vacina != null ? vacina.getNome() : "—")
                    + "  |  Qtd: " + lc.getQtd()
                    + "  |  mRNA: " + String.format("%.4f%%", mrna);

                window.adicionarInfoCard(titulo, sub);
            }
        }

        window.setBotaoVoltar(() -> mostrarCaixa(caixa, placa));
        window.redimensionar(460, 400);
    }

    // ── GRÁFICO ───────────────────────────────────────────────

    public void abrirGrafico(DataLogger dataLogger) {
        JFrame grafico = new JFrame("Gráfico — " + dataLogger.getModelo());
        grafico.setSize(700, 420);
        grafico.setLocationRelativeTo(window);
        grafico.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GraficoPanel painel = new GraficoPanel(dataLogger);
        grafico.add(painel);
        grafico.setVisible(true);

        Timer timer = new Timer(5000, e -> {
            painel.atualizar(dataLogger.getId());
            painel.repaint();
        });
        timer.start();

        grafico.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) { timer.stop(); }
        });
    }

    // ── UTILITÁRIO ────────────────────────────────────────────

    private String abreviar(String id) {
        if (id == null) return "s/id";
        return id.length() > 8 ? id.substring(0, 8) + "..." : id;
    }

    public void setWindow(FluxoQualidadeWindow w) {
        this.window = w;
    }   
}