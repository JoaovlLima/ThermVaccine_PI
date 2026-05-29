package com.thermvaccine.controller;

import com.thermvaccine.model.Lote;
import com.thermvaccine.model.Lote_coman;
import com.thermvaccine.model.Vacina;
import com.thermvaccine.service.ComandaService;
import com.thermvaccine.service.LoteService;
import com.thermvaccine.service.VacinaService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CriarComandaPanel extends JPanel {

    private static final Color GREEN_DARK = new Color(34, 105, 66);
    private static final Color BORDER     = new Color(220, 220, 220);
    private static final Color TEXT_MUTED = new Color(130, 130, 130);

    private final FluxoLogisticoWindow window;
    private final LoteService    loteService    = new LoteService();
    private final VacinaService  vacinaService  = new VacinaService();
    private final ComandaService comandaService = new ComandaService();

    private final List<Lote_coman> lotesSelecionados = new ArrayList<>();

    private JComboBox<Vacina> cmbVacina;
    private JComboBox<Lote>   cmbLote;
    private JSpinner           spnQuantidade;
    private JPanel             painelLotesSelecionados;
    private JTextField         txtCep;
    private JTextField         txtNumero;
    private JLabel             lblFeedback;

    public CriarComandaPanel(FluxoLogisticoWindow window) {
        this.window = window;
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel sectionTitle = new JLabel("Criar comanda");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        sectionTitle.setForeground(new Color(40, 40, 40));
        sectionTitle.setBorder(new EmptyBorder(0, 0, 16, 0));
        add(sectionTitle, BorderLayout.NORTH);
        add(buildForm(), BorderLayout.CENTER);
        add(buildActions(), BorderLayout.SOUTH);
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Vacina ---
        List<Vacina> vacinas = vacinaService.listar();
        cmbVacina = new JComboBox<>(vacinas.toArray(new Vacina[0]));
        cmbVacina.setRenderer(vacinaRenderer());
        styleInput(cmbVacina);
        cmbVacina.addActionListener(e -> atualizarLotes());

        // --- Lote (filtrado pela vacina) ---
        cmbLote = new JComboBox<>();
        cmbLote.setRenderer(loteRenderer());
        styleInput(cmbLote);
        atualizarLotes();

        // --- Quantidade ---
        spnQuantidade = new JSpinner(new SpinnerNumberModel(1, 1, 99999, 1));
        styleInput(spnQuantidade);

        // --- Botão adicionar ---
        JButton btnAdicionar = new JButton("+ Adicionar");
        btnAdicionar.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnAdicionar.setBackground(Color.WHITE);
        btnAdicionar.setForeground(GREEN_DARK);
        btnAdicionar.setBorder(BorderFactory.createLineBorder(GREEN_DARK, 1, true));
        btnAdicionar.setFocusPainted(false);
        btnAdicionar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAdicionar.setPreferredSize(new Dimension(100, 34));
        btnAdicionar.addActionListener(e -> adicionarLote());

        // --- Lotes adicionados ---
        painelLotesSelecionados = new JPanel();
        painelLotesSelecionados.setBackground(new Color(248, 248, 248));
        painelLotesSelecionados.setLayout(new BoxLayout(painelLotesSelecionados, BoxLayout.Y_AXIS));
        painelLotesSelecionados.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        atualizarPainelLotes();

        // --- CEP e Número ---
        txtCep    = new JTextField();
        txtNumero = new JTextField();
        styleInput(txtCep);
        styleInput(txtNumero);

        // Layout
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 1.0; gbc.insets = new Insets(0, 0, 10, 0);
        form.add(buildField("Vacina", cmbVacina), gbc);

        gbc.gridy = 1;
        form.add(buildField("Lote", cmbLote), gbc);

        gbc.gridwidth = 1; gbc.weightx = 0.5;
        gbc.gridx = 0; gbc.gridy = 2; gbc.insets = new Insets(0, 0, 10, 8);
        form.add(buildField("Quantidade", spnQuantidade), gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.insets = new Insets(0, 0, 10, 0);
        gbc.anchor = GridBagConstraints.SOUTH;
        form.add(btnAdicionar, gbc);

        JLabel lblLotesTitle = new JLabel("Lotes adicionados");
        lblLotesTitle.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblLotesTitle.setForeground(TEXT_MUTED);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.weightx = 1.0; gbc.insets = new Insets(0, 0, 4, 0);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        form.add(lblLotesTitle, gbc);

        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 10, 0);
        form.add(painelLotesSelecionados, gbc);

        gbc.gridwidth = 1; gbc.weightx = 0.5;
        gbc.gridx = 0; gbc.gridy = 5; gbc.insets = new Insets(0, 0, 0, 8);
        form.add(buildField("CEP", txtCep), gbc);

        gbc.gridx = 1; gbc.gridy = 5; gbc.insets = new Insets(0, 0, 0, 0);
        form.add(buildField("Número", txtNumero), gbc);

        return form;
    }

    private void atualizarLotes() {
        Vacina vacina = (Vacina) cmbVacina.getSelectedItem();
        cmbLote.removeAllItems();
        if (vacina == null) return;

        List<Lote> lotes = loteService.listarPorVacina(vacina.getId());
        for (Lote l : lotes) cmbLote.addItem(l);
    }

    private void adicionarLote() {
        Lote lote     = (Lote) cmbLote.getSelectedItem();
        int quantidade = (int) spnQuantidade.getValue();

        if (lote == null) return;

        int qtdJaAdd = loteService.qtdVacPorLote(lote.getId(), lotesSelecionados) + quantidade;
        if (qtdJaAdd > lote.getQuantidade()) {
            mostrarFeedback("Quantidade maior que o disponível no lote (" + lote.getQuantidade() + ").", true);
            return;
        }

        lotesSelecionados.add(new Lote_coman(lote, quantidade));
        atualizarPainelLotes();
        spnQuantidade.setValue(1);
        mostrarFeedback(" ", false);
    }

    private void atualizarPainelLotes() {
        painelLotesSelecionados.removeAll();

        if (lotesSelecionados.isEmpty()) {
            JLabel vazio = new JLabel("Nenhum lote adicionado");
            vazio.setFont(new Font("SansSerif", Font.PLAIN, 12));
            vazio.setForeground(TEXT_MUTED);
            painelLotesSelecionados.add(vazio);
        } else {
            for (Lote_coman lc : lotesSelecionados) {
                JLabel item = new JLabel("• " + lc.getLote().getVacina().getNome()
                        + " — " + lc.getLote().getFabricante()
                        + " x" + lc.getQtd());
                item.setFont(new Font("SansSerif", Font.PLAIN, 12));
                item.setForeground(new Color(40, 40, 40));
                painelLotesSelecionados.add(item);
            }
        }

        painelLotesSelecionados.revalidate();
        painelLotesSelecionados.repaint();
    }

    private JPanel buildActions() {
        lblFeedback = new JLabel(" ");
        lblFeedback.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblFeedback.setForeground(GREEN_DARK);

        JButton btnCancelar = actionButton("Cancelar", false);
        JButton btnSalvar   = actionButton("Salvar comanda", true);

        btnCancelar.addActionListener(e -> { limpar(); window.voltarMenu(); });
        btnSalvar.addActionListener(e -> salvar());

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.setBackground(Color.WHITE);
        btns.add(btnCancelar);
        btns.add(btnSalvar);

        JPanel actions = new JPanel(new BorderLayout());
        actions.setBackground(Color.WHITE);
        actions.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER),
                new EmptyBorder(12, 0, 0, 0)
        ));
        actions.add(lblFeedback, BorderLayout.WEST);
        actions.add(btns, BorderLayout.EAST);
        return actions;
    }

    private void salvar() {
        String cep    = txtCep.getText().trim();
        String numero = txtNumero.getText().trim();

        if (lotesSelecionados.isEmpty()) {
            mostrarFeedback("Adicione ao menos um lote.", true);
            return;
        }
        if (cep.isEmpty() || numero.isEmpty()) {
            mostrarFeedback("Preencha o CEP e o número.", true);
            return;
        }

        int numEndereco;
        try {
            numEndereco = Integer.parseInt(numero);
        } catch (NumberFormatException ex) {
            mostrarFeedback("Número do endereço inválido.", true);
            return;
        }

        comandaService.criarComanda(cep, numEndereco, lotesSelecionados);
        mostrarFeedback("Comanda criada com sucesso!", false);

        Timer t = new Timer(1500, ev -> { limpar(); window.voltarMenu(); });
        t.setRepeats(false);
        t.start();
    }

    private void limpar() {
        lotesSelecionados.clear();
        atualizarPainelLotes();
        txtCep.setText("");
        txtNumero.setText("");
        spnQuantidade.setValue(1);
        lblFeedback.setText(" ");
    }

    private void mostrarFeedback(String msg, boolean erro) {
        lblFeedback.setForeground(erro ? new Color(180, 60, 60) : GREEN_DARK);
        lblFeedback.setText(msg);
    }

    private ListCellRenderer<Vacina> vacinaRenderer() {
        return (list, value, index, sel, focus) -> {
            JLabel lbl = new JLabel(value != null ? value.getNome() : "Selecione...");
            lbl.setBorder(new EmptyBorder(4, 8, 4, 8));
            lbl.setOpaque(true);
            lbl.setForeground(new Color(40, 40, 40));
            lbl.setBackground(sel ? new Color(34, 105, 66, 20) : Color.WHITE);
            return lbl;
        };
    }

    private ListCellRenderer<Lote> loteRenderer() {
        return (list, value, index, sel, focus) -> {
            String texto = value != null
                    ? value.getFabricante() + " — qtd: " + value.getQuantidade()
                    : "Selecione...";
            JLabel lbl = new JLabel(texto);
            lbl.setBorder(new EmptyBorder(4, 8, 4, 8));
            lbl.setOpaque(true);
            lbl.setForeground(new Color(40, 40, 40));
            lbl.setBackground(sel ? new Color(34, 105, 66, 20) : Color.WHITE);
            return lbl;
        };
    }

    private JPanel buildField(String labelText, JComponent input) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lbl.setForeground(TEXT_MUTED);
        lbl.setBorder(new EmptyBorder(0, 0, 4, 0));
        p.add(lbl, BorderLayout.NORTH);
        p.add(input, BorderLayout.CENTER);
        return p;
    }

    private void styleInput(JComponent c) {
        c.setPreferredSize(new Dimension(0, 34));
        c.setFont(new Font("SansSerif", Font.PLAIN, 13));
        c.setBackground(Color.WHITE);
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(0, 8, 0, 8)
        ));
    }

    private JButton actionButton(String text, boolean primary) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setPreferredSize(new Dimension(130, 34));
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
}