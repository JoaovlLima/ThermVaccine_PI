package com.thermvaccine.controller;

import com.thermvaccine.model.Transporte;
import com.thermvaccine.service.TransporteService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class FinalizarTransportePanel extends JPanel {

    private static final Color GREEN_DARK = new Color(34, 105, 66);
    private static final Color BORDER     = new Color(220, 220, 220);
    private static final Color TEXT_MUTED = new Color(130, 130, 130);

    private final FluxoLogisticoWindow window;
    private final TransporteService transporteService = new TransporteService();

    private final JPanel painelTransportes;
    private final JLabel lblFeedback;

    public FinalizarTransportePanel(FluxoLogisticoWindow window) {
        this.window = window;
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(24, 24, 24, 24));

        // título
        JLabel title = new JLabel("Registrar retorno");
        title.setFont(new Font("SansSerif", Font.BOLD, 13));
        title.setForeground(new Color(40, 40, 40));
        title.setBorder(new EmptyBorder(0, 0, 14, 0));
        add(title, BorderLayout.NORTH);

        // painel de transportes
        painelTransportes = new JPanel();
        painelTransportes.setLayout(new BoxLayout(painelTransportes, BoxLayout.Y_AXIS));
        painelTransportes.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(painelTransportes);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER, 1, true));
        scroll.getViewport().setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);

        // feedback — inicializado antes de buildActions
        lblFeedback = new JLabel(" ");
        lblFeedback.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblFeedback.setForeground(GREEN_DARK);

        add(buildActions(), BorderLayout.SOUTH);
    }

    public void onShow() {
        lblFeedback.setText(" ");
        lblFeedback.setForeground(GREEN_DARK);
        carregarTransportes();
    }

    private void carregarTransportes() {
        painelTransportes.removeAll();

        List<Transporte> emUso = transporteService.listarTransportesEmUso();

        if (emUso.isEmpty()) {
            JLabel vazio = new JLabel("Nenhum transporte em uso.");
            vazio.setFont(new Font("SansSerif", Font.PLAIN, 12));
            vazio.setForeground(TEXT_MUTED);
            vazio.setBorder(new EmptyBorder(12, 12, 12, 12));
            painelTransportes.add(vazio);
        } else {
            for (Transporte t : emUso) {
                painelTransportes.add(buildTransporteItem(t));
            }
        }

        painelTransportes.revalidate();
        painelTransportes.repaint();
        revalidate();
        repaint();
    }

    private JPanel buildTransporteItem(Transporte transporte) {
        JLabel lblPlaca = new JLabel("Placa: " + transporte.getPlaca());
        lblPlaca.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblPlaca.setForeground(new Color(40, 40, 40));

        JLabel lblCap = new JLabel("Capacidade: " + transporte.getCapacidade() + " caixas");
        lblCap.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblCap.setForeground(TEXT_MUTED);

        JButton btnRetorno = new JButton("Registrar retorno");
        btnRetorno.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnRetorno.setBackground(Color.WHITE);
        btnRetorno.setForeground(GREEN_DARK);
        btnRetorno.setBorder(BorderFactory.createLineBorder(GREEN_DARK, 1, true));
        btnRetorno.setFocusPainted(false);
        btnRetorno.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRetorno.setPreferredSize(new Dimension(150, 30));
        btnRetorno.addActionListener(e -> registrarRetorno(transporte.getPlaca()));

        JPanel texts = new JPanel();
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        texts.setBackground(Color.WHITE);
        texts.add(lblPlaca);
        texts.add(Box.createVerticalStrut(2));
        texts.add(lblCap);

        JPanel item = new JPanel(new BorderLayout(10, 0));
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                new EmptyBorder(10, 12, 10, 12)
        ));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        item.add(texts, BorderLayout.CENTER);
        item.add(btnRetorno, BorderLayout.EAST);

        return item;
    }

    private void registrarRetorno(String placa) {
        try {
            transporteService.finalizarTransporte(placa);
            mostrarFeedback("Retorno registrado com sucesso!", false);
            carregarTransportes();
        } catch (IllegalStateException e) {
            mostrarFeedback(e.getMessage(), true);
        }
    }

    private JPanel buildActions() {
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnVoltar.setPreferredSize(new Dimension(100, 34));
        btnVoltar.setBackground(Color.WHITE);
        btnVoltar.setForeground(new Color(60, 60, 60));
        btnVoltar.setBorder(BorderFactory.createLineBorder(BORDER, 1, true));
        btnVoltar.setFocusPainted(false);
        btnVoltar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVoltar.addActionListener(e -> window.voltarMenu());

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.setBackground(Color.WHITE);
        btns.add(btnVoltar);

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

    private void mostrarFeedback(String msg, boolean erro) {
        lblFeedback.setForeground(erro ? new Color(180, 60, 60) : GREEN_DARK);
        lblFeedback.setText(msg);
    }
}