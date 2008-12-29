/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainJFrame.java
 *
 * Created on 2008-12-10, 00:57:38
 */
package jqcompgui;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.jscience.mathematics.number.Complex;
import pl.lodz.p.ics.quantum.jqcomp.MoreMath;
import pl.lodz.p.ics.quantum.jqcomp.QCircuit;
import pl.lodz.p.ics.quantum.jqcomp.QGate;
import pl.lodz.p.ics.quantum.jqcomp.QRegister;
import pl.lodz.p.ics.quantum.jqcomp.Stage;
import pl.lodz.p.ics.quantum.jqcomp.qgates.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

/**
 *
 * @author rob
 */
public class MainJFrame extends javax.swing.JFrame {

    private abstract class MyAction
		extends AbstractAction implements MouseListener {

		public MyAction(String text, ImageIcon icon, String desc) {
				super(text, icon);

				putValue(SHORT_DESCRIPTION, desc);
		}

		public MyAction(String text, ImageIcon icon) {
			super(text, icon);
		}

        public MyAction(String text, String desc) {
			super(text);
            putValue(SHORT_DESCRIPTION, desc);

		}

		public void mouseEntered(MouseEvent arg0) {
			//setStatus((String)getValue(SHORT_DESCRIPTION));
		}

		public void mouseExited(MouseEvent arg0) {
			//setStatus("");
		}

		public void mousePressed(MouseEvent arg0) { }
		public void mouseReleased(MouseEvent arg0) { }
		public void mouseClicked(MouseEvent arg0) {	}
	}


    private abstract class AddGateAction extends MyAction {
        public AddGateAction(String name) {
            super(name, "Add " + name + " quantum gate to the selected circuit");
        }

        public AddGateAction(String name, ImageIcon icon) {
            super(name, icon, "Add " + name + " quantum gate to the selected circuit");
        }

        public void doAction(QGate gate) {
            Object icon = getValue(LARGE_ICON_KEY);
            if(icon == null) {
                icon = getValue(SMALL_ICON);
            }

            doQGateAddDialog(gate, (ImageIcon)icon);
        }
    }

    private class AddSwapGateAction extends AddGateAction {
        public AddSwapGateAction() {
            super("Swap", new ImageIcon(MainJFrame.this.getClass().getResource(
                    "/jqcompgui/img/swap_icon.png")));
        }
        
        @Override
        public void actionPerformed(ActionEvent ae) {
            doAction(new Swap());
        }
    }
    private AddSwapGateAction swapAction = new AddSwapGateAction();

    private class AddHadamardGateAction extends AddGateAction {
        public AddHadamardGateAction() {
            super("Hadamard", new ImageIcon(MainJFrame.this.getClass().getResource(
                    "/jqcompgui/img/hadamard_icon.png")));
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            doAction(new Hadamard());
        }
    }
    private AddHadamardGateAction hadamardAction = new AddHadamardGateAction();

    private class AddCNotGateAction extends AddGateAction {
        public AddCNotGateAction() {
            super("Controlled Not", new ImageIcon(MainJFrame.this.getClass().getResource(
                    "/jqcompgui/img/CNOT_icon.png")));
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            doAction(new CNot());
        }
    }
    private AddCNotGateAction cNotAction = new AddCNotGateAction();

    private class AddNotGateAction extends AddGateAction {
        public AddNotGateAction() {
            super("Not", new ImageIcon(MainJFrame.this.getClass().getResource(
                    "/jqcompgui/img/Not_icon.png")));
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            doAction(new Not());
        }
    }
    private AddNotGateAction notAction = new AddNotGateAction();

    private class AddPhaseShiftGateAction extends AddGateAction {
        public AddPhaseShiftGateAction() {
            super("Phase Shift", new ImageIcon(MainJFrame.this.getClass().getResource(
                    "/jqcompgui/img/phase_icon.png")));
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            doAction(new PhaseShift(Math.PI / 2));
        }
    }
    private AddPhaseShiftGateAction phaseShiftAction = new AddPhaseShiftGateAction();

    private class AddToffoliGateAction extends AddGateAction {
        public AddToffoliGateAction() {
            super("Toffoli", new ImageIcon(MainJFrame.this.getClass().getResource(
                    "/jqcompgui/img/Toffoli_icon.png")));
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            doAction(new Toffoli());
        }
    }
    private AddToffoliGateAction toffoliAction = new AddToffoliGateAction();

    private class AddFredkinGateAction extends AddGateAction {
        public AddFredkinGateAction() {
            super("Fredkin", new ImageIcon(MainJFrame.this.getClass().getResource(
                    "/jqcompgui/img/fredkin_icon.png")));
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            System.out.println("Fredkin");
            doAction(new Fredkin());
        }
    }
    private AddFredkinGateAction fredkinAction = new AddFredkinGateAction();

