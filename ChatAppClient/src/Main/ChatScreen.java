package Main;

import Main.Panel.*;
import Networking.SocketManager;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
            case "emoji": {
                JDialog emojiDialog = new JDialog();
                Object[][] emojiMatrix = new Object[30][8];
                int emojiCode = 0x1F601;
                for (int i = 0; i < 26; i++) {
                    for (int j = 0; j < 8; j++)
                        emojiMatrix[i][j] = new String(Character.toChars(emojiCode++));
                }

                JTable emojiTable = new JTable();
                emojiTable.setModel(new DefaultTableModel(emojiMatrix, new String[] { "", "", "", "", "", "", "", "" }) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                });
                emojiTable.setFont(new Font("Dialog", Font.PLAIN, 20));
                emojiTable.setShowGrid(false);
                emojiTable.setIntercellSpacing(new Dimension(0, 0));
                emojiTable.setRowHeight(30);
                emojiTable.getTableHeader().setVisible(false);

                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
                for (int i = 0; i < emojiTable.getColumnCount(); i++) {
                    emojiTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                    emojiTable.getColumnModel().getColumn(i).setMaxWidth(30);
                }
                emojiTable.setCellSelectionEnabled(true);
                emojiTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                emojiTable.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        inputMessagePanel.addEmoji(
                                emojiTable.getValueAt(emojiTable.rowAtPoint(e.getPoint()), emojiTable.columnAtPoint(e.getPoint())).toString());
                    }
                });

                emojiTable.setPreferredScrollableViewportSize(new Dimension(emojiTable.getPreferredSize().width, 200));
                JScrollPane srollpane = new JScrollPane(emojiTable,
                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

                emojiDialog.setContentPane(srollpane);
                emojiDialog.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
                emojiDialog.pack();
                emojiDialog.setLocationRelativeTo(Main.chatScreen.chatPanel);
                emojiDialog.setVisible(true);
                break;
            }
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
