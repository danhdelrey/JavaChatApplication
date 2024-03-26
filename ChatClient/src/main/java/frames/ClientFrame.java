/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package frames;

import java.awt.event.KeyEvent;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.swing.JFileChooser;
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
    private String clientUsername;

    public boolean isLoggedIn = false;

    public static DangNhap dangNhap;
    public static FilesFrame filesFrame;

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
        filesFrame = new FilesFrame();
        setUpSocket();

    }

    public void setUpSocket() {
        try {

            thread = new Thread() {
                @Override
                public void run() {
                    if (isLoggedIn == false) {
                        dangNhap = new DangNhap();
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
                                        setClientUsername(dangNhap.getClientUsername());
                                        successfulLogin();
                                        setUpSocket();
                                        System.out.println("dang nhap thanh cong");
                                    } else if (messageSplit[1].equals("failed")) {
                                        JOptionPane.showMessageDialog(rootPane, "Sai tên đăng nhập hoặc mật khẩu!!!");
                                        System.out.println("dang nhap that bai");
                                    }
                                }
                                if (messageSplit[0].equals("signup_status")) {
                                    if (messageSplit[1].equals("successful")) {
                                        JOptionPane.showMessageDialog(rootPane, "Successfully!");
                                        System.out.println("dang ky thanh cong");
                                    } else if (messageSplit[1].equals("failed")) {
                                        JOptionPane.showMessageDialog(rootPane, "Tên tài khoản đã có người sử dụng!");
                                        System.out.println("dang ky that bai");
                                    }
                                }
                                if (messageSplit[0].equals("login_an_online_account")) {
                                    if (messageSplit[1].equals("true")) {
                                        JOptionPane.showMessageDialog(rootPane, "The account is currently online!");
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
                                if (messageSplit[0].equals("get-clientUsername")) {
                                    setClientUsername(messageSplit[1]);
                                    setClientUsernameTitle();
                                }
                                if (messageSplit[0].equals("update-online-list")) {
                                    onlineList = new ArrayList<>();
                                    String online = "";
                                    String[] onlineSplit = messageSplit[1].split("-");
                                    for (int i = 0; i < onlineSplit.length; i++) {
                                        onlineList.add(onlineSplit[i]);

                                        if (onlineSplit[i].equals(clientUsername)) {
                                            online += onlineSplit[i] + " (you)" + "\n";
                                        } else {
                                            online += onlineSplit[i] + "\n";
                                        }

                                    }
                                    jTextArea2.setText(online);
                                    updateCombobox(onlineList);
                                }
                                if (messageSplit[0].equals("global-message")) {

                                    jTextArea1.setText(jTextArea1.getText() + messageSplit[1] + "\n" + getCurrentDateTime() + "\n\n");
                                    jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
                                }
                                if (messageSplit[0].equals("update-file-list")) {
                                    filesFrame.addFileToTable(messageSplit[1], messageSplit[2], Float.parseFloat(messageSplit[3]), messageSplit[4]);
                                    jTextArea1.setText(jTextArea1.getText() + messageSplit[5] + "\n" + getCurrentDateTime() + "\n\n");
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

    private void updateCombobox(List<String> onlineList) {
        jComboBox1.removeAllItems();
        jComboBox1.addItem("All");
        String clientUsername = this.clientUsername;
        for (String e : onlineList) {
            if (!e.equals(clientUsername)) {
                jComboBox1.addItem(e);
            }
        }

    }

    private void setClientUsernameTitle() {
        this.setTitle(this.clientUsername);
    }

    private void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
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
        Send_file_Button = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/send.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jButton2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton2KeyPressed(evt);
            }
        });

        Send_file_Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/attach-file.png"))); // NOI18N
        Send_file_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Send_file_ButtonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("To:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setFocusable(false);
        jComboBox1.setRequestFocusEnabled(false);
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Online list");

        jScrollPane3.setBorder(null);

        jTextArea2.setEditable(false);
        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextArea2.setLineWrap(true);
        jTextArea2.setRows(5);
        jTextArea2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jTextArea2.setFocusable(false);
        jScrollPane3.setViewportView(jTextArea2);

        jScrollPane2.setBorder(null);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 5));
        jTextArea1.setFocusable(false);
        jScrollPane2.setViewportView(jTextArea1);

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/file.png"))); // NOI18N
        jButton1.setText("View files");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chat.png"))); // NOI18N
        jButton3.setText("Save the chat");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Send_file_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Send_file_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)))
                .addGap(8, 8, 8))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    String getCurrentDateTime() {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String currentDateTime = date.format(myFormatObj);
        return currentDateTime;
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String messageContent = jTextField1.getText();

        if (messageContent.isEmpty()) {
            JOptionPane.showMessageDialog(rootPane, "Bạn chưa nhập tin nhắn");
            return;
        }
        if (jComboBox1.getSelectedIndex() == 0) {
            try {
                write("send-to-global" + "," + messageContent + "," + this.clientUsername);
                jTextArea1.setText(jTextArea1.getText() + "You: " + messageContent + "\n" + getCurrentDateTime() + "\n\n");
                jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "Có lỗi xảy ra");
            }
        } else {
            try {
                String selectedUsername = (String) jComboBox1.getSelectedItem();
                write("send-to-person" + "," + messageContent + "," + selectedUsername);
                jTextArea1.setText(jTextArea1.getText() + "You (to " + selectedUsername + "): " + messageContent + "\n" + getCurrentDateTime() + "\n\n");
                jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "Có lỗi xảy ra");
            }
        }
        jTextField1.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed

    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void Send_file_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Send_file_ButtonActionPerformed
        // Hộp thoại chọn file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file để gửi");

        //Hiển thị hộp thoại chọn file và lưu kết quả vào biến result.
        //Kết quả sẽ là một trong các giá trị sau:
        //JFileChooser.APPROVE_OPTION, JFileChooser.CANCEL_OPTION, hoặc JFileChooser.ERROR_OPTION.
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {

            //Lấy file mà người dùng đã chọn từ hộp thoại.
            File selectedFile = fileChooser.getSelectedFile();

            String fileName = selectedFile.getName();

            //Tìm kích thước của file ở dạng mb
            long fileSizeInBytes = selectedFile.length();
            float fileSizeInMB = (float) fileSizeInBytes / (1024 * 1024);

            if (fileSizeInMB <= 5) {
                try {

                    // Đọc dữ liệu từ file
                    byte[] fileData = Files.readAllBytes(selectedFile.toPath());
                    String base64FileData = Base64.getEncoder().encodeToString(fileData);

                    // Gửi tin nhắn chứa dữ liệu file
                    sendFileMessage(base64FileData, fileName, fileSizeInMB, getCurrentDateTime());

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Có lỗi xảy ra khi đọc file");
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "File has a maximum size of 5MB");
            }

        }
    }//GEN-LAST:event_Send_file_ButtonActionPerformed

    private void sendFileMessage(String base64FileData, String fileName, float fileSize, String dateTime) {
        if (jComboBox1.getSelectedIndex() == 0) {
            try {
                write("send-file-to-global" + "," + base64FileData + "," + this.clientUsername + "," + fileName + "," + fileSize + "," + dateTime);
                jTextArea1.setText(jTextArea1.getText() + "You has sent a file: " + fileName + "\n" + getCurrentDateTime() + "\n\n");
                jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "Có lỗi xảy ra");
            }
        } else {
            try {
                String selectedUsername = (String) jComboBox1.getSelectedItem();
                write("send-file-to-person" + "," + base64FileData + "," + this.clientUsername + "," + fileName + "," + fileSize + "," + dateTime + "," + selectedUsername);
                jTextArea1.setText(jTextArea1.getText() + "You has sent a file (to " + selectedUsername + "): " + fileName + "\n" + getCurrentDateTime() + "\n\n");
                jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "Có lỗi xảy ra");
            }
        }
    }

    private void jButton2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton2KeyPressed

    }//GEN-LAST:event_jButton2KeyPressed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String messageContent = jTextField1.getText();

            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedDate = date.format(myFormatObj);

            if (messageContent.isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Bạn chưa nhập tin nhắn");
                return;
            }
            if (jComboBox1.getSelectedIndex() == 0) {
                try {
                    write("send-to-global" + "," + messageContent + "," + this.clientUsername);
                    jTextArea1.setText(jTextArea1.getText() + "You: " + messageContent + "\n" + formattedDate + "\n\n");
                    jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Có lỗi xảy ra");
                }
            } else {
                try {
                    String selectedUsername = (String) jComboBox1.getSelectedItem();
                    write("send-to-person" + "," + messageContent + "," + selectedUsername);
                    jTextArea1.setText(jTextArea1.getText() + "You (to " + selectedUsername + "): " + messageContent + "\n" + formattedDate + "\n\n");
                    jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Có lỗi xảy ra");
                }
            }
            jTextField1.setText("");
        }
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        filesFrame.setVisible(true);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Send_file_Button;
    private javax.swing.JButton jButton1;
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
