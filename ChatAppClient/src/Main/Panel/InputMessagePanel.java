package Main.Panel;

import Main.GridBagStatus;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionListener;

public class InputMessagePanel extends JPanel {
    // Component
    JButton fileButton;
    JTextArea inputMessage;
    JButton sendButton;

    // Scroll Text Area
    JScrollPane scrollPane;

    // Action listener
    private ActionListener serverScreenListener;

    // Constructor
    public InputMessagePanel(ActionListener serverScreenListener) {
        // Set ActionListener
        this.serverScreenListener = serverScreenListener;

        // Set Layout
        this.setLayout(new GridBagLayout());
        GridBagStatus status = new GridBagStatus();

        // File Chooser
        fileButton = new JButton("File");
        fileButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        fileButton.setActionCommand("file");
        fileButton.addActionListener(serverScreenListener);
        fileButton.setPreferredSize(new Dimension(60,50));
        this.add(fileButton, status.setGrid(1,1).setInsets(0,0, 0, 0)
                .setFill(GridBagConstraints.BOTH).setWeight(1.0,1.0));

        // TextInput
        inputMessage = new JTextArea();
        inputMessage.setFont(new Font("Tahoma", Font.PLAIN, 16));
        scrollPane = new JScrollPane(inputMessage,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(480, 50));
        this.add(scrollPane, status.setGrid(2,1).setWitdh(1).setInsets(0,0,0, 0));

        // Button
        sendButton = new JButton("Gửi");
        sendButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        sendButton.setActionCommand("send");
        sendButton.addActionListener(serverScreenListener);
        sendButton.setPreferredSize(new Dimension(60,50));
        this.add(sendButton, status.setGrid(3,1).setWitdh(1).setInsets(0,0, 0, 0));

        // Visible
        this.setEnableButton(false);
    }

    // Get Input Message
    public String getInputMessage() {
        // Get Input
        return this.inputMessage.getText();
    }
    static public String formatToHTML(String message, String alignment) {
        String formattedInput = message.replace("\n", "<br>");
        return "<html><body style='text-align: " + alignment + "'>" + formattedInput + "</body></html>";
    }

    // Clear Input Message
    public void clearInputMessage() {
        // Clear Input
        this.inputMessage.setText("");
    }

    // Set Visible
    public void setEnableButton(boolean enable) {
        this.fileButton.setEnabled(enable);
        this.sendButton.setEnabled(enable);
    }
}
