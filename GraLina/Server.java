package GraLina;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{
    int port=8000;
    ArrayList<ClientHandler> connections=new ArrayList<>();
    int statusliny=0;
    ServerSocket server;
    boolean running=true;
    ExecutorService pool;
    PrintWriter terminalout;
    public static void main(String[] args){
        Server server=new Server();
        server.run();
    }


    @Override
    public void run() {
        try {
            server=new ServerSocket(port);
            terminalout=new PrintWriter(System.out,true);
            pool= Executors.newCachedThreadPool();
            while(running) {
                Socket client = server.accept();
                ClientHandler handler = new ClientHandler(client,this);
                connections.add(handler);
                pool.execute(handler);
            }
        }
        catch (Exception e) {
            shutdown();
        }
    }
    public void broadcast(String message){
        for(ClientHandler c:connections) {
            if (c != null) {
                c.sendMessage(message);
            }
        }
        terminalout.println("["+new Date()+"] "+message);
    }
    public void shutdown(){
        running=false;
        pool.shutdown();
        terminalout.close();
        try {
            if (!server.isClosed()) {
                server.close();
            }
            for (ClientHandler c:connections) {
                c.shutdown();
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void zmienstatusliny(ClientHandler c){
        Random random = new Random();
        int wartosc_przesuniecia=random.nextInt(5) + 1;
        if(c.userteam==Team.RED){
            this.statusliny-=wartosc_przesuniecia;
        } else if (c.userteam==Team.BLUE) {
            this.statusliny+=wartosc_przesuniecia;
        }
        broadcast("Uzytkownik "+AnsiHandler.addcolor(c.username,"yellow")+" druzyny "+AnsiHandler.addcolor(c.userteam.toString(),c.userteam.toString().toLowerCase())
                +" przesunal line o "+wartosc_przesuniecia+". Nowa wartosc liny to "+AnsiHandler.addcolor(String.valueOf(this.statusliny),"green"));
        if(statusliny<=-50){
            broadcast(AnsiHandler.addcolor("Dryzyna RED wygrala przeciaganie liny","red"));
            broadcast("Rozpoczynanie nowej rozgrywki");
            statusliny=0;
        }
        else if(statusliny>=50){
            broadcast(AnsiHandler.addcolor("Dryzyna BLUE wygrala przeciaganie liny","blue"));
            broadcast("Rozpoczynanie nowej rozgrywki");
            statusliny=0;
        }
    }
}
