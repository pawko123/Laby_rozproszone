package GraLina;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable{
    static int port=8000;
    ArrayList<ClientHandler> connections;
    public static void main(String[] args){

    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket=new ServerSocket(port);
            Socket client=serverSocket.accept();
            ClientHandler handler=new ClientHandler(client);
            connections.add(handler);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
