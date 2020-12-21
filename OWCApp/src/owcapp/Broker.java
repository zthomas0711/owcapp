/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package owcapp;

import com.mysql.cj.conf.ConnectionUrlParser.Pair;
import java.awt.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;

/**
 *
 * @author Zachary
 */
public class Broker {
    public Status ConnectToDatabase()
    {      
        try 
        {  
            Status status = new Status();
            if(!IsConnected())
            {
                String jdbcUrl = "jdbc:mysql://localhost:3306/owcapp";            
                connection = DriverManager.getConnection(jdbcUrl, "root", "oceansideworshipcenter");
                isConnected = true;
                status.SetSuccess("Successful connection to database made");
            }

            else
            {
                status.SetSuccess("Connection already established.");
            }
            return status;
        } 
        catch (SQLException ex) 
        {
            isConnected = false;
            Status stat = new Status();
            stat.SetFail("Unable to connect to database.");
            return stat;
        }
        
    }
    
     public void DisConnectFromDatabase()
    {      
        try 
        {       
            connection.close();
            isConnected = false;
        } 
        catch (SQLException ex) 
        {
            isConnected = false;
            Status stat = new Status();
            stat.SetFail("Failure to disconnect from database.");
        }
        
    }
    public Status AddOffering(Offering _offering)
    {
        Status status = new Status();
        String query = " insert into offerings (depositid, offeringdate, name, paymenttype, checknum, amount, category, department, comments)"
        + " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStmt;
        try 
        {
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString (1, _offering.depositId);
            preparedStmt.setString (2, _offering.dateOfOffering);
            preparedStmt.setString   (3, _offering.name);
            preparedStmt.setString   (4, _offering.paymentType);
            preparedStmt.setString   (5, _offering.paymentNumber);
            preparedStmt.setDouble(6, _offering.amount);
            preparedStmt.setString   (7, _offering.category);
            preparedStmt.setString   (8, _offering.department);
            preparedStmt.setString   (9, _offering.comments);
            preparedStmt.execute();
        } 
     
        catch (Exception e)
        {
           // Logger.getLogger(Broker.class.getName()).log(Level.SEVERE, null, ex);
            status.SetFail(e.getMessage());
            return status;
        }
      
        return status;
    }
    
    public ArrayList<Offering>GetOfferings(String depositId)
    {
        Status status = new Status();
        ArrayList<Offering>offerings = new ArrayList();
        try 
        {
            Statement s = connection.createStatement();
            s.executeQuery ("SELECT offeringid, depositid, offeringdate, name, paymenttype, checknum, amount, category, department, comments, documentlink FROM offerings WHERE depositid = '" + depositId + "'");
            ResultSet rs = s.getResultSet ();
            int count = 0;
            while (rs.next ())
            {
                
                int idVal = rs.getInt ("offeringid");
                String depId = rs.getString ("depositid");
                String offeringDate = rs.getDate("offeringdate").toString();
                String name = rs.getString("name");
                String payType = rs.getString("paymenttype");
                String checkNum = rs.getString("checknum");
                Double amount = rs.getDouble("amount");
                String category = rs.getString("category");
                String department = rs.getString("department");
                String comments = rs.getString("comments");
                
                Offering o = new Offering(name,payType, checkNum, amount, department, category, comments,offeringDate, depId, false, 0, idVal);
                o.receiptLink = rs.getString("documentlink");
                offerings.add(o);
                ++count;
            }
            rs.close ();
            s.close ();
        } 
     
        catch (Exception e)
        {
           // Logger.getLogger(Broker.class.getName()).log(Level.SEVERE, null, ex);
            status.SetFail(e.getMessage());
            JOptionPane.showMessageDialog(null, status.msg, "Database Query Error", ERROR_MESSAGE);
            offerings = null;
            return offerings;
        }
      
        return offerings;
    }
    
    public ArrayList<Offering> GetAllOfferings()
    {
        Status status = new Status();
        ArrayList<Offering>offerings = new ArrayList();
        try 
        {
            Statement s = connection.createStatement();
            s.executeQuery ("SELECT offeringid, depositid, offeringdate, name, paymenttype, checknum, amount, category, department, comments FROM offerings");
            ResultSet rs = s.getResultSet ();
            int count = 0;
            while (rs.next ())
            {
                
                int idVal = rs.getInt ("offeringid");
                String depId = rs.getString ("depositid");
                String offeringDate = rs.getDate("offeringdate").toString();
                String name = rs.getString("name");
                String payType = rs.getString("paymenttype");
                String checkNum = rs.getString("checknum");
                Double amount = rs.getDouble("amount");
                String category = rs.getString("category");
                String department = rs.getString("department");
                String comments = rs.getString("comments");
                Offering o = new Offering(name,payType, checkNum, amount, department, category, comments,offeringDate, depId, false, 0, idVal);
                offerings.add(o);
                ++count;
            }
            rs.close ();
            s.close ();
        } 
     
        catch (Exception e)
        {
           // Logger.getLogger(Broker.class.getName()).log(Level.SEVERE, null, ex);
            status.SetFail(e.getMessage());
            JOptionPane.showMessageDialog(null, status.msg, "Database Query Error", ERROR_MESSAGE);
            offerings = null;
            return offerings;
        }
      
        return offerings;
    }
    
