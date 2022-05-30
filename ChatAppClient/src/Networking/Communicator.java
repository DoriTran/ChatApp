package Networking;

import Main.Main;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

import Main.*;

public class Communicator extends Thread {
    // Networking
    public Socket socket;
    public BufferedReader receiver;
    public BufferedWriter sender;

    // Constructor
    public Communicator(Socket socket, BufferedReader receiver, BufferedWriter sender) throws IOException {
        this.socket = socket;
        this.receiver = receiver;
        this.sender = sender;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String header = receiver.readLine();
                System.out.println("Header " + header);
                if (header == null)
                    throw new IOException();

                switch (header) {
                    case "online user": {
                        String newUser = receiver.readLine();
                        Main.socketManager.onlineUsers.add(newUser);
                        Main.chatScreen.updateOnlineUser();
                        break;
                    }
                    case "offline user": {
                        String offUser = receiver.readLine();
                        Main.socketManager.onlineUsers.remove(offUser);
                        Main.chatScreen.updateOnlineUser();
                        Main.chatScreen.offlineMessage(offUser);
                        Main.chatScreen.offlineFile(offUser);
                        if (Main.socketManager.onlineUsers.isEmpty())
                            Main.chatScreen.setInputMessagePanelEnable(false);
                        break;
                    }
                    case "new message": {
                        String fromUser = receiver.readLine();
                        String message = receiver.readLine();
                        Main.chatScreen.addNewMessage(fromUser, message);
                        break;
                    }
                    case "new file notification": {
                        String fromUser = receiver.readLine();
                        String newFileName = receiver.readLine();
                        Main.chatScreen.addNewFile(fromUser, newFileName);
                        break;
                    }
                    case "file download request": {
                        String fromUser = receiver.readLine();
                        Integer requestedFileIndex = Integer.parseInt(receiver.readLine());
                        Main.socketManager.SendFileRequested(fromUser, requestedFileIndex);
                        break;
                    }
                    case "download file": {
                        // File info
                        String fileName = receiver.readLine();
                        Integer fileLength = Integer.parseInt(receiver.readLine());

                        // Download file
                        File file = new File(Main.socketManager.downloadToPath + "/" + fileName);
                        byte[] buffer = new byte[1024];
                        InputStream in = socket.getInputStream();
                        OutputStream out = new FileOutputStream(file);

                        for (int receivedSize = 0, count = 0; (count = in.read(buffer)) > 0; ) {
                            out.write(buffer, 0, count);
                            receivedSize += count;
                            if (receivedSize >= fileLength) break;

                        }
                        out.close();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            try {
                if (Main.socketManager != null)
                    Main.socketManager.socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                Main.serverScreen = new ServerScreen();
                Main.chatScreen.setVisible(false);
                Main.chatScreen.dispose();
            }
        }
    }
}
