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
import pl.lodz.p.ics.quantum.jqcomp.qgates.Swap;

/**
 *
 * @author rob
 */
public class QCircuitJPanel extends javax.swing.JPanel {

    private QCircuit qcircuit;
    private int currentStage;
    private Stage selectedStage;
    private Color backgroundColor;
    private Color currentStageColor = Color.RED;
    private Color selectedStageColor = Color.YELLOW;
    private Stroke currentStageStroke = new BasicStroke(2);
    private int xstep = 50;
    private int ystep = 50;
    public float zoom = 1.0f;
    private int x_offset = 70;

    /** Creates new form QCircuitJPanel */
    public QCircuitJPanel() {
        initComponents();
        setPreferredSize(new Dimension(200, 100));
    }

    @Override
    public void paintComponent(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        backgroundColor = Color.WHITE;
        g.scale(zoom, zoom);
        g.setColor(backgroundColor);
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
            return;
        }

        /*
         * Draw quantum cicruit
         */

        if (qcircuit.getStages().size() == 0) {
            return;
        }

        // draw selected stage marker
        if (selectedStage != null) {
            for (int si = 0; si < qcircuit.getStages().size(); si++) {
                Stage s = qcircuit.getStages().get(si);
                if (s == selectedStage) {
                    g.setColor(selectedStageColor);
                    g.fillRect(x_offset + (si + 1) * xstep - xstep / 2, ystep - 5 - 10,
                            xstep, ystep * (s.getSize() - 1) + 20);
                    break;
                }
            }
        }

        // draw qubits labels
        for (int i = 0; i < qcircuit.getStages().get(0).getSize(); i++) {
            g.setColor(Color.BLACK);
            g.drawString("|q" +
                    new Integer(qcircuit.getStages().get(0).getSize() - i - 1).toString() +
                    ">", x_offset - 40, ystep + i * ystep);
            g.drawLine(x_offset, ystep - 5 + i * ystep, x_offset + xstep, ystep - 5 + i * ystep);
        }

        // draw points and qubit lines
        for (int si = 0; si < qcircuit.getStages().size(); si++) {
            Stage s = qcircuit.getStages().get(si);
            for (int q = 0; q < s.getSize(); q++) {
                g.drawLine(x_offset + xstep + si * xstep, ystep - 5 + q * ystep,
                        x_offset + xstep + si * xstep + xstep, ystep - 5 + q * ystep);
                g.drawOval(x_offset + xstep - 2 + si * xstep, ystep - 5 + q * ystep - 2, 4, 4);
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
        g.drawLine(x_offset + xstep / 2 + xstep * currentStage, ystep - 10,
                x_offset + xstep / 2 + xstep * currentStage, ystep + ystep * (qcircuit.getStages().get(0).getSize() - 1) + 10);
    }

    private void drawQGate(Graphics2D g, ElementaryQGate gate, int si, int gi) {
        if (gate instanceof Hadamard) {
            drawGateBox(g, si, gi, gate);
            g.drawString("H", x_offset + xstep - 10 + si * xstep + 5, ystep - 10 + gi * ystep - 5 + 15);
        } else if (gate instanceof CNot) {
            CNot cnot = (CNot) gate;
            int x1 = x_offset + xstep + si * xstep;
            int y1 = ystep + gi * ystep - 5;
            int y2 = y1 + ystep * (gate.getSize() - 1);
            if (cnot.getControl() == 1) {
                g.fillOval(x1 - 5, y1 - 5, 10, 10);
                g.setColor(backgroundColor);
                g.fillOval(x1 - 5, y2 - 5, 10, 10);
                g.setColor(Color.BLACK);
                g.drawOval(x1 - 5, y2 - 5, 10, 10);
                g.drawLine(x1 - 5, y2, x1 + 5, y2);
            } else {
                g.setColor(backgroundColor);
                g.fillOval(x1 - 5, y1 - 5, 10, 10);
                g.setColor(Color.BLACK);
                g.drawOval(x1 - 5, y1 - 5, 10, 10);
                g.drawLine(x1 - 5, y1, x1 + 5, y1);
                g.fillOval(x1 - 5, y2 - 5, 10, 10);
            }
            g.drawLine(x1, y1 - 5, x1, y2 + 5);
        } else if (gate instanceof Swap) {
            if (qcircuit.getStages().get(si) == selectedStage) {
                g.setColor(selectedStageColor);
            } else {
                g.setColor(backgroundColor);
            }
            g.fillRect(x_offset + xstep + si * xstep - 10, ystep + gi * ystep - 5 - 5,
                    20, ystep + 10);
            g.setColor(Color.BLACK);
            g.drawLine(x_offset + xstep + si * xstep - 10, ystep + gi * ystep - 5,
                    x_offset + xstep + si * xstep + 10, ystep + (gi + 1) * ystep - 5);
            g.drawLine(x_offset + xstep + si * xstep + 10, ystep + gi * ystep - 5,
                    x_offset + xstep + si * xstep - 10, ystep + (gi + 1) * ystep - 5);
        } else {
            drawGateBox(g, si, gi, gate);
        }
    }

    private void drawGateBox(Graphics2D g, int si, int gi, ElementaryQGate gate) {
        g.setColor(backgroundColor);
        g.fillRect(x_offset + xstep - 10 + si * xstep, ystep - 10 + gi * ystep - 5, 20, 20 + (ystep * (gate.getSize() - 1)));
        g.setColor(Color.BLACK);
        g.drawRect(x_offset + xstep - 10 + si * xstep, ystep - 10 + gi * ystep - 5, 20, 20 + (ystep * (gate.getSize() - 1)));
    }

    private int getStageIndexByXY(int x, int y) {
        for (int i = 0; i < qcircuit.getStages().size(); i++) {
            if (Math.abs(x_offset + xstep * (i + 1) - x) < xstep / 2) {
                return i;
            }
        }
        return -1;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });

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

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        int x = (int) (1.0f * evt.getX() / zoom);
        int y = (int) (1.0f * evt.getY() / zoom);
//        System.out.println(String.format("%1$s %2$s", x, y));
        int nsi = getStageIndexByXY(x, y);
        if (nsi < 0) {
            return;
        }
        selectedStage = qcircuit.getStages().get(nsi);
        repaint();
    }//GEN-LAST:event_formMouseClicked

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        int x = (int) (1.0f * evt.getX() / zoom);
        int y = (int) (1.0f * evt.getY() / zoom);
        int nsi = getStageIndexByXY(x, y);
        if (nsi < 0) {
            return;
        }
        Stage newStage = qcircuit.getStages().get(nsi);
        if (newStage != selectedStage) {
            // reorder stages
            qcircuit.getStages().remove(selectedStage);
            qcircuit.getStages().add(nsi, selectedStage);
            repaint();
        }
    }//GEN-LAST:event_formMouseDragged

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        formMouseClicked(evt);
    }//GEN-LAST:event_formMousePressed

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
     * @return the selectedStage
     */
    public Stage getSelectedStage() {
        return selectedStage;
    }

    /**
     * @param selectedStage the selectedStage to set
     */
    public void setSelectedStage(Stage selectedStage) {
        this.selectedStage = selectedStage;
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
