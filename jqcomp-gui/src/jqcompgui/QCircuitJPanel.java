/*
 * QCircuitJPanel.java
 *
 * Created on 2008-12-04, 15:26:36
 */
package jqcompgui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import pl.lodz.p.ics.quantum.jqcomp.Measurement;
import pl.lodz.p.ics.quantum.jqcomp.QCircuit;
import pl.lodz.p.ics.quantum.jqcomp.Stage;
import pl.lodz.p.ics.quantum.jqcomp.qgates.CNot;
import pl.lodz.p.ics.quantum.jqcomp.qgates.CompoundQGate;
import pl.lodz.p.ics.quantum.jqcomp.qgates.ElementaryQGate;
import pl.lodz.p.ics.quantum.jqcomp.qgates.Hadamard;
import pl.lodz.p.ics.quantum.jqcomp.qgates.Identity;

/**
 *
 * @author rob
 */
public class QCircuitJPanel extends javax.swing.JPanel {

    private QCircuit qcircuit;
    private int currentStage;
    private Color currentStageColor = Color.RED;
    private Stroke currentStageStroke = new BasicStroke(2);
    private int xstep = 50;
    private int ystep = 50;
    public float zoom = 1.0f;

    /** Creates new form QCircuitJPanel */
    public QCircuitJPanel() {
        initComponents();
        setPreferredSize(new Dimension(200, 100));
    }

    @Override
    public void paintComponent(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        g.scale(zoom, zoom);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.BLACK);

        if (qcircuit == null) {
            // draw some preview
            g.drawString("|0>", 30, 50);
            g.drawString("|0>", 30, 90);
            g.drawString("|0>", 30, 130);

            g.drawLine(60, 45, 150, 45);
            g.drawLine(60, 85, 150, 85);
            g.drawLine(60, 125, 150, 125);
//            setPreferredSize(new Dimension(200, 100));
            return;
        }

        /*
         * Draw quantum cicruit
         */

        if (qcircuit.getStages().size() == 0) {
            return;
        }

        // draw qubits labels
        for (int i = 0; i < qcircuit.getStages().get(0).getSize(); i++) {
            g.drawString("|q" +
                    new Integer(qcircuit.getStages().size() - i - 1).toString() +
                    ">", 30, ystep + i * ystep);
            g.drawLine(70, ystep - 5 + i * ystep, 70 + xstep, ystep - 5 + i * ystep);
        }

        // draw points and qubit lines
        for (int si = 0; si < qcircuit.getStages().size(); si++) {
            Stage s = qcircuit.getStages().get(si);
            for (int q = 0; q < s.getSize(); q++) {
                g.drawLine(70 + xstep + si * xstep, ystep - 5 + q * ystep,
                        70 + xstep + si * xstep + xstep, ystep - 5 + q * ystep);
                g.drawOval(70 + xstep - 2 + si * xstep, ystep - 5 + q * ystep - 2, 4, 4);
            }
        }

        // draw array of quantum gates in circuit
        for (int si = 0; si < qcircuit.getStages().size(); si++) {
            Stage s = qcircuit.getStages().get(si);
            if (s instanceof CompoundQGate) {
                CompoundQGate c = (CompoundQGate) s;
                for (int gi = 0; gi < c.getGates().size(); gi++) {
                    ElementaryQGate gate = c.getGates().get(gi);
                    if (gate instanceof Identity) {
                        continue;
                    }
                    drawQGate(g, gate, si, gi);
                }
            } else if (s instanceof ElementaryQGate) {
                ElementaryQGate gate = (ElementaryQGate) s;
                drawQGate(g, gate, si, 0);
            } else if (s instanceof Measurement) {
            }
        }

        // draw line representing current state
        g.setPaint(currentStageColor);
        g.setStroke(currentStageStroke);
        g.drawLine(70 + xstep / 2 + xstep * currentStage, ystep - 10,
                70 + xstep / 2 + xstep * currentStage, ystep + ystep * (qcircuit.getStages().get(0).getSize() - 1) + 10);
    }

    private void drawQGate(Graphics2D g, ElementaryQGate gate, int si, int gi) {
        if (gate instanceof Hadamard) {
            drawGateBox(g, si, gi, gate);
            g.drawString("H", 70 + xstep - 10 + si * xstep + 5, ystep - 10 + gi * ystep - 5 + 15);
        } else if (gate instanceof CNot) {
            CNot cnot = (CNot) gate;
            int x1 = 70 + xstep + si * xstep;
            int y1 = ystep + gi * ystep - 5;
            int y2 = y1 + ystep * (gate.getSize() - 1);
            if (cnot.getControl() == 1) {
                g.fillOval(x1 - 5, y1 - 5, 10, 10);
                g.setColor(Color.WHITE);
                g.fillOval(x1 - 5, y2 - 5, 10, 10);
                g.setColor(Color.BLACK);
                g.drawOval(x1 - 5, y2 - 5, 10, 10);
                g.drawLine(x1 - 5, y2, x1 + 5, y2);
            } else {
                g.setColor(Color.WHITE);
                g.fillOval(x1 - 5, y1 - 5, 10, 10);
                g.setColor(Color.BLACK);
                g.drawOval(x1 - 5, y1 - 5, 10, 10);
                g.drawLine(x1 - 5, y1, x1 + 5, y1);
                g.fillOval(x1 - 5, y2 - 5, 10, 10);
            }
            g.drawLine(x1, y1 - 5, x1, y2 + 5);
        } else {
            drawGateBox(g, si, gi, gate);
        }
    }

    private void drawGateBox(Graphics2D g, int si, int gi, ElementaryQGate gate) {
        g.setColor(Color.WHITE);
        g.fillRect(70 + xstep - 10 + si * xstep, ystep - 10 + gi * ystep - 5, 20, 20 + (ystep * (gate.getSize() - 1)));
        g.setColor(Color.BLACK);
        g.drawRect(70 + xstep - 10 + si * xstep, ystep - 10 + gi * ystep - 5, 20, 20 + (ystep * (gate.getSize() - 1)));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 389, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 206, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @return the qcircuit
     */
    public QCircuit getQcircuit() {
        return qcircuit;
    }

    /**
     * @param qcircuit the qcircuit to set
     */
    public void setQcircuit(QCircuit qcircuit) {
        this.qcircuit = qcircuit;
        setPreferredSize(new Dimension(
                xstep * (qcircuit.getStages().size() + 3),
                ystep * (qcircuit.getStages().get(0).getSize() + 1)));
        revalidate();
    }

    /**
     * @return the currentStage
     */
    public int getCurrentStage() {
        return currentStage;
    }

    /**
     * @param currentStage the currentStage to set
     */
    public void setCurrentStage(int currentStage) {
        this.currentStage = currentStage;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
