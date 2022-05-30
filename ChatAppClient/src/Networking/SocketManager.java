package Networking;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import Data.ServerInfo;
import Main.*;

public class SocketManager {
    // Client info
    public String userName;

    // Server info
    public ServerInfo serverConnectedInfo;
    public List<String> onlineUsers;

    // Networking
    public Socket socket;
    public BufferedReader receiver;
    public BufferedWriter sender;
    public String downloadToPath;

    // Connect to server
    public SocketManager(ServerInfo serverConnectedInfo, String userName) {
        onlineUsers = new ArrayList<String>();
        try {
            // Data info
            this.userName = userName;
            this.serverConnectedInfo = serverConnectedInfo;

            // Connecting
            socket = new Socket(serverConnectedInfo.serverIP, serverConnectedInfo.serverPort);
            receiver = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            sender = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

        } catch (IOException e1) {
            //Main.connectServerScreen.loginResultAction("closed");
        }
    }

    // Login to server & wait for header
    public void Login() {
        try {
            // Send Header
            sender.write("user login"); sender.newLine();
            sender.write(userName); sender.newLine();
            sender.flush();

            // Wait Content
            String loginResult = receiver.readLine();
            if (loginResult.equals("user login success")) {
                // Total user
                int currentOnlineUser = Integer.parseInt(receiver.readLine());

                // All user
                for (int i = 0; i < currentOnlineUser - 1; i++) {
                    this.onlineUsers.add(receiver.readLine());
                }

                // Update chat screen
                Main.chatScreen.updateInfoServer();
                Main.chatScreen.updateOnlineUser();

                // Communicator
                new Thread(() -> {
                    try {
                        Communicator clientCommunicator = new Communicator(socket, receiver, sender);
                        clientCommunicator.start();
                    } catch (IOException e) {
                        System.out.println("Server or client socket closed");
                    }
                }).start();
            } else {
                // Reset chat screen data
                if (Main.chatScreen != null) Main.chatScreen.closeresetData();

                // Back to server screen
                Main.serverScreen = new ServerScreen();

                // Notify Dialog
                JOptionPane.showMessageDialog(new JDialog(), "Đã tồn tại user này trên server", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    // Logout from server by close socket
    public void Logout() {
        try {
            socket.close();
            receiver = null;
            sender = null;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Send message to user through server
    public void SendMessage(String toUser, String message) {
        try {
            // Send Header
            sender.write("send message"); sender.newLine();
            sender.write(userName); sender.newLine(); // From
            sender.write(toUser); sender.newLine(); // To
            sender.write(message); sender.newLine(); // Message
            sender.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    // Send file notification to user through server
    public void SendFileNotification(String toUser, String fileName) {
        try {
            // Send Header
            sender.write("send file notification"); sender.newLine();
            sender.write(userName); sender.newLine(); // From
            sender.write(toUser); sender.newLine(); // To
            sender.write(fileName); sender.newLine(); // fileIndex
            sender.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    // Send file download request to user through server
    public void SendFileDownloadRequest(String toUser, Integer fileIndex) {
        try {
            // Send Header
            sender.write("send file download request"); sender.newLine();
            sender.write(userName); sender.newLine(); // From
            sender.write(toUser); sender.newLine(); // To
            sender.write("" + fileIndex); sender.newLine(); // fileIndex
            sender.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    // Send file requested to user through server
    public void SendFileRequested(String toUser, int fileIndex) {
        // Check if file exist
        try {
            File file = new File(Main.chatScreen.getRequestedFilePath(toUser, fileIndex));

            sender.write("send file response"); sender.newLine();
            sender.write(userName); sender.newLine(); // From
            sender.write(toUser); sender.newLine(); // To
            sender.write(Main.chatScreen.getRequestedFileName(toUser, fileIndex)); sender.newLine(); // fileName

            if (file.exists()) {
                // File info
                sender.write("" + file.length()); sender.newLine();
                sender.flush();

                // Buffer
                byte[] buffer = new byte[1024];
                InputStream in = new FileInputStream(file);
                OutputStream out = socket.getOutputStream();

                // Sending file
                int count;
                while ((count = in.read(buffer)) > 0) {
                    out.write(buffer, 0, count);
                }

                in.close();
                out.flush();
            }
            else {
                System.out.println("Cant open file");
                sender.write("-1"); sender.newLine();
                sender.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Send message to server
    public void sendTextToRoom(int roomID, String content) {
        try {
            sender.write("text to room");
            sender.newLine();
            sender.write("" + roomID);
            sender.newLine();
            sender.write(content);
            sender.write('\0');
            sender.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}