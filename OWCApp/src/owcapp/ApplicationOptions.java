/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package owcapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

/**
 *
 * @author Zachary
 */
public class ApplicationOptions {
    private static ApplicationOptions single_instance = null;
    // static method to create instance of Singleton class 
    public static ApplicationOptions getInstance() 
    { 
        if (single_instance == null) 
        {
            single_instance = new ApplicationOptions(); 
        }          
  
        return single_instance; 
    }   
    
    public Status ReadCategories()
    {
        try
        {
            Status status = new Status();
            String directory = System.getProperty("user.dir");
            String fileName = directory + "/Config Files/Categories.txt";
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            categories = new Vector();
            while((line = br.readLine()) != null)
            {
                categories.add(line);
            }
            return status;
        }
        
        catch(FileNotFoundException ex) 
        {
            Status status = new Status();
            status.SetFail(ex.getMessage());
            return status;
        }
        
        catch(IOException e)
        {
            Status status = new Status();
            status.SetFail(e.getMessage());
            return status;
        }
    }
    
    public Status ReadAccounts()
    {
        try
        {
            Status status = new Status();
            String directory = System.getProperty("user.dir");
            String fileName = directory + "/Config Files/Starting Balances.txt";
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            
            BufferedReader br = new BufferedReader(fr);
            String line;
            auxiliaries = new Vector();
            while((line = br.readLine()) != null)
            {
                int delimiterPosName = line.indexOf(":");
                String name = line.substring(0, delimiterPosName);
                int delimiterPosCheckBal = line.indexOf(",");
                String checkBal = line.substring(delimiterPosName+2, delimiterPosCheckBal);
                double startCheckBal = Double.valueOf(line.substring(delimiterPosName+2, delimiterPosCheckBal));
                double startSavBal = Double.valueOf(line.substring(delimiterPosCheckBal+2));
                Auxiliary aux = new Auxiliary(startCheckBal, startSavBal,name);
                boolean add = auxiliaries.add(aux);
                
            }
            return status;
        }
        
        catch(FileNotFoundException ex) 
        {
            Status status = new Status();
            status.SetFail(ex.getMessage());
            return status;
        }
        
        catch(IOException e)
        {
            Status status = new Status();
            status.SetFail(e.getMessage());
            return status;
        }
    }
    
    public Vector<String> GetCategories()
    {
        return categories;
    }
    
    public Vector<Auxiliary> GetAuxiliaries()
    {
        return auxiliaries;
    }
    
    public int GetAuxiliaryIndex(String auxName)
    {
        int index = -1;
        for(int i = 0; i<auxiliaries.size();i++)
        {
            if(auxiliaries.get(i).name.equals(auxName))
            {
                index = i;
                return index;
            }
        }
        return index;
    }
    private Vector<String> categories;
    private Vector<Auxiliary>auxiliaries;
}

