package frames;

import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.JOptionPane;

public class SignupFrame extends javax.swing.JFrame {

    public SignupFrame() {
        initComponents();
        this.setVisible(true);
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Dangky_label = new javax.swing.JLabel();
        txtname = new javax.swing.JTextField();
        txtpass = new javax.swing.JTextField();
        passL = new javax.swing.JLabel();
        nameL = new javax.swing.JLabel();
        dangky_B = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(247, 247, 247));

        Dangky_label.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        Dangky_label.setText("SIGN UP");

        txtpass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtpassKeyPressed(evt);
            }
        });

        passL.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        passL.setText("Password:");

        nameL.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        nameL.setText("Username:");

        dangky_B.setText("Register");
        dangky_B.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dangky_BActionPerformed(evt);
            }
        });
        dangky_B.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dangky_BKeyPressed(evt);
            }
        });

        jButton1.setText("Already have an account?");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(passL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nameL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtname, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                                .addComponent(txtpass))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(jButton1))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(149, 149, 149)
                        .addComponent(Dangky_label))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(160, 160, 160)
                        .addComponent(dangky_B)))
                .addContainerGap(87, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(Dangky_label, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameL))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtpass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passL))
                .addGap(37, 37, 37)
                .addComponent(dangky_B)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dangky_BActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dangky_BActionPerformed
        String username = txtname.getText();
        String password = txtpass.getText();

        if (username.length() == 0) {
            JOptionPane.showMessageDialog(rootPane, "Please provide a username!!!");
            return;
        }

        if (username.length() > 15) {
            JOptionPane.showMessageDialog(rootPane, "The username can only be up to 15 characters");
            return;
        }

        if (password.length() == 0) {
            JOptionPane.showMessageDialog(rootPane, "Please provide a password!!!");
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(rootPane, "Password must be at least 6 characters long");
            return;
        }

        try {
            ClientFrame.write("request-signup" + ClientFrame.splitterString + username + ClientFrame.splitterString + password);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "An error occurred");
        }
    }//GEN-LAST:event_dangky_BActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Hiển thị giao diện đăng nhập
        ClientFrame.dangNhap.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void dangky_BKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dangky_BKeyPressed

    }//GEN-LAST:event_dangky_BKeyPressed

    private void txtpassKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtpassKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String username = txtname.getText();
            String password = txtpass.getText();

            if (username.length() == 0) {
                JOptionPane.showMessageDialog(rootPane, "Please provide a username!!!");
                return;
            }

            if (username.length() > 15) {
                JOptionPane.showMessageDialog(rootPane, "The username can only be up to 15 characters");
                return;
            }

            if (password.length() == 0) {
                JOptionPane.showMessageDialog(rootPane, "Please provide a password");
                return;
            }

            if (password.length() < 6) {
                JOptionPane.showMessageDialog(rootPane, "Password must be at least 6 characters long");
                return;
            }

            try {
                ClientFrame.write("request-signup" + ClientFrame.splitterString + username + ClientFrame.splitterString + password);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "An error occurred");
            }
        }
    }//GEN-LAST:event_txtpassKeyPressed

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Dangky_label;
    private javax.swing.JButton dangky_B;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel nameL;
    private javax.swing.JLabel passL;
    private javax.swing.JTextField txtname;
    private javax.swing.JTextField txtpass;
    // End of variables declaration//GEN-END:variables
}
