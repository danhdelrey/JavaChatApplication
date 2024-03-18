/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package frames;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 *
 * @author Danh Del Rey
 */
public class ClientFrame extends javax.swing.JFrame {

    private Thread thread;
    public static BufferedWriter os;
    public static BufferedReader is;
    private Socket socketOfClient;
    private List<String> onlineList;
    private int id;

    public boolean isLoggedIn = false;

    /**
     * Creates new form ClientFrame
     */
    public ClientFrame() {
        initComponents();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(false);
        jTextArea1.setEditable(false);
        jTextArea2.setEditable(false);
        onlineList = new ArrayList<>();
        setUpSocket();
        id = -1;

    }

    public void setUpSocket() {
        try {

            thread = new Thread() {
                @Override
                public void run() {
                    if (isLoggedIn == false) {
                        DangNhap dangNhap = new DangNhap();
                        try {
                            // Gửi yêu cầu kết nối tới Server đang lắng nghe
                            // trên máy 'localhost' cổng 7777.
                            if (socketOfClient == null) {
                                socketOfClient = new Socket("localhost", 7777);
                                System.out.println("Kết nối thành công!");
                                // Tạo luồng đầu ra tại client (Gửi dữ liệu tới server)
                                os = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));
                                // Luồng đầu vào tại Client (Nhận dữ liệu từ server).
                                is = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));
                            }

                            String message;
                            while (true) {

                                message = is.readLine();
                                if (message == null) {
                                    break;
                                }
                                String[] messageSplit = message.split(",");
                                if (messageSplit[0].equals("login_status")) {
                                    if (messageSplit[1].equals("successful")) {
                                        dangNhap.setVisible(false);
                                        successfulLogin();
                                        setUpSocket();
                                        System.out.println("dang nhap thanh cong");
                                    }
                                }
                                if (messageSplit[0].equals("signup_status")) {
                                    if (messageSplit[1].equals("successful")) {
                                        JOptionPane.showMessageDialog(rootPane, "Successfully!");
                                        System.out.println("dang ky thanh cong");
                                    }
                                }
                            }

                        } catch (UnknownHostException e) {
                            return;
                        } catch (IOException e) {
                            return;
                        }
                    } else {
                        try {

                            String message;
                            while (true) {

                                message = is.readLine();
                                if (message == null) {
                                    break;
                                }
                                String[] messageSplit = message.split(",");
                                if (messageSplit[0].equals("get-id")) {
                                    setID(Integer.parseInt(messageSplit[1]));
                                    setIDTitle();
                                }
                                if (messageSplit[0].equals("update-online-list")) {
                                    onlineList = new ArrayList<>();
                                    String online = "";
                                    String[] onlineSplit = messageSplit[1].split("-");
                                    for (int i = 0; i < onlineSplit.length; i++) {
                                        onlineList.add(onlineSplit[i]);
                                        online += "Client " + onlineSplit[i] + " đang online\n";
                                    }
                                    jTextArea2.setText(online);
                                    updateCombobox();
                                }
                                if (messageSplit[0].equals("global-message")) {
                                    jTextArea1.setText(jTextArea1.getText() + messageSplit[1] + "\n");
                                    jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
                                }

                            }
//                    os.close();
//                    is.close();
//                    socketOfClient.close();
                        } catch (UnknownHostException e) {
                            return;
                        } catch (IOException e) {
                            return;
                        }
                    }

                }
            };
            thread.run();
        } catch (Exception e) {
        }
    }

    private void updateCombobox() {
        jComboBox1.removeAllItems();
        jComboBox1.addItem("Gửi tất cả");
        String idString = "" + this.id;
        for (String e : onlineList) {
            if (!e.equals(idString)) {
                jComboBox1.addItem("Client " + e);
            }
        }

    }

    private void setIDTitle() {
        this.setTitle("Client " + this.id);
    }

    private void setID(int id) {
        this.id = id;
    }

    public static void write(String message) throws IOException {
        os.write(message);
        os.newLine();
        os.flush();
    }

    void successfulLogin() {
        isLoggedIn = true;
        this.setVisible(true);
    }

    public static void main(String args[]) {
        ClientFrame clientFrame = new ClientFrame();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/send.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/attach-file.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Gửi đến:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setFocusable(false);
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Danh sách online");

        jScrollPane3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextArea2.setEditable(false);
        jTextArea2.setColumns(20);
        jTextArea2.setLineWrap(true);
        jTextArea2.setRows(5);
        jTextArea2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 7, 5, 5));
        jTextArea2.setFocusable(false);
        jScrollPane3.setViewportView(jTextArea2);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setFocusable(false);
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String messageContent = jTextField1.getText();
        if (messageContent.isEmpty()) {
            JOptionPane.showMessageDialog(rootPane, "Bạn chưa nhập tin nhắn");
            return;
        }
        if (jComboBox1.getSelectedIndex() == 0) {
            try {
                write("send-to-global" + "," + messageContent + "," + this.id);
                jTextArea1.setText(jTextArea1.getText() + "Bạn: " + messageContent + "\n");
                jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "Có lỗi xảy ra");
            }
        } else {
            try {
                String[] parner = ((String) jComboBox1.getSelectedItem()).split(" ");
                write("send-to-person" + "," + messageContent + "," + this.id + "," + parner[1]);
                jTextArea1.setText(jTextArea1.getText() + "Bạn (tới Client " + parner[1] + "): " + messageContent + "\n");
                jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "Có lỗi xảy ra");
            }
        }
        jTextField1.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed

    }//GEN-LAST:event_jComboBox1ActionPerformed

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
