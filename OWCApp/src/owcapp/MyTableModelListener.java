/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package owcapp;

import javax.swing.event.TableModelEvent;
import static javax.swing.event.TableModelEvent.UPDATE;
import javax.swing.event.TableModelListener;

/**
 *
 * @author Zachary
 */
public class MyTableModelListener implements TableModelListener{
    @Override
    public void tableChanged(TableModelEvent e)
    {
        CustomModel tableModel = (CustomModel) e.getSource();
        
    }
}
