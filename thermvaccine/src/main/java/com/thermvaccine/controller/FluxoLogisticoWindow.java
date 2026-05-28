package com.thermvaccine.controller;

import com.thermvaccine.model.UserLogistica;
import com.thermvaccine.model.Usuario.Tier;
import com.thermvaccine.model.Vacina;
import com.thermvaccine.service.LoteService;
import com.thermvaccine.service.VacinaService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class FluxoLogisticoWindow extends JFrame {

    private static final Color GREEN_DARK = new Color(34, 105, 66);
    private static final Color BORDER     = new Color(220, 220, 220);
    private static final Color TEXT_MUTED = new Color(130, 130, 130);

    private final UserLogistica usuario;
    private final LoteService loteService     = new LoteService();
    private final VacinaService vacinaService = new VacinaService();

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel     cards      = new JPanel(cardLayout);

    public FluxoLogisticoWindow(UserLogistica usuario) {
        this.usuario = usuario;

        setTitle("ThermVaccine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 260);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        add(buildHeader(), BorderLayout.NORTH);

        cards.setBackground(Color.WHITE);
        cards.add(buildMenu(),  "menu");
        cards.add(buildLote(),  "lote");
        add(cards, BorderLayout.CENTER);

        cardLayout.show(cards, "menu");
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                new EmptyBorder(14, 24, 14, 24)
        ));

        JLabel title = new JLabel("ThermVaccine");
        title.setFont(new Font("SansSerif", Font.BOLD, 15));
        title.setForeground(GREEN_DARK);

        JLabel user = new JLabel(usuario.getNome());
        user.setFont(new Font("SansSerif", Font.PLAIN, 12));
        user.setForeground(TEXT_MUTED);

        header.add(title, BorderLayout.WEST);
        header.add(user,  BorderLayout.EAST);
        return header;
    }

    private JPanel buildMenu() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton btnLote       = menuButton("Cadastrar lote");
        JButton btnComanda    = menuButton("Criar comanda");
        JButton btnTransporte = menuButton("Iniciar transporte");

        btnLote.addActionListener(e -> { cardLayout.show(cards, "lote"); setSize(420, 460); setLocationRelativeTo(null); });

        panel.add(btnLote);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnComanda);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnTransporte);

        return panel;
    }

    private JButton menuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setForeground(new Color(40, 40, 40));
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createLineBorder(BORDER, 1, true));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setPreferredSize(new Dimension(0, 42));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBorder(BorderFactory.createLineBorder(new Color(34, 105, 66), 1, true));
                btn.setForeground(GREEN_DARK);
                btn.setBackground(new Color(34, 105, 66, 10));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBorder(BorderFactory.createLineBorder(BORDER, 1, true));
                btn.setForeground(new Color(40, 40, 40));
                btn.setBackground(Color.WHITE);
            }
        });

        return btn;
    }

    private JPanel buildLote() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel sectionTitle = new JLabel("Cadastrar lote");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        sectionTitle.setForeground(new Color(40, 40, 40));
        sectionTitle.setBorder(new EmptyBorder(0, 0, 16, 0));
        panel.add(sectionTitle, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 8);

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

        SpinnerDateModel dateModel = new SpinnerDateModel();
        SpinnerDateModel descongelamentoModel = new SpinnerDateModel();
        JSpinner spnValidade = new JSpinner(dateModel);
        JSpinner spnDescongelamento = new JSpinner(descongelamentoModel);
        spnValidade.setEditor(new JSpinner.DateEditor(spnValidade, "dd/MM/yyyy"));
        spnDescongelamento.setEditor(new JSpinner.DateEditor(spnDescongelamento, "dd/MM/yyyy"));
        styleInput(spnValidade);
        styleInput(spnDescongelamento);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        form.add(buildField("Vacina", cmbVacina), gbc);

        gbc.gridwidth = 1; gbc.weightx = 0.5; gbc.insets = new Insets(0, 0, 10, 8);
        gbc.gridx = 0; gbc.gridy = 1;
        form.add(buildField("Fabricante", txtFabricante), gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.insets = new Insets(0, 0, 10, 0);
        form.add(buildField("Quantidade", spnQuantidade), gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0.5; gbc.insets = new Insets(0, 0, 0, 0);
        form.add(buildField("Validade", spnValidade), gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.insets = new Insets(0, 0, 0, 0);
        form.add(buildField("Data Descongelamento", spnDescongelamento), gbc);

        panel.add(form, BorderLayout.CENTER);

        JLabel lblFeedback = new JLabel(" ");
        lblFeedback.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblFeedback.setForeground(GREEN_DARK);

        JButton btnCancelar = actionButton("Cancelar", false);
        JButton btnSalvar   = actionButton("Salvar lote", true);

        btnCancelar.addActionListener(e -> voltarMenu());

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
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            LocalDateTime data_desc = dataSel.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            loteService.criar(quantidade, fabricante, validade, data_desc, usuario, vacina);

            lblFeedback.setForeground(GREEN_DARK);
            lblFeedback.setText("Lote cadastrado com sucesso!");

            Timer t = new Timer(1500, ev -> {
                txtFabricante.setText("");
                spnQuantidade.setValue(1);
                cmbVacina.setSelectedIndex(0);
                lblFeedback.setText(" ");
                voltarMenu();
            });
            t.setRepeats(false);
            t.start();
        });

        JPanel actions = new JPanel(new BorderLayout());
        actions.setBackground(Color.WHITE);
        actions.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER),
                new EmptyBorder(12, 0, 0, 0)
        ));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.setBackground(Color.WHITE);
        btns.add(btnCancelar);
        btns.add(btnSalvar);

        actions.add(lblFeedback, BorderLayout.WEST);
        actions.add(btns, BorderLayout.EAST);
        panel.add(actions, BorderLayout.SOUTH);

        return panel;
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
            btn.setForeground(Color.WHITE); // texto branco OK aqui — fundo é verde escuro
            btn.setBorder(BorderFactory.createLineBorder(GREEN_DARK, 1, true));
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(new Color(60, 60, 60));
            btn.setBorder(BorderFactory.createLineBorder(BORDER, 1, true));
        }
        return btn;
    }

    private void voltarMenu() {
        cardLayout.show(cards, "menu");
        setSize(420, 260);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
             UserLogistica u = new UserLogistica("114", "Carlos", "1234", "Chefe Logistico", Tier.LOG);
            new FluxoLogisticoWindow(u).setVisible(true);
        });
    }
}