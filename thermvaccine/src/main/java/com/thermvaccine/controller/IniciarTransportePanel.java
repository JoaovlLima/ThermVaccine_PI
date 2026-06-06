package com.thermvaccine.controller;

import com.thermvaccine.model.Caixa;
import com.thermvaccine.model.Comanda;
import com.thermvaccine.model.HistoricoCaixa;
import com.thermvaccine.model.Transporte;
import com.thermvaccine.model.Vacina;
import com.thermvaccine.service.CaixaService;
import com.thermvaccine.service.ComandaService;
import com.thermvaccine.service.TransporteService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class IniciarTransportePanel extends JPanel {

    private static final Color GREEN_DARK = new Color(34, 105, 66);
    private static final Color BORDER = new Color(220, 220, 220);
    private static final Color TEXT_MUTED = new Color(130, 130, 130);

    private final FluxoLogisticoWindow window;
    private final ComandaService comandaService = new ComandaService();
    private final CaixaService caixaService = new CaixaService();
    private final TransporteService transporteService = new TransporteService();

    private List<Comanda> comandasSelecionadas = new ArrayList<>();
    private List<Caixa> caixasAlocadas = new ArrayList<>();
    private List<HistoricoCaixa> historicos = new ArrayList<>();

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);

    // tela 1
    private JPanel painelComandas;
    private JLabel lblFeedback1;

    // tela 2
    private JPanel painelResumo;
    private JComboBox<Transporte> cmbTransporte;
    private JLabel lblFeedback2;

    public IniciarTransportePanel(FluxoLogisticoWindow window) {
        this.window = window;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        cards.setBackground(Color.WHITE);
        cards.add(buildTela1(), "tela1");
        cards.add(buildTela2(), "tela2");
        cardLayout.show(cards, "tela1");

        add(cards, BorderLayout.CENTER);
    }

    // ── Tela 1 ──────────────────────────────────────────────────────────────

    private JPanel buildTela1() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        root.setBorder(new EmptyBorder(24, 24, 24, 24));

        // título
        JLabel title = new JLabel("Iniciar transporte");
        title.setFont(new Font("SansSerif", Font.BOLD, 13));
        title.setForeground(new Color(40, 40, 40));
        title.setBorder(new EmptyBorder(0, 0, 14, 0));
        root.add(title, BorderLayout.NORTH);

        // lista de comandas
        painelComandas = new JPanel();
        painelComandas.setLayout(new BoxLayout(painelComandas, BoxLayout.Y_AXIS));
        painelComandas.setBackground(Color.WHITE);
        carregarComandas();

        JScrollPane scroll = new JScrollPane(painelComandas);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER, 1, true));
        scroll.setBackground(Color.WHITE);
        scroll.getViewport().setBackground(Color.WHITE);
        root.add(scroll, BorderLayout.CENTER);

        // actions
        lblFeedback1 = new JLabel(" ");
        lblFeedback1.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JButton btnCancelar = buildButton("Cancelar", false);
        JButton btnContinuar = buildButton("Continuar", true);
        btnCancelar.addActionListener(e -> {
            limpar();
            window.voltarMenu();
        });
        btnContinuar.addActionListener(e -> continuar());

        root.add(buildActions(lblFeedback1, btnCancelar, btnContinuar), BorderLayout.SOUTH);
        return root;
    }

    private void carregarComandas() {
        painelComandas.removeAll();

        List<Comanda> disponiveis = comandaService.listarComandasDisponiveis();

        if (disponiveis.isEmpty()) {
            JLabel vazio = new JLabel("Nenhuma comanda disponível");
            vazio.setFont(new Font("SansSerif", Font.PLAIN, 12));
            vazio.setForeground(TEXT_MUTED);
            vazio.setBorder(new EmptyBorder(12, 12, 12, 12));
            painelComandas.add(vazio);
        } else {
            for (Comanda comanda : disponiveis) {
                painelComandas.add(buildComandaItem(comanda));
            }
        }

        painelComandas.revalidate();
        painelComandas.repaint();
    }

    private JPanel buildComandaItem(Comanda comanda) {
        JCheckBox cb = new JCheckBox();
        cb.setBackground(Color.WHITE);
        cb.setFocusPainted(false);

        JLabel lblNome = new JLabel("CEP: " + comanda.getCep() + ", nº " + comanda.getNumEndereco());
        lblNome.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblNome.setForeground(new Color(40, 40, 40));

        JLabel lblQtd = new JLabel(comandaService.qtdTotalComanda(List.of(comanda)) + " vacinas");
        lblQtd.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblQtd.setForeground(TEXT_MUTED);

        JPanel texts = new JPanel();
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        texts.setBackground(Color.WHITE);
        texts.add(lblNome);
        texts.add(Box.createVerticalStrut(2));
        texts.add(lblQtd);

        JPanel item = new JPanel(new BorderLayout(10, 0));
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                new EmptyBorder(10, 12, 10, 12)));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        item.add(cb, BorderLayout.WEST);
        item.add(texts, BorderLayout.CENTER);
        item.putClientProperty("comanda", comanda);
        item.putClientProperty("cb", cb);

        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                cb.setSelected(!cb.isSelected());
                Color bg = cb.isSelected() ? new Color(34, 105, 66, 12) : Color.WHITE;
                item.setBackground(bg);
                texts.setBackground(bg);
            }
        });

        return item;
    }

    private void continuar() {
        comandasSelecionadas.clear();
        for (Component c : painelComandas.getComponents()) {
            if (c instanceof JPanel item) {
                JCheckBox cb = (JCheckBox) item.getClientProperty("cb");
                if (cb != null && cb.isSelected()) {
                    comandasSelecionadas.add((Comanda) item.getClientProperty("comanda"));
                }
            }
        }

        if (comandasSelecionadas.isEmpty()) {
            mostrarFeedback(lblFeedback1, "Selecione ao menos uma comanda.", true);
            return;
        }

        try {
            caixasAlocadas = caixaService.acharCaixas(comandasSelecionadas);
            historicos = caixaService.vincularDatalogger(caixasAlocadas);
        } catch (RuntimeException ex) {
            mostrarFeedback(lblFeedback1, ex.getMessage(), true);
            return;
        }

        atualizarResumo();
        cardLayout.show(cards, "tela2");
        window.navegarPara("transporte", 560);
    }

    // ── Tela 2 ──────────────────────────────────────────────────────────────

    private JPanel buildTela2() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        root.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Resumo da alocação");
        title.setFont(new Font("SansSerif", Font.BOLD, 13));
        title.setForeground(new Color(40, 40, 40));
        title.setBorder(new EmptyBorder(0, 0, 14, 0));
        root.add(title, BorderLayout.NORTH);

        // resumo + transporte
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Color.WHITE);

        painelResumo = new JPanel();
        painelResumo.setLayout(new BoxLayout(painelResumo, BoxLayout.Y_AXIS));
        painelResumo.setBackground(Color.WHITE);
        center.add(painelResumo);

        center.add(Box.createVerticalStrut(14));

        // campo transporte
        JLabel lblT = new JLabel("Transporte");
        lblT.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblT.setForeground(TEXT_MUTED);
        lblT.setBorder(new EmptyBorder(0, 0, 4, 0));
        lblT.setAlignmentX(Component.LEFT_ALIGNMENT);

        cmbTransporte = new JComboBox<>();
        cmbTransporte.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        cmbTransporte.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbTransporte.setRenderer((list, value, index, sel, focus) -> {
            String texto = value != null
                    ? value.getPlaca() + " — cap: " + value.getCapacidade() + " caixas"
                    : "Selecione...";
            JLabel lbl = new JLabel(texto);
            lbl.setBorder(new EmptyBorder(4, 8, 4, 8));
            lbl.setOpaque(true);
            lbl.setForeground(new Color(40, 40, 40));
            lbl.setBackground(sel ? new Color(34, 105, 66, 20) : Color.WHITE);
            return lbl;
        });
        styleInput(cmbTransporte);

        center.add(lblT);
        center.add(cmbTransporte);

        root.add(center, BorderLayout.CENTER);

        lblFeedback2 = new JLabel(" ");
        lblFeedback2.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JButton btnVoltar = buildButton("Voltar", false);
        JButton btnConfirmar = buildButton("Confirmar", true);
        btnVoltar.addActionListener(e -> {
            cardLayout.show(cards, "tela1");
            window.navegarPara("transporte", 460);
        });
        btnConfirmar.addActionListener(e -> confirmar());

        root.add(buildActions(lblFeedback2, btnVoltar, btnConfirmar), BorderLayout.SOUTH);
        return root;
    }

    private void atualizarResumo() {
        painelResumo.removeAll();

        for (int i = 0; i < caixasAlocadas.size(); i++) {
            Caixa caixa = caixasAlocadas.get(i);
            HistoricoCaixa h = historicos.get(i);

            // header caixa + datalogger
            JLabel lblCaixa = new JLabel("Caixa #" + (i + 1));
            lblCaixa.setFont(new Font("SansSerif", Font.BOLD, 12));
            lblCaixa.setForeground(new Color(40, 40, 40));

            JLabel lblDL = new JLabel(h.getDataLogger().getModelo());
            lblDL.setFont(new Font("SansSerif", Font.PLAIN, 11));
            lblDL.setForeground(GREEN_DARK);
            lblDL.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(34, 105, 66, 80), 1, true),
                    new EmptyBorder(2, 8, 2, 8)));

            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(Color.WHITE);
            header.add(lblCaixa, BorderLayout.WEST);
            header.add(lblDL, BorderLayout.EAST);
            header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

            // comandas da caixa
            JPanel comandasPanel = new JPanel();
            comandasPanel.setLayout(new BoxLayout(comandasPanel, BoxLayout.Y_AXIS));
            comandasPanel.setBackground(Color.WHITE);
            comandasPanel.setBorder(new EmptyBorder(6, 0, 0, 0));

            for (Comanda comanda : comandasSelecionadas) {
                if (caixa.getId().equals(comanda.getIdCaixa())) {
                    JLabel lblC = new JLabel("• CEP: " + comanda.getCep()
                            + ", nº " + comanda.getNumEndereco()
                            + " — " + comandaService.qtdTotalComanda(List.of(comanda)) + " vacinas");
                    lblC.setFont(new Font("SansSerif", Font.PLAIN, 12));
                    lblC.setForeground(new Color(80, 80, 80));
                    lblC.setAlignmentX(Component.LEFT_ALIGNMENT);
                    comandasPanel.add(lblC);
                    comandasPanel.add(Box.createVerticalStrut(2));
                }
            }

            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER, 1, true),
                    new EmptyBorder(10, 12, 10, 12)));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(header, BorderLayout.NORTH);
            card.add(comandasPanel, BorderLayout.CENTER);

            painelResumo.add(card);
            painelResumo.add(Box.createVerticalStrut(8));
        }

        // carrega transportes
        cmbTransporte.removeAllItems();
        for (Transporte t : transporteService.listarTransportesDisponiveis()) {
            cmbTransporte.addItem(t);
        }

        painelResumo.revalidate();
        painelResumo.repaint();
    }

    private void confirmar() {
        Transporte transporte = (Transporte) cmbTransporte.getSelectedItem();

        if (transporte == null) {
            mostrarFeedback(lblFeedback2, "Selecione um transporte.", true);
            return;
        }

        for (Caixa caixa : caixasAlocadas) {
            caixa.setIdTransporte(transporte.getPlaca());
        }

        comandaService.iniciarTransporte(comandasSelecionadas);
        caixaService.salvarTransporteCaixa(caixasAlocadas);
        caixaService.salvarVinculoDataCaixa(historicos);
        transporteService.mudarStatus(transporte.getPlaca());

        mostrarFeedback(lblFeedback2, "Transporte iniciado com sucesso!", false);

        Timer t = new Timer(1500, ev -> {
            limpar();
            window.voltarMenu();
        });
        t.setRepeats(false);
        t.start();
    }

    // ── Utilitários ─────────────────────────────────────────────────────────

    private void limpar() {
        comandasSelecionadas.clear();
        caixasAlocadas.clear();
        historicos.clear();
        carregarComandas();
        cardLayout.show(cards, "tela1");
    }

    private void mostrarFeedback(JLabel lbl, String msg, boolean erro) {
        lbl.setForeground(erro ? new Color(180, 60, 60) : GREEN_DARK);
        lbl.setText(msg);
    }

    private JPanel buildActions(JLabel feedback, JButton btnLeft, JButton btnRight) {
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.setBackground(Color.WHITE);
        btns.add(btnLeft);
        btns.add(btnRight);

        JPanel actions = new JPanel(new BorderLayout());
        actions.setBackground(Color.WHITE);
        actions.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER),
                new EmptyBorder(12, 0, 0, 0)));
        actions.add(feedback, BorderLayout.WEST);
        actions.add(btns, BorderLayout.EAST);
        return actions;
    }

    private void styleInput(JComponent c) {
        c.setFont(new Font("SansSerif", Font.PLAIN, 13));
        c.setBackground(Color.WHITE);
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(0, 8, 0, 8)));
    }

    private JButton buildButton(String text, boolean primary) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setPreferredSize(new Dimension(110, 34));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (primary) {
            btn.setBackground(GREEN_DARK);
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createLineBorder(GREEN_DARK, 1, true));
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(new Color(60, 60, 60));
            btn.setBorder(BorderFactory.createLineBorder(BORDER, 1, true));
        }
        return btn;
    }

     public void onShow() {
        carregarComandas();
}
    
}