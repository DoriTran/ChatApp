package Main;

import Data.DataManager;
import Data.ServerInfo;
import Networking.SocketManager;
import TableButton.TableButtonRenderer;
import TableButton.TableRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ServerScreen extends JFrame implements ActionListener {
    // JLabel
    private JLabel lb_ServerIP;
    private JLabel lb_ServerIPValue;
    private JLabel lb_CurrentServerOpen;
    private JLabel lb_CurrentServerOpenValue;

    // Button
    private JButton btn_CreateServer;

    // Table server
    private String[] columnServerTable = { "ServerName", "ServerPort", "Trạng thái", "Open/Close", "Delete" };
    private JTable tableServer;

    // Table client
    private String[] columnClientTable = { "UserName" };
    private JTable tableClient;

    // JScrollPanel
    private JScrollPane serverScrollPane;
    private JScrollPane clientScrollPane;

    // Constructor
    public ServerScreen() {
        // Read Save data
        try {
            DataManager.loadSaves();
            SocketManager.initServerIP();
        }
        catch (IOException e) {
            System.out.println("Unable to open server data file");
        }

        // Init content
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent window) {
                try {
                    DataManager.uploadSaves();
                }
                catch (IOException e) {
                    System.out.println("Error saving server data file");
                }
            }
        });

        // Main Panel
        JPanel serverPanel = new JPanel(new GridBagLayout());
        GridBagStatus status = new GridBagStatus();

        // Server table
        tableServer = new JTable();
        updateServerTable();

        // JScrollPanel contain ServerTable
        serverScrollPane = new JScrollPane(tableServer,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        serverScrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách server đã tạo: "));
        serverPanel.add(serverScrollPane, status.setGrid(1,1).setInsets(10,10,10,5).setHeight(5));

        // Client table
        tableClient = new JTable();
        updateClientTable();

        // JScrollPanel contain ClientTable
        clientScrollPane = new JScrollPane(tableClient,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        clientScrollPane.setBorder(BorderFactory.createTitledBorder("Số client đang kết nối: 0 "));
        serverPanel.add(clientScrollPane, status.setGrid(2,1).setInsets(10,5,10,5).setHeight(5));

        // JLabel
        lb_ServerIP = new JLabel("ServerIP");
        lb_ServerIP.setFont(new Font("Tahoma", Font.BOLD, 22));
        serverPanel.add(lb_ServerIP, status.setGrid(3,1).setInsets(10,10,10,10).setHeight(1));

        lb_ServerIPValue = new JLabel(SocketManager.serverIP);
        lb_ServerIPValue.setFont(new Font("Tahoma", Font.PLAIN, 16));
        serverPanel.add(lb_ServerIPValue, status.setGrid(3,2).setInsets(10,10,10,10).setHeight(1));

        lb_CurrentServerOpen = new JLabel("Running server");
        lb_CurrentServerOpen.setFont(new Font("Tahoma", Font.BOLD, 22));
        serverPanel.add(lb_CurrentServerOpen, status.setGrid(3,3).setInsets(10,10,10,10).setHeight(1));

        lb_CurrentServerOpenValue = new JLabel();
        lb_CurrentServerOpenValue.setFont(new Font("Tahoma", Font.PLAIN, 16));
        serverPanel.add(lb_CurrentServerOpenValue, status.setGrid(3,4).setInsets(10,10,10,10).setHeight(1));

        // Button
        this.btn_CreateServer = new JButton("Create Server");
        this.btn_CreateServer.setFont(new Font("Tahoma", Font.PLAIN, 20));
        this.btn_CreateServer.setFocusPainted(false);
        this.btn_CreateServer.setActionCommand("create");
        this.btn_CreateServer.addActionListener(this);
        serverPanel.add(btn_CreateServer, status.setGrid(3,5).setHeight(1).setInsets(10,10,10,10));

        // Main
        this.setTitle("Server Chat");
        this.setContentPane(serverPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    // Server Data to Table
    public void updateServerTable() {
        // Update table
        tableServer.setModel(new DefaultTableModel(DataManager.getObjectsData(), columnServerTable));

        // Table Alignment - Color
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        tableServer.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableServer.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tableServer.getColumnModel().getColumn(2).setCellRenderer( new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                cell.setForeground(value.toString().equals("Đang hoạt động") ?
                        new Color(60,130,50):
                        new Color(255,64,64));
                cell.setFont(new Font("Dialog", Font.BOLD, 13));

                return cell;
            }
        });

        tableServer.getColumnModel().getColumn(0).setPreferredWidth(200);
        tableServer.getColumnModel().getColumn(1).setPreferredWidth(80);
        tableServer.getColumnModel().getColumn(2).setPreferredWidth(150);
        tableServer.getColumnModel().getColumn(3).setPreferredWidth(80);
        tableServer.getColumnModel().getColumn(4).setPreferredWidth(80);

        // Table Button
        tableServer.getColumnModel().getColumn(3).setCellRenderer(new TableButtonRenderer());
        tableServer.getColumnModel().getColumn(3).setCellEditor(new TableRenderer(new JCheckBox(), this, "status"));
        tableServer.getColumnModel().getColumn(4).setCellRenderer(new TableButtonRenderer());
        tableServer.getColumnModel().getColumn(4).setCellEditor(new TableRenderer(new JCheckBox(), this, "delete"));

        // Table height
        tableServer.setRowHeight(35);

        // Table width
        tableServer.setPreferredScrollableViewportSize(new Dimension(tableServer.getPreferredSize().width, 400));

        // Table font
        tableServer.setFont(new Font("Tahoma", Font.PLAIN, 16));
    }

    // Client Data to Table
    public void updateClientTable() {
        Object[][] data = {{"Đông 1"},{"Đông 2"},{"Đông 3"},{"Đông 4"},{"Đông 5"}};
        // Update table
        tableClient.setModel(new DefaultTableModel(data, columnClientTable));

        // Table Col Width
        tableClient.getColumnModel().getColumn(0).setPreferredWidth(200);

        // Table height
        tableClient.setRowHeight(30);

        // Table width
        tableClient.setPreferredScrollableViewportSize(new Dimension(tableClient.getPreferredSize().width, 400));

        // Table font
        tableClient.setFont(new Font("Tahoma", Font.PLAIN, 14));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "status": {
                // Switch Status
                DataManager.switchStatusServer(tableServer.getSelectedRow());

                // Update table
                updateServerTable();
                updateClientTable();
                break;
            }
            case "create": {
                JDialog createDialog = new JDialog();

                // Create Panel
                GridBagStatus status = new GridBagStatus(1, 1).setFill(GridBagConstraints.BOTH);
                JPanel addServerContent = new JPanel(new GridBagLayout());

                // ServerIP Input
                JLabel lb_ServerIP = new JLabel("ServerIP");
                lb_ServerIP.setFont(new Font("Tahoma", Font.PLAIN, 16));
                addServerContent.add(lb_ServerIP, status.setGrid(1, 4).setInsets(10,10,0,10).setWeight(0, 0));

                JTextField tf_ServerIP = new JTextField();
                tf_ServerIP.setFont(new Font("Tahoma", Font.PLAIN, 16));
                tf_ServerIP.setPreferredSize(new Dimension(200,25));
                tf_ServerIP.setEditable(false);
                addServerContent.add(tf_ServerIP, status.setGrid(1, 5).setInsets(2,10,0,10).setWeight(2, 0));

                // ServerName Input
                JLabel lb_ServerName = new JLabel("ServerName");
                lb_ServerName.setFont(new Font("Tahoma", Font.PLAIN, 16));
                addServerContent.add(lb_ServerName, status.setGrid(1,1).setInsets(20,10,0,5).setWeight(0, 0));

                JTextField tf_ServerName = new JTextField();
                tf_ServerName.setFont(new Font("Tahoma", Font.PLAIN, 16));
                tf_ServerName.setPreferredSize(new Dimension(200,25));
                addServerContent.add(tf_ServerName, status.setGrid(1, 3).setInsets(2,10,0,10).setWeight(2, 0));

                // ServerPort Input
                JLabel lb_ServerPort = new JLabel("ServerPort");
                lb_ServerPort.setFont(new Font("Tahoma", Font.PLAIN, 16));
                addServerContent.add(lb_ServerPort, status.setGrid(1, 6).setInsets(10,10,0,10).setWeight(0, 0));

                JTextField tf_ServerPort = new JTextField();
                tf_ServerPort.setFont(new Font("Tahoma", Font.PLAIN, 16));
                tf_ServerPort.setPreferredSize(new Dimension(200,25));
                addServerContent.add(tf_ServerPort, status.setGrid(1, 7).setInsets(2,10,20,10).setWeight(2, 0));

                // Button Create
                JButton btn_CreateServer = new JButton("Create Server");
                btn_CreateServer.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            // Getting input data
                            String ServerName = tf_ServerName.getText();
                            String ServerIP = tf_ServerIP.getText();
                            Integer ServerPort = Integer.parseInt(tf_ServerPort.getText());

                            // Check server name
                            if (ServerName.isEmpty()) {
                                ServerName = "Rename to Server real name";//SocketController.serverName(ip, port);
                            }

                            // Check port
                            if (ServerPort < 0) {
                                JOptionPane.showMessageDialog(createDialog,
                                        "Port phải là 1 số nguyên dương", "Lỗi port là số âm", JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            // Check similar
                            if (DataManager.isExist(new ServerInfo(ServerName, ServerPort, true))) {
                                JOptionPane.showMessageDialog(createDialog,
                                        "Server đang tạo đã tồn tại", "Đã tồn tại server", JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            // Add server
                            DataManager.addServer(new ServerInfo(ServerName, ServerPort, true));

                            // Updating
                            updateServerTable();
                            updateClientTable();
                            createDialog.setVisible(false);
                            createDialog.dispose();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(createDialog, "Port phải là 1 số nguyên dương", "Lỗi port không đúng định dạng",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    }
                });
                btn_CreateServer.setFocusPainted(false);
                addServerContent.add(btn_CreateServer, status.setGrid(1, 8)
                        .setWitdh(2).setFill(GridBagConstraints.NONE).setWeight(0, 0)
                        .setInsets(5, 10, 10, 10));

                // Add Panel to Dialog
                createDialog.setTitle("Nhập thông tin server: ");
                createDialog.setContentPane(addServerContent);
                createDialog.getRootPane().setDefaultButton(btn_CreateServer);
                createDialog.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
                createDialog.pack();
                createDialog.setLocationRelativeTo(null);
                createDialog.setVisible(true);

                break;
            }
            case "delete": {
                JDialog deleteDialog = new JDialog();

                // Create Panel
                GridBagStatus status = new GridBagStatus(1, 1).setFill(GridBagConstraints.BOTH);
                JPanel joinServerContent = new JPanel(new GridBagLayout());

                // Confirm Label
                JLabel lb_TitleConfirm = new JLabel("Xác nhận xóa server", SwingConstants.CENTER);
                lb_TitleConfirm.setFont(new Font("Tahoma", Font.PLAIN, 20));
                lb_TitleConfirm.setForeground(new Color(255,64,64));
                lb_TitleConfirm.setSize(new Dimension(250,30));
                joinServerContent.add(lb_TitleConfirm, status.setGrid(1,1).setInsets(20,10,20,5).setWeight(0, 0));

                // Confirm Button
                JButton btn_DeleteServer = new JButton("Delete Server");
                btn_DeleteServer.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Delete server
                        DataManager.removeServer(tableServer.getSelectedRow());

                        // Dispose Dialog
                        updateServerTable();
                        updateClientTable();
                        deleteDialog.setVisible(false);
                        deleteDialog.dispose();
                    }
                });
                btn_DeleteServer.setFocusPainted(false);
                joinServerContent.add(btn_DeleteServer, status.setGrid(1, 2)
                        .setWitdh(2).setFill(GridBagConstraints.NONE).setWeight(0, 0)
                        .setInsets(5, 10, 10, 10));

                // Add Panel to Dialog
                deleteDialog.setTitle("Xóa server " + tableServer.getValueAt(tableServer.getSelectedRow(), 0).toString());
                deleteDialog.setContentPane(joinServerContent);
                deleteDialog.getRootPane().setDefaultButton(btn_DeleteServer);
                deleteDialog.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
                deleteDialog.pack();
                deleteDialog.setLocationRelativeTo(null);
                deleteDialog.setVisible(true);
            }
        }
    }
}
