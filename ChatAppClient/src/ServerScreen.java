import Data.*;
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
    // Button
    private JButton btn_Refresh;
    private JButton btn_CreateNew;

    // Table
    private String[] columnNames = { "ServerName", "ServerIP", "ServerPort", "ServerStatus", "Join Server", "Edit Server", "Delete Server" };
    private JTable table;

    // JScrollPanel
    JScrollPane serverScrollPane;

    // Constructor
    public ServerScreen() {
        // Read Server data
        try {
            DataManager.readServers();
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
                    DataManager.saveServers();
                }
                catch (IOException e) {
                    System.out.println("Error saving server data file");
                }

            }
        });

        // Main Panel
        JPanel serverPanel = new JPanel(new GridBagLayout());
        GridBagStatus status = new GridBagStatus();

        // Button
        this.btn_Refresh = new JButton("Làm mới các server");
        this.btn_Refresh.setFont(new Font("Tahoma", Font.PLAIN, 20));
        this.btn_Refresh.setFocusPainted(false);
        this.btn_Refresh.setPreferredSize(new Dimension(390, 40));
        this.btn_Refresh.setActionCommand("refresh");
        this.btn_Refresh.addActionListener(this);
        serverPanel.add(btn_Refresh, status.setGrid(1,1).setInsets(10,10,10,10).setFill(GridBagConstraints.BOTH));

        this.btn_CreateNew = new JButton("Tạo mới một server");
        this.btn_CreateNew.setFont(new Font("Tahoma", Font.PLAIN, 20));
        this.btn_CreateNew.setFocusPainted(false);
        this.btn_CreateNew.setPreferredSize(new Dimension(390, 40));
        this.btn_CreateNew.setActionCommand("create");
        this.btn_CreateNew.addActionListener(this);
        serverPanel.add(btn_CreateNew, status.setGrid(2,1).setInsets(10,10,10,10));

        // Server table
        table = new JTable();
        updateTable();

        // JScrollPanel contain Table
        serverScrollPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        serverScrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách server: "));
        serverPanel.add(serverScrollPane, status.setGrid(1,2).setWitdh(2).setInsets(10,10,10,10));

        // Main
        this.setTitle("Client - Chọn server");
        this.setContentPane(serverPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    // Server Data to Table
    void updateTable() {
        // Update table
        table.setModel(new DefaultTableModel(DataManager.getObjectsData(), columnNames) {

            public boolean isCellEditable(int row, int column)
            {
                return (column == 4 || column == 5 || column == 6);
            }
        });

        // Table Button
        table.getColumnModel().getColumn(4).setCellRenderer(new TableButtonRenderer());
        table.getColumnModel().getColumn(4).setCellEditor(new TableRenderer(new JCheckBox(), this, "join"));
        table.getColumnModel().getColumn(5).setCellRenderer(new TableButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new TableRenderer(new JCheckBox(), this, "edit"));
        table.getColumnModel().getColumn(6).setCellRenderer(new TableButtonRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new TableRenderer(new JCheckBox(), this, "delete"));

        // Table Alignment - Color
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer( new DefaultTableCellRenderer() {

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

        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(70);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(80);
        table.getColumnModel().getColumn(6).setPreferredWidth(80);

        // Table height
        table.setRowHeight(30);

        table.setFont(new Font("Tahoma", Font.PLAIN, 16));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "refresh": {
                updateTable();
                break;
            }
            case "create": {
                JDialog createDialog = new JDialog();

                // Create Panel
                GridBagStatus status = new GridBagStatus(1, 1).setFill(GridBagConstraints.BOTH);
                JPanel addServerContent = new JPanel(new GridBagLayout());

                // ServerName Input
                JLabel lb_ServerName = new JLabel("ServerNickName");
                lb_ServerName.setFont(new Font("Tahoma", Font.PLAIN, 16));
                addServerContent.add(lb_ServerName, status.setGrid(1,1).setInsets(20,10,0,5).setWeight(0, 0));

                JLabel lb_ServerEmpty = new JLabel("Để trống sẽ dùng tên của Server");
                lb_ServerEmpty.setFont(new Font("Tahoma", Font.PLAIN, 12));
                lb_ServerEmpty.setForeground(Color.BLUE);
                addServerContent.add(lb_ServerEmpty, status.setGrid(1,2).setInsets(2,10,0,5).setWeight(0, 0));

                JTextField tf_ServerName = new JTextField();
                tf_ServerName.setFont(new Font("Tahoma", Font.PLAIN, 16));
                tf_ServerName.setPreferredSize(new Dimension(200,25));
                addServerContent.add(tf_ServerName, status.setGrid(1, 3).setInsets(2,10,0,10).setWeight(2, 0));

                // ServerIP Input
                JLabel lb_ServerIP = new JLabel("ServerIP");
                lb_ServerIP.setFont(new Font("Tahoma", Font.PLAIN, 16));
                addServerContent.add(lb_ServerIP, status.setGrid(1, 4).setInsets(10,10,0,10).setWeight(0, 0));

                JTextField tf_ServerIP = new JTextField();
                tf_ServerIP.setFont(new Font("Tahoma", Font.PLAIN, 16));
                tf_ServerIP.setPreferredSize(new Dimension(200,25));
                addServerContent.add(tf_ServerIP, status.setGrid(1, 5).setInsets(2,10,0,10).setWeight(2, 0));

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
                            if (DataManager.isExist(new ServerInfo(ServerName, ServerIP, ServerPort, true))) {
                                JOptionPane.showMessageDialog(createDialog,
                                        "Server đang tạo đã tồn tại", "Đã tồn tại server", JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            // Add server
                            DataManager.addServer(new ServerInfo(ServerName, ServerIP, ServerPort, false));

                            // Updating
                            updateTable();
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
            case "join": {
                JDialog joinDialog = new JDialog();

                // Create Panel
                GridBagStatus status = new GridBagStatus(1, 1).setFill(GridBagConstraints.BOTH);
                JPanel joinServerContent = new JPanel(new GridBagLayout());

                // YourName Input
                JLabel lb_ServerName = new JLabel("Enter your name", SwingConstants.CENTER);
                lb_ServerName.setFont(new Font("Tahoma", Font.PLAIN, 20));
                joinServerContent.add(lb_ServerName, status.setGrid(1,1).setInsets(20,10,0,5).setWeight(0, 0));

                JTextField tf_UserName = new JTextField("", SwingConstants.CENTER);
                tf_UserName.setFont(new Font("Tahoma", Font.PLAIN, 16));
                tf_UserName.setPreferredSize(new Dimension(200,25));
                joinServerContent.add(tf_UserName, status.setGrid(1, 2).setInsets(2,10,0,10).setWeight(2, 0));

                JLabel lb_ServerEmpty = new JLabel("Tên đại diện cho bạn khi chat với người khác", SwingConstants.CENTER);
                lb_ServerEmpty.setFont(new Font("Tahoma", Font.PLAIN, 12));
                lb_ServerEmpty.setForeground(Color.BLUE);
                joinServerContent.add(lb_ServerEmpty, status.setGrid(1,3).setInsets(2,10,0,5).setWeight(0, 0));

                // Button Create
                JButton btn_CreateServer = new JButton("Join Server");
                btn_CreateServer.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        // Getting input data
                        String ServerIP = table.getValueAt(table.getSelectedRow(), 1).toString() ;
                        Integer ServerPort = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 2).toString());

                        // Join server
                        new ChatScreen(tf_UserName.getText());
                        setVisible(false);
                        dispose();

                        // Dispose Dialog
                        joinDialog.setVisible(false);
                        joinDialog.dispose();
                    }
                });
                btn_CreateServer.setFocusPainted(false);
                joinServerContent.add(btn_CreateServer, status.setGrid(1, 4)
                        .setWitdh(2).setFill(GridBagConstraints.NONE).setWeight(0, 0)
                        .setInsets(5, 10, 10, 10));

                // Add Panel to Dialog
                joinDialog.setTitle("Tham gia server " + table.getValueAt(table.getSelectedRow(), 0).toString());
                joinDialog.setContentPane(joinServerContent);
                joinDialog.getRootPane().setDefaultButton(btn_CreateServer);
                joinDialog.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
                joinDialog.pack();
                joinDialog.setLocationRelativeTo(null);
                joinDialog.setVisible(true);

                break;
            }
            case "edit": {
                JDialog editDialog = new JDialog();

                // Create Panel
                GridBagStatus status = new GridBagStatus(1, 1).setFill(GridBagConstraints.BOTH);
                JPanel addServerContent = new JPanel(new GridBagLayout());

                // ServerName Input
                JLabel lb_ServerName = new JLabel("ServerNickName");
                lb_ServerName.setFont(new Font("Tahoma", Font.PLAIN, 16));
                addServerContent.add(lb_ServerName, status.setGrid(1,1).setInsets(20,10,0,5).setWeight(0, 0));

                JLabel lb_ServerEmpty = new JLabel("Để trống sẽ dùng tên của Server");
                lb_ServerEmpty.setFont(new Font("Tahoma", Font.PLAIN, 12));
                lb_ServerEmpty.setForeground(Color.BLUE);
                addServerContent.add(lb_ServerEmpty, status.setGrid(1,2).setInsets(2,10,0,5).setWeight(0, 0));

                JTextField tf_ServerName = new JTextField(table.getValueAt(table.getSelectedRow(), 0).toString());
                tf_ServerName.setFont(new Font("Tahoma", Font.PLAIN, 16));
                tf_ServerName.setPreferredSize(new Dimension(200,25));
                addServerContent.add(tf_ServerName, status.setGrid(1, 3).setInsets(2,10,0,10).setWeight(2, 0));

                // ServerIP Input
                JLabel lb_ServerIP = new JLabel("ServerIP");
                lb_ServerIP.setFont(new Font("Tahoma", Font.PLAIN, 16));
                addServerContent.add(lb_ServerIP, status.setGrid(1, 4).setInsets(10,10,0,10).setWeight(0, 0));

                JTextField tf_ServerIP = new JTextField(table.getValueAt(table.getSelectedRow(), 1).toString());
                tf_ServerIP.setFont(new Font("Tahoma", Font.PLAIN, 16));
                tf_ServerIP.setPreferredSize(new Dimension(200,25));
                addServerContent.add(tf_ServerIP, status.setGrid(1, 5).setInsets(2,10,0,10).setWeight(2, 0));

                // ServerPort Input
                JLabel lb_ServerPort = new JLabel("ServerPort");
                lb_ServerPort.setFont(new Font("Tahoma", Font.PLAIN, 16));
                addServerContent.add(lb_ServerPort, status.setGrid(1, 6).setInsets(10,10,0,10).setWeight(0, 0));

                JTextField tf_ServerPort = new JTextField(table.getValueAt(table.getSelectedRow(), 2).toString());
                tf_ServerPort.setFont(new Font("Tahoma", Font.PLAIN, 16));
                tf_ServerPort.setPreferredSize(new Dimension(200,25));
                addServerContent.add(tf_ServerPort, status.setGrid(1, 7).setInsets(2,10,20,10).setWeight(2, 0));

                // Button Create
                JButton btn_CreateServer = new JButton("Hoàn tất chỉnh sửa");
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
                                JOptionPane.showMessageDialog(editDialog,
                                        "Port phải là 1 số nguyên dương", "Lỗi port là số âm", JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            // Check similar
                            if (DataManager.isExist(new ServerInfo(ServerName, ServerIP, ServerPort, true))) {
                                JOptionPane.showMessageDialog(editDialog,
                                        "Server đang chỉnh sửa đã tồn tại", "Đã tồn tại server", JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            // Add server
                            DataManager.editServer(table.getSelectedRow(), new ServerInfo(ServerName, ServerIP, ServerPort, false));

                            // Updating
                            updateTable();
                            editDialog.setVisible(false);
                            editDialog.dispose();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(editDialog, "Port phải là 1 số nguyên dương", "Lỗi port không đúng định dạng",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    }
                });
                btn_CreateServer.setFocusPainted(false);
                addServerContent.add(btn_CreateServer, status.setGrid(1, 8)
                        .setWitdh(2).setFill(GridBagConstraints.NONE).setWeight(0, 0)
                        .setInsets(5, 10, 10, 10));

                // Add Panel to Dialog
                editDialog.setTitle("Chỉnh sửa thông tin server: ");
                editDialog.setContentPane(addServerContent);
                editDialog.getRootPane().setDefaultButton(btn_CreateServer);
                editDialog.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
                editDialog.pack();
                editDialog.setLocationRelativeTo(null);
                editDialog.setVisible(true);
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
                        DataManager.removeServer(table.getSelectedRow());

                        // Dispose Dialog
                        updateTable();
                        deleteDialog.setVisible(false);
                        deleteDialog.dispose();
                    }
                });
                btn_DeleteServer.setFocusPainted(false);
                joinServerContent.add(btn_DeleteServer, status.setGrid(1, 2)
                        .setWitdh(2).setFill(GridBagConstraints.NONE).setWeight(0, 0)
                        .setInsets(5, 10, 10, 10));

                // Add Panel to Dialog
                deleteDialog.setTitle("Xóa server " + table.getValueAt(table.getSelectedRow(), 0).toString());
                deleteDialog.setContentPane(joinServerContent);
                deleteDialog.getRootPane().setDefaultButton(btn_DeleteServer);
                deleteDialog.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
                deleteDialog.pack();
                deleteDialog.setLocationRelativeTo(null);
                deleteDialog.setVisible(true);
                break;
            }
        }
    }
}