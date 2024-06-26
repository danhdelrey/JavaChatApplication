package frames;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class LoginFrame extends javax.swing.JFrame {

    public LoginFrame() {
        initComponents();
        setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public String getClientUsername() {
        return usernameInputField.getText();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        signInLabel = new javax.swing.JLabel();
        usernameLabel = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        usernameInputField = new javax.swing.JTextField();
        passwordInputField = new javax.swing.JTextField();
        logInButton = new javax.swing.JButton();
        gotoSignupButton = new javax.swing.JButton();
        noAccountLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        signInLabel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        signInLabel.setText("SIGN IN");

        usernameLabel.setText("Username");

        passwordLabel.setText("Password");

        passwordInputField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passwordInputFieldKeyPressed(evt);
            }
        });

        logInButton.setText("Login");
        logInButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logInButtonActionPerformed(evt);
            }
        });
        logInButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                logInButtonKeyPressed(evt);
            }
        });

        gotoSignupButton.setText("Create account");
        gotoSignupButton.setBorder(null);
        gotoSignupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gotoSignupButtonActionPerformed(evt);
            }
        });

        noAccountLabel.setText("Don't have an account yet?");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(126, 126, 126)
                .addComponent(noAccountLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gotoSignupButton)
                .addGap(139, 139, 139))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(passwordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(usernameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(usernameInputField)
                            .addComponent(passwordInputField, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(194, 194, 194)
                        .addComponent(signInLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(151, 151, 151)
                        .addComponent(logInButton, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(signInLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameLabel)
                    .addComponent(usernameInputField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(passwordInputField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(logInButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(noAccountLabel)
                    .addComponent(gotoSignupButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void gotoSignupButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gotoSignupButtonActionPerformed

        SignupFrame dangKy = new SignupFrame();
        this.setVisible(false);
    }//GEN-LAST:event_gotoSignupButtonActionPerformed

    private void logInButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logInButtonActionPerformed
        String username = usernameInputField.getText();
        String password = passwordInputField.getText();

        if (username.length() == 0 || password.length() == 0) {
            JOptionPane.showMessageDialog(rootPane, "Please fill in the field");
            return;
        }

        try {
            ClientFrame.write("request-login" + ClientFrame.splitterString + username + ClientFrame.splitterString + password);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "An error occurred");
        }
    }//GEN-LAST:event_logInButtonActionPerformed

    private void logInButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_logInButtonKeyPressed

    }//GEN-LAST:event_logInButtonKeyPressed

    private void passwordInputFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordInputFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String username = usernameInputField.getText();
            String password = passwordInputField.getText();

            if (username.length() == 0 || password.length() == 0) {
                JOptionPane.showMessageDialog(rootPane, "Please fill in the field");
                return;
            }

            try {
                ClientFrame.write("request-login" + ClientFrame.splitterString + username + ClientFrame.splitterString + password);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "An error occurred");
            }
        }
    }//GEN-LAST:event_passwordInputFieldKeyPressed

//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(DangKy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(DangKy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(DangKy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(DangKy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new DangNhap().setVisible(true);
//            }
//        });
//    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton gotoSignupButton;
    private javax.swing.JButton logInButton;
    private javax.swing.JLabel noAccountLabel;
    private javax.swing.JTextField passwordInputField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JLabel signInLabel;
    private javax.swing.JTextField usernameInputField;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables
}