    public Status EditOffering(Offering _offering)
    {
        Status status = new Status();
        String query = " UPDATE offerings SET depositid = ?, offeringdate = ?, name = ?, paymenttype = ?, checknum = ?, amount = ?, category = ?, department = ?, comments = ?)"
        + " WHERE depositid = ?";
        PreparedStatement preparedStmt;
        try 
        {
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString (1, _offering.depositId);
            preparedStmt.setString (2, _offering.dateOfOffering);
            preparedStmt.setString   (3, _offering.name);
            preparedStmt.setString   (4, _offering.paymentType);
            preparedStmt.setString   (5, _offering.paymentNumber);
            preparedStmt.setDouble(6, _offering.amount);
            preparedStmt.setString   (7, _offering.category);
            preparedStmt.setString   (8, _offering.department);
            preparedStmt.setString   (9, _offering.comments);
            preparedStmt.setString (10, _offering.depositId);
            preparedStmt.execute();
        } 
     
        catch (Exception e)
        {
           // Logger.getLogger(Broker.class.getName()).log(Level.SEVERE, null, ex);
            status.SetFail(e.getMessage());
            return status;
        }
      
        return status;
    }
    
    public Status DeleteOffering(Offering _offering)
    {
          Status status = new Status();
        String query = " DELETE FROM offerings WHERE offeringid = ?";
        PreparedStatement preparedStmt;
        try 
        {
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString (1, _offering.depositId);
            preparedStmt.execute();
        } 
     
        catch (Exception e)
        {
           // Logger.getLogger(Broker.class.getName()).log(Level.SEVERE, null, ex);
            status.SetFail(e.getMessage());
            return status;
        }
      
        return status;
    }
    public Status AddTransaction(Transaction _transaction, String account)
    {
        Status status = new Status();
        String query = " insert into " +account+" (transactiondate, checknum, description, debit, credit, category, department, comments, documentlink, isreconciled, monthReconciled)"
        + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStmt;
        Double debit = 0.0;
        Double credit = 0.0;
        if(_transaction.amount >0)
        {
            debit = 0.0;
            credit = _transaction.amount;
        }
        
        else
        {
            debit = _transaction.amount;
            credit = 0.0;
        }
        try 
        {
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString (1, _transaction.date);
            preparedStmt.setString (2, _transaction.paymentNumber);
            preparedStmt.setString   (3, _transaction.description);
            preparedStmt.setDouble  (4, debit);
            preparedStmt.setDouble   (5, credit);
            preparedStmt.setString(6, _transaction.category);
            preparedStmt.setString   (7, _transaction.department);
            preparedStmt.setString   (8, _transaction.comments);
            preparedStmt.setString   (9, _transaction.documentLinkAddress);
            preparedStmt.setBoolean   (10, _transaction.isReconciled);
            preparedStmt.setInt  (11, _transaction.monthReconciled);
            preparedStmt.execute();
        } 
     
        catch (Exception e)
        {
           // Logger.getLogger(Broker.class.getName()).log(Level.SEVERE, null, ex);
            status.SetFail(e.getMessage());
            return status;
        }
      return status;
    }
    
    public Status UpdateReceiptForOfferings(String depositId, String docLink)
    {
         Status status = new Status();
        String query = " UPDATE offerings SET documentlink = '" +docLink +"' WHERE depositid = '" + depositId + "'";
        PreparedStatement preparedStmt;
        try 
        {
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.execute();
        } 
     
        catch (Exception e)
        {
           // Logger.getLogger(Broker.class.getName()).log(Level.SEVERE, null, ex);
            status.SetFail(e.getMessage());
            return status;
        }
      return status;
    }
    public Transaction GetTransaction()
    {
        Transaction transaction = null;
        return transaction;
    }
    public Status EditTransaction(Transaction _transaction)
    {
        Status status = null;
        return status;
    }
    public Status AddUser()
    {
        Status status = null;
        return status;
    }
    public int GetUser(String username, String password)
    {
        String query = "SELECT usertype FROM userinfo WHERE username ='"+username+"' AND password='"+password+"'";
        try
        {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int userType = -1;
            int counter = 0;
            while(rs.next())
            {
                counter++;
                userType = rs.getInt("usertype");
            }
            if(counter != 1)// Should only be one matching result
            {
                return -1;
            }
            
            else
            {
                return userType;
            }
        }
        
         catch (SQLException ex) 
        {
            Status stat = new Status();
            stat.SetFail("Unable to connect to database." + ex.getMessage());
            return -1;
        }
               
    }
    
