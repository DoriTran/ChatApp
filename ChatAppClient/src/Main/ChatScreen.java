package Main;

import Main.Panel.*;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatScreen extends JFrame implements ActionListener {
    String UserName = "Unknown";

    // Panel
    InfoPanel infoPanel; // Top

    OnlineUserPanel onlineUserPanel; // Left
    ChatPanel chatPanel; // Mid
    FilePanel filePanel; // Right

    InputMessagePanel inputMessagePanel; // Bottom

    // FileChooser
    JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView());

    // Constructor
    public ChatScreen(String UserName) {
        // Main Split Panel
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

        this.setTitle("Client - Chat");
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /* Info panel function (Top) */
    public void updateInfoServer() { infoPanel.updateData(); }

    /* Online user panel function (Left) */
    public void updateOnlineUser() {
        onlineUserPanel.updateTable();
    }

    /* Conversation panel function (Mid) */

    /* File panel function (Right) */

    /* Input panel function (Bottom) */

    // Close Chatscreen
    public void closeresetData() {
        setVisible(false);
        dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "test":
                System.out.println("Test complete!");
        }
    }
}
