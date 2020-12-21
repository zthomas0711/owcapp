/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package owcapp;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author Zachary
 */
public class OfferingTable implements TableModel {

    @Override
    public int getRowCount() {
        return table.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return int.class ;
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return table[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        table[rowIndex][columnIndex] = aValue;
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    Object[][] table;
    String[] columnNames = {"Deposit ID", "Date", "Name", "Payment Type", "Payment Number", "Amount", "Department", "Category", "Comments"};
    
}
