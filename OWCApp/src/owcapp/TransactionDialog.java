/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package owcapp;

import java.awt.FileDialog;
import java.io.File;
import static java.lang.Math.abs;
import java.math.BigDecimal;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import javax.swing.JTextField;
import static owcapp.OWCApp.IsAmountNearestHundreth;
import static owcapp.OWCApp.ValidateDate;
/**
 *
 * @author Zachary
 */
public class TransactionDialog extends javax.swing.JFrame {

    /**
     * Creates new form TransactionDialog
     */
    public TransactionDialog() {
        initComponents();
        
        // Load categories and departments
        ApplicationOptions opts = ApplicationOptions.getInstance();
        Vector<String> categories = opts.GetCategories();
        Vector<Auxiliary> departments = opts.GetAuxiliaries();
        for(int i = 0; i < categories.size(); i++)
        {
            category1.addItem(categories.get(i));
            category2.addItem(categories.get(i));
            category3.addItem(categories.get(i));
            category4.addItem(categories.get(i));
            category5.addItem(categories.get(i));
        }
        category1.setSelectedItem("Church Supplies");
        category2.setSelectedItem("Church Supplies");
        category3.setSelectedItem("Church Supplies");
        category4.setSelectedItem("Church Supplies");
        category5.setSelectedItem("Church Supplies");
        for(int i = 0; i < departments.size(); i++)
        {
            department1.addItem(departments.get(i).name);
            department2.addItem(departments.get(i).name);
            department3.addItem(departments.get(i).name);
            department4.addItem(departments.get(i).name);
            department5.addItem(departments.get(i).name);
        }
        department1.setSelectedItem("General");
        department2.setSelectedItem("General");
        department3.setSelectedItem("General");
        department4.setSelectedItem("General");
        department5.setSelectedItem("General");     
        
        checkingRadio.setSelected(true);
        purchaseRadio.setSelected(true);
        DisplayFields(false);
        
    }
    
    

    private void DisplayFields(boolean isSplit)
    {
        boolean doShowField;
        if(isSplit)
        {
            doShowField = true;
        }
        
        else
        {
            doShowField = false;
        }

        // Categorys
        category2.setVisible(doShowField);
        category3.setVisible(doShowField);
        category4.setVisible(doShowField);
        category5.setVisible(doShowField);
       
        // Departments
        department2.setVisible(doShowField);
        department3.setVisible(doShowField);
        department4.setVisible(doShowField);
        department5.setVisible(doShowField);
        
        // Descriptions
        description2.setVisible(doShowField);
        description3.setVisible(doShowField);
        description4.setVisible(doShowField);
        description5.setVisible(doShowField);
        
        // Amounts
        amount1.setVisible(doShowField);
        amount2.setVisible(doShowField);
        amount3.setVisible(doShowField);
        amount4.setVisible(doShowField);
        amount5.setVisible(doShowField);
        amountLabel.setVisible(doShowField);
    }
    
    private void ResetSplitAttributes()
    {
        amount1.setText("0.00");
        amount2.setText("0.00");
        amount3.setText("0.00");
        amount4.setText("0.00");
        amount5.setText("0.00");
        
        description1.setText("");
        description2.setText("");
        description3.setText("");
        description4.setText("");
        description5.setText("");
        
        category1.setSelectedItem("Church Supplies");
        category2.setSelectedItem("Church Supplies");
        category3.setSelectedItem("Church Supplies");
        category4.setSelectedItem("Church Supplies");
        category5.setSelectedItem("Church Supplies");
        
        department1.setSelectedItem("General");
        department2.setSelectedItem("General");
        department3.setSelectedItem("General");
        department4.setSelectedItem("General");
        department5.setSelectedItem("General");
        
        
    }
    
    private void ResetPrimaryAttributes()
    {
        transDate.setText("");
        checkNum.setText("");
        vendor.setText("");
        total.setText("");
        amount1.setText("");
        description1.setText("");
        category1.setSelectedItem("Church Supplies");
        department1.setSelectedItem("General");
        receiptLink.setText("");
        comments.setText("");
    }
    
    private void ResetAll()
    {
        ResetSplitAttributes();
        ResetPrimaryAttributes();
        doSplit.setSelected(false);
        DisplayFields(doSplit.isSelected());
    }
    