    private class AddCustomGateAction extends AddGateAction {
        public AddCustomGateAction() {
            super("Custom", new ImageIcon(MainJFrame.this.getClass().getResource(
                    "/jqcompgui/img/custom_icon.png")));
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            doAction(new Custom(new Identity().getMatrix()));
        }
    }
    private AddCustomGateAction customAction = new AddCustomGateAction();

    private static MainJFrame instance;

    public static MainJFrame getInstance() {
        if (instance == null) {
            instance = new MainJFrame();
        }
        return instance;
    }

    /*
     * Required to prevent applet memory leakage
     */
    public static void destroyInstance() {
        if (instance != null) {
            instance.setVisible(false);
            instance.dispose();
        }
        instance = null;
        System.gc();
    }

    /** Creates new form MainJFrame */
    private MainJFrame() {
        
        assert(instance == null);

        initComponents();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            /* DO NOTHING */
        }

        writeMsg("Quantum Computer Simulator started");

        this.inputRegister = QRegister.ket(15, 4);
        writeMsg(inputRegister.dirac());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        hadamardJButton = new javax.swing.JButton();
        notJButton = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        leftJPanel = new javax.swing.JPanel();
        resetJButton = new javax.swing.JButton();
        stepJButton = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        forwardJButton = new javax.swing.JButton();
        backwardJButton = new javax.swing.JButton();
        removeJButton = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        zoomInJButton = new javax.swing.JButton();
        zoomOutJButton = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JSeparator();
        addQubitJButton = new javax.swing.JButton();
        removeQubitJButton = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        outputJTextArea = new javax.swing.JTextArea();
        statusBarJPanel = new javax.swing.JPanel();
        statusBarJLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileJMenu = new javax.swing.JMenu();
        newQuantumCircuitJMenuItem = new javax.swing.JMenuItem();
        loadJMenuItem = new javax.swing.JMenuItem();
        saveJMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        quitJMenuItem = new javax.swing.JMenuItem();
        quantumGatesJMenu = new javax.swing.JMenu();
        jmiHadamard = new javax.swing.JMenuItem();
        jtmCNot = new javax.swing.JMenuItem();
        jtmNot = new javax.swing.JMenuItem();
        jtmPhaseShift = new javax.swing.JMenuItem();
        jmiSwap = new javax.swing.JMenuItem();
        jtmToffoli = new javax.swing.JMenuItem();
        jmiFredkin = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jMenuItem16 = new javax.swing.JMenuItem();
        algorithmsJMenu = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();
        SuperdenseJMenuItem = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        entanglementAlgorithmJMenuItem = new javax.swing.JMenuItem();
        settingsJMenu = new javax.swing.JMenu();
        chkHideImaginary = new javax.swing.JCheckBoxMenuItem();
        windowsJMenu = new javax.swing.JMenu();
        closeAllWindowsJMenuItem = new javax.swing.JMenuItem();
        minimizeAllWindowsJMenuItem = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JSeparator();
        helpJMenu = new javax.swing.JMenu();
        helpContentsJMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        aboutJMenuItem = new javax.swing.JMenuItem();

