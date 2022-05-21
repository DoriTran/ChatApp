package Main.Panel;

import Main.GridBagStatus;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ConversationPanel extends JPanel {
    // Data
    private ArrayList<MessagePanel> messages = new ArrayList<MessagePanel>();
    GridBagStatus status = new GridBagStatus().setAnchor(GridBagConstraints.NORTH);

    // Constructor
    public ConversationPanel() {
        // Test Data
        messages.add(new MessagePanel("Alice", "Xin chào!", SwingConstants.LEFT));
        messages.add(new MessagePanel("Alice", "Mình là Alice", SwingConstants.LEFT));
        messages.add(new MessagePanel("Alice", "Rất vui được gặp bạn <3", SwingConstants.LEFT));

        messages.add(new MessagePanel("Đông", "Xin chào bạn!", SwingConstants.RIGHT));
        messages.add(new MessagePanel("Đông", "Mình tên là Đông", SwingConstants.RIGHT));
        messages.add(new MessagePanel("Đông", "Bạn có thể gọi mình là Đông Trần", SwingConstants.RIGHT));

        messages.add(new MessagePanel("Alice", "Xin chào Đông Trần !!!", SwingConstants.LEFT));
        messages.add(new MessagePanel("Alice", "Bạn có muốn học khóa học lập trình hôm nay không?", SwingConstants.LEFT));
        messages.add(new MessagePanel("Alice", "Bạn có muốn học khóa học lập trình hôm nay không?", SwingConstants.LEFT));
        messages.add(new MessagePanel("Alice", "Bạn có muốn học khóa học lập trình hôm nay không?", SwingConstants.LEFT));
        messages.add(new MessagePanel("Alice", "Bạn có muốn học khóa học lập trình hôm nay không?", SwingConstants.LEFT));
        messages.add(new MessagePanel("Alice", "Bạn có muốn học khóa học \nlập trình\n hôm nay không?", SwingConstants.LEFT));
        messages.add(new MessagePanel("Alice", "Bạn có muốn học khóa học \nlập trình hôm nay không?", SwingConstants.LEFT));
        messages.add(new MessagePanel("Alice", "Bạn có muốn học khóa học lập trình hôm nay không?", SwingConstants.LEFT));
        messages.add(new MessagePanel("Alice", "Bạn có muốn học khóa học lập trình hôm nay không?", SwingConstants.LEFT));
        messages.add(new MessagePanel("Alice", "Bạn có muốn học khóa học lập trình hôm nay không?", SwingConstants.LEFT));

        // Format
        this.setLayout(new GridBagLayout());
        reformat();
    }

    // HTML format
    private String formatToHTML(String message) {
        message.replace("\n", "<br>");
        return "<html>" + message + "</html>";
    }

    // Format
    public void reformat() {
        this.removeAll();
        for (int index = 0; index < messages.size(); index++) {
            this.add(messages.get(index), status.setGrid(1,index + 1));
        }
    }

    // Add new message
    public void addMessage(String sender, String message, boolean isFromMe) {
        messages.add(new MessagePanel(sender, formatToHTML(message), isFromMe ? GridBagConstraints.EAST : GridBagConstraints.WEST));
        this.add(messages.get(messages.size() - 1), status.setGrid(1,messages.size()));
    }
}
