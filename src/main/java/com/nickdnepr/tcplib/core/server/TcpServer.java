package com.nickdnepr.tcplib.core.server;

import com.nickdnepr.tcplib.core.utils.OnMessageReceivedListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class TcpServer {

    private int port;
    private ServerSocket serverSocket;
    private OnMessageReceivedListener listener;
    private ArrayList<ClientHandler> handlers;
    private boolean closed = false;

    public TcpServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        this.handlers = new ArrayList<>();
        start();
    }

    public TcpServer(int port, OnMessageReceivedListener listener) throws IOException {
        this(port);
        this.listener = listener;
    }

    private void start() {
        new Thread(() -> {
            while (!closed) {
                try {
                    ClientHandler handler = new ClientHandler(serverSocket.accept(), listener);
                    handlers.add(handler);
                    new Thread(handler).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendAll(String message) {
        for (ClientHandler handler : handlers) {
            handler.writeMessage(message);
        }
    }

    public void setOnMessageReceivedListener(OnMessageReceivedListener listener) {
        this.listener = listener;
    }

    public void close() throws IOException {
        closed = true;
        for (ClientHandler handler : handlers) {
            handler.close();
        }
    }
}
