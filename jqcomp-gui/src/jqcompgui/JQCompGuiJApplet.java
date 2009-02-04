/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JQCompGuiJApplet.java
 *
 * Created on 2008-12-16, 15:25:07
 */
package jqcompgui;

import javax.swing.UIManager;

/**
 *
 * @author rob
 */
public class JQCompGuiJApplet extends javax.swing.JApplet {

    static public JQCompGuiJApplet instance;

    /** Initializes the applet JQCompGuiJApplet */
    @Override
    public void init() {
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (Exception ex) {
                        /* DO NOTHING */
                    }
                    initComponents();
                    MainJFrame mjf = MainJFrame.getInstance();
                    mjf.setVisible(true);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        instance = this;
    }

    @Override
    public void destroy() {
        MainJFrame.destroyInstance();
    }

    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        jLabel1.setText("(jqcomp-gui should be started in a separate window)");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addContainerGap(21, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
