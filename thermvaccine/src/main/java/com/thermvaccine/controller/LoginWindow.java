package com.thermvaccine.controller;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.thermvaccine.model.UserLogistica;
import com.thermvaccine.model.UserQualidade;
import com.thermvaccine.model.Usuario;
import com.thermvaccine.service.UsuarioService;

import java.awt.*;

public class LoginWindow extends JFrame {

    private static final Color GREEN_DARK = new Color(34, 105, 66);
    private static final Color BORDER = new Color(220, 220, 220);
    private static final Color TEXT_MUTED = new Color(130, 130, 130);

    private final UsuarioService usuarioService = new UsuarioService();

    private JTextField txtRe;
    private JPasswordField txtSenha;
    private JLabel lblFeedback;

    public LoginWindow() {
        setTitle("ThermVaccine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(360, 280);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildForm(), BorderLayout.CENTER);
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
        header.add(title, BorderLayout.WEST);
        return header;
    }

    private JPanel buildForm() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        txtRe = new JTextField();
        txtSenha = new JPasswordField();
        styleInput(txtRe);
        styleInput(txtSenha);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 12, 0);
        fields.add(buildField("RE", txtRe), gbc);

        gbc.gridy = 1;
        fields.add(buildField("Senha", txtSenha), gbc);

        panel.add(fields, BorderLayout.CENTER);
        panel.add(buildActions(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildActions() {
        lblFeedback = new JLabel(" ");
        lblFeedback.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblFeedback.setForeground(new Color(180, 60, 60));

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnEntrar.setPreferredSize(new Dimension(110, 34));
        btnEntrar.setBackground(GREEN_DARK);
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setBorder(BorderFactory.createLineBorder(GREEN_DARK, 1, true));
        btnEntrar.setFocusPainted(false);
        btnEntrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEntrar.addActionListener(e -> logar());

        // permite logar com Enter
        txtSenha.addActionListener(e -> logar());

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btns.setBackground(Color.WHITE);
        btns.add(btnEntrar);

        JPanel actions = new JPanel(new BorderLayout());
        actions.setBackground(Color.WHITE);
        actions.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER),
                new EmptyBorder(12, 0, 0, 0)));
        actions.add(lblFeedback, BorderLayout.WEST);
        actions.add(btns, BorderLayout.EAST);
        return actions;
    }

    private void logar() {
        String re = txtRe.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        if (re.isEmpty() || senha.isEmpty()) {
            lblFeedback.setText("Preencha todos os campos.");
            return;
        }

        try {
            Usuario usuario = usuarioService.autenticacao(re, senha);
            if (usuario instanceof UserLogistica u) {
                dispose();
                new FluxoLogisticoWindow(u).setVisible(true);
            } else if (usuario instanceof UserQualidade u) {
                dispose();
                FluxoQualidade controller = new FluxoQualidade();
                controller.start(u);
            }
        } catch (IllegalArgumentException e) {
            lblFeedback.setText("Usuário não encontrado.");
        } catch (IllegalAccessError e) {
            lblFeedback.setText("Senha incorreta.");
        }
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
                new EmptyBorder(0, 8, 0, 8)));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new LoginWindow().setVisible(true);
        });
    }
}