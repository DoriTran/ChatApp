package Main.Panel;

import Main.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;

public class ChatPanel extends JPanel {
    // Component
    private JScrollPane scrollPane;
    public HashMap<String , ConversationPanel> conversations;

    // Constructor
    public ChatPanel() {
        conversations = new HashMap<String , ConversationPanel>();
        this.setLayout(new BorderLayout());

        scrollPane = new JScrollPane(new ConversationPanel(),
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(
                BorderFactory.createTitledBorder(null, "Hệ thống", TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma", Font.PLAIN,25)));
        this.add(scrollPane);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(650, 580);
    }

    // Change user
    public void changeUserConversation(String UserName) {
        if (!conversations.containsKey(UserName))
            conversations.put(UserName, new ConversationPanel(UserName));
        scrollPane.setViewportView(conversations.get(UserName));
        scrollPane.setBorder(
                BorderFactory.createTitledBorder(null, UserName, TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma", Font.PLAIN,25)));
    }
    // Offline user
    public void userChatOffline(String UserName) {
        ConversationPanel currentDisplay = (ConversationPanel) scrollPane.getViewport().getView();
        conversations.remove(UserName);
        scrollPane.setViewportView(new ConversationPanel());
        scrollPane.setBorder(
                BorderFactory.createTitledBorder(null, "Hệ thống", TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma", Font.PLAIN,25)));
        blink();
    }

    // Blink to update
    private void blink() {
        scrollPane.setVisible(false);
        scrollPane.setVisible(true);
    }

    // Add new message
    public void addMyNewMessage(String message) {
        ConversationPanel currentDisplay = (ConversationPanel) scrollPane.getViewport().getView();
        conversations.get(currentDisplay.getToUser()).addMessage(Main.socketManager.userName,
                InputMessagePanel.formatToHTML(message, "right") , true);
        Main.socketManager.SendMessage(currentDisplay.getToUser(),
                InputMessagePanel.formatToHTML(message, "left"));
        blink();
    }
    public void addOtherUserNewMassage(String from, String message) {
        if (!conversations.containsKey(from))
            conversations.put(from, new ConversationPanel(from));
        conversations.get(from).addMessage(from, message, false);
        blink();
    }
}
