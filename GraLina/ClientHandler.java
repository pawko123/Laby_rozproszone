package GraLina;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Random;


public class ClientHandler implements Runnable{
    Socket client;
    BufferedReader in;
    PrintWriter out;
    String userteam;
    String username;
    Server server;
    public ClientHandler(Socket client,Server server){
        this.client=client;
        this.server=server;
        try {
            this.in=new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.out=new PrintWriter(client.getOutputStream(),true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            String line1 = in.readLine();
            String line2 = in.readLine();
            String[] parts1 = line1.split(":");
            String[] parts2 = line2.split(":");
            if(Objects.equals(parts1[0], "Username")){
                username = parts1[1];
            }else if(Objects.equals(parts1[0], "UserTeam")){
                userteam = parts1[1];
            }
            if(Objects.equals(parts2[0], "Username")){
                username = parts2[1];
            }else if(Objects.equals(parts2[0], "UserTeam")){
                userteam = parts2[1];
            }
                server.broadcast("Uzytkownik o nazwie "+AnsiHandler.addcolor(username,"yellow")+" dolaczyl do zespolu "+
                        AnsiHandler.addcolor(userteam,userteam.toLowerCase()));
                String message;
                while((message = in.readLine())!=null){
                    if(message.startsWith("/wyjdz")){
                        server.broadcast("Uzytkownik "+AnsiHandler.addcolor(username,"yellow")+" druzyny "+
                                AnsiHandler.addcolor(userteam,userteam.toLowerCase())+" wyszedl z gry.");
                        shutdown();
                    }
                    if(message.startsWith("/ciagnijline")){
                        server.zmienstatusliny(this);
                    }
                }
            }

        catch (IOException e){
            shutdown();
        }
    }
    public void sendMessage(String message){
        out.println(message);
    }
    public void shutdown(){
        try {
            in.close();
            out.close();
            if (client.isClosed()) {
                client.close();
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