    private boolean ValidateSplitEntries(JTextField description, JTextField amount)
    {
        if(!IsAmountNearestHundreth(amount.getText()))
        {
            return false;
        }
        
        if(description.getText().isEmpty() && Double.valueOf(amount.getText())==0.0)
        {
            return true;
        }
        
        if(description.getText().isEmpty() && Double.valueOf(amount.getText())!=0.0)
        {
            return false;
        }
        
        if(!description.getText().isEmpty() && Double.valueOf(amount.getText())<=0.0)
        {
            return false;
        }
        
        if(!description.getText().isEmpty() && Double.valueOf(amount.getText())>0.0)
        {
            return true;
        }
        
        return true;
    }
    
    private boolean ValidateEntries(boolean isSplit)
    {
        try
        {
            
            
            String msg = new String();
            boolean isValid = ValidateDate(transDate.getText());
            if(!isValid)
            {
                msg += "Date format is invalid.\r\n";
            }
            
            if(vendor.getText().isEmpty())
            {
                msg += "Vendor field must be filled out.\r\n";
            }

            boolean isHundredthsPlace = IsAmountNearestHundreth(total.getText());

            if(!isHundredthsPlace)
            {
                msg += "Total isn't in money format.\r\n";
            }

            if(description1.getText().isEmpty())
            {
                msg += "Description1 is empty.\r\n";
            }

            if(isSplit)
            {
                isValid = ValidateSplitEntries(description1, amount1);
                if(isValid)
                {
                    isValid = ValidateSplitEntries(description2, amount2);
                }

                if(isValid)
                {
                    isValid = ValidateSplitEntries(description3, amount3);
                }

                if(isValid)
                {
                    isValid = ValidateSplitEntries(description4, amount4);
                }

                if(isValid)
                {
                    isValid = ValidateSplitEntries(description5, amount5);
                }
                
                if(!isValid)
                {
                    msg += "All split transactions need to have both description and amount filled out or description should be left blank and amount at 0. Amounts need to have monetary format.\r\n";
                }
                
                isValid = ValidateTotal();
                if(!isValid)
                {
                    msg += "All transaction amounts have to equal the total.\r\n";
                }                
            }

            if(!msg.isEmpty())
            {
                JOptionPane.showMessageDialog(null, msg, "Invalid Entry", ERROR_MESSAGE);
                isValid = false;
            }       
            
            return isValid;        
        }
        
         catch (NumberFormatException nfe) 
        {  
            JOptionPane.showMessageDialog(null, "Amount isn't numeric", "Invalid Entry", ERROR_MESSAGE); 
            return false;
        }
    }
    
    private boolean ValidateTotal()
    {
        if(doSplit.isSelected())
        {
            double subtotal = 0.0;
            subtotal += Double.valueOf(amount1.getText());
            subtotal += Double.valueOf(amount2.getText());
            subtotal += Double.valueOf(amount3.getText());
            subtotal += Double.valueOf(amount4.getText());
            subtotal += Double.valueOf(amount5.getText());
            if(subtotal == Double.valueOf(total.getText()))
            {
                return true;
            }
            
            else
            {
                return false;
            }
            
        }
        
        else
        {
            return true;
        }
  
    }
    
    private Transaction CreateTransaction(JTextField description, JComboBox department, JComboBox category, JTextField amount, int multiplier)
    {
        String _transPaymentNumber = checkNum.getText();
        String _transDate = transDate.getText();
        String _transComments = comments.getText();
        String _receipt = receiptLink.getText();
        String _transDescription = new String();
        String _transDepartment = new String();
        String _transCategory = new String();
        Double _transAmount = Double.valueOf(amount.getText()) *multiplier;
        if(doSplit.isSelected())
        {
            _transDescription = vendor.getText() + "- " +description.getText() +" (total: $" + total.getText() + ")";
        }
        
        else
        {
            _transDescription = vendor.getText() + "- " +description.getText();
        }
        
         _transCategory = category.getSelectedItem().toString();
         _transDepartment = department.getSelectedItem().toString();
        Transaction t = new Transaction(0, _transDate, _transDescription, _transAmount, _transComments, _transCategory, _transDepartment, 
                    _receipt, _transPaymentNumber, 0, false, false);
         return t;
    }
    
