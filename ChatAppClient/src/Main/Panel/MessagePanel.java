package Main.Panel;

import Main.GridBagStatus;

import javax.swing.*;
import java.awt.*;

public class MessagePanel extends JPanel {
    public JLabel sender;
    public JLabel message;

    // Constructor
    public MessagePanel( String Message) {
        sender = new JLabel("", SwingConstants.CENTER);
        message = new JLabel(Message, SwingConstants.CENTER);
        format(new Color(255, 222, 244));
    }

    public MessagePanel(String Sender, String Message, int Direction) {
        sender = new JLabel(Sender, Direction);
        message = new JLabel(Message, Direction);
        format((Direction ==  SwingConstants.RIGHT) ? new Color(246, 249, 250) : new Color(228, 230,235));
    }

    // Format
    private void format(Color color) {
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(600, (int) (sender.getPreferredSize().height + message.getPreferredSize().height * 1.5 + 20)));
        GridBagStatus status = new GridBagStatus().setWeight(1.0,0.1);

        sender.setFont(new Font("Tahoma", Font.BOLD, 16));
        sender.setBackground(color);
        sender.setOpaque(true);
        this.add(sender, status.setGrid(1,1).setInsets(5,5,0,5).setFill(GridBagConstraints.BOTH));
        message.setFont(new Font("Dialog", Font.PLAIN, 20));
        message.setBackground(color);
        message.setOpaque(true);
        this.add(message, status.setGrid(1,2).setInsets(0,5,5,5).setFill(GridBagConstraints.BOTH));
    }
}
