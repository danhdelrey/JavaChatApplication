package frames;

import java.awt.event.KeyEvent;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class ClientFrame extends javax.swing.JFrame {

    private Thread thread;
    public static BufferedWriter os;
    public static BufferedReader is;
    private Socket socketOfClient;
    private List<String> onlineList;
    public static String clientUsername;

    public boolean isLoggedIn = false;

    public static LoginFrame dangNhap;
    public static FilesFrame filesFrame;

    public static String splitterString = ":,;,:";

    public ClientFrame() {
        initComponents();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(false);
        Content_Message_TextArea.setEditable(false);
        Online_list_TextArea.setEditable(false);
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
                        dangNhap = new LoginFrame();
                        try {
                            // Gửi yêu cầu kết nối tới Server đang lắng nghe
                            // trên máy 'localhost' cổng 7777.
                            if (socketOfClient == null) {
                                socketOfClient = new Socket("localhost", 7777);
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
                                String[] messageSplit = message.split(splitterString);
                                if (messageSplit[0].equals("login-status")) {
                                    if (messageSplit[1].equals("successful")) {
                                        dangNhap.setVisible(false);
                                        setClientUsername(dangNhap.getClientUsername());
                                        successfulLogin();
                                        setUpSocket();

                                    } else if (messageSplit[1].equals("failed")) {
                                        JOptionPane.showMessageDialog(rootPane, "Sai tên đăng nhập hoặc mật khẩu!!!");

                                    }
                                }
                                if (messageSplit[0].equals("signup-status")) {
                                    if (messageSplit[1].equals("successful")) {
                                        JOptionPane.showMessageDialog(rootPane, "Successfully!");

                                    } else if (messageSplit[1].equals("failed")) {
                                        JOptionPane.showMessageDialog(rootPane, "Tên tài khoản đã có người sử dụng!");

                                    }
                                }
                                if (messageSplit[0].equals("login-an-online-account")) {
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
                                String[] messageSplit = message.split(splitterString);
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
                                    Online_list_TextArea.setText(online);
                                    updateCombobox(onlineList);
                                }
                                if (messageSplit[0].equals("global-message")) {

                                    Content_Message_TextArea.setText(Content_Message_TextArea.getText() + messageSplit[1] + "\n" + getCurrentDateTime() + "\n\n");
                                    Content_Message_TextArea.setCaretPosition(Content_Message_TextArea.getDocument().getLength());
                                }
                                if (messageSplit[0].equals("update-file-list")) {
                                    filesFrame.addFileToTable(messageSplit[1], messageSplit[2], Float.parseFloat(messageSplit[3]), messageSplit[4]);
                                    Content_Message_TextArea.setText(Content_Message_TextArea.getText() + messageSplit[5] + "\n" + getCurrentDateTime() + "\n\n");
                                    Content_Message_TextArea.setCaretPosition(Content_Message_TextArea.getDocument().getLength());

                                }
                                if (messageSplit[0].equals("get-files")) {
                                    String messages[] = message.split("&:::::&");
                                    String fileData[] = messages[1].split(";:::::;");
                                    String fileNames[] = messages[2].split(";:::::;");
                                    String pathToSave = messages[3];
                                    for (int i = 0; i < fileData.length; i++) {
                                        saveFileToClient(fileData[i], fileNames[i], pathToSave);
                                    }
                                    filesFrame.showMessageDialog("Files are saved successfully.");
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
        clientUsername_ComboBox.removeAllItems();
        clientUsername_ComboBox.addItem("All");
        String clientUsername = this.clientUsername;
        for (String e : onlineList) {
            if (!e.equals(clientUsername)) {
                clientUsername_ComboBox.addItem(e);
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

    void saveFileToClient(String base64FileData, String fileName, String pathToSave) {
        byte[] fileData = Base64.getDecoder().decode(base64FileData);

        //Tạo đường dẫn để lưu file vào client
        Path filePath = Paths.get(pathToSave, fileName);

        try {
            // Lưu file vào đường dẫn đã tạo
            Files.write(filePath, fileData);
        } catch (IOException ex) {

        }
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

        Send_Message_TextField = new javax.swing.JTextField();
        Send_picture_button = new javax.swing.JButton();
        Send_file_Button = new javax.swing.JButton();
        Receiver_Label = new javax.swing.JLabel();
        clientUsername_ComboBox = new javax.swing.JComboBox<>();
        Online_list_Label = new javax.swing.JLabel();
        Online_list_ScrollPane = new javax.swing.JScrollPane();
        Online_list_TextArea = new javax.swing.JTextArea();
        Content_Message_ScrollPane = new javax.swing.JScrollPane();
        Content_Message_TextArea = new javax.swing.JTextArea();
        View_file_button = new javax.swing.JButton();
        Save_chat_button = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        Send_Message_TextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        Send_picture_button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/send.png"))); // NOI18N
        Send_picture_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        Send_picture_button.addKeyListener(new java.awt.event.KeyAdapter() {
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

        Receiver_Label.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Receiver_Label.setText("To:");

        clientUsername_ComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        clientUsername_ComboBox.setFocusable(false);
        clientUsername_ComboBox.setRequestFocusEnabled(false);
        clientUsername_ComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        Online_list_Label.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Online_list_Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Online_list_Label.setText("Online list");

        Online_list_ScrollPane.setBorder(null);

        Online_list_TextArea.setEditable(false);
        Online_list_TextArea.setColumns(20);
        Online_list_TextArea.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Online_list_TextArea.setLineWrap(true);
        Online_list_TextArea.setRows(5);
        Online_list_TextArea.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        Online_list_TextArea.setFocusable(false);
        Online_list_ScrollPane.setViewportView(Online_list_TextArea);

        Content_Message_ScrollPane.setBorder(null);

        Content_Message_TextArea.setEditable(false);
        Content_Message_TextArea.setColumns(20);
        Content_Message_TextArea.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Content_Message_TextArea.setLineWrap(true);
        Content_Message_TextArea.setRows(5);
        Content_Message_TextArea.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 5));
        Content_Message_TextArea.setFocusable(false);
        Content_Message_ScrollPane.setViewportView(Content_Message_TextArea);

        View_file_button.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        View_file_button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/file.png"))); // NOI18N
        View_file_button.setText("View files");
        View_file_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        Save_chat_button.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Save_chat_button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chat.png"))); // NOI18N
        Save_chat_button.setText("Save the chat");
        Save_chat_button.addActionListener(new java.awt.event.ActionListener() {
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
                        .addComponent(Receiver_Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clientUsername_ComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(Save_chat_button, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                    .addComponent(Online_list_ScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(View_file_button, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Online_list_Label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Send_Message_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Send_file_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Send_picture_button, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(Content_Message_ScrollPane))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Online_list_Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Online_list_ScrollPane)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(View_file_button)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Save_chat_button))
                    .addComponent(Content_Message_ScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Send_Message_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Send_picture_button, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Send_file_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(clientUsername_ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Receiver_Label)))
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
        String messageContent = Send_Message_TextField.getText();

        if (messageContent.isEmpty()) {
            JOptionPane.showMessageDialog(rootPane, "Bạn chưa nhập tin nhắn");
            return;
        }
        if (clientUsername_ComboBox.getSelectedIndex() == 0) {
            try {
                write("send-to-global" + splitterString + messageContent + splitterString + this.clientUsername);
                Content_Message_TextArea.setText(Content_Message_TextArea.getText() + "You: " + messageContent + "\n" + getCurrentDateTime() + "\n\n");
                Content_Message_TextArea.setCaretPosition(Content_Message_TextArea.getDocument().getLength());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "Có lỗi xảy ra");
            }
        } else {
            try {
                String selectedUsername = (String) clientUsername_ComboBox.getSelectedItem();
                write("send-to-person" + splitterString + messageContent + splitterString + selectedUsername);
                Content_Message_TextArea.setText(Content_Message_TextArea.getText() + "You (to " + selectedUsername + "): " + messageContent + "\n" + getCurrentDateTime() + "\n\n");
                Content_Message_TextArea.setCaretPosition(Content_Message_TextArea.getDocument().getLength());

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "Có lỗi xảy ra");
            }
        }
        Send_Message_TextField.setText("");
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
        if (clientUsername_ComboBox.getSelectedIndex() == 0) {
            try {
                write("send-file-to-global" + splitterString + base64FileData + splitterString + this.clientUsername + splitterString + fileName + splitterString + fileSize + splitterString + dateTime);
                Content_Message_TextArea.setText(Content_Message_TextArea.getText() + "You has sent a file: " + fileName + "\n" + getCurrentDateTime() + "\n\n");
                Content_Message_TextArea.setCaretPosition(Content_Message_TextArea.getDocument().getLength());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "Có lỗi xảy ra");
            }
        } else {
            try {
                String selectedUsername = (String) clientUsername_ComboBox.getSelectedItem();
                write("send-file-to-person" + splitterString + base64FileData + splitterString + this.clientUsername + splitterString + fileName + splitterString + fileSize + splitterString + dateTime + splitterString + selectedUsername);
                Content_Message_TextArea.setText(Content_Message_TextArea.getText() + "You has sent a file (to " + selectedUsername + "): " + fileName + "\n" + getCurrentDateTime() + "\n\n");
                Content_Message_TextArea.setCaretPosition(Content_Message_TextArea.getDocument().getLength());

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "Có lỗi xảy ra");
            }
        }
    }

    private void jButton2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton2KeyPressed

    }//GEN-LAST:event_jButton2KeyPressed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String messageContent = Send_Message_TextField.getText();

            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedDate = date.format(myFormatObj);

            if (messageContent.isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Bạn chưa nhập tin nhắn");
                return;
            }
            if (clientUsername_ComboBox.getSelectedIndex() == 0) {
                try {
                    write("send-to-global" + splitterString + messageContent + splitterString + this.clientUsername);
                    Content_Message_TextArea.setText(Content_Message_TextArea.getText() + "You: " + messageContent + "\n" + formattedDate + "\n\n");
                    Content_Message_TextArea.setCaretPosition(Content_Message_TextArea.getDocument().getLength());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Có lỗi xảy ra");
                }
            } else {
                try {
                    String selectedUsername = (String) clientUsername_ComboBox.getSelectedItem();
                    write("send-to-person" + splitterString + messageContent + splitterString + selectedUsername);
                    Content_Message_TextArea.setText(Content_Message_TextArea.getText() + "You (to " + selectedUsername + "): " + messageContent + "\n" + formattedDate + "\n\n");
                    Content_Message_TextArea.setCaretPosition(Content_Message_TextArea.getDocument().getLength());

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Có lỗi xảy ra");
                }
            }
            Send_Message_TextField.setText("");
        }
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        filesFrame.setVisible(true);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Choose a directory to save");

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String fileName = "backupchat.txt";
            String fileData = Content_Message_TextArea.getText();
            String selectedPath = fileChooser.getSelectedFile().getPath() + "\\" + fileName;

            File file = new File(selectedPath);
            try {
                FileWriter fileWriter = new FileWriter(selectedPath);
                fileWriter.write(fileData);
                fileWriter.close();
                JOptionPane.showMessageDialog(rootPane, "Saved successfully!");
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Send_file_Button;
    private javax.swing.JButton View_file_button;
    private javax.swing.JButton Send_picture_button;
    private javax.swing.JButton Save_chat_button;
    private javax.swing.JComboBox<String> clientUsername_ComboBox;
    private javax.swing.JLabel Online_list_Label;
    private javax.swing.JLabel Receiver_Label;
    private javax.swing.JScrollPane Content_Message_ScrollPane;
    private javax.swing.JScrollPane Online_list_ScrollPane;
    private javax.swing.JTextArea Content_Message_TextArea;
    private javax.swing.JTextArea Online_list_TextArea;
    private javax.swing.JTextField Send_Message_TextField;
    // End of variables declaration//GEN-END:variables
}
