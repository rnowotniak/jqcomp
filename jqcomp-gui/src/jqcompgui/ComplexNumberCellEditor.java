/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jqcompgui;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import org.jscience.mathematics.number.Complex;

/**
 *
 * @author Rafal
 */
public class ComplexNumberCellEditor extends AbstractCellEditor implements TableCellEditor

{
    private ComplexJPanel panel;
    private JTable table;

    public ComplexNumberCellEditor() {
        panel = new ComplexJPanel();
    }

    public Object getCellEditorValue() {
        Complex ret = panel.getValue();
        return ret;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;
        panel.setValue( (Complex)table.getModel().getValueAt(row, column) );
        return panel;
    }



}
