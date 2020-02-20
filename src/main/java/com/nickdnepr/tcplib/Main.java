package com.nickdnepr.tcplib;

import com.nickdnepr.tcplib.core.client.TcpClient;
import com.nickdnepr.tcplib.core.server.TcpServer;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        TcpServer server = new TcpServer(6789, (message) -> System.out.println("Server: " + message));
        TcpClient client = new TcpClient("localhost", 6789, (message) -> System.out.println("Client: " + message));

        client.writeMessage("Hello server");
        server.sendAll("Hello all");

        Scanner scanner = new Scanner(System.in);

        while (true){
            server.sendAll(scanner.nextLine());
        }
    }
}
