/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package frames;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Danh Del Rey
 */
public class FilesFrame extends javax.swing.JFrame {

    /**
     * Creates new form FilesFrame
     */
    public FilesFrame() {
        initComponents();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(false);
    }

    public void addFileToTable(String sender, String fileName, float size, String dateTime) {
        Object row[] = {sender, fileName, size, dateTime};
        DefaultTableModel table = (DefaultTableModel) fileListTable.getModel();
        table.addRow(row);
    }

    public void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(rootPane, message);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileListScrollPane = new javax.swing.JScrollPane();
        fileListTable = new javax.swing.JTable();
        saveAllButton = new javax.swing.JButton();
        saveTheSelectFile = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        fileListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Username", "Filename", "Size", "Datetime"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        fileListTable.setShowGrid(true);
        fileListScrollPane.setViewportView(fileListTable);

        saveAllButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        saveAllButton.setText("Save all");
        saveAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAllButtonActionPerformed(evt);
            }
        });

        saveTheSelectFile.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        saveTheSelectFile.setText("Save the selected files");
        saveTheSelectFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveTheSelectFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 577, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(saveTheSelectFile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveAllButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveTheSelectFile)
                    .addComponent(saveAllButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fileListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAllButtonActionPerformed
        String fileInfoToSave = "";
        if (fileListTable.getRowCount() == 0) {
            JOptionPane.showMessageDialog(rootPane, "No files to download!");
        } else {
            // Lặp qua các dòng
            for (int row = 0; row < fileListTable.getRowCount(); row++) {
                // Lặp qua 2 cột usernaem với filename
                for (int col = 0; col < 2; col++) {
                    String fileInfo = (String) fileListTable.getValueAt(row, col);
                    fileInfoToSave = fileInfoToSave + fileInfo + ";;;;;";
                }
                fileInfoToSave = fileInfoToSave + ":::::";
            }
            choosePathAndRequestToSaveFile(fileInfoToSave);
        }

    }//GEN-LAST:event_saveAllButtonActionPerformed

    void choosePathAndRequestToSaveFile(String fileInfoToSave) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Choose a directory to save");

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String selectedPath = fileChooser.getSelectedFile().getPath() + "\\";
            try {
                ClientFrame.write("request-save-files" + ClientFrame.splitterString + ClientFrame.clientUsername + ClientFrame.splitterString + selectedPath + ClientFrame.splitterString + fileInfoToSave);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "An error occurred");
            }
        }
    }

    private void saveTheSelectFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveTheSelectFileActionPerformed
        int[] selectedRows = fileListTable.getSelectedRows();

        String fileToSave = "";

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(rootPane, "No files selected!");
        } else {
            for (int row = 0; row < selectedRows.length; row++) {
                for (int col = 0; col < 2; col++) {
                    String fileInfo = (String) fileListTable.getValueAt(selectedRows[row], col);
                    fileToSave = fileToSave + fileInfo + ";;;;;";
                }
                fileToSave = fileToSave + ":::::";
            }
            choosePathAndRequestToSaveFile(fileToSave);
        }
    }//GEN-LAST:event_saveTheSelectFileActionPerformed

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane fileListScrollPane;
    private javax.swing.JTable fileListTable;
    private javax.swing.JButton saveAllButton;
    private javax.swing.JButton saveTheSelectFile;
    // End of variables declaration//GEN-END:variables
}
