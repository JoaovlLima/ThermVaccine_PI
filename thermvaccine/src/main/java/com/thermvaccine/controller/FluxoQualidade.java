package com.thermvaccine.controller;

import com.thermvaccine.model.*;
import com.thermvaccine.service.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class FluxoQualidade {

    private final ComandaService comandaService = new ComandaService();
    private final TransporteService transporteService = new TransporteService();
    private final CaixaService      caixaService      = new CaixaService();
    private Timer timerComanda = null;
    private final CalculoVidaUtilService calculoService = new CalculoVidaUtilService(); 
    private final DataLoggerService dataLoggerService = new DataLoggerService();

    private boolean alerta10Disparado = false;
    private boolean alerta5Disparado  = false;
    private boolean alertaCompDisparado = false;

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

       
        window.adicionarSecao("Datalogger");
        DataLogger dl = caixaService.dataLoggerDaCaixa(caixa.getId());
        
        if (dl == null) {
            window.adicionarMensagem("Nenhum datalogger vinculado.");
        } else {
            String titulo = dl.getModelo() + " — " + abreviar(dl.getId());
            String sub    = "Disponível: " + (dl.isDisponivel() ? "Sim" : "Não (em uso)");
            window.adicionarInfoCard(titulo, sub);
            LocalDateTime dataSaidaGrafico = caixaService.dataSaidaPorTransporte(placa); // essa é a errada qualquer coisa
            window.adicionarBotaoAcao("Ver Gráfico", () -> abrirGrafico(dl, dataSaidaGrafico));
        }

        window.adicionarEspacador();

        
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
                    + (entregue ? "   ✓ ENTREGUE" : "")
                    + (entregue && comanda.getData_Chegada() != null 
                        ? " | Chegada: " + comanda.getData_Chegada().format(DateTimeFormatter.ofPattern("dd/MM HH:mm")) 
                        : "");
                window.adicionarCard(titulo, sub, () -> mostrarComanda(comanda, caixa, placa));
            }
        }

        window.setBotaoVoltar(() -> mostrarCaixas(placa));
        window.redimensionar(460, 560);
    }

    // ── TELA 4 ────────────────────────────────────────────────

    public void mostrarComanda(Comanda comanda, Caixa caixa, String placa) {

        if (timerComanda != null) {
            timerComanda.stop();
            timerComanda = null;
        }

        Runnable renderizar = () -> {
            DataLogger dl = caixaService.dataLoggerDaCaixa(caixa.getId());
            window.limparConteudo();
            boolean entregue = comanda.getStatus() == Comanda.StatusComanda.ENTREGUE;
            window.setTitulo("Lotes da Comanda" + (entregue ? "   ✓ ENTREGUE" : "")); 

            List<Lote_coman> lotes = comanda.getLote_coman();

            if (lotes == null || lotes.isEmpty()) {
                window.adicionarMensagem("Nenhum lote nesta comanda.");
            } else {
                double[] mrnaAtualFinal = {0.0};
                for (Lote_coman lc : lotes) {
                    Lote   lote   = lc.getLote();
                    Vacina vacina = lote != null ? lote.getVacina() : null;

                    double mrnaAtual;
                    if (dl != null && vacina != null) {
                        // recalcula usando os registros reais do datalogger
                        mrnaAtual = calculoService.calcularRegistros(
                            dl.getRegistroDatalogger(),
                            vacina.getEa(),
                            vacina.getA(),
                            vacina.getThreshold(),
                            (lc.getMRNA_Disponivel() / CalculoVidaUtilService.MRNA_INICIAL) * 100.0
                        );
                        mrnaAtualFinal[0] = mrnaAtual;

                        double threshold = vacina.getThreshold();

                        if (!alertaCompDisparado && mrnaAtual <= threshold) {
                            alertaCompDisparado = true;
                            alerta10Disparado = true;
                            alerta5Disparado = true;
                            // para tudo
                            Timer t = timerComanda;
                            if (t != null) { t.stop(); timerComanda = null; }
                            dataLoggerService.pararDataLogger(dl.getId());
                            // mostra alerta
                            SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(window,
                                    "Transporte: " + placa + "\nCaixa: " + caixa.getId() +
                                    "\nComanda: " + abreviar(comanda.getId()) +
                                    "\nLote: " + abreviar(lote.getId()) +
                                    "\nmRNA: " + String.format("%.4f%%", mrnaAtual) +
                                    "\n\nValor mínimo de " + threshold + "% atingido.",
                                    "⚠ Vida útil comprometida", JOptionPane.ERROR_MESSAGE)
                            );

                        } else if (!alerta5Disparado && mrnaAtual <= threshold + 5) {
                            alerta5Disparado = true;
                            SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(window,
                                    "Transporte: " + placa + "\nCaixa: " + caixa.getId() +
                                    "\nComanda: " + abreviar(comanda.getId()) +
                                    "\nLote: " + abreviar(lote.getId()) +
                                    "\nmRNA: " + String.format("%.4f%%", mrnaAtual) +
                                    "\n\nFaltam 5% para atingir o mínimo de " + threshold + "%.",
                                    "⚠ Atenção", JOptionPane.WARNING_MESSAGE)
                            );

                        } else if (!alerta10Disparado && mrnaAtual <= threshold + 10) {
                            alerta10Disparado = true;
                            SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(window,
                                    "Transporte: " + placa + "\nCaixa: " + caixa.getId() +
                                    "\nComanda: " + abreviar(comanda.getId()) +
                                    "\nLote: " + abreviar(lote.getId()) +
                                    "\nmRNA: " + String.format("%.4f%%", mrnaAtual) +
                                    "\n\nFaltam 10% para atingir o mínimo de " + threshold + "%.",
                                    "⚠ Atenção", JOptionPane.WARNING_MESSAGE)
                            );
                        }

                        System.out.println("Registros do DL: " + dl.getRegistroDatalogger().size());
                        System.out.println("mrnaInicial: " + lc.getMRNA_Disponivel());
                        System.out.println("mrnaAtual calculado: " + mrnaAtual);
                    } else {
                        // sem datalogger, usa o valor salvo convertido
                        mrnaAtual = (lc.getMRNA_Disponivel() / CalculoVidaUtilService.MRNA_INICIAL) * 100.0;
                    }

                    String idL    = lote != null ? abreviar(lote.getId()) : "s/id";
                    String titulo = "Lote — " + idL;
                    String sub    = "Vacina: " + (vacina != null ? vacina.getNome() : "—")
                        + "  |  Qtd: " + lc.getQtd()
                        + "  |  mRNA: " + String.format("%.4f%%", mrnaAtual);

                    window.adicionarInfoCard(titulo, sub);
                }

                if (comanda.getStatus() != Comanda.StatusComanda.ENTREGUE) { // se falhar, retirar
                    timerComanda = new Timer(5000, e -> renderizar.run());
                    timerComanda.start();
                }

                if (comanda.getStatus() != Comanda.StatusComanda.ENTREGUE) {
                    
                    window.adicionarEspacador();
                    window.adicionarBotaoAcao("✓ Marcar como Entregue", () -> {
                        Timer t = timerComanda; 
                        if (t != null) {
                            t.stop();
                            timerComanda = null;    
                        }
                        comandaService.entregarComanda(comanda.getId(), mrnaAtualFinal[0]);
                        if (dl != null) {                                                  
                            dataLoggerService.pararDataLogger(dl.getId());
                        }    
                        mostrarCaixa(caixa, placa);
                    });
                }

            }

            window.setBotaoVoltar(() -> {
                if (timerComanda != null) {
                    timerComanda.stop();
                    timerComanda = null;
                }
                mostrarCaixa(caixa, placa);
            });
            window.redimensionar(460, 400);
        };

        renderizar.run();

        if (comanda.getStatus() != Comanda.StatusComanda.ENTREGUE) {
            timerComanda = new Timer(5000, e -> renderizar.run());
            timerComanda.start();
        }
    }

    // ── GRÁFICO ───────────────────────────────────────────────

    public void abrirGrafico(DataLogger dataLogger, LocalDateTime dataSaida) {
        JFrame grafico = new JFrame("Gráfico — " + dataLogger.getModelo());
        grafico.setSize(700, 420);
        grafico.setLocationRelativeTo(window);
        grafico.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GraficoPanel painel = new GraficoPanel(dataLogger, dataSaida);
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