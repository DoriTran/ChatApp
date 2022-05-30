package Main.Panel;

import Main.GridBagStatus;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ConversationPanel extends JPanel {
    // Data
    private ArrayList<MessagePanel> messages = new ArrayList<MessagePanel>();
    private String toUser = "";
    private GridBagStatus status = new GridBagStatus().setAnchor(GridBagConstraints.NORTH);

    // Constructor
    public ConversationPanel() {
        // Test Data
        messages.add(new MessagePanel("Chọn user đang online để trò chuyện"));

        // Format
        this.setLayout(new GridBagLayout());
        this.add(messages.get(0), status.setGrid(1,1));
    }

    public ConversationPanel(String UserName) {
        // Data
        this.toUser = UserName;
        messages.add(new MessagePanel("Bắt đầu cuộc trò chuyện với " + UserName));
        //messages.add(new MessagePanel("Hello " + new String( Character.toChars(0x1F601))));
        this.add(messages.get(0), status.setGrid(1,1));

        // Format
        this.setLayout(new GridBagLayout());
    }

    // Add new message
    public void addMessage(String sender, String message, boolean isFromMe) {
        messages.add(new MessagePanel(sender, message, isFromMe ? SwingConstants.RIGHT : SwingConstants.LEFT));
        this.add(messages.get(messages.size() - 1), status.setGrid(0, messages.size()));
    }

    // Get to User
    public String getToUser() {
        return this.toUser;
    }
}
