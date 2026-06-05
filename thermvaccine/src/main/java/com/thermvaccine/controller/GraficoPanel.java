package com.thermvaccine.controller;

import com.thermvaccine.model.DataLogger;
import com.thermvaccine.model.RegistroDatalogger;
import com.thermvaccine.repository.DataLoggerRepository;
import com.thermvaccine.service.DataLoggerService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Painel Swing que desenha o gráfico de temperatura do datalogger.
 * Atualizado via timer a cada 5 segundos.
 */
public class GraficoPanel extends JPanel {

    private static final Color GREEN_DARK = new Color(34, 105, 66);
    private static final Color EIXO       = new Color(100, 100, 100);
    private static final Font  FONTE_EIXO = new Font("SansSerif", Font.PLAIN, 10);

    private final DataLoggerService dataLoggerService = new DataLoggerService();
    private List<Double> temperaturas = new ArrayList<>();
    private List<String> labels       = new ArrayList<>();

    public GraficoPanel(DataLogger dataLogger) {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 30, 20));
        atualizar(dataLogger.getId());
    }

    public void atualizar(String idDataLogger) {
        DataLogger dl = dataLoggerService.buscarPorId(idDataLogger);
        if (dl == null) return;

        List<RegistroDatalogger> registros = dl.getRegistroDatalogger();
        if (registros == null || registros.isEmpty()) return;

        temperaturas.clear();
        labels.clear();

        // últimos 60 registros
        int inicio = Math.max(0, registros.size() - 60);
        for (int i = inicio; i < registros.size(); i++) {
            RegistroDatalogger r = registros.get(i);
            temperaturas.add((double) r.getTemperatura());
            String hora = r.getData_hora() != null
                ? r.getData_hora().toLocalTime().toString().substring(0, 8)
                : String.valueOf(r.getId());
            labels.add(hora);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (temperaturas.isEmpty()) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("SansSerif", Font.PLAIN, 13));
            g.drawString("Aguardando dados...", getWidth() / 2 - 60, getHeight() / 2);
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int offX = 45, offY = 10;
        int w = getWidth()  - offX - 10;
        int h = getHeight() - offY - 40;

        double min   = temperaturas.stream().mapToDouble(Double::doubleValue).min().orElse(0);
        double max   = temperaturas.stream().mapToDouble(Double::doubleValue).max().orElse(1);
        double range = max - min == 0 ? 1 : max - min;

        // grid + eixo Y
        g2.setFont(FONTE_EIXO);
        for (int i = 0; i <= 5; i++) {
            double val = min + (range / 5) * i;
            int y = offY + h - (int)((val - min) / range * h);
            g2.setColor(new Color(230, 230, 230));
            g2.drawLine(offX, y, offX + w, y);
            g2.setColor(EIXO);
            g2.drawString(String.format("%.1f", val), 2, y + 4);
        }

        // eixos
        g2.setColor(EIXO);
        g2.drawLine(offX, offY, offX, offY + h);
        g2.drawLine(offX, offY + h, offX + w, offY + h);

        // labels X
        int n    = temperaturas.size();
        int step = Math.max(1, n / 8);
        for (int i = 0; i < n; i += step) {
            int x = offX + (n == 1 ? w / 2 : (int)((double) i / (n - 1) * w));
            g2.setColor(EIXO);
            g2.drawString(labels.get(i), x - 16, offY + h + 14);
        }

        // linha do gráfico
        g2.setColor(GREEN_DARK);
        g2.setStroke(new BasicStroke(2f));
        for (int i = 1; i < n; i++) {
            int x1 = offX + (n == 1 ? w / 2 : (int)((double)(i - 1) / (n - 1) * w));
            int y1 = offY + h - (int)((temperaturas.get(i - 1) - min) / range * h);
            int x2 = offX + (n == 1 ? w / 2 : (int)((double) i       / (n - 1) * w));
            int y2 = offY + h - (int)((temperaturas.get(i)     - min) / range * h);
            g2.drawLine(x1, y1, x2, y2);
        }

        // pontos
        for (int i = 0; i < n; i++) {
            int x = offX + (n == 1 ? w / 2 : (int)((double) i / (n - 1) * w));
            int y = offY + h - (int)((temperaturas.get(i) - min) / range * h);
            g2.fillOval(x - 3, y - 3, 6, 6);
        }

        // título
        g2.setColor(Color.DARK_GRAY);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g2.drawString("Temperatura (°C) — últimos " + n + " registros", offX, offY + h + 28);
    }
}