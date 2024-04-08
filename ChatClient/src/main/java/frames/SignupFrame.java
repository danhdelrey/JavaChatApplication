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

        signUpPanel = new javax.swing.JPanel();
        signUpLabel = new javax.swing.JLabel();
        usernameInputField = new javax.swing.JTextField();
        passwordInputField = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        usernameLabel = new javax.swing.JLabel();
        signUpButton = new javax.swing.JButton();
        goToLoginButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        signUpPanel.setBackground(new java.awt.Color(247, 247, 247));

        signUpLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        signUpLabel.setText("SIGN UP");

        passwordInputField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passwordInputFieldKeyPressed(evt);
            }
        });

        passwordLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        passwordLabel.setText("Password:");

        usernameLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        usernameLabel.setText("Username:");

        signUpButton.setText("Register");
        signUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signUpButtonActionPerformed(evt);
            }
        });
        signUpButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                signUpButtonKeyPressed(evt);
            }
        });

        goToLoginButton.setText("Already have an account?");
        goToLoginButton.setBorder(null);
        goToLoginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goToLoginButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout signUpPanelLayout = new javax.swing.GroupLayout(signUpPanel);
        signUpPanel.setLayout(signUpPanelLayout);
        signUpPanelLayout.setHorizontalGroup(
            signUpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(signUpPanelLayout.createSequentialGroup()
                .addGroup(signUpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(signUpPanelLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(signUpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(passwordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(usernameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(signUpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(signUpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(usernameInputField, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                                .addComponent(passwordInputField))
                            .addGroup(signUpPanelLayout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(goToLoginButton))))
                    .addGroup(signUpPanelLayout.createSequentialGroup()
                        .addGap(149, 149, 149)
                        .addComponent(signUpLabel))
                    .addGroup(signUpPanelLayout.createSequentialGroup()
                        .addGap(160, 160, 160)
                        .addComponent(signUpButton)))
                .addContainerGap(87, Short.MAX_VALUE))
        );
        signUpPanelLayout.setVerticalGroup(
            signUpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(signUpPanelLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(signUpLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(signUpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameInputField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(usernameLabel))
                .addGap(24, 24, 24)
                .addGroup(signUpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordInputField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordLabel))
                .addGap(37, 37, 37)
                .addComponent(signUpButton)
                .addGap(18, 18, 18)
                .addComponent(goToLoginButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(signUpPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(signUpPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void signUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signUpButtonActionPerformed
        String username = usernameInputField.getText();
        String password = passwordInputField.getText();

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
    }//GEN-LAST:event_signUpButtonActionPerformed

    private void goToLoginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goToLoginButtonActionPerformed
        // Hiển thị giao diện đăng nhập
        ClientFrame.dangNhap.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_goToLoginButtonActionPerformed

    private void signUpButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_signUpButtonKeyPressed

    }//GEN-LAST:event_signUpButtonKeyPressed

    private void passwordInputFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordInputFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String username = usernameInputField.getText();
            String password = passwordInputField.getText();

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
    }//GEN-LAST:event_passwordInputFieldKeyPressed

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton goToLoginButton;
    private javax.swing.JTextField passwordInputField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JButton signUpButton;
    private javax.swing.JLabel signUpLabel;
    private javax.swing.JPanel signUpPanel;
    private javax.swing.JTextField usernameInputField;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables
}
