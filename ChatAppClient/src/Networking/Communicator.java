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
                        break;
                    }
                    case "download file": {
                        int fileSize = Integer.parseInt(receiver.readLine());
                        File file = new File(Main.socketManager.downloadToPath);
                        byte[] buffer = new byte[1024];
                        InputStream in = socket.getInputStream();
                        OutputStream out = new FileOutputStream(file);

                        int count;
                        int receivedFileSize = 0;
                        while ((count = in.read(buffer)) > 0) {
                            out.write(buffer, 0, count);
                            receivedFileSize += count;
                            if (receivedFileSize >= fileSize)
                                break;
                        }

                        out.close();
                        break;
                    }

                }
            }
        } catch (IOException e) {
            try {
                Main.socketManager.socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                Main.serverScreen = new ServerScreen();
            }
        }
    }
}
