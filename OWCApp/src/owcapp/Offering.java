/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package owcapp;

import java.math.BigDecimal;
import java.util.Set;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static owcapp.OWCApp.ValidateDate;

/**
 *
 * @author Zachary
 */
public class Offering implements TableItem {
    public Offering()
    {
        
    }
    public Offering(String name_, String paymentType_, String paymentNumber_, double amount_, String department_, String category_, String comments_, String dateOfOffering_, String depositId_, Boolean _isReconciled,int _monthReconciled, int _transactionId)
    {
        name = name_;
        paymentType = paymentType_;
        paymentNumber = paymentNumber_;
        amount = amount_;
        department = department_;
        category = category_;
        comments = comments_;
        dateOfOffering = dateOfOffering_;
        depositId = depositId_;
        transactionId = _transactionId;
        isReconciled = _isReconciled;
        monthReconciled = _monthReconciled;
    }
    
    Status Validate()
    {
        Status status = new Status();
        String msg= new String();
        boolean isValid = true;
        try
        {
            boolean isDateValid = ValidateDate(dateOfOffering);
            if(!isDateValid)
            {
                msg += "Date format is invalid.\r\n";
            }
            
            if(depositId.isEmpty())
            {
                msg += "Deposit ID is empty.\r\n";
            }
            
            boolean isNearestHundredth = (BigDecimal.valueOf(amount).scale() <= 2);
            if(amount <=0)
            {
                msg += "Amount is not greater than 0.\r\n";
            }

            if(!isNearestHundredth)
            {
                msg += "Amount cannot be more than 2 decimals.\r\n";
            }
        
        
        }
        
        catch (NumberFormatException nfe) 
        {  
            
            status.SetFail("Amount isn't numeric");
            return status; 
        }
        
        if(name.isEmpty())
        {
           msg += "Name field is empty\r\n.";
        }
        
        if(paymentType == "Check" && paymentNumber.isEmpty())
        {
            msg += "Payment number field is empty.\r\n";
        }   
  
 
        if(!msg.isEmpty())
        {
            status.SetFail(msg);
            
        }
        return status;
    }
    public String name = "";
    public String paymentType = "";
    public String paymentNumber = "";
    public double amount = 0.0;
    public String department = "";
    public String category = "";
    public String comments = "";
    public String dateOfOffering = "";
    public String depositId = "";
    public int transactionId = 0;
    public String receiptLink  = "";
    public boolean isReconciled = false;
    public int monthReconciled = 0;

    @Override
    public void SetItem(int col,Object value) {
          switch(col)
         {             
            case 0:
                 this.transactionId = (int) value;
                 break;                          
            case 1:
                this.depositId = (String) value;
                break;            
            case 2:
                this.dateOfOffering = (String)value;
                break;            
            case 3:
                this.name = (String)value;
                break;
            case 4:
                 this.paymentType = (String)value;
                 break;                 
            case 5:
                 this.paymentNumber = (String)value;
                 break;
            case 6:
                 this.amount = (double)value;
                 break;  
            case 7:
                this.department = (String)value;
                break;            
            case 8:
                this.category = (String)value;
                break;
            case 9:
                this.comments = (String)value;
                break;
         }
    }

    @Override
    public Object GetItem(int col) {
            Object rowObject = new Object();
         switch(col)
         {             
            case 0:
                 rowObject = this.transactionId;
                 break;                          
            case 1:
                rowObject = this.depositId;
                break;            
            case 2:
                rowObject = this.dateOfOffering;
                break;            
            case 3:
                rowObject = this.name;
                break;
            case 4:
                 rowObject = this.paymentType;
                 break;                 
            case 5:
                 rowObject = this.paymentNumber;
                 break;
            case 6:
                 rowObject = this.amount;
                 break;  
            case 7:
                rowObject = this.department;
                break;            
            case 8:
                rowObject = this.category;
                break;
            case 9:
                rowObject = this.comments;
                break;
         }
         return rowObject;
    }

    @Override
    public Object GetWholeItem() {
       return this;
    }
}
