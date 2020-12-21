/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package owcapp;

import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author Zachary
 */
public class Deposit implements TableItem {
    Deposit(String _id, double _amount, String _link, String _date)
    {
        id = _id;
        amount = _amount;
        link = _link;
        date = _date;
    }

    Deposit() 
    {
        
    }
    
    public void SetOfferings(ArrayList<Offering> _offerings)
    {
        offerings = _offerings;
    }
    
    public void SetTransactions(ArrayList<Transaction> _transactions)
    {
        transactions = _transactions;
    }
    public void AddOffering(Offering offering)
    {
        offerings.add(offering);
    }
    
    public String GetSummary()
    {
        String summary = new String();
        summary="";
        ApplicationOptions opts = ApplicationOptions.getInstance();
        Vector<Auxiliary> departments = opts.GetAuxiliaries();
        ArrayList<String> deptNames = new ArrayList<String>();
        ArrayList amounts = new ArrayList<Double>();
        for(int i = 0; i< departments.size();i++)
        {
            amounts.add(0.0);
            deptNames.add(departments.get(i).name);
        }
        for(Offering o: offerings)
        {
            int i = deptNames.indexOf(o.department);
            amounts.set(i, (Double)amounts.get(i) + o.amount);           
        }
        
        for(int i = 0; i< departments.size();i++)
        {
            if((Double)amounts.get(i)>0.0)
            {
                String s = String.format("%s: %f\r\n",departments.get(i).name, (Double)amounts.get(i));
                summary += s;
            }
        }
        return summary;
    }
     @Override
    public void SetItem(int col,Object value) {
          switch(col)
         {             
            case 0:
                 this.id= (String) value;
                 break;                          
            case 1:
                this.date = (String) value;
                break;            
            case 2:
                this.amount = (double)value;
                break;            
            case 3:
                this.link = (String)value;
                break;
         }
    }

    @Override
    public Object GetItem(int col) {
            Object rowObject = new Object();
         switch(col)
         {             
            case 0:
                 rowObject = this.id;
                 break;                          
            case 1:
                rowObject = this.date;
                break;            
            case 2:
                rowObject = this.amount;
                break;            
            case 3:
                rowObject = this.link;
                break;
         }
         return rowObject;
    }

    @Override
    public Object GetWholeItem() {
       return this;
    }
    private ArrayList<Transaction>transactions;
    private ArrayList<Offering>offerings;
    public String id;
    public double amount;
    public String link;
    public String date;
}