        setTitle("jqcomp-gui: Java Quantum Computer Simulator. Technical University of Lodz");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        hadamardJButton.setAction(hadamardAction);
        hadamardJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jqcompgui/img/hadamard_icon.png"))); // NOI18N
        hadamardJButton.setText("Hadamard");
        hadamardJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hadamardJButtonActionPerformed(evt);
            }
        });
        jPanel2.add(hadamardJButton);

        notJButton.setAction(notAction);
        notJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jqcompgui/img/Not_icon.png"))); // NOI18N
        notJButton.setText("Not");
        notJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notJButtonActionPerformed(evt);
            }
        });
        jPanel2.add(notJButton);

        jButton5.setAction(cNotAction);
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jqcompgui/img/CNOT_icon.png"))); // NOI18N
        jButton5.setText("CNot");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5);

        jButton6.setAction(phaseShiftAction);
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jqcompgui/img/phase_icon.png"))); // NOI18N
        jButton6.setText("Phase");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton6);

        jButton7.setAction(toffoliAction);
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jqcompgui/img/Toffoli_icon.png"))); // NOI18N
        jButton7.setText("Toffoli");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton7);

        jButton8.setAction(customAction);
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jqcompgui/img/custom_icon.png"))); // NOI18N
        jButton8.setText("Custom");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton8);

        leftJPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        resetJButton.setText("reset");
        resetJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetJButtonActionPerformed(evt);
            }
        });

        stepJButton.setText("step");
        stepJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stepJButtonActionPerformed(evt);
            }
        });

        jButton2.setText("run");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        forwardJButton.setText("Forward");
        forwardJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forwardJButtonActionPerformed(evt);
            }
        });

        backwardJButton.setText("Backward");
        backwardJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backwardJButtonActionPerformed(evt);
            }
        });

        removeJButton.setText("Remove");
        removeJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeJButtonActionPerformed(evt);
            }
        });

        zoomInJButton.setText("Zoom In");
        zoomInJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomInJButtonActionPerformed(evt);
            }
        });

        zoomOutJButton.setText("Zoom Out");
        zoomOutJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomOutJButtonActionPerformed(evt);
            }
        });

        addQubitJButton.setText("Add Qubit");
        addQubitJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addQubitJButtonActionPerformed(evt);
            }
        });

        removeQubitJButton.setText("Remove Qubit");
        removeQubitJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeQubitJButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout leftJPanelLayout = new javax.swing.GroupLayout(leftJPanel);
        leftJPanel.setLayout(leftJPanelLayout);
        leftJPanelLayout.setHorizontalGroup(
            leftJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(resetJButton, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
            .addComponent(stepJButton, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
            .addComponent(jSeparator4, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
            .addComponent(forwardJButton, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
            .addComponent(backwardJButton, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
            .addComponent(removeJButton, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
            .addComponent(jSeparator5, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
            .addComponent(zoomInJButton, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
            .addComponent(zoomOutJButton, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
            .addComponent(jSeparator7, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
            .addComponent(addQubitJButton, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
            .addComponent(removeQubitJButton, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
        );
        leftJPanelLayout.setVerticalGroup(
            leftJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftJPanelLayout.createSequentialGroup()
                .addComponent(resetJButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stepJButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(forwardJButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(backwardJButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeJButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(zoomInJButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(zoomOutJButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addQubitJButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeQubitJButton)
                .addContainerGap(66, Short.MAX_VALUE))
        );

        jSplitPane1.setDividerLocation(250);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.9);

        jDesktopPane1.setDragMode(javax.swing.JDesktopPane.OUTLINE_DRAG_MODE);
        jSplitPane1.setLeftComponent(jDesktopPane1);

        outputJTextArea.setColumns(20);
        outputJTextArea.setFont(new java.awt.Font("Monospaced", 1, 13));
        outputJTextArea.setRows(4);
        jScrollPane1.setViewportView(outputJTextArea);

        jSplitPane1.setRightComponent(jScrollPane1);

        statusBarJPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        statusBarJLabel.setText("status bar");

        javax.swing.GroupLayout statusBarJPanelLayout = new javax.swing.GroupLayout(statusBarJPanel);
        statusBarJPanel.setLayout(statusBarJPanelLayout);
        statusBarJPanelLayout.setHorizontalGroup(
            statusBarJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusBarJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 694, Short.MAX_VALUE)
        );
        statusBarJPanelLayout.setVerticalGroup(
            statusBarJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusBarJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
        );

        fileJMenu.setText("File");

        newQuantumCircuitJMenuItem.setText("New quantum circuit...");
        newQuantumCircuitJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newQuantumCircuitJMenuItemActionPerformed(evt);
            }
        });
        fileJMenu.add(newQuantumCircuitJMenuItem);

        loadJMenuItem.setText("Load quantum circuit...");
        loadJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadJMenuItemActionPerformed(evt);
            }
        });
        fileJMenu.add(loadJMenuItem);

        saveJMenuItem.setText("Save quantum circuit...");
        saveJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveJMenuItemActionPerformed(evt);
            }
        });
        fileJMenu.add(saveJMenuItem);
        fileJMenu.add(jSeparator3);

        quitJMenuItem.setText("Quit");
        quitJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitJMenuItemActionPerformed(evt);
            }
        });
        fileJMenu.add(quitJMenuItem);

        jMenuBar1.add(fileJMenu);

        quantumGatesJMenu.setText("Quantum Gates");

        jmiHadamard.setAction(hadamardAction);
        jmiHadamard.setText("Hadamard");
        quantumGatesJMenu.add(jmiHadamard);

        jtmCNot.setAction(cNotAction);
        jtmCNot.setText("CNot");
        quantumGatesJMenu.add(jtmCNot);

        jtmNot.setAction(notAction);
        jtmNot.setText("Not");
        quantumGatesJMenu.add(jtmNot);

        jtmPhaseShift.setAction(phaseShiftAction);
        jtmPhaseShift.setText("Phase Shift");
        quantumGatesJMenu.add(jtmPhaseShift);

        jmiSwap.setAction(swapAction);
        jmiSwap.setText("Swap");
        quantumGatesJMenu.add(jmiSwap);

        jtmToffoli.setAction(toffoliAction);
        jtmToffoli.setText("Toffoli");
        quantumGatesJMenu.add(jtmToffoli);

        jmiFredkin.setAction(fredkinAction);
        quantumGatesJMenu.add(jmiFredkin);
        quantumGatesJMenu.add(jSeparator2);

        jMenuItem16.setAction(customAction);
        jMenuItem16.setText("Custom quantum gate");
        quantumGatesJMenu.add(jMenuItem16);

        jMenuBar1.add(quantumGatesJMenu);

        algorithmsJMenu.setText("Algorithms");

        jMenuItem10.setText("Quantum teleportation");
        algorithmsJMenu.add(jMenuItem10);

        SuperdenseJMenuItem.setText("Superdense coding");
        SuperdenseJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SuperdenseJMenuItemActionPerformed(evt);
            }
        });
        algorithmsJMenu.add(SuperdenseJMenuItem);

        jMenuItem12.setText("Grover's fast search");
        algorithmsJMenu.add(jMenuItem12);

        entanglementAlgorithmJMenuItem.setText("Entangled states generation");
        entanglementAlgorithmJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                entanglementAlgorithmJMenuItemActionPerformed(evt);
            }
        });
        algorithmsJMenu.add(entanglementAlgorithmJMenuItem);

        jMenuBar1.add(algorithmsJMenu);

        settingsJMenu.setText("Settings");

        chkHideImaginary.setSelected(true);
        chkHideImaginary.setText("Hide imaginary components");
        settingsJMenu.add(chkHideImaginary);

        jMenuBar1.add(settingsJMenu);

        windowsJMenu.setText("Windows");
        windowsJMenu.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                windowsJMenuMenuSelected(evt);
            }
        });

        closeAllWindowsJMenuItem.setText("Close all windows");
        closeAllWindowsJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeAllWindowsJMenuItemActionPerformed(evt);
            }
        });
        windowsJMenu.add(closeAllWindowsJMenuItem);

        minimizeAllWindowsJMenuItem.setText("Minimize all windows");
        minimizeAllWindowsJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minimizeAllWindowsJMenuItemActionPerformed(evt);
            }
        });
        windowsJMenu.add(minimizeAllWindowsJMenuItem);
        windowsJMenu.add(jSeparator6);

        jMenuBar1.add(windowsJMenu);

        helpJMenu.setText("Help");

        helpContentsJMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        helpContentsJMenuItem.setText("Contents");
        helpContentsJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpContentsJMenuItemActionPerformed(evt);
            }
        });
        helpJMenu.add(helpContentsJMenuItem);
        helpJMenu.add(jSeparator1);

        aboutJMenuItem.setText("About");
        aboutJMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutJMenuItemActionPerformed(evt);
            }
        });
        helpJMenu.add(aboutJMenuItem);

        jMenuBar1.add(helpJMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(leftJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE))
            .addComponent(statusBarJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 698, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                    .addComponent(leftJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusBarJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void helpContentsJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpContentsJMenuItemActionPerformed
        JOptionPane.showMessageDialog(this, "help contents");
}//GEN-LAST:event_helpContentsJMenuItemActionPerformed

    private void aboutJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutJMenuItemActionPerformed
        AboutJDialog ajd = new AboutJDialog(this, true);
        ajd.setVisible(true);
}//GEN-LAST:event_aboutJMenuItemActionPerformed

    private void quitJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitJMenuItemActionPerformed
        /* TODO: Ask for confirmation */
        System.exit(0);
    }//GEN-LAST:event_quitJMenuItemActionPerformed

    private QCircuitJInternalFrame addNewQCircuitJInternalFrame() {
        return addNewQCircuitJInternalFrame("");
    }
   
    private QCircuitJInternalFrame addNewQCircuitJInternalFrame(String title) {
        QCircuitJInternalFrame qif = new QCircuitJInternalFrame(title);
        qif.setVisible(true);
        jDesktopPane1.add(qif);
        qif.moveToFront();
        try {
            qif.setSelected(true);
        } catch (PropertyVetoException ex) {
            throw new RuntimeException(ex);
        }
        updateWindowsJMenu();
        
        return qif;
    }

    public void updateWindowsJMenu() {
        // remove constant items
        for (Component i: windowsJMenu.getMenuComponents()) {
            if (i == closeAllWindowsJMenuItem) {
                continue;
            }
            if (i == minimizeAllWindowsJMenuItem) {
                continue;
            }
            if (i == jSeparator6) {
                continue;
            }
            windowsJMenu.remove(i);
        }
        // add all QCircuitJInternalFrames
        for (JInternalFrame iframe: jDesktopPane1.getAllFrames()) {
            if (!(iframe instanceof QCircuitJInternalFrame)) {
                continue;
            }
            if (!iframe.isVisible()) {
                continue;
            }
            final QCircuitJInternalFrame qif = (QCircuitJInternalFrame) iframe;
            JMenuItem item = new JMenuItem(qif.getTitle());
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        qif.setIcon(false);
                        qif.setSelected(true);
                    } catch (PropertyVetoException ex) {
                        throw new RuntimeException(ex);
                    }
                    qif.moveToFront();
                }
            });
            windowsJMenu.add(item);
        }
    }

    private void newQuantumCircuitJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newQuantumCircuitJMenuItemActionPerformed
        QCircuitJInternalFrame qif = addNewQCircuitJInternalFrame();
    }//GEN-LAST:event_newQuantumCircuitJMenuItemActionPerformed

    private void entanglementAlgorithmJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_entanglementAlgorithmJMenuItemActionPerformed
        QCircuitJInternalFrame qif = addNewQCircuitJInternalFrame("Entangled states generation circuit");
        CompoundQGate s1 = new CompoundQGate(new ElementaryQGate[]{new Identity(),
                    new Hadamard(), new Identity()});
        CompoundQGate s2 = new CompoundQGate(
                new ElementaryQGate[]{new Identity(), new CNot()});
        CompoundQGate s3 = new CompoundQGate(new ElementaryQGate[]{new CNot(0, 1),
                    new Identity()});

        QCircuit qcirc = new QCircuit(new CompoundQGate[]{s1, s2, s3});
        qif.setQCircuit(qcirc);
        writeMsg("Entangled states generation quantum circuit loaded");
    }//GEN-LAST:event_entanglementAlgorithmJMenuItemActionPerformed

    private QCircuitJInternalFrame getSelectedQCircuitJInternalFrame() {
        JInternalFrame iframe = jDesktopPane1.getSelectedFrame();
        if (iframe == null || !(iframe instanceof QCircuitJInternalFrame)) {
            return null;
        }
        return (QCircuitJInternalFrame) iframe;
    }

    private void resetJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetJButtonActionPerformed
        QCircuitJInternalFrame f = getSelectedQCircuitJInternalFrame();
        if(f != null) {
            f.getExecutionMonitor().reset();
        }
    }//GEN-LAST:event_resetJButtonActionPerformed

    private void stepJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stepJButtonActionPerformed
