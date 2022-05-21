package Main.Panel;

import Data.DataManager;
import Main.GridBagStatus;
import Main.Main;
import TableButton.TableButtonRenderer;
import TableButton.TableRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class OnlineUserPanel extends JPanel {
    // Test Data
    private Object[][] data = {{"Alice 1"},{"Alice 2"},{"Alice 3"},{"Alice 4"},{"Alice 5"},{"Alice 6"},{"Alice 7"},{"Alice 8"},{"Alice 9"},{"Alice 10"},{"Alice 11"},{"Alice 12"},{"Alice 13"},{"Alice 14"},{"Alice 15"},{"Alice 16"},{"Alice 17"},{"Alice 18"}};
    private String[] colName = {"UserName"};

    // Component
    private JScrollPane scrollPane;
    private JTable table;

    // Action listener
    private ActionListener serverScreenListener;

    public OnlineUserPanel(ActionListener serverScreenListener) {
        // Set ActionListener
        this.serverScreenListener = serverScreenListener;

        // Set Layout
        this.setLayout(new GridBagLayout());
        GridBagStatus status = new GridBagStatus();

        // JTable
        table = new JTable();
        updateTable();

        // JScrollPanel contain Table
        scrollPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Số user online: 1 "));
        this.add(scrollPane, status.setGrid(1,1).setInsets(0));
    }

    // Table Update
    public void updateTable() {
        // Update table
        table.setModel(new DefaultTableModel((Object[][]) Main.socketManager.onlineUsers.toArray(), colName) {
            public boolean isCellEditable(int row, int column)
            {
                return (column == 0);
            }
        });
        table.getTableHeader().setUI(null);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Số user online: " + Main.socketManager.onlineUsers.size() + " "));

        // Table Button
        table.getColumnModel().getColumn(0).setCellRenderer(new TableButtonRenderer());
        table.getColumnModel().getColumn(0).setCellEditor(new TableRenderer(new JCheckBox(), serverScreenListener, "chat"));

        // Table width
        table.getColumnModel().getColumn(0).setPreferredWidth(150);

        // Table row height
        table.setRowHeight(40);

        // Table width
        table.setSize(150, 700);
        table.setPreferredScrollableViewportSize(new Dimension(table.getPreferredSize().width, 600));

        // Table font
        table.setFont(new Font("Tahoma", Font.PLAIN, 16));
    }
}
