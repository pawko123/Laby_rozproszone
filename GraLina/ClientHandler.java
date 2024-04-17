package GraLina;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class ClientHandler implements Runnable{
    Socket client;
    BufferedReader in;
    PrintWriter out;
    int usersteam;
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
                out.println("Wybierz swoj zespol:");
                out.println("1.Red");
                out.println("2.Blue");
                String enteredvalue = in.readLine();
                if (Objects.equals(enteredvalue, "1") || Objects.equals(enteredvalue, "2")) {
                    enteredvalid = true;
                    usersteam=Integer.parseInt(enteredvalue);
                }
                else{
                    out.println("Podano nieprawidlowa wartosc");
                }
            }
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
