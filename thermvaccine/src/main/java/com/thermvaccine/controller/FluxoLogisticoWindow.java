package com.thermvaccine.controller;

import com.thermvaccine.model.UserLogistica;
import com.thermvaccine.model.UserQualidade;
import com.thermvaccine.model.Usuario;
import com.thermvaccine.model.Usuario.Tier;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FluxoLogisticoWindow extends JFrame {

    private static final Color GREEN_DARK = new Color(34, 105, 66);
    private static final Color BORDER = new Color(220, 220, 220);
    private static final Color TEXT_MUTED = new Color(130, 130, 130);

    private final CriarComandaPanel criarComandaPanel = new CriarComandaPanel(this);
    private final IniciarTransportePanel iniciarTransportePanel = new IniciarTransportePanel(this);
    private final FinalizarTransportePanel finalizarTransportePanel = new FinalizarTransportePanel(this);

    private final UserLogistica usuario;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);

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
        cards.add(buildMenu(), "menu");
        cards.add(new CadastrarLotePanel(this), "lote");
        cards.add(criarComandaPanel, "comanda");
        cards.add(iniciarTransportePanel, "transporte");
        cards.add(finalizarTransportePanel, "finalizarTransporte");
        add(cards, BorderLayout.CENTER);

        cardLayout.show(cards, "menu");
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                new EmptyBorder(14, 24, 14, 24)));

        JLabel title = new JLabel("ThermVaccine");
        title.setFont(new Font("SansSerif", Font.BOLD, 15));
        title.setForeground(GREEN_DARK);

        JLabel user = new JLabel(usuario.getNome());
        user.setFont(new Font("SansSerif", Font.PLAIN, 12));
        user.setForeground(TEXT_MUTED);

        JButton btnSair = new JButton("Sair");
        btnSair.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnSair.setForeground(new Color(180, 60, 60));
        btnSair.setBackground(Color.WHITE);
        btnSair.setBorder(BorderFactory.createLineBorder(BORDER, 1, true));
        btnSair.setFocusPainted(false);
        btnSair.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSair.addActionListener(e -> {
            dispose();
            new LoginWindow().setVisible(true);
        });

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setBackground(Color.WHITE);
        right.add(user);
        right.add(btnSair);

        header.add(title, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    private JPanel buildMenu() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton btnLote = menuButton("Cadastrar lote");
        JButton btnComanda = menuButton("Criar comanda");
        JButton btnTransporte = menuButton("Iniciar transporte");
        JButton btnFinalizarTransporte = menuButton("Finalizar Transporte");

        btnLote.addActionListener(e -> navegarPara("lote", 460));
        btnComanda.addActionListener(e -> navegarPara("comanda", 560));
        btnTransporte.addActionListener(e -> navegarPara("transporte", 460));
        btnFinalizarTransporte.addActionListener(e -> navegarPara("finalizarTransporte", 460));

        panel.add(btnLote);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnComanda);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnTransporte);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnFinalizarTransporte);

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
                btn.setBorder(BorderFactory.createLineBorder(GREEN_DARK, 1, true));
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

    public void navegarPara(String card, int altura) {
        cardLayout.show(cards, card);
        setSize(420, altura);
        setLocationRelativeTo(null);

        // notifica o painel
        if (card.equals("comanda"))
            criarComandaPanel.onShow();
        if (card.equals("transporte"))
            iniciarTransportePanel.onShow();
        if (card.equals("finalizarTransporte"))
            finalizarTransportePanel.onShow();
    }

    public void voltarMenu() {
        cardLayout.show(cards, "menu");
        setSize(420, 260);
        setLocationRelativeTo(null);
    }

    public UserLogistica getUsuario() {
        return usuario;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            UserLogistica u = new UserLogistica("114", "Carlos", "1234", "Chefe Logistico", Tier.LOG);
            new FluxoLogisticoWindow(u).setVisible(true);
        });
    }
}