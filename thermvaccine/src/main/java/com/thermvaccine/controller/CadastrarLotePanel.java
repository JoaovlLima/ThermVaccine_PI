package com.thermvaccine.controller;

import com.thermvaccine.model.Vacina;
import com.thermvaccine.service.LoteService;
import com.thermvaccine.service.VacinaService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class CadastrarLotePanel extends JPanel {

    private static final Color GREEN_DARK = new Color(34, 105, 66);
    private static final Color BORDER     = new Color(220, 220, 220);
    private static final Color TEXT_MUTED = new Color(130, 130, 130);

    private final FluxoLogisticoWindow window;
    private final LoteService loteService     = new LoteService();
    private final VacinaService vacinaService = new VacinaService();

    public CadastrarLotePanel(FluxoLogisticoWindow window) {
        this.window = window;
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel sectionTitle = new JLabel("Cadastrar lote");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        sectionTitle.setForeground(new Color(40, 40, 40));
        sectionTitle.setBorder(new EmptyBorder(0, 0, 16, 0));
        add(sectionTitle, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        List<Vacina> vacinas = vacinaService.listar();
        JComboBox<Vacina> cmbVacina = new JComboBox<>(vacinas.toArray(new Vacina[0]));
        cmbVacina.setRenderer((list, value, index, sel, focus) -> {
            JLabel lbl = new JLabel(value != null ? value.getNome() : "Selecione...");
            lbl.setBorder(new EmptyBorder(4, 8, 4, 8));
            lbl.setOpaque(true);
            lbl.setBackground(sel ? new Color(34, 105, 66, 20) : Color.WHITE);
            return lbl;
        });
        styleInput(cmbVacina);

        JTextField txtFabricante = new JTextField();
        styleInput(txtFabricante);

        JSpinner spnQuantidade = new JSpinner(new SpinnerNumberModel(1, 1, 99999, 1));
        styleInput(spnQuantidade);

        JSpinner spnValidade = new JSpinner(new SpinnerDateModel());
        spnValidade.setEditor(new JSpinner.DateEditor(spnValidade, "dd/MM/yyyy"));
        styleInput(spnValidade);

        JSpinner spnDescongelamento = new JSpinner(new SpinnerDateModel());
        spnDescongelamento.setEditor(new JSpinner.DateEditor(spnDescongelamento, "dd/MM/yyyy"));
        styleInput(spnDescongelamento);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 1.0; gbc.insets = new Insets(0, 0, 10, 0);
        form.add(buildField("Vacina", cmbVacina), gbc);

        gbc.gridwidth = 1; gbc.weightx = 0.5;
        gbc.gridx = 0; gbc.gridy = 1; gbc.insets = new Insets(0, 0, 10, 8);
        form.add(buildField("Fabricante", txtFabricante), gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.insets = new Insets(0, 0, 10, 0);
        form.add(buildField("Quantidade", spnQuantidade), gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.insets = new Insets(0, 0, 0, 8);
        form.add(buildField("Validade", spnValidade), gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.insets = new Insets(0, 0, 0, 0);
        form.add(buildField("Data Descongelamento", spnDescongelamento), gbc);

        add(form, BorderLayout.CENTER);

        JLabel lblFeedback = new JLabel(" ");
        lblFeedback.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblFeedback.setForeground(GREEN_DARK);

        JButton btnCancelar = actionButton("Cancelar", false);
        JButton btnSalvar   = actionButton("Salvar lote", true);

        btnCancelar.addActionListener(e -> window.voltarMenu());

        btnSalvar.addActionListener(e -> {
            Vacina vacina     = (Vacina) cmbVacina.getSelectedItem();
            String fabricante = txtFabricante.getText().trim();
            int quantidade    = (int) spnQuantidade.getValue();
            Date dataSel      = (Date) spnValidade.getValue();
            Date dataDesc     = (Date) spnDescongelamento.getValue();

            if (vacina == null || fabricante.isEmpty()) {
                lblFeedback.setForeground(new Color(180, 60, 60));
                lblFeedback.setText("Preencha todos os campos.");
                return;
            }

            LocalDateTime validade = dataSel.toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();

            LocalDateTime descongelamento = dataDesc.toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();

            loteService.criar(quantidade, fabricante, validade, descongelamento, window.getUsuario(), vacina);

            lblFeedback.setForeground(GREEN_DARK);
            lblFeedback.setText("Lote cadastrado com sucesso!");

            Timer t = new Timer(1500, ev -> {
                txtFabricante.setText("");
                spnQuantidade.setValue(1);
                cmbVacina.setSelectedIndex(0);
                lblFeedback.setText(" ");
                window.voltarMenu();
            });
            t.setRepeats(false);
            t.start();
        });

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
        add(actions, BorderLayout.SOUTH);
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
}