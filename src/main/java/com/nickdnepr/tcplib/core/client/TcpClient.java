package com.nickdnepr.tcplib.core.client;

import com.nickdnepr.tcplib.core.utils.OnMessageReceivedListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

public class TcpClient {

    private String host;
    private int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private OnMessageReceivedListener listener;
    //TODO make configurable
    private boolean autoRestoreConnection = true;
    private boolean closed = false;

    public TcpClient(String host, int port, OnMessageReceivedListener listener) throws IOException {
        this.host = host;
        this.port = port;
        this.listener = listener;
        setup();
    }

    public TcpClient(String host, int port) throws IOException {
        this(host, port, null);
    }

    private void setup() throws IOException {
        if (connect()) {
            listen();
        } else {
            if (autoRestoreConnection) {
                while (!closed) {
                    if (connect()) {
                        listen();
                        break;
                    }
                }
            } else {
                close();
            }
        }
    }

    private void manageConnectionLost() throws IOException {
        System.out.println("a");
        if (autoRestoreConnection) {
            setup();
        } else {
            close();
        }
    }

    public void close() throws IOException {
        closed = true;
        if (socket != null) {
            socket.close();
        }
    }

    private boolean connect() throws IOException {
        try {
            this.socket = new Socket(host, port);
            this.out = new PrintWriter(socket.getOutputStream());
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connection successful.");
            return true;
        } catch (ConnectException connectException) {
            System.out.println("Unable to connect.");
            return false;
        }
    }

    private void listen() {
        new Thread(() -> {
            while (!closed) {
                System.out.println("Working");
                String message;
                try {
                    if ((message = in.readLine()) != null) {
                        if (listener != null) {
                            listener.onMessageReceived(message);
                        }
                    } else {
                        if (socket.isClosed()){
                            System.out.println("Closed");
                        }
                        manageConnectionLost();
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
//        System.out.println("a");
    }

    public void setOnMessageReceivedListener(OnMessageReceivedListener listener) {
        this.listener = listener;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public void writeMessage(String message) {
        out.println(message);
        out.flush();
    }
}
