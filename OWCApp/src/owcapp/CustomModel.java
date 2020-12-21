 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package owcapp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Zachary
 */
public class CustomModel extends AbstractTableModel{

    final String[] columnNames;
         
    ArrayList<TableItem> data;
    ArrayList<Integer>rowsEdited;
    ArrayList<Integer>rowsAdded;
    ArrayList<TableItem>itemsDeleted;
    ArrayList<TableItem>itemsAdded;
    ArrayList<TableItem>itemsEdited;
    ArrayList<Boolean>readOnlyColumns;
    int originalSize = 0;
    boolean doColorChange = false;
    
    CustomModel( String[] colNames, ArrayList<Boolean>columnsReadOnly ) {
    this.data = new ArrayList();
    rowsEdited = new ArrayList();
    itemsDeleted = new ArrayList();
    itemsAdded = new ArrayList();
    itemsEdited = new ArrayList();
    rowsAdded = new ArrayList();
    columnNames = colNames;
    readOnlyColumns = columnsReadOnly;
    }
    
    public void SetColorChangeOnEdit(boolean _doChange)
    {
        doColorChange = _doChange;
    }
    
    public void ResetTable()
    {
        this.data.clear();
        rowsEdited.clear();
        itemsDeleted.clear();
        rowsAdded.clear();
        itemsAdded.clear();
        itemsEdited.clear();
    }
        
    public void SetOriginalSize(int _originalSize)
    {
        originalSize = _originalSize;
    }
    @Override
    public int getColumnCount() {
      return columnNames.length;
    }

    @Override
    public int getRowCount() {
      return data.size();
    }

    @Override
    public String getColumnName(int col) {
      return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        TableItem t =  data.get(row);
        Object rowObject = t.GetItem(col);
         /*switch(col)
         {             
            case 0:
                 rowObject = offering.transactionId;
                 break;                          
            case 1:
                rowObject = offering.depositId;
                break;            
            case 2:
                rowObject = offering.dateOfOffering;
                break;            
            case 3:
                rowObject = offering.name;
                break;
            case 4:
                 rowObject = offering.paymentType;
                 break;                 
            case 5:
                 rowObject = offering.paymentNumber;
                 break;
            case 6:
                 rowObject = offering.amount;
                 break;  
            case 7:
                rowObject = offering.department;
                break;            
            case 8:
                rowObject = offering.category;
                break;
            case 9:
                rowObject = offering.comments;
                break;
         }*/
        return rowObject;
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
         TableItem t = data.get(row);
         t.SetItem(col, value);

         data.set(row, t);
         fireTableCellUpdated(row, col);

     }

    /*
     * JTable uses this method to determine the default renderer/ editor for
     * each cell. If we didn't implement this method, then the last column
     * would contain text ("true"/"false"), rather than a check box.
     */
    @Override
    public Class getColumnClass(int c) {
      return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's editable.
     */
    @Override
    public boolean isCellEditable(int row, int col) 
    {
      //Note that the data/cell address is constant,
      //no matter where the cell appears onscreen.
        try
        {
            return !readOnlyColumns.get(col);
        }

        catch(ArrayIndexOutOfBoundsException e)
        {
            return false;
        }
      
    }
    

    public void addRow(TableItem item) 
    {

        data.add(item);
        if(doColorChange)
        {
            rowsAdded.add(data.size()-1);
            itemsAdded.add(item);
            
        
        }
        this.fireTableDataChanged();

    }

    public void delRow(int row) 
    {
        if(row <0)
        {
            return;
        }
        itemsDeleted.add(data.get(row));
        data.remove(row);
        ModifyIndexArrays(row);
        this.fireTableDataChanged();
    }
    
    private void ModifyIndexArrays(int rowRemoved)
    {

        int count = 0;
        for(int i:rowsEdited)
        {
            if(i>rowRemoved)
            {
                rowsEdited.set(count, i-1);                
            }
            count++;
        }
        
        count = 0;
        for(int i:rowsEdited)
        {
            if(i>rowRemoved)
            {
                rowsAdded.set(count, i-1);                
            }
            count++;
        }
    }
    

  }

