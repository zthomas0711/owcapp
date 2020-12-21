/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package owcapp;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.oxbow.swingbits.table.filter.TableRowFilterSupport;
 
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import javax.swing.table.DefaultTableCellRenderer;
import static owcapp.OWCApp.GetSummaryOfDeposit;
//import owcapp.OWCApp.MyTableModel;

/**
 *
 * @author Zachary
 */


public class ViewOfferingsDialog extends javax.swing.JFrame {

    /**
     * Creates new form ViewOfferings
     */
    public ViewOfferingsDialog() {
            
        initComponents();
        summaryViewButton.setSelected(true);
        detailedViewButton.setSelected(false);
        SetSummaryTable();        
    }
    
    private void SetSummaryTable()
    {
        String[] columnNames = {"Deposit ID", "Date", "Amount", "Link"};
        ArrayList<Boolean>columnsReadOnly = new ArrayList<>(Arrays.asList(true, true, true, true));
        depositsTable.setModel(new CustomModel(columnNames, columnsReadOnly));
        CustomTableModelRender myRenderer = new CustomTableModelRender();
       depositsTable.setDefaultRenderer(Object.class, myRenderer);
        //depositsTable.setDefaultRenderer(Integer.class, myRenderer);
        depositsTable.setDefaultRenderer(Double.class, myRenderer);
        CustomModel model = (CustomModel) depositsTable.getModel();
        model.addTableModelListener(new MyTableModelListener());
        //offeringTable = new JTable(new CustomModel(columnNames));
        Action action = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                TableCellListener tcl = (TableCellListener)e.getSource();
                CustomModel customModel = (CustomModel) tcl.getTable().getModel();
                if(tcl.getOldValue()!= tcl.getNewValue() && !customModel.rowsAdded.contains(tcl.getRow()))
                {
                    customModel.rowsEdited.add(tcl.getRow());
            
                }
            }
        };
        
        depositsTable.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        int row = depositsTable.rowAtPoint(evt.getPoint());
        int col = depositsTable.columnAtPoint(evt.getPoint());
        if (col==3)
        {
            CustomModel model = (CustomModel) depositsTable.getModel();
            row = depositsTable.convertRowIndexToModel(row);
            String fileLocation = (String) model.getValueAt(row, col);
            
            if(!fileLocation.isEmpty())
            {
                try 
                {
                    Desktop.getDesktop().open(new File(fileLocation));
                } 
                catch (IOException ex)
                {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "File Handling Error", ERROR_MESSAGE);
                }
                
            }

        }
    }
});

        TableCellListener tcl = new TableCellListener(depositsTable, action);
       
        
       depositsTable = TableRowFilterSupport
        .forTable(depositsTable)
        .searchable(true)
        .actions(true)
        .useTableRenderers(true)
        .apply();
        
        PopulateSummaryTable();
    }
    
    private void SetDetailedTable()
    {
        String[] columnNames = {"ID", "Deposit ID", "Date", "Name", "Payment Type", "Check#", "Amount","Department","Category","Comments"};
        ArrayList<Boolean>columnsReadOnly = new ArrayList<>(Arrays.asList(true, false, false, false, false, false, false, false, false, false));
        offeringTable.setModel(new CustomModel(columnNames, columnsReadOnly));
        CustomTableModelRender myRenderer = new CustomTableModelRender();
        offeringTable.setDefaultRenderer(Object.class, myRenderer);
        offeringTable.setDefaultRenderer(Integer.class, myRenderer);
        offeringTable.setDefaultRenderer(Double.class, myRenderer);
        CustomModel model = (CustomModel) offeringTable.getModel();
        model.addTableModelListener(new MyTableModelListener());
        //offeringTable = new JTable(new CustomModel(columnNames));
        Action action = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                TableCellListener tcl = (TableCellListener)e.getSource();
                CustomModel customModel = (CustomModel) tcl.getTable().getModel();
                if(tcl.getOldValue()!= tcl.getNewValue() && !customModel.rowsAdded.contains(tcl.getRow()))
                {
                    customModel.rowsEdited.add(tcl.getRow());
            
                }
            }
        };

        TableCellListener tcl = new TableCellListener(offeringTable, action);
        offeringTable = TableRowFilterSupport
        .forTable(offeringTable)
        .searchable(true)
        .actions(true)
        .useTableRenderers(true)
        .apply();
        
         
        ApplicationOptions opts = ApplicationOptions.getInstance();
        Vector<String> categories = opts.GetCategories();
        Vector<Auxiliary> departments = opts.GetAuxiliaries();
        categoryBox = new JComboBox();
        departmentBox = new JComboBox();
        paymentTypeBox = new JComboBox();
        for(int i = 0; i < categories.size(); i++)
        {
            categoryBox.addItem(categories.get(i));
        }
        
        for(int i = 0; i < departments.size(); i++)
        {
            departmentBox.addItem(departments.get(i).name);
        }
        
        paymentTypeBox.addItem("Cash");
        paymentTypeBox.addItem("Check");
        
        TableColumn deptColumn = offeringTable.getColumnModel().getColumn(7);
        TableColumn catColumn = offeringTable.getColumnModel().getColumn(8);
        TableColumn payColumn = offeringTable.getColumnModel().getColumn(4);
        deptColumn.setCellEditor(new DefaultCellEditor(departmentBox));
        catColumn.setCellEditor(new DefaultCellEditor(categoryBox));
        payColumn.setCellEditor(new DefaultCellEditor(paymentTypeBox));
        PopulateTable();
    }
    
    private void SetView()
    {
        
        if(summaryViewButton.isSelected())
        {
            addButton.setEnabled(false);
            saveButton.setEnabled(false);
            documentLinkLabel.setVisible(true);
            documentLink.setVisible(true);
            browseButton.setVisible(true);
            summaryButton.setEnabled(true);
            UploadButton.setVisible(true);
        }
        
        else
        {
            addButton.setEnabled(true);
            saveButton.setEnabled(true);
            documentLinkLabel.setVisible(false);
            documentLink.setVisible(false);
            browseButton.setVisible(false); 
            summaryButton.setEnabled(false);
            UploadButton.setVisible(false);
        }
    }
    
    private void ClearTable()
    {
        // Clear offering table
        offeringTable.setModel(new DefaultTableModel());
        offeringTable.revalidate();
    }
    
    private void ClearDepositTable()
    {
        depositsTable.setModel(new DefaultTableModel());
        depositsTable.revalidate();
    }
    
    private void PopulateTable()
    {
        Broker b = Broker.getInstance();
        ArrayList<Offering>offerings = b.GetAllOfferings();
        CustomModel model = (CustomModel) offeringTable.getModel();
        for(int i = 0; i < offerings.size(); i++)
        { 
            model.addRow(offerings.get(i));            
        }
        model.SetOriginalSize(offerings.size());
        model.SetColorChangeOnEdit(true);
        
    }
        
    private void PopulateSummaryTable()
    {
        Broker b = Broker.getInstance();
        ArrayList<Deposit>deposits= b.GetDeposits();
        CustomModel model = (CustomModel) depositsTable.getModel();
        for(int i = 0; i < deposits.size(); i++)
        { 
            model.addRow(deposits.get(i));            
        }
        model.SetOriginalSize(deposits.size());
        model.SetColorChangeOnEdit(false);
        
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        buttonGroup6 = new javax.swing.ButtonGroup();
        buttonGroup7 = new javax.swing.ButtonGroup();
        addButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        offeringTable = new javax.swing.JTable();
        removeButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        documentLinkLabel = new javax.swing.JLabel();
        documentLink = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        detailedViewButton = new javax.swing.JRadioButton();
        summaryViewButton = new javax.swing.JRadioButton();
        summaryButton = new javax.swing.JButton();
        UploadButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        depositsTable = new javax.swing.JTable();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        offeringTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(offeringTable);

        removeButton.setText("Remove");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        jButton3.setText("Exit");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        documentLinkLabel.setText("Upload Document");

        documentLink.setEditable(false);

        browseButton.setText("Browse");
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(detailedViewButton);
        detailedViewButton.setText("Detailed View");
        detailedViewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detailedViewButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(summaryViewButton);
        summaryViewButton.setText("Summary View");
        summaryViewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                summaryViewButtonActionPerformed(evt);
            }
        });

        summaryButton.setText("Get Summary");
        summaryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                summaryButtonActionPerformed(evt);
            }
        });

        UploadButton.setText("Upload Doc");
        UploadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UploadButtonActionPerformed(evt);
            }
        });

        depositsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(depositsTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(summaryButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(documentLinkLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(documentLink, javax.swing.GroupLayout.PREFERRED_SIZE, 633, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(browseButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(UploadButton))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(summaryViewButton)
                        .addGap(41, 41, 41)
                        .addComponent(detailedViewButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(118, 118, 118)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(detailedViewButton)
                    .addComponent(summaryViewButton))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addGap(30, 30, 30)
                        .addComponent(removeButton)
                        .addGap(35, 35, 35)
                        .addComponent(saveButton)
                        .addGap(29, 29, 29)
                        .addComponent(summaryButton)
                        .addGap(37, 37, 37)
                        .addComponent(jButton3))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(documentLinkLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(documentLink, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseButton)
                    .addComponent(UploadButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        CustomModel model = (CustomModel) offeringTable.getModel();
        Offering offering = new Offering();
        model.addRow(offering);
    }//GEN-LAST:event_addButtonActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        CustomModel model = (CustomModel) offeringTable.getModel();
        Offering offering = new Offering();
        model.delRow(offeringTable.getSelectedRow());
        
    }//GEN-LAST:event_removeButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
       int result = JOptionPane.showConfirmDialog(this.rootPane, "Are you sure you would like to save the changes?", "Confirm Savings of Data",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
       switch(result)
       {
           case JOptionPane.YES_OPTION:
               Status status = SaveEntries();
               if(!status.isSuccess)
               {
                   JOptionPane.showMessageDialog(null, status.msg, "Database Query Error", ERROR_MESSAGE);
               }
               
               else
               {
                   JOptionPane.showMessageDialog(null, "Database Saved Successfully", "Database Saved Successfully", PLAIN_MESSAGE);
                   CustomModel model = (CustomModel) offeringTable.getModel();
                   offeringTable.setBackground(Color.white);
                   
               }
               break;
               
           case JOptionPane.NO_OPTION:
               break;
           default:
               break;
       }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        // TODO add your handling code here:
        documentLink.setText(OWCApp.GetFileName(this));
    }//GEN-LAST:event_browseButtonActionPerformed

    private void summaryViewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_summaryViewButtonActionPerformed
        // TODO add your handling code here:
        ClearTable();
        SetView();
        SetSummaryTable();
    }//GEN-LAST:event_summaryViewButtonActionPerformed

    private void detailedViewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detailedViewButtonActionPerformed
        // TODO add your handling code here:
        ClearDepositTable();
        SetView();
        SetDetailedTable();
    }//GEN-LAST:event_detailedViewButtonActionPerformed

    private void UploadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UploadButtonActionPerformed
        // TODO add your handling code here:

        int row = depositsTable.getSelectedRow();
        if (row<0)
        {
            JOptionPane.showMessageDialog(null, "Select a deposit to link the document to.", "Database Query Error", ERROR_MESSAGE);
            return;
        }
        int col = 0;
        CustomModel model = ((CustomModel)depositsTable.getModel());
        String depositId = (String)model.getValueAt(row, col);
        Broker broker = Broker.getInstance();
        String doc = documentLink.getText();
        doc = doc.replace("\\", "\\\\");
        Status status = broker.UpdateReceiptForOfferings(depositId, doc);
        if(status.isSuccess)
        {
            col = 3; // This is where the receipts are
            model.setValueAt(documentLink.getText(), row, col);
            documentLink.setText("");
            JOptionPane.showMessageDialog(null, "Receipt loaded successfully", "Receipt loaded successfully", PLAIN_MESSAGE);
        }
        
        else
        {
            JOptionPane.showMessageDialog(null, status.msg, "Database Query Error", ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_UploadButtonActionPerformed

    private void summaryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_summaryButtonActionPerformed
        int row = depositsTable.getSelectedRow();
        if (row<0)
        {
            JOptionPane.showMessageDialog(null, "Select a deposit to link the document to.", "Database Query Error", ERROR_MESSAGE);
            return;
        }
        int col = 0;
        CustomModel model = ((CustomModel)depositsTable.getModel());
        String depositId = (String)model.getValueAt(row, col);
        String summary = GetSummaryOfDeposit(depositId);
        JOptionPane.showMessageDialog(null, summary, "Deposit Summary", PLAIN_MESSAGE);
        return;
    }//GEN-LAST:event_summaryButtonActionPerformed

    private Status SaveEntries()
    {
        Status status = new Status();
        CustomModel model = (CustomModel) offeringTable.getModel();
        int maxSize = Math.max(model.itemsAdded.size(), model.itemsDeleted.size());
        maxSize = Math.max(maxSize, model.itemsEdited.size());
        Broker broker = Broker.getInstance();
        for(int i = 0; i< maxSize; i++)
        {
            if(i < model.itemsAdded.size()&& model.itemsAdded.size()>0)
            {
                Offering offering = (Offering) model.itemsAdded.get(i).GetWholeItem();
                status = broker.AddOffering(offering);
                
            }
            
            if(status.isSuccess && i < model.itemsEdited.size() && model.itemsEdited.size()>0)
            {
                Offering offering = (Offering) model.itemsEdited.get(i).GetWholeItem();
                status = broker.EditOffering(offering);
            }
            
            if(status.isSuccess && i < model.itemsDeleted.size() && model.itemsDeleted.size() >0)
            {
                Offering offering = (Offering) model.itemsDeleted.get(i).GetWholeItem();
                status = broker.DeleteOffering(offering);
            }
        }
        return status;
    }
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
            java.util.logging.Logger.getLogger(ViewOfferingsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewOfferingsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewOfferingsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewOfferingsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewOfferingsDialog().setVisible(true);
            }
        });
    }
    
    @Override
public void dispose() {
    return;
}

JComboBox categoryBox;
JComboBox departmentBox;
JComboBox paymentTypeBox;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton UploadButton;
    private javax.swing.JButton addButton;
    private javax.swing.JButton browseButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.ButtonGroup buttonGroup6;
    private javax.swing.ButtonGroup buttonGroup7;
    private javax.swing.JTable depositsTable;
    private javax.swing.JRadioButton detailedViewButton;
    private javax.swing.JTextField documentLink;
    private javax.swing.JLabel documentLinkLabel;
    private javax.swing.JButton jButton3;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable offeringTable;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton summaryButton;
    private javax.swing.JRadioButton summaryViewButton;
    // End of variables declaration//GEN-END:variables
}
