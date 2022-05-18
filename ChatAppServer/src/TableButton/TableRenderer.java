package TableButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class TableRenderer extends DefaultCellEditor
{
    private JButton button;
    private String label;
    private boolean clicked;
    private int row, col;
    private JTable table;

    public TableRenderer(JCheckBox checkBox, ActionListener actionListener, String ActionCommand)
    {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(actionListener);
        button.setActionCommand(ActionCommand);
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
    {
        this.table = table;
        this.row = row;
        this.col = column;

        button.setForeground(Color.black);
        button.setBackground(UIManager.getColor("Button.background"));
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        clicked = true;
        return button;
    }

    public Object getCellEditorValue()
    {
        if (clicked)
        {
            JOptionPane.showMessageDialog(button, "Column with Value: "+table.getValueAt(row, col) + " -  Clicked!");
        }
        clicked = false;
        return new String(label);
    }

    public boolean stopCellEditing()
    {
        clicked = false;
        return super.stopCellEditing();
    }

    protected void fireEditingStopped()
    {
        super.fireEditingStopped();
    }
}
