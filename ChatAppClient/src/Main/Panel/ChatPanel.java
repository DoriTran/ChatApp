package Main.Panel;

import Main.GridBagStatus;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ChatPanel extends JPanel {
    // Component
    private JScrollPane scrollPane;
    private ConversationPanel conversation;

    // Constructor
    public ChatPanel() {
        this.setLayout(new BorderLayout());
        conversation = new ConversationPanel();

        scrollPane = new JScrollPane(conversation,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(
                BorderFactory.createTitledBorder(null, "Nguyễn Thị Thu Thủy", TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma", Font.PLAIN,25)));
        this.add(scrollPane);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(650, 580);
    }
}
