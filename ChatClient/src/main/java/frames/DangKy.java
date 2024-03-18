package frames;

import java.io.IOException;
import javax.swing.JOptionPane;

public class DangKy extends javax.swing.JFrame {

    public DangKy() {
        initComponents();
        this.setVisible(true);
        setLocationRelativeTo(null);
        warning_name.setVisible(false);
        warning_pass.setVisible(false);
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
        warning_name = new javax.swing.JLabel();
        warning_pass = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(247, 247, 247));

        Dangky_label.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        Dangky_label.setText("SIGN UP");

        passL.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        passL.setText("Password:");

        nameL.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        nameL.setText("Username:");

        dangky_B.setText("Đăng ký");
        dangky_B.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dangky_BActionPerformed(evt);
            }
        });

        warning_name.setText("Username chỉ tối đa 8 kí tự");

        warning_pass.setText("Mật khẩu phải có ít nhất 6 kí tự");

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
                            .addComponent(warning_name)
                            .addComponent(warning_pass)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(149, 149, 149)
                        .addComponent(Dangky_label))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(126, 126, 126)
                        .addComponent(jButton1))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(warning_name)
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtpass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passL))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(warning_pass)
                .addGap(15, 15, 15)
                .addComponent(dangky_B)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(14, Short.MAX_VALUE))
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

        warning_name.setVisible(false);
        warning_pass.setVisible(false);

        if (username.length() == 0) {
            warning_name.setText("Không được để trống username!!!");
            warning_name.setVisible(true);
            return;
        }

        if (username.length() > 8) {
            warning_name.setText("Username chỉ tối đa 8 kí tự");
            warning_name.setVisible(true);
            return;
        }

        if (password.length() == 0) {
            warning_pass.setText("Không được để trống password!!!");
            warning_pass.setVisible(true);
            return;
        }

        if (password.length() < 6) {
            warning_pass.setText("Mật khẩu phải có ít nhất 6 kí tự");
            warning_pass.setVisible(true);
            return;
        }

        try {
            ClientFrame.write("request_signup" + "," + username + "," + password);
            System.out.println("da gui yeu cau dang ky");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "Có lỗi xảy ra");
        }

    }//GEN-LAST:event_dangky_BActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        // Hiển thị giao diện đăng nhập
        ClientFrame.dangNhap.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

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
    private javax.swing.JLabel warning_name;
    private javax.swing.JLabel warning_pass;
    // End of variables declaration//GEN-END:variables
}
