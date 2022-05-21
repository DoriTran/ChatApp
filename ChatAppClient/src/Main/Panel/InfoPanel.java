package Main.Panel;

import Main.GridBagStatus;
import Main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class InfoPanel extends JPanel {
    // Component
    private JLabel lb_Server;
    private JLabel lb_UserName;
    private JLabel lb_IP;
    private JLabel lb_Port;

    private JLabel lb_ServerValue;
    private JLabel lb_UserNameValue;
    private JLabel lb_IPValue;
    private JLabel lb_PortValue;

    private JButton btn_ExitServer;

    // Action listener
    private ActionListener serverScreenListener;

    public InfoPanel(ActionListener serverScreenListener) {
        // Set ActionListener
        this.serverScreenListener = serverScreenListener;

        // Color
        setBackground(new Color(211,211,211));

        // Layout
        this.setLayout(new GridBagLayout());
        GridBagStatus status = new GridBagStatus().setAnchor(GridBagConstraints.WEST);

        // Server Label
        this.lb_Server = new JLabel("ServerName: ", SwingConstants.LEFT);
        this.lb_Server.setFont(new Font("Tahoma", Font.BOLD, 16));
        this.lb_Server.setVerifyInputWhenFocusTarget(false);
        this.add(this.lb_Server, status.setGrid(1,1).setWitdh(1).setInsets(5,5,0,10));

        this.lb_ServerValue = new JLabel("<This server name>", SwingConstants.LEFT);
        this.lb_ServerValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
        this.lb_ServerValue.setVerifyInputWhenFocusTarget(false);
        this.add(this.lb_ServerValue, status.setGrid(2,1).setWitdh(2).setInsets(5,5,0,30));

        // UserName Label
        this.lb_UserName = new JLabel("UserName: ", SwingConstants.LEFT);
        this.lb_UserName.setFont(new Font("Tahoma", Font.BOLD, 16));
        this.lb_UserName.setVerifyInputWhenFocusTarget(false);
        this.add(this.lb_UserName, status.setGrid(1,2).setWitdh(1).setInsets(0,5,5,10));

        this.lb_UserNameValue = new JLabel("<This server name>", SwingConstants.LEFT);
        this.lb_UserNameValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
        this.lb_UserNameValue.setVerifyInputWhenFocusTarget(false);
        this.add(this.lb_UserNameValue, status.setGrid(2,2).setWitdh(2).setInsets(0,5,5,30));

        // IP Label
        this.lb_IP = new JLabel("IPServer: ", SwingConstants.LEFT);
        this.lb_IP.setFont(new Font("Tahoma", Font.BOLD, 16));
        this.lb_IP.setVerifyInputWhenFocusTarget(false);
        this.add(this.lb_IP, status.setGrid(4,1).setWitdh(1).setInsets(5,5,0,10));

        this.lb_IPValue = new JLabel("<This server IP>", SwingConstants.LEFT);
        this.lb_IPValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
        this.lb_IPValue.setVerifyInputWhenFocusTarget(false);
        this.add(this.lb_IPValue, status.setGrid(5,1).setWitdh(2).setInsets(5,5,0,30));

        // Port Label
        this.lb_Port = new JLabel("PortServer: ", SwingConstants.LEFT);
        this.lb_Port.setFont(new Font("Tahoma", Font.BOLD, 16));;
        this.lb_Port.setVerifyInputWhenFocusTarget(false);
        this.add(this.lb_Port, status.setGrid(4,2).setWitdh(1).setInsets(0,5,5,10));

        this.lb_PortValue = new JLabel("<This server Port>", SwingConstants.LEFT);
        this.lb_PortValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
        this.lb_PortValue.setVerifyInputWhenFocusTarget(false);
        this.add(this.lb_PortValue, status.setGrid(5,2).setWitdh(2).setInsets(0,5,5,30));

        // Exit server Button
        this.btn_ExitServer = new JButton("Log out server");
        this.btn_ExitServer.setFont(new Font("Tahoma", Font.PLAIN, 20));
        this.btn_ExitServer.setFocusPainted(false);
        this.btn_ExitServer.setActionCommand("exit");
        this.btn_ExitServer.addActionListener(serverScreenListener);
        this.add(btn_ExitServer, status.setGrid(7,1).setHeight(2).setInsets(10,200,10,10));
    }

    // Update
    public void updateData() {
        this.lb_ServerValue.setText(Main.socketManager.serverConnectedInfo.serverName);
        this.lb_UserNameValue.setText(Main.socketManager.userName);
        this.lb_IPValue.setText(Main.socketManager.serverConnectedInfo.serverIP);
        this.lb_PortValue.setText(Main.socketManager.serverConnectedInfo.serverPort.toString());
    }
}