    private void SendTransactionToDatabase()
    {
        Broker b = Broker.getInstance();
        Status status = new Status();
        int multiplier = 1;
        String account = new String();
        if(purchaseRadio.isSelected())
        {
            if(creditCardRadio.isSelected())
            {
                multiplier = 1;
            }
            
            else
            {
                multiplier = -1;
            }            
        }
        
        if(refundRadio.isSelected())
        {
            if(creditCardRadio.isSelected())
            {
                multiplier = -1;
            }
            
            else
            {
                multiplier = 1;
            }  
        }
        
        if(checkingRadio.isSelected())
        {
            account = "checking";
        }
        
        if(savingsRadio.isSelected())
        {
            account = "saving";
        }
        
        if(creditCardRadio.isSelected())
        {
            account = "creditcard";
        }
        
        if(doSplit.isSelected())
        {
           Transaction t = CreateTransaction(description1, department1, category1, amount1, multiplier);
            status = b.AddTransaction(t, account);
            if(status.isSuccess && !description2.getText().isEmpty()&& Double.valueOf(amount1.getText())>0)
            {
                t = CreateTransaction(description2, department2, category2, amount2, multiplier);
                status = b.AddTransaction(t, account);
            }
            
            if(status.isSuccess && !description3.getText().isEmpty()&& Double.valueOf(amount3.getText())>0)
            {
                t = CreateTransaction(description3, department3, category3, amount3, multiplier);
                status = b.AddTransaction(t, account);
            }
            
            if(status.isSuccess && !description4.getText().isEmpty()&& Double.valueOf(amount4.getText())>0)
            {
                t = CreateTransaction(description4, department4, category4, amount4, multiplier);
                status = b.AddTransaction(t, account);
            }
            
            if(status.isSuccess && !description5.getText().isEmpty()&& Double.valueOf(amount5.getText())>0)
            {
                t = CreateTransaction(description5, department5, category5, amount5, multiplier);
                status = b.AddTransaction(t, account);
            }
        }
        
        else
        {
            Transaction t = CreateTransaction(description1, department1, category1, total, multiplier);
            status = b.AddTransaction(t, account);            
        }
        
        if(status.isSuccess)
        {
            JOptionPane.showMessageDialog(null, "Data Logged Successfully", "Data Logged Successfully", PLAIN_MESSAGE);
            return;
        }
        
        else
        {
            JOptionPane.showMessageDialog(null, status.msg, "Database Query Error", ERROR_MESSAGE);
            return;
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        jFileChooser1 = new javax.swing.JFileChooser();
        jFileChooser2 = new javax.swing.JFileChooser();
        buttonGroup5 = new javax.swing.ButtonGroup();
        buttonGroup6 = new javax.swing.ButtonGroup();
        buttonGroup7 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        checkingRadio = new javax.swing.JRadioButton();
        savingsRadio = new javax.swing.JRadioButton();
        creditCardRadio = new javax.swing.JRadioButton();
        purchaseRadio = new javax.swing.JRadioButton();
        refundRadio = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        amount1 = new javax.swing.JTextField();
        description1 = new javax.swing.JTextField();
        category1 = new javax.swing.JComboBox<>();
        department1 = new javax.swing.JComboBox<>();
        description3 = new javax.swing.JTextField();
        category3 = new javax.swing.JComboBox<>();
        department3 = new javax.swing.JComboBox<>();
        amount3 = new javax.swing.JTextField();
        description2 = new javax.swing.JTextField();
        category2 = new javax.swing.JComboBox<>();
        department2 = new javax.swing.JComboBox<>();
        amount2 = new javax.swing.JTextField();
        description4 = new javax.swing.JTextField();
        category4 = new javax.swing.JComboBox<>();
        department4 = new javax.swing.JComboBox<>();
        amount4 = new javax.swing.JTextField();
        description5 = new javax.swing.JTextField();
        category5 = new javax.swing.JComboBox<>();
        department5 = new javax.swing.JComboBox<>();
        amount5 = new javax.swing.JTextField();
        transDate = new javax.swing.JTextField();
        checkNum = new javax.swing.JTextField();
        vendor = new javax.swing.JTextField();
        total = new javax.swing.JTextField();
        doSplit = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        browseButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        receiptLink = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        amountLabel = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        comments = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Date [yyyy-mm-dd]");

        jLabel2.setText("Check Num");

        buttonGroup1.add(checkingRadio);
        checkingRadio.setText("Checking");

        buttonGroup1.add(savingsRadio);
        savingsRadio.setText("Savings");

        buttonGroup1.add(creditCardRadio);
        creditCardRadio.setText("Credit Card");

        buttonGroup2.add(purchaseRadio);
        purchaseRadio.setText("Purchase");

        buttonGroup2.add(refundRadio);
        refundRadio.setText("Refund");

        jLabel6.setText("Total");

        jLabel7.setText("Vendor");

        total.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalActionPerformed(evt);
            }
        });

        doSplit.setText("Split Transaction");
        doSplit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doSplitActionPerformed(evt);
            }
        });

        jButton1.setText("Enter Transaction");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Exit");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        browseButton.setText("...");
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        jLabel3.setText("Receipt Link");

        receiptLink.setEditable(false);

        jLabel4.setText("Description");

        jLabel5.setText("Category");

        jLabel8.setText("Department");

        amountLabel.setText("Amount");

        jLabel9.setText(" Comments");

        comments.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(checkingRadio)
                        .addGap(0, 0, 0)
                        .addComponent(savingsRadio)
                        .addGap(2, 2, 2)
                        .addComponent(creditCardRadio))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(purchaseRadio)
                        .addGap(0, 0, 0)
                        .addComponent(refundRadio))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(40, 40, 40)
                        .addComponent(jLabel2)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel7)
                        .addGap(231, 231, 231)
                        .addComponent(jLabel6))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(transDate, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(checkNum, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(vendor, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(description2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(category2, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(department2, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(description3, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(category3, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(department3, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(amount3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(amount2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(description4, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(category4, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(department4, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(description5, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(category5, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(department5, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(amount5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(amount4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(description1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(category1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(department1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(195, 195, 195)
                                .addComponent(jLabel5)
                                .addGap(88, 88, 88)
                                .addComponent(jLabel8)))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(amountLabel)
                            .addComponent(amount1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(doSplit)
                                .addGap(29, 29, 29)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(receiptLink, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(6, 6, 6)
                                        .addComponent(browseButton))
                                    .addComponent(comments, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(412, 412, 412)
                                .addComponent(jButton1)))
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkingRadio)
                    .addComponent(savingsRadio)
                    .addComponent(creditCardRadio))
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(purchaseRadio)
                    .addComponent(refundRadio))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(transDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vendor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(doSplit)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(receiptLink, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(browseButton))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(comments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel8)
                    .addComponent(amountLabel))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(description1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(category1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(department1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(amount1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(description2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(category2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(department2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(amount2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(description3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(category3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(department3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(amount3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(description4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(category4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(department4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(amount4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(description5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(category5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(department5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(amount5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jButton2)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void totalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalActionPerformed
        
    }//GEN-LAST:event_totalActionPerformed

    private void doSplitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doSplitActionPerformed
        DisplayFields(doSplit.isSelected());
        if(doSplit.isSelected())
        {
            ResetSplitAttributes();
        }
    }//GEN-LAST:event_doSplitActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        boolean areEntriesValid = ValidateEntries(doSplit.isSelected());
        if(areEntriesValid)
        {
            SendTransactionToDatabase();
            ResetAll();
        }
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
       receiptLink.setText(OWCApp.GetFileName(this));
    }//GEN-LAST:event_browseButtonActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TransactionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TransactionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TransactionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TransactionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TransactionDialog().setVisible(true);
            }
        });
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField amount1;
    private javax.swing.JTextField amount2;
    private javax.swing.JTextField amount3;
    private javax.swing.JTextField amount4;
    private javax.swing.JTextField amount5;
    private javax.swing.JLabel amountLabel;
    private javax.swing.JButton browseButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.ButtonGroup buttonGroup6;
    private javax.swing.ButtonGroup buttonGroup7;
    private javax.swing.JComboBox<String> category1;
    private javax.swing.JComboBox<String> category2;
    private javax.swing.JComboBox<String> category3;
    private javax.swing.JComboBox<String> category4;
    private javax.swing.JComboBox<String> category5;
    private javax.swing.JTextField checkNum;
    private javax.swing.JRadioButton checkingRadio;
    private javax.swing.JTextField comments;
    private javax.swing.JRadioButton creditCardRadio;
    private javax.swing.JComboBox<String> department1;
    private javax.swing.JComboBox<String> department2;
    private javax.swing.JComboBox<String> department3;
    private javax.swing.JComboBox<String> department4;
    private javax.swing.JComboBox<String> department5;
    private javax.swing.JTextField description1;
    private javax.swing.JTextField description2;
    private javax.swing.JTextField description3;
    private javax.swing.JTextField description4;
    private javax.swing.JTextField description5;
    private javax.swing.JCheckBox doSplit;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JFileChooser jFileChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JRadioButton purchaseRadio;
    private javax.swing.JTextField receiptLink;
    private javax.swing.JRadioButton refundRadio;
    private javax.swing.JRadioButton savingsRadio;
    private javax.swing.JTextField total;
    private javax.swing.JTextField transDate;
    private javax.swing.JTextField vendor;
    // End of variables declaration//GEN-END:variables

}