//        QCircuitJInternalFrame f = getSelectedQCircuitJInternalFrame();
//        if(f == null) {
//            return;
//        }
//
//        f.showExecutionInfoJDialog(this);
//
//        ExecutionMonitor m = f.getExecutionMonitor();
//        if(!m.isStepExecuting()) {
//            QRegister input = getInputRegister();
//            if(input == null) {
//                return;
//            }
//
//            m.reset();
//            try {
//                m.startStepExecution(input);
//            } catch(ExecutionMonitorException e) {
//                writeMsg(e.getMessage());
//            }
//        } else {
//            m.nextStep();
//        }

        QCircuitJInternalFrame f = getSelectedQCircuitJInternalFrame();
        if(f == null) {
            return;
        }

        f.showExecutionInfoJDialog(this);

        ExecutionMonitor m = f.getExecutionMonitor();
        try {
            m.cyclicNextStep();
        } catch(ExecutionMonitorException e) {
            writeMsg(e.getMessage());
        }
    }//GEN-LAST:event_stepJButtonActionPerformed



    private void doQGateAddDialog(QGate gate, ImageIcon icon) {
        QGateInfoJDialog f = new QGateInfoJDialog(this, true);
        QCircuitJInternalFrame frame = getSelectedQCircuitJInternalFrame();
        if(frame == null) {
            return;
        }
        
        QCircuit qc = frame.getQCircuitJPanel().getQCircuit();

        int maxRow = -1;
        if(qc.getStages().size() > 0) {
            maxRow = qc.getStages().get(0).getSize();
        }

        f.setShowImaginary(isShowImaginary());
        f.setMaxRow(maxRow);
        f.setGate(gate);
        f.setIcon(icon);
        f.setVisible(true);       

        writeMsg("Koniec");
        System.out.println("Koniec dialogu");
        
        if(f.getDialogResult() == QGateInfoJDialog.DIALOG_ACCEPTED) {
            QGate newGate = f.getGate();
            if(!gate.equals(newGate)) {
                System.out.println("Gate changed.");
                gate = newGate;
            }

            Stage stage = makeStage(gate, f.getRow(), maxRow);
            if(stage == null) {
                return;
            }

            QCircuitJPanel qcj = frame.getQCircuitJPanel();
            java.util.List<Stage> stages = qcj.getQCircuit().getStages();
            int selected = stages.indexOf(qcj.getSelectedStage());
            if(selected > -1) {
                stages.add(selected, stage);
            } else {
                stages.add(stage);
            }

            qcj.repaint();
            qcj.revalidate();
        }
    }

    private Stage makeStage(QGate gate, int qubit, int maxRow) {
        if(qubit < 0) {
            return null;
        }
        
        int size = maxRow - (gate.getSize() - 1);
        if(size < 1) {
            return null;
        }

        QGate[] gates = new QGate[size];
        // ***
        qubit = size - qubit -1;
        // ***
        gates[qubit] = gate;
        for(int i = 0; i < gates.length; i++) {
            if(i != qubit) {
                gates[i] = new Identity();
            }
        }

        return new CompoundQGate(gates);
    }

    public boolean isShowImaginary() {
        return !chkHideImaginary.isSelected();
    }

    private void hadamardJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hadamardJButtonActionPerformed
        //doQGateAddDialog(new Hadamard());
    }//GEN-LAST:event_hadamardJButtonActionPerformed

    private void removeJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeJButtonActionPerformed
        QCircuitJInternalFrame qif = getSelectedQCircuitJInternalFrame();
        if (qif == null) {
            return;
        }
        Stage selected = qif.getQCircuitJPanel().getSelectedStage();
        if (selected == null) {
            return;
        }
        qif.getQCircuit().getStages().remove(selected);
        repaint();
}//GEN-LAST:event_removeJButtonActionPerformed

    private void zoomOutJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomOutJButtonActionPerformed
        if (getSelectedQCircuitJInternalFrame() == null) {
            return;
        }
        
        QCircuitJPanel qcjp = getSelectedQCircuitJInternalFrame().getQCircuitJPanel();
        qcjp.setZoom(qcjp.getZoom() / 1.5f);
        
