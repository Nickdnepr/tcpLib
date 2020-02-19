package com.nickdnepr.tcplib.core.server;

import com.nickdnepr.tcplib.core.utils.OnMessageReceivedListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private int id;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private OnMessageReceivedListener listener;
    private boolean closed = false;

    private static int identifier = 0;

    public ClientHandler(Socket socket) throws IOException {
        this.id = identifier++;
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream());
    }

    public ClientHandler(Socket socket, OnMessageReceivedListener listener) throws IOException {
        this(socket);
        this.listener = listener;
    }

    @Override
    public void run() {
        while (!closed) {
            String message;
            try {
                if ((message = in.readLine()) != null) {
                    if (listener != null) {
                        listener.onMessageReceived(message);
                    }
                } else {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeMessage(String message){
        out.println(message);
        out.flush();
    }

    public void setOnMessageReceivedListener(OnMessageReceivedListener listener) {
        this.listener = listener;
    }

    public int getId() {
        return id;
    }

    public void close() throws IOException {
        closed = true;
        socket.close();
    }
}
