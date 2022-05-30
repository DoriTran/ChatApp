package Main.Panel;

import Main.GridBagStatus;
import Main.Main;
import TableButton.TableButtonRenderer;
import TableButton.TableRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class FilePanel extends JPanel {
    // Component
    private String[] colName = {"FileName"};

    // Current toUser
    private String toUser;

    // Receive Panel
    private JScrollPane receiveScrollPane;
    public HashMap<String, JTable> fileReceiveTables;

    // Send Panel
    private JScrollPane sendScrollPane;
    public HashMap<String, JTable> fileSendTables;
    public HashMap<String, ArrayList<String>> fileSendPaths;

    // Action listener
    private ActionListener serverScreenListener;

    public FilePanel(ActionListener serverScreenListener) {
        // Set ActionListener
        this.serverScreenListener = serverScreenListener;

        // HashTable
        fileReceiveTables = new HashMap<>();
        fileSendTables = new HashMap<>();
        fileSendPaths = new HashMap<>();

        // Set Layout
        this.setLayout(new GridBagLayout());
        GridBagStatus status = new GridBagStatus();

        // JScrollPanel contain receiveTable
        receiveScrollPane = new JScrollPane(getDefaultReceiveTable(),
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        receiveScrollPane.setBorder(BorderFactory.createTitledBorder("Quản lý file nhận"));
        this.add(receiveScrollPane, status.setGrid(1,1).setInsets(0));

        // JScrollPanel contain sendTable
        sendScrollPane = new JScrollPane(getDefaultSendTable(),
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sendScrollPane.setBorder(BorderFactory.createTitledBorder("Quản lý file gửi"));
        this.add(sendScrollPane, status.setGrid(1,2).setInsets(0));
    }

    // Change user
    public void changeUser(String UserName) {
        // Set toUser
        this.toUser = UserName;

        // Create if null
        if (!fileReceiveTables.containsKey(UserName))
            fileReceiveTables.put(UserName, getDefaultReceiveTable());
        if (!fileSendTables.containsKey(UserName))
            fileSendTables.put(UserName, getDefaultSendTable());
        if (!fileSendPaths.containsKey(UserName))
            fileSendPaths.put(UserName, new ArrayList<String>());

        // Update UI
        receiveScrollPane.setViewportView(fileReceiveTables.get(UserName));

        sendScrollPane.setViewportView(fileSendTables.get(UserName));
    }
    // Offline user
    public void userFileOffline(String UserName) {
        fileReceiveTables.remove(UserName);
        fileSendTables.remove(UserName);
        fileSendPaths.remove(UserName);
        receiveScrollPane.setViewportView(getDefaultReceiveTable());
        sendScrollPane.setViewportView(getDefaultSendTable());
        blink();
    }

    // Blink to update
    private void blink() {
        receiveScrollPane.setVisible(false);
        receiveScrollPane.setVisible(true);
        sendScrollPane.setVisible(false);
        sendScrollPane.setVisible(true);
    }

    // Add new file
    public void addSendFile(String fileName, String filePath) {
        if (!fileReceiveTables.containsKey(toUser))
            fileSendTables.put(toUser, getDefaultSendTable());
        // Add path
        fileSendPaths.get(toUser).add(filePath);
        // Add UI
        JTable sendTable = (JTable) sendScrollPane.getViewport().getView();
        DefaultTableModel model = (DefaultTableModel) sendTable.getModel();
        model.addRow(new Object[]{fileName});
        Main.socketManager.SendFileNotification(toUser, fileName);
        blink();
    }
    public void addReceiveFile(String from, String fileName) {
        if (!fileReceiveTables.containsKey(from))
            fileReceiveTables.put(from, getDefaultReceiveTable());
        DefaultTableModel model = (DefaultTableModel) fileReceiveTables.get(from).getModel();
        model.addRow(new Object[]{fileName});
        blink();
    }

    // Request download
    public void requestDownload() {
        System.out.println("File receive table: " + fileReceiveTables.get(toUser).getSelectedRow());
        Main.socketManager.SendFileDownloadRequest(toUser, fileReceiveTables.get(toUser).getSelectedRow());
    }

    // Response download
    public String getRequestedFilePath(String from, Integer fileIndex) {
        return fileSendPaths.get(from).get(fileIndex);
    }
    public String getRequestedFileName(String from, Integer fileIndex) {
        return fileSendTables.get(from).getValueAt(fileIndex, 0).toString();
    }

    // Table formater
    public JTable getDefaultReceiveTable() {
        // Table
        JTable table = new JTable();

        // Update table
        table.setModel(new DefaultTableModel(null, colName) {
            public boolean isCellEditable(int row, int column)
            {
                return (column == 0);
            }
        });
        table.getTableHeader().setUI(null);

        // Table Button
        table.getColumnModel().getColumn(0).setCellRenderer(new TableButtonRenderer());
        table.getColumnModel().getColumn(0).setCellEditor(new TableRenderer(new JCheckBox(), serverScreenListener, "download"));

        // Table width
        table.getColumnModel().getColumn(0).setPreferredWidth(150);

        // Table row height
        table.setRowHeight(40);

        // Table width
        table.setSize(150, 290);
        table.setPreferredScrollableViewportSize(new Dimension(table.getPreferredSize().width, 290));

        // Table font
        table.setFont(new Font("Tahoma", Font.PLAIN, 16));

        // Table Edit
        table.setDefaultEditor(Object.class, null);

        // Return
        return table;
    }
    public JTable getDefaultSendTable() {
        // Table
        JTable table = new JTable();

        // Update table
        table.setModel(new DefaultTableModel(null, colName));
        table.getTableHeader().setUI(null);

        // Table width
        table.getColumnModel().getColumn(0).setPreferredWidth(150);

        // Table row height
        table.setRowHeight(40);

        // Table width
        table.setSize(150, 290);
        table.setPreferredScrollableViewportSize(new Dimension(table.getPreferredSize().width, 290));

        // Table font
        table.setFont(new Font("Tahoma", Font.PLAIN, 16));

        // Table Edit
        table.setDefaultEditor(Object.class, null);

        // Return
        return table;
    }
}