//        qcjp.setPreferredSize(
//                new Dimension(
//                (int) (qcjp.getPreferredSize().getWidth() / 1.5),
//                (int) (qcjp.getPreferredSize().getHeight() / 1.5)));
//        qcjp.revalidate();
//        qcjp.repaint();
}//GEN-LAST:event_zoomOutJButtonActionPerformed

    private void zoomInJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomInJButtonActionPerformed
        if (getSelectedQCircuitJInternalFrame() == null) {
            return;
        }

        QCircuitJPanel qcjp = getSelectedQCircuitJInternalFrame().getQCircuitJPanel();
        qcjp.setZoom(qcjp.getZoom() * 1.5f);

//        qcjp.setPreferredSize(
//                new Dimension(
//                (int) (qcjp.getPreferredSize().getWidth() * 1.5),
//                (int) (qcjp.getPreferredSize().getHeight() * 1.5)));
//        qcjp.revalidate();
//        qcjp.repaint();
    }//GEN-LAST:event_zoomInJButtonActionPerformed

    private void saveJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveJMenuItemActionPerformed
        if (getSelectedQCircuitJInternalFrame() == null) {
            JOptionPane.showMessageDialog(this, "Create quantum circuit first");
            return;
        }
        QCircuitJPanel qcjp = getSelectedQCircuitJInternalFrame().getQCircuitJPanel();
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setAcceptAllFileFilterUsed(true);
        jfc.addChoosableFileFilter(new JqmlFileFilter());
        if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            XStream xstream = new XStream(new DomDriver());
            try {
                xstream.toXML(qcjp.getQCircuit(),
                        new FileOutputStream(jfc.getSelectedFile()));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Create quantum circuit first");
                return;
            }
            writeMsg("Quantum circuit saved: " + jfc.getSelectedFile().getName());
        }
    }//GEN-LAST:event_saveJMenuItemActionPerformed

    private void loadJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadJMenuItemActionPerformed
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setAcceptAllFileFilterUsed(true);
        jfc.addChoosableFileFilter(new JqmlFileFilter());
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                XStream xstream = new XStream(new DomDriver());
                QCircuitJInternalFrame qif = addNewQCircuitJInternalFrame(
                        jfc.getSelectedFile().getName());
                QCircuitJPanel qcjp = qif.getQCircuitJPanel();
                qcjp.setQCircuit(
                        (QCircuit) xstream.fromXML(new FileInputStream(jfc.getSelectedFile())));
                qcjp.repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed loading quantum circuit");
                throw new RuntimeException(ex);
            }
            writeMsg("Quantum circuit loaded: " + jfc.getSelectedFile().getName());
        }
    }//GEN-LAST:event_loadJMenuItemActionPerformed

    private void SuperdenseJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SuperdenseJMenuItemActionPerformed
        QCircuitJInternalFrame qif = addNewQCircuitJInternalFrame("Superdense coding quantum circuit");
        QCircuit qc = new QCircuit();
        CompoundQGate s1 = new CompoundQGate(new Swap(), new Identity(), new Identity());
        CompoundQGate s2 = new CompoundQGate(new Identity(), new Custom(new Complex[][]{
                    {QGate.cx(1), QGate.cx(0), QGate.cx(0), QGate.cx(0)},
                    {QGate.cx(0), QGate.cx(1), QGate.cx(0), QGate.cx(0)},
                    {QGate.cx(0), QGate.cx(0), QGate.cx(1), QGate.cx(0)},
                    {QGate.cx(0), QGate.cx(0), QGate.cx(0), QGate.cx(-1)},}), new Identity());
        CompoundQGate s3 = new CompoundQGate(new Swap(), new Identity(), new Identity());
        CompoundQGate s4 = new CompoundQGate(new Identity(), new CNot(1, 0), new Identity());
        CompoundQGate s5 = new CompoundQGate(new Identity(), new Identity(), new CNot(1, 0));
        CompoundQGate s6 = new CompoundQGate(new Identity(), new Identity(), new Hadamard(), new Identity());

        qc.addStage(s1);
        qc.addStage(s2);
        qc.addStage(s3);
        qc.addStage(s4);
        qc.addStage(s5);
        qc.addStage(s6);
        qif.setQCircuit(qc);
        writeMsg("Superdense coding quantum circuit loaded");

        QRegister input;

        double sqr2 = Math.sqrt(2) / 2;
        QRegister EPR = new QRegister(MoreMath.asComplexMatrix(new double[][]{{sqr2, 0, 0, sqr2}}));

        writeMsg("Testing superdense coding...");
        writeMsg("----------------------------");
        writeMsg("In each case, two least significact qubits below should match input bits:");
        for (int i = 0; i < 4; i++) {
            QRegister cbits = QRegister.ket(i, 2);
            input = cbits.tensor(EPR);
            setInputRegister(input);
            writeMsg("input: " + cbits.dirac() + "    output: " + qc.compute(input).dirac());
        }

    }//GEN-LAST:event_SuperdenseJMenuItemActionPerformed

    
    private void minimizeAllWindowsJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minimizeAllWindowsJMenuItemActionPerformed
        for (JInternalFrame f : jDesktopPane1.getAllFrames()) {
            try {
                f.setIcon(true);
            } catch (PropertyVetoException ex) {
                throw new RuntimeException(ex);
            }
        }
    }//GEN-LAST:event_minimizeAllWindowsJMenuItemActionPerformed

    private void closeAllWindowsJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeAllWindowsJMenuItemActionPerformed
        for (JInternalFrame f : jDesktopPane1.getAllFrames()) {
            f.setVisible(true);
            f.dispose();
        }
        System.gc();
    }//GEN-LAST:event_closeAllWindowsJMenuItemActionPerformed

    private void windowsJMenuMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_windowsJMenuMenuSelected
        updateWindowsJMenu();
    }//GEN-LAST:event_windowsJMenuMenuSelected

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            // it would fail in an applet
            System.exit(0);
        }
        catch (Exception ex) {
            /* DO NOTHING */
        }
    }//GEN-LAST:event_formWindowClosing

    private void forwardJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forwardJButtonActionPerformed
        QCircuitJInternalFrame qif = getSelectedQCircuitJInternalFrame();
        if (qif == null) {
            return;
        }
        Stage selected = qif.getQCircuitJPanel().getSelectedStage();
        if (selected == null) {
            return;
        }
        QCircuit qcirc = qif.getQCircuit();
        for (int i = 0; i < qcirc.getStages().size() - 1; i++) {
            Stage s = qcirc.getStages().get(i);
            if (s == selected) {
                qcirc.getStages().remove(selected);
                qcirc.getStages().add(i + 1, selected);
                break;
            }
        }
        repaint();
    }//GEN-LAST:event_forwardJButtonActionPerformed

    private void backwardJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backwardJButtonActionPerformed
        QCircuitJInternalFrame qif = getSelectedQCircuitJInternalFrame();
        if (qif == null) {
            return;
        }
        Stage selected = qif.getQCircuitJPanel().getSelectedStage();
        if (selected == null) {
            return;
        }
        QCircuit qcirc = qif.getQCircuit();
        for (int i = 1; i < qcirc.getStages().size(); i++) {
            Stage s = qcirc.getStages().get(i);
            if (s == selected) {
                qcirc.getStages().remove(selected);
                qcirc.getStages().add(i - 1, selected);
                break;
            }
        }
        repaint();
    }//GEN-LAST:event_backwardJButtonActionPerformed

    private void notJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notJButtonActionPerformed
