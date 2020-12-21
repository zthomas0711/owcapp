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
public class Transaction {
    Transaction(int _entryId, String _date, String _description, double _amount, String _comments,
            String _category, String _department, String _documentLinkAddress, String _paymentNumber,
            int _monthReconciled, boolean _isReconciled, boolean _isInternal)
    {
        date = _date;
        description = _description;
        amount = _amount;
        comments = _comments;
        category = _category;
        department = _department;
        isReconciled = _isReconciled;
        monthReconciled = _monthReconciled;
        documentLinkAddress = _documentLinkAddress;
        paymentNumber = _paymentNumber;
        entryId = _entryId;
        isInternal = _isInternal;
    };

    Transaction() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void SetReconciliation(boolean _isReconciled, int _month)
    {
        isReconciled = _isReconciled;
        monthReconciled = _month;
    }
    
    public void UploadDocument(String _address)
    {
        documentLinkAddress = _address;
    }
   

    public String date;
    public String description;
    public double amount =0;
    public String comments;
    public String category;
    public String department;
    public boolean isReconciled = false;
    public int monthReconciled = 0;
    public String documentLinkAddress;
    public String paymentNumber;
    public int entryId;
    public boolean isInternal = false;
}
