package GraLina;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {
    Socket client;
    BufferedReader in;
    PrintWriter out;
    boolean running = true;
    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
    @Override
    public void run() {
        try{
            client = new Socket("127.0.0.1", 8000);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            InputHandler inHandler = new InputHandler();
            Thread thread = new Thread(inHandler);
            thread.start();
            String inMesage;
            while((inMesage = in.readLine()) != null) {
                System.out.println(inMesage);
            }
        }
        catch(Exception e){
            shutdown();
        }
    }
    public void shutdown(){
        running = false;
        try{
            in.close();
            out.close();
            if(!client.isClosed()){
                client.close();
            }
        }catch(Exception e){
            //ignore
        }
    }
    class InputHandler implements Runnable {

        @Override
        public void run() {
            try {
                BufferedReader inreader = new BufferedReader(new InputStreamReader(System.in));
                while(running){
                    String message = inreader.readLine();
                    if(message.equals("/wyjdz")){
                        out.println(message);
                        inreader.close();
                        shutdown();
                    }else{
                        out.println(message);
                    }
                }
            }catch (IOException e){
                shutdown();
            }
        }
    }
}
