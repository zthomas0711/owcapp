/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package owcapp;

/**
 *
 * @author Zachary
 */
public class Status {
    Status()
    {
        
    }
    Status(boolean _isSuccess, String _msg)
    {
        isSuccess= _isSuccess;
        msg= _msg;
    }
       public void SetSuccess(String _msg)
    {
        isSuccess = true;
        msg = _msg;
        
    }
       
       public void SetFail(String _msg)
    {
        isSuccess = false;
        msg = _msg;
        
    }
    public boolean IsSuccess()
    {
        return isSuccess;
    }
    public int errorCode = 0;
    public String msg = "Success";
    public boolean isSuccess = true;
 
};
