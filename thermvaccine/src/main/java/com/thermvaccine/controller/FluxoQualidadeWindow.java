    package com.thermvaccine.controller;

    import com.thermvaccine.model.UserQualidade;
    import com.thermvaccine.model.Usuario;

    import javax.swing.*;
    import javax.swing.border.*;
    import java.awt.*;
    import java.awt.event.*;

    /**
     * View Swing do fluxo de qualidade.
     * Não contém lógica — só monta componentes e expõe métodos para o controller
     * usar.
     */
    public class FluxoQualidadeWindow extends JFrame {

        // ── DESIGN ───────────────────────────────────────────────
        private static final Color GREEN_DARK = new Color(34, 105, 66);
        private static final Color BORDER_CLR = new Color(220, 220, 220);
        private static final Color TEXT_MUTED = new Color(130, 130, 130);
        private static final Color BG = Color.WHITE;

        // ── ESTRUTURA ─────────────────────────────────────────────
        private final FluxoQualidade controller;

        private final JLabel lblTitulo = new JLabel();
        private final JPanel conteudo = new JPanel();
        private final JPanel topoPanel = new JPanel(new BorderLayout());
        private JButton btnVoltar = null;

        public FluxoQualidadeWindow(UserQualidade user, FluxoQualidade controller) {
            this.controller = controller;

            setTitle("ThermVaccine");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(460, 300);
            setResizable(false);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());
            getContentPane().setBackground(BG);

            add(buildHeader(user.getNome()), BorderLayout.NORTH);
            add(buildBody(), BorderLayout.CENTER);
        }

        // ── HEADER ────────────────────────────────────────────────
        private JPanel buildHeader(String nomeUser) {
            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(BG);
            header.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_CLR),
                    new EmptyBorder(14, 24, 14, 24)));

            JLabel title = new JLabel("ThermVaccine");
            title.setFont(new Font("SansSerif", Font.BOLD, 15));
            title.setForeground(GREEN_DARK);

            JLabel nome = new JLabel(nomeUser);
            nome.setFont(new Font("SansSerif", Font.PLAIN, 12));
            nome.setForeground(TEXT_MUTED);

            JButton btnSair = new JButton("Sair");
            btnSair.setFont(new Font("SansSerif", Font.PLAIN, 12));
            btnSair.setForeground(new Color(180, 60, 60));
            btnSair.setBackground(BG);
            btnSair.setBorder(BorderFactory.createLineBorder(BORDER_CLR, 1, true));
            btnSair.setFocusPainted(false);
            btnSair.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnSair.addActionListener(e -> {
                dispose();
                new LoginWindow().setVisible(true);
            });

            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            right.setBackground(BG);
            right.add(nome);
            right.add(btnSair);

            header.add(title, BorderLayout.WEST);
            header.add(right, BorderLayout.EAST);
            return header;
        }

        // ── BODY ──────────────────────────────────────────────────
        private JPanel buildBody() {
            JPanel body = new JPanel(new BorderLayout());
            body.setBackground(BG);
            body.setBorder(new EmptyBorder(20, 24, 20, 24));

            // topo — título + botão voltar
            topoPanel.setBackground(BG);
            topoPanel.setBorder(new EmptyBorder(0, 0, 14, 0));

            lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
            topoPanel.add(lblTitulo, BorderLayout.CENTER);

            body.add(topoPanel, BorderLayout.NORTH);

            // conteúdo scrollável
            conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
            conteudo.setBackground(BG);

            JScrollPane scroll = new JScrollPane(conteudo);
            scroll.setBorder(null);
            scroll.getViewport().setBackground(BG);
            body.add(scroll, BorderLayout.CENTER);

            return body;
        }

        // ── API PARA O CONTROLLER ─────────────────────────────────

        public void limparConteudo() {
            conteudo.removeAll();
        }

        public void setTitulo(String titulo) {
            lblTitulo.setText(titulo);
        }

        public void adicionarCard(String titulo, String sub, Runnable onClick) {
            conteudo.add(buildCard(titulo, sub, onClick));
            conteudo.add(Box.createVerticalStrut(8));
            conteudo.revalidate();
            conteudo.repaint();
        }

        public void adicionarInfoCard(String titulo, String sub) {
            conteudo.add(buildInfoCard(titulo, sub));
            conteudo.add(Box.createVerticalStrut(8));
            conteudo.revalidate();
            conteudo.repaint();
        }

        public void adicionarMensagem(String texto) {
            JLabel lbl = new JLabel(texto);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lbl.setForeground(TEXT_MUTED);
            conteudo.add(lbl);
            conteudo.revalidate();
            conteudo.repaint();
        }

        public void adicionarSecao(String texto) {
            JLabel lbl = new JLabel(texto);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            conteudo.add(lbl);
            conteudo.add(Box.createVerticalStrut(6));
            conteudo.revalidate();
            conteudo.repaint();
        }

        public void adicionarBotaoAcao(String texto, Runnable onClick) {
            JButton btn = new JButton(texto);
            btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
            btn.setForeground(GREEN_DARK);
            btn.setBackground(BG);
            btn.setBorder(BorderFactory.createLineBorder(GREEN_DARK));
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.addActionListener(e -> onClick.run());
            conteudo.add(btn);
            conteudo.add(Box.createVerticalStrut(8));
            conteudo.revalidate();
            conteudo.repaint();
        }

        public void adicionarEspacador() {
            conteudo.add(Box.createVerticalStrut(16));
        }

        public void setBotaoVoltar(Runnable onClick) {
            // remove botão anterior se existir
            if (btnVoltar != null)
                topoPanel.remove(btnVoltar);

            btnVoltar = new JButton("← Voltar");
            btnVoltar.setFont(new Font("SansSerif", Font.PLAIN, 12));
            btnVoltar.setForeground(GREEN_DARK);
            btnVoltar.setBackground(BG);
            btnVoltar.setBorder(BorderFactory.createLineBorder(GREEN_DARK));
            btnVoltar.setFocusPainted(false);
            btnVoltar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnVoltar.addActionListener(e -> onClick.run());

            topoPanel.add(btnVoltar, BorderLayout.WEST);
            lblTitulo.setBorder(new EmptyBorder(0, 10, 0, 0));
            topoPanel.revalidate();
            topoPanel.repaint();
        }

        public void semBotaoVoltar() {
            if (btnVoltar != null) {
                topoPanel.remove(btnVoltar);
                btnVoltar = null;
                lblTitulo.setBorder(null);
                topoPanel.revalidate();
                topoPanel.repaint();
            }
        }

        public void redimensionar(int largura, int altura) {
            setSize(largura, altura);
            setLocationRelativeTo(null);
        }

        // ── COMPONENTES INTERNOS ──────────────────────────────────

        private JPanel buildCard(String titulo, String sub, Runnable onClick) {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(BG);
            card.setBorder(BorderFactory.createLineBorder(BORDER_CLR, 1, true));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
            card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel lblT = new JLabel("  " + titulo);
            lblT.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lblT.setForeground(new Color(40, 40, 40));

            JLabel lblS = new JLabel("  " + sub);
            lblS.setFont(new Font("SansSerif", Font.PLAIN, 11));
            lblS.setForeground(TEXT_MUTED);

            card.add(Box.createVerticalStrut(6));
            card.add(lblT);
            card.add(lblS);
            card.add(Box.createVerticalStrut(6));

            card.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    onClick.run();
                }

                public void mouseEntered(MouseEvent e) {
                    card.setBorder(BorderFactory.createLineBorder(GREEN_DARK, 1, true));
                    lblT.setForeground(GREEN_DARK);
                    card.setBackground(new Color(34, 105, 66, 10));
                }

                public void mouseExited(MouseEvent e) {
                    card.setBorder(BorderFactory.createLineBorder(BORDER_CLR, 1, true));
                    lblT.setForeground(new Color(40, 40, 40));
                    card.setBackground(BG);
                }
            });

            return card;
        }

        private JPanel buildInfoCard(String titulo, String sub) {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(BG);
            card.setBorder(BorderFactory.createLineBorder(BORDER_CLR, 1, true));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel lblT = new JLabel("  " + titulo);
            lblT.setFont(new Font("SansSerif", Font.PLAIN, 13));

            JLabel lblS = new JLabel("  " + sub);
            lblS.setFont(new Font("SansSerif", Font.PLAIN, 11));
            lblS.setForeground(TEXT_MUTED);

            card.add(Box.createVerticalStrut(6));
            card.add(lblT);
            card.add(lblS);
            card.add(Box.createVerticalStrut(6));

            return card;
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ignored) {
                }
                UserQualidade u = new UserQualidade("id1", "Tavi", "123", "Analista", Usuario.Tier.QUA);
                FluxoQualidade controller = new FluxoQualidade();
                FluxoQualidadeWindow window = new FluxoQualidadeWindow(u, controller);
                controller.setWindow(window);
                controller.mostrarTransportes();
                window.setVisible(true);
            });
        }

    }