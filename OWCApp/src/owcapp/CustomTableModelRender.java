/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package owcapp;

import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Zachary
 */
import java.awt.Color;
import java.awt.Component;
 
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
 
/**
 * @author ashraf_sarhan
 * 
 */
public class CustomTableModelRender implements TableCellRenderer {
 
    public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
 
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = DEFAULT_RENDERER.getTableCellRendererComponent(table,
                value, isSelected, hasFocus, row, column);
        
        CustomModel myTableModel = (CustomModel)table.getModel();
        if(myTableModel.rowsEdited.contains(row))
        {
            c.setBackground(Color.YELLOW);
        }
        
        else if(myTableModel.rowsAdded.contains(row))
        {
            c.setBackground(Color.CYAN);
        }
        
        else
        {
            c.setBackground(Color.WHITE);
        }
  
        return c;
    }
 
}
