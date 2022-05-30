package Main;

import Main.Panel.*;
import Networking.SocketManager;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatScreen extends JFrame implements ActionListener {
    // Data
    private String UserName = "Unknown";

    // Panel
    private InfoPanel infoPanel; // Top

    private OnlineUserPanel onlineUserPanel; // Left
    private ChatPanel chatPanel; // Mid
    private FilePanel filePanel; // Right

    private InputMessagePanel inputMessagePanel; // Bottom

    // FileChooser
    private JFileChooser fileChooser;
    private JFileChooser folderChooser;

    // Constructor
    public ChatScreen(String UserName) {
        // FileChooser
        fileChooser = new JFileChooser(FileSystemView.getFileSystemView());
        folderChooser = new JFileChooser(FileSystemView.getFileSystemView());
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagStatus status = new GridBagStatus();

        /* Top Section */
        infoPanel = new InfoPanel(this);
        mainPanel.add(infoPanel, status.setGrid(1,1).setWitdh(3).setFill(GridBagConstraints.BOTH));

        /* SplitBottom Section */
        // Left Panel
        onlineUserPanel = new OnlineUserPanel(this);
        mainPanel.add(onlineUserPanel, status.setGrid(1,2).setWitdh(1).setHeight(2));

        // Mid Panel
        chatPanel = new ChatPanel();
        mainPanel.add(chatPanel, status.setGrid(2,2).setWitdh(1).setHeight(1));

        // Right Panel
        filePanel = new FilePanel(this);
        mainPanel.add(filePanel, status.setGrid(3,2).setWitdh(1).setHeight(2));

        /* Bottom Panel */
        inputMessagePanel = new InputMessagePanel(this);
        inputMessagePanel.setPreferredSize(new Dimension(600,60));
        mainPanel.add(inputMessagePanel, status.setGrid(2,3).setWitdh(1).setHeight(1));

        // Frame
        this.setTitle("Client - Chat");
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }

    /* Info panel function (Top) */
    public void updateInfoServer() { infoPanel.updateData(); }

    /* Online user panel function (Left) */
    public void updateOnlineUser() {
        onlineUserPanel.updateTable();
    }

    /* Chat panel function (Mid) */
    public void addNewMessage(String from, String message) {
        chatPanel.addOtherUserNewMassage(from, message);
    }
    public void offlineMessage(String whoOffline) {
        chatPanel.userChatOffline(whoOffline);
    }

    /* File panel function (Right) */
    public void addNewFile(String from, String fileName) {
        filePanel.addReceiveFile(from, fileName);
    }
    public void offlineFile(String whoOffline) {
        filePanel.userFileOffline(whoOffline);
    }
    public String getRequestedFilePath(String whoRequested, Integer fileIndex) {
        return filePanel.getRequestedFilePath(whoRequested, fileIndex);
    }
    public String getRequestedFileName(String whoRequested, Integer fileIndex) {
        return filePanel.getRequestedFileName(whoRequested, fileIndex);
    }

    /* Input panel function (Bottom) */
    public void setInputMessagePanelEnable(boolean enable) {
        this.inputMessagePanel.setEnableButton(enable);
    }

    // Close Chatscreen
    public void closeresetData() {
        Main.socketManager = null;
        setVisible(false);
        dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "exit": {
                Main.socketManager.Logout();
                closeresetData();
                break;
            }
            // OnlineUserPanel event
            case "chat": {
                chatPanel.changeUserConversation(onlineUserPanel.getSelectedUser());
                filePanel.changeUser(onlineUserPanel.getSelectedUser());
                inputMessagePanel.setEnableButton(true);
                break;
            }
            // InputMessagePanel event
            case "send": {
                chatPanel.addMyNewMessage(inputMessagePanel.getInputMessage());
                inputMessagePanel.clearInputMessage();
                break;
            }
            case "file": {
                fileChooser.showOpenDialog(null);
                filePanel.addSendFile(fileChooser.getSelectedFile().getName(), fileChooser.getSelectedFile().toString());
                break;
            }
            // FilePanel event
            case "download": {
                folderChooser.showOpenDialog(null);
                Main.socketManager.downloadToPath = folderChooser.getSelectedFile().toString();
                filePanel.requestDownload();
                break;
            }
        }
    }
}
