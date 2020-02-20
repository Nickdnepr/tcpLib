package com.nickdnepr.tcplib;

import com.nickdnepr.tcplib.core.client.TcpClient;
import com.nickdnepr.tcplib.core.server.TcpServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        TcpServer server = new TcpServer(6789, (message) -> System.out.println("Server: " + message));
        TcpClient client = new TcpClient("localhost", 6789, (message) -> System.out.println("Client: " + message));

        client.writeMessage("Hello server");
        server.sendAll("Hello all");
//        System.out.println("end");
//        Thread.sleep(1000);
//        server.close();

//        Thread t1 = new Thread(() -> {
//        });
//
//        Thread t2 = new Thread(() -> {
//        });
//
//        Thread t3 = new Thread(() -> {
//        });
//
//        Thread t4 = new Thread(() -> {
//        });
//
//        t1.start();
//        t2.start();
//        t3.start();
//        t4.start();
//        System.out.println(Thread.getAllStackTraces().size());
    }
}
