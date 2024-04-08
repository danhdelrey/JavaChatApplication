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
        messageTextArea.setEditable(false);
        onlineListTextarea.setEditable(false);
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
                                        JOptionPane.showMessageDialog(rootPane, "Wrong username or password!!!");

                                    }
                                }
                                if (messageSplit[0].equals("signup-status")) {
                                    if (messageSplit[1].equals("successful")) {
                                        JOptionPane.showMessageDialog(rootPane, "Successfully!");

                                    } else if (messageSplit[1].equals("failed")) {
                                        JOptionPane.showMessageDialog(rootPane, "The username is already in use!");

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
                                    onlineListTextarea.setText(online);
                                    updateCombobox(onlineList);
                                }
                                if (messageSplit[0].equals("global-message")) {

                                    messageTextArea.setText(messageTextArea.getText() + messageSplit[1] + "\n" + getCurrentDateTime() + "\n\n");
                                    messageTextArea.setCaretPosition(messageTextArea.getDocument().getLength());
                                }
                                if (messageSplit[0].equals("update-file-list")) {
                                    filesFrame.addFileToTable(messageSplit[1], messageSplit[2], Float.parseFloat(messageSplit[3]), messageSplit[4]);
                                    messageTextArea.setText(messageTextArea.getText() + messageSplit[5] + "\n" + getCurrentDateTime() + "\n\n");
                                    messageTextArea.setCaretPosition(messageTextArea.getDocument().getLength());

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
        receiverComboBox.removeAllItems();
        receiverComboBox.addItem("All");
        String clientUsername = this.clientUsername;
        for (String e : onlineList) {
            if (!e.equals(clientUsername)) {
                receiverComboBox.addItem(e);
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

        messageInput = new javax.swing.JTextField();
        sentMessageButton = new javax.swing.JButton();
        sendFileButton = new javax.swing.JButton();
        toUserLabel = new javax.swing.JLabel();
        receiverComboBox = new javax.swing.JComboBox<>();
        onlineListLabel = new javax.swing.JLabel();
        onlineListScrollPane = new javax.swing.JScrollPane();
        onlineListTextarea = new javax.swing.JTextArea();
        messageScrollPane = new javax.swing.JScrollPane();
        messageTextArea = new javax.swing.JTextArea();
        viewFileButton = new javax.swing.JButton();
        saveTheChatButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        messageInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                messageInputKeyPressed(evt);
            }
        });

        sentMessageButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/send.png"))); // NOI18N
        sentMessageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sentMessageButtonActionPerformed(evt);
            }
        });
        sentMessageButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sentMessageButtonKeyPressed(evt);
            }
        });

        sendFileButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/attach-file.png"))); // NOI18N
        sendFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendFileButtonActionPerformed(evt);
            }
        });

        toUserLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        toUserLabel.setText("To:");

        receiverComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        receiverComboBox.setFocusable(false);
        receiverComboBox.setRequestFocusEnabled(false);
        receiverComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                receiverComboBoxActionPerformed(evt);
            }
        });

        onlineListLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        onlineListLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        onlineListLabel.setText("Online list");

        onlineListScrollPane.setBorder(null);

        onlineListTextarea.setEditable(false);
        onlineListTextarea.setColumns(20);
        onlineListTextarea.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        onlineListTextarea.setLineWrap(true);
        onlineListTextarea.setRows(5);
        onlineListTextarea.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        onlineListTextarea.setFocusable(false);
        onlineListScrollPane.setViewportView(onlineListTextarea);

        messageScrollPane.setBorder(null);

        messageTextArea.setEditable(false);
        messageTextArea.setColumns(20);
        messageTextArea.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        messageTextArea.setLineWrap(true);
        messageTextArea.setRows(5);
        messageTextArea.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 5));
        messageTextArea.setFocusable(false);
        messageScrollPane.setViewportView(messageTextArea);

        viewFileButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        viewFileButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/file.png"))); // NOI18N
        viewFileButton.setText("View files");
        viewFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewFileButtonActionPerformed(evt);
            }
        });

        saveTheChatButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        saveTheChatButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chat.png"))); // NOI18N
        saveTheChatButton.setText("Save the chat");
        saveTheChatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveTheChatButtonActionPerformed(evt);
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
                        .addComponent(toUserLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(receiverComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(saveTheChatButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                    .addComponent(onlineListScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(viewFileButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(onlineListLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(messageInput, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendFileButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sentMessageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(messageScrollPane))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(onlineListLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(onlineListScrollPane)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(viewFileButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveTheChatButton))
                    .addComponent(messageScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(messageInput, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sentMessageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendFileButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(receiverComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(toUserLabel)))
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

    private void sentMessageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sentMessageButtonActionPerformed
        String messageContent = messageInput.getText();

        if (messageContent.isEmpty()) {
            JOptionPane.showMessageDialog(rootPane, "No message has been entered");
            return;
        }
        if (receiverComboBox.getSelectedIndex() == 0) {
            try {
                write("send-to-global" + splitterString + messageContent + splitterString + this.clientUsername);
                messageTextArea.setText(messageTextArea.getText() + "You: " + messageContent + "\n" + getCurrentDateTime() + "\n\n");
                messageTextArea.setCaretPosition(messageTextArea.getDocument().getLength());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "An error occurred");
            }
        } else {
            try {
                String selectedUsername = (String) receiverComboBox.getSelectedItem();
                write("send-to-person" + splitterString + messageContent + splitterString + selectedUsername);
                messageTextArea.setText(messageTextArea.getText() + "You (to " + selectedUsername + "): " + messageContent + "\n" + getCurrentDateTime() + "\n\n");
                messageTextArea.setCaretPosition(messageTextArea.getDocument().getLength());

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "An error occurred");
            }
        }
        messageInput.setText("");
    }//GEN-LAST:event_sentMessageButtonActionPerformed

    private void receiverComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_receiverComboBoxActionPerformed

    }//GEN-LAST:event_receiverComboBoxActionPerformed

    private void sendFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendFileButtonActionPerformed
        // Hộp thoại chọn file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose files to send");

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
                    JOptionPane.showMessageDialog(rootPane, "Encountered an error while attempting to read the file.");
                }
            } else {
                JOptionPane.showMessageDialog(rootPane, "File has a maximum size of 5MB");
            }

        }
    }//GEN-LAST:event_sendFileButtonActionPerformed

    private void sendFileMessage(String base64FileData, String fileName, float fileSize, String dateTime) {
        if (receiverComboBox.getSelectedIndex() == 0) {
            try {
                write("send-file-to-global" + splitterString + base64FileData + splitterString + this.clientUsername + splitterString + fileName + splitterString + fileSize + splitterString + dateTime);
                messageTextArea.setText(messageTextArea.getText() + "You has sent a file: " + fileName + "\n" + getCurrentDateTime() + "\n\n");
                messageTextArea.setCaretPosition(messageTextArea.getDocument().getLength());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "An error occurred");
            }
        } else {
            try {
                String selectedUsername = (String) receiverComboBox.getSelectedItem();
                write("send-file-to-person" + splitterString + base64FileData + splitterString + this.clientUsername + splitterString + fileName + splitterString + fileSize + splitterString + dateTime + splitterString + selectedUsername);
                messageTextArea.setText(messageTextArea.getText() + "You has sent a file (to " + selectedUsername + "): " + fileName + "\n" + getCurrentDateTime() + "\n\n");
                messageTextArea.setCaretPosition(messageTextArea.getDocument().getLength());

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "An error occurred");
            }
        }
    }

    private void sentMessageButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sentMessageButtonKeyPressed

    }//GEN-LAST:event_sentMessageButtonKeyPressed

    private void messageInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_messageInputKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String messageContent = messageInput.getText();

            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedDate = date.format(myFormatObj);

            if (messageContent.isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "No message has been entered");
                return;
            }
            if (receiverComboBox.getSelectedIndex() == 0) {
                try {
                    write("send-to-global" + splitterString + messageContent + splitterString + this.clientUsername);
                    messageTextArea.setText(messageTextArea.getText() + "You: " + messageContent + "\n" + formattedDate + "\n\n");
                    messageTextArea.setCaretPosition(messageTextArea.getDocument().getLength());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(rootPane, "An error occurred");
                }
            } else {
                try {
                    String selectedUsername = (String) receiverComboBox.getSelectedItem();
                    write("send-to-person" + splitterString + messageContent + splitterString + selectedUsername);
                    messageTextArea.setText(messageTextArea.getText() + "You (to " + selectedUsername + "): " + messageContent + "\n" + formattedDate + "\n\n");
                    messageTextArea.setCaretPosition(messageTextArea.getDocument().getLength());

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(rootPane, "An error occurred");
                }
            }
            messageInput.setText("");
        }
    }//GEN-LAST:event_messageInputKeyPressed

    private void viewFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewFileButtonActionPerformed
        filesFrame.setVisible(true);

    }//GEN-LAST:event_viewFileButtonActionPerformed

    private void saveTheChatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveTheChatButtonActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Choose a directory to save");

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String fileName = "backupchat.txt";
            String fileData = messageTextArea.getText();
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

    }//GEN-LAST:event_saveTheChatButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField messageInput;
    private javax.swing.JScrollPane messageScrollPane;
    private javax.swing.JTextArea messageTextArea;
    private javax.swing.JLabel onlineListLabel;
    private javax.swing.JScrollPane onlineListScrollPane;
    private javax.swing.JTextArea onlineListTextarea;
    private javax.swing.JComboBox<String> receiverComboBox;
    private javax.swing.JButton saveTheChatButton;
    private javax.swing.JButton sendFileButton;
    private javax.swing.JButton sentMessageButton;
    private javax.swing.JLabel toUserLabel;
    private javax.swing.JButton viewFileButton;
    // End of variables declaration//GEN-END:variables
}