    public Deposit GetDeposit(String depositId)
    {
        Double amount = 0.0;
        ArrayList<Offering>offerings = GetOfferings(depositId);
        Deposit deposit = new Deposit();
        deposit.id = depositId;
        deposit.date = offerings.get(0).dateOfOffering;
        
        if(offerings.get(0).receiptLink == null)
        {
            deposit.link = "";
        }

        else
        {
            deposit.link =offerings.get(0).receiptLink;
        }
        ArrayList<Transaction>transactions = new ArrayList<Transaction>();
        try
        {
            for(Offering o: offerings)
            {
                boolean isNewTransactionNeeded = true;
                for(int i = 0; i< transactions.size();i++)
                {
                    if(transactions.get(i).category.contentEquals(o.category) && transactions.get(i).department.contentEquals(o.department))
                    {
                        transactions.get(i).amount += o.amount;
                        isNewTransactionNeeded = false;
                        break;
                    }                    
                }
                
                if(isNewTransactionNeeded)
                {
                    transactions.add(new Transaction(0, o.dateOfOffering, "Offering", o.amount, o.comments,
                    o.category, o.department, o.receiptLink, o.paymentNumber,0, false, false));
                }
                amount += o.amount;
            }
            deposit.amount = amount;
            deposit.SetOfferings(offerings);
            deposit.SetTransactions(transactions);
            
        }        
         catch(NoSuchElementException ex)
        {
            Status stat = new Status();
            stat.SetFail(ex.getMessage());
            return null;
        }
        return deposit;
    }
    
    public ArrayList<Deposit> GetDeposits()
    {
        String s = new String();
        s = "";
        String deptName = new String();
        Double amount = 0.0;
        int counter = 0;
        String query = "SELECT depositid FROM offerings";
        ArrayList<String>depositIds = new ArrayList<String>();
        ArrayList<Deposit>deposits = new ArrayList<Deposit>();
        
        try
        {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next())
            {
                String depositId = new String();
                counter++;
                depositId = rs.getString("depositid");
                if(!depositIds.contains(depositId))
                {
                    depositIds.add(depositId);
                }           
            }
            
            for (String d: depositIds)
            {
                Deposit currentDeposit = GetDeposit(d);
                if(currentDeposit != null)
                {
                    deposits.add(currentDeposit);
                }
                
                else
                {
                    deposits.clear();
                }
                
            }
            
            return deposits;
            
        }
        
         catch (SQLException ex) 
        {
            Status stat = new Status();
            stat.SetFail("Unable to connect to database." + ex.getMessage());
            return null;
        }
        
        /*ApplicationOptions opts = ApplicationOptions.getInstance();
        Vector<Auxiliary> departments = opts.GetAuxiliaries();
        ArrayList amounts = new ArrayList<Double>();
        Double total = 0.0;
        try
        {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next())
            {
                counter++;
                deptName = rs.getString("department");
                amount = rs.getDouble("amount");
                int i = departments.indexOf(deptName);
                Double currentAmount= (Double) amounts.get(i);
                currentAmount += amount;
                total += amount;
                amounts.set(i, currentAmount);               
            }
            
            for(int i = 0; i<departments.size();i++)
            {
                s+= String.format("%s = %f\r\n", departments.get(i).name, amounts.get(i));
            }    
        }
        
         catch (SQLException ex) 
        {
            Status stat = new Status();
            stat.SetFail("Unable to connect to database." + ex.getMessage());
            return stat.msg;
        }
        */
    }
    private boolean isConnected =false;
    public boolean IsConnected()
    {
        return isConnected;
    }
    /*public Status EditUser(User _user)
    {
        Status status = null;
        return status;
    }*/
    
  private static Broker single_instance = null;
  Connection connection = null;    
    // static method to create instance of Singleton class 
    public static Broker getInstance() 
    { 
        if (single_instance == null) 
        {
            single_instance = new Broker(); 
        }          
  
        return single_instance; 
    }   
}
