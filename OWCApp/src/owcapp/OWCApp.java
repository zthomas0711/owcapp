/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package owcapp;

import java.awt.Color;
import java.util.List;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.table.DefaultTableModel;

/**
 * 
 *
 * @author Zachary
 */

public class OWCApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        String bal = "200.0";
        double x = Double.valueOf(bal);
        Boolean isAccountsOk = false;
        Boolean isCategoriesOk = false;
        ApplicationOptions options = ApplicationOptions.getInstance();
        Status status=options.ReadAccounts();
        if(status.IsSuccess())
        {
            isAccountsOk = true;
            status = options.ReadCategories();
        }
        
        if(status.IsSuccess())
        {
            isCategoriesOk = true;
            Broker appBroker = new Broker();
            JFrame j = new LoginDialog();
            j.setVisible(true);
        }
        
        else
        {
            String msg = new String();
            if(!isAccountsOk)
            {
                msg += "Error reading accounts file";
            }
            
            if(!isCategoriesOk)
            {
                msg+="\r\nError reading categories file.";
            }
            JOptionPane.showMessageDialog(null, msg, "Configuration File Error", 0);
             
        }        
        
    }
    
    public static boolean IsAmountNearestHundreth(String text)
    {
        try
        {
            String msg = new String();
            if(text.isEmpty())
            {
                return false;
            }
            double amountNumber = Double.valueOf(text);
            boolean isNearestHundredth = (BigDecimal.valueOf(amountNumber).scale() <= 2);
            return isNearestHundredth;
            
        }       
        
        catch (NumberFormatException nfe) 
        {  
            JOptionPane.showMessageDialog(null, "Amount isn't numeric", "Invalid Entry", ERROR_MESSAGE); 
            return false;
        }
    }
    
    public static boolean ValidateDate(String d)
    {
        boolean isValid = true;
        // Validate that format is yyyy-mm-dd
        
        // Validate Year\
        int index = d.indexOf("-");
        if(index !=4)
        {
            isValid = false;
        }
        
        if(isValid)
        {
            String year = d.substring(0, index);
            if(Double.valueOf(year)< 1000)
            {
                isValid = false;
            }
        }
        
        // Validate Month
        if(isValid)
        {
            index = d.indexOf("-", index +1);
            if(index < 6 || index > 7)
            {
                isValid = false;
            }
        }
        
        if(isValid)
        {
            String month = d.substring(5, index);
            if(Double.valueOf(month) < 1 ||Double.valueOf(month) >12 )
            {
                isValid = false;
            }
        }
        
        // Validate Day
        if(isValid)
        {
            String day = d.substring(index+1);
            if(Double.valueOf(day) < 1 || Double.valueOf(day) > 31)
            {
                isValid = false;
            }
        }
        
        return isValid;
    }
    
    public static String GetSummaryOfDeposit(String depositId)
    {
        String summary = new String();
        Broker appBroker = Broker.getInstance();
        ArrayList<Offering>offerings = new ArrayList();
        offerings = appBroker.GetOfferings(depositId);
        Map<String,Double>deptOfferings = new HashMap<String, Double>();
        for(Offering o: offerings)
        {
            String key = o.department;
            double amount = o.amount;
            if(deptOfferings.containsKey(key))
            {
                amount +=  deptOfferings.get(key);
                              
            }
            deptOfferings.put(key, amount);  
        }
        
        for(Map.Entry<String, Double>entry : deptOfferings.entrySet())
        {
            summary = String.format("%s\r\n%s : $%.2f", summary, entry.getKey(), entry.getValue());
        }
        
        return summary;
    }
    
    public static String GetFileName(JFrame frame)
    {
       JFileChooser openFileChooser;
       openFileChooser = new JFileChooser();
       String filePath = "";
        int val = openFileChooser.showDialog(frame, "Select Receipt File");
        if(val == JFileChooser.APPROVE_OPTION)
        {
            filePath = openFileChooser.getSelectedFile().getAbsolutePath();
            File tmpDir = new File(filePath); 
            boolean doesExist = tmpDir. exists();
            if(!doesExist)
            {
               JOptionPane.showMessageDialog(null, "File does not exist.", "File Does Not Exist", ERROR_MESSAGE);
                
            }            
        }
        
        else
        {
             JOptionPane.showMessageDialog(null, "No file was chosen.", "No File Chosen", ERROR_MESSAGE);
        }
        
        return filePath;
    }
    
    static class MyTableModel extends DefaultTableModel {

    List<Color> rowColours = Arrays.asList(
        Color.RED,
        Color.GREEN,
        Color.CYAN     
    );

    public void setRowColour(int row, Color c) {
        rowColours.set(row, c);
        fireTableRowsUpdated(row, row);
    }

    public Color getRowColour(int row) {
        return rowColours.get(row);
    }
    
      @Override
    public int getRowCount() {
        return 3;
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int row, int column) {
        return String.format("%d %d", row, column);
    }
}
    
}