//        doQGateAddDialog(new Not());
    }//GEN-LAST:event_notJButtonActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        //doQGateAddDialog(new CNot());
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        //doQGateAddDialog(new PhaseShift(Math.PI / 2));
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        //doQGateAddDialog(new Toffoli());
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        //doQGateAddDialog(new Custom(new Identity().getMatrix()));
    }//GEN-LAST:event_jButton8ActionPerformed

    private void addQubitJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addQubitJButtonActionPerformed
        QCircuitJInternalFrame frame = getSelectedQCircuitJInternalFrame();
        if(frame == null) {
            return;
        }
        
        QCircuit qc = frame.getQCircuit();

        if(qc.getStages().size() == 0) {
            qc.addStage(new Identity());
        } else {
            addRow(qc);
        }
        
        frame.setQCircuit(qc);
}//GEN-LAST:event_addQubitJButtonActionPerformed

    private void removeQubitJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeQubitJButtonActionPerformed
        QCircuitJInternalFrame frame = getSelectedQCircuitJInternalFrame();
        if(frame == null) {
            return;
        }

        QCircuit qc = frame.getQCircuit();
        if(qc.getStages().size() == 0) {
            return;
        }

        removeRow(qc);
        frame.setQCircuit(qc);
}//GEN-LAST:event_removeQubitJButtonActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        QCircuitJInternalFrame f = getSelectedQCircuitJInternalFrame();
        if(f != null) {
            try {
                f.getExecutionMonitor().compute();
            } catch (ExecutionMonitorException e) {
                writeMsg(e.getMessage());
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void addRow(QCircuit qc) {
        for(int i = 0; i < qc.getStages().size(); i++) {
            QGate gate = (QGate)qc.getStages().get(i);
            CompoundQGate newGate = new CompoundQGate(gate, new Identity());
            qc.getStages().set(i, newGate);
        }
    }

    private boolean removeRow(QCircuit qc) {
        if(checkIfCanRemoveLastRow(qc)) {
            for(int i = 0; i < qc.getStages().size(); i++) {
                QGate gate = (QGate)qc.getStages().get(i);
                CompoundQGate compound = (CompoundQGate)gate;
                List<ElementaryQGate> gates = compound.getGates();     
                List<QGate> gates2 = new ArrayList<QGate>(gates.size());
                for(ElementaryQGate g : gates) {
                    gates2.add(g);
                }

                gates2.remove(gates.size() - 1);
                qc.getStages().set(i, new CompoundQGate(gates2));
            }
            
            return true;
        }

        return false;
    }

    private boolean checkIfCanRemoveLastRow(QCircuit qc) {
        if(qc.getStages().size() == 0 || qc.getStages().get(0).getSize() < 2) {
            return false;
        }

        for(int i = 0; i < qc.getStages().size(); i++) {
            QGate gate = (QGate)qc.getStages().get(i);

            if(gate.getMatrix() == null ||
                    gate.getMatrix().getNumberOfRows() < 2) {
                writeMsg("Too few qubits to remove.");
                return false;
            }
            
            CompoundQGate compound = (CompoundQGate)gate;
            java.util.List<ElementaryQGate> gates = compound.getGates();
            if(!(gates.get(gates.size() - 1) instanceof Identity)) {
                writeMsg("Please remove stages which act on this qubit first.");
                return false;
            }
        }

        return true;
    }

    public void writeMsg(String msg) {
        outputJTextArea.append(msg + "\n");
        statusBarJLabel.setText(msg);
    }

    public void writeMsg(String msg, String qCiruitName) {
        outputJTextArea.append("[" + qCiruitName + "] " + msg + "\n");
        statusBarJLabel.setText(msg);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                getInstance().setVisible(true);
            }
        });
    }

    public void setInputRegister(QRegister reg) {
        this.inputRegister = reg;
    }

    public QRegister getInputRegister() {
        return this.inputRegister;
    }

    private QRegister inputRegister = null;


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem SuperdenseJMenuItem;
    private javax.swing.JMenuItem aboutJMenuItem;
    private javax.swing.JButton addQubitJButton;
    private javax.swing.JMenu algorithmsJMenu;
    private javax.swing.JButton backwardJButton;
    private javax.swing.JCheckBoxMenuItem chkHideImaginary;
    private javax.swing.JMenuItem closeAllWindowsJMenuItem;
    private javax.swing.JMenuItem entanglementAlgorithmJMenuItem;
    private javax.swing.JMenu fileJMenu;
    private javax.swing.JButton forwardJButton;
    private javax.swing.JButton hadamardJButton;
    private javax.swing.JMenuItem helpContentsJMenuItem;
    private javax.swing.JMenu helpJMenu;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JMenuItem jmiFredkin;
    private javax.swing.JMenuItem jmiHadamard;
    private javax.swing.JMenuItem jmiSwap;
    private javax.swing.JMenuItem jtmCNot;
    private javax.swing.JMenuItem jtmNot;
    private javax.swing.JMenuItem jtmPhaseShift;
    private javax.swing.JMenuItem jtmToffoli;
    private javax.swing.JPanel leftJPanel;
    private javax.swing.JMenuItem loadJMenuItem;
    private javax.swing.JMenuItem minimizeAllWindowsJMenuItem;
    private javax.swing.JMenuItem newQuantumCircuitJMenuItem;
    private javax.swing.JButton notJButton;
    private javax.swing.JTextArea outputJTextArea;
    private javax.swing.JMenu quantumGatesJMenu;
    private javax.swing.JMenuItem quitJMenuItem;
    private javax.swing.JButton removeJButton;
    private javax.swing.JButton removeQubitJButton;
    private javax.swing.JButton resetJButton;
    private javax.swing.JMenuItem saveJMenuItem;
    private javax.swing.JMenu settingsJMenu;
    private javax.swing.JLabel statusBarJLabel;
    private javax.swing.JPanel statusBarJPanel;
    private javax.swing.JButton stepJButton;
    private javax.swing.JMenu windowsJMenu;
    private javax.swing.JButton zoomInJButton;
    private javax.swing.JButton zoomOutJButton;
    // End of variables declaration//GEN-END:variables
}
