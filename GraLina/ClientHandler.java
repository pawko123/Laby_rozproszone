package GraLina;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Random;

enum Team{
    BLUE,
    RED
}

public class ClientHandler implements Runnable{
    Socket client;
    BufferedReader in;
    PrintWriter out;
    Team userteam;
    String username;
    public ClientHandler(Socket client){
        this.client=client;
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
            boolean enteredvalid = false;
            while (!enteredvalid) {
                out.println("Podaj nazwe uzytkownika:");
                username=in.readLine();
                out.println("Wybierz swoj zespol:");
                out.println("1.Red");
                out.println("2.Blue");
                String enteredvalue = in.readLine();
                if (Objects.equals(enteredvalue, "1")) {
                    enteredvalid = true; 
                    userteam=Team.RED;
                }
                else if (Objects.equals(enteredvalue, "2")) {
                    userteam=Team.BLUE;
                }
                else{
                    out.println("Podano nieprawidlowa wartosc");
                }
                Server.broadcast("Uzytkownik o nazwie "+AnsiHandler.addcolor(username,"yellow")+" dolaczyl do zespolu "+
                        AnsiHandler.addcolor(userteam.toString(),userteam.toString().toLowerCase()));
                String message;
                while((message = in.readLine())!=null){
                    if(message.startsWith("/wyjdz")){
                        Server.broadcast("Uzytkownik "+AnsiHandler.addcolor(username,"yellow")+" druzyny "+
                                AnsiHandler.addcolor(userteam.toString(),userteam.toString().toLowerCase())+" wyszedl z gry.");
                        shutdown();
                    }
                    else{
                        Server.zmienstatusliny(this);
                    }
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
