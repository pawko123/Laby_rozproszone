package GraLina;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientGraphical extends JFrame {
    private JProgressBar progressBar;
    private JButton connectbutton;
    private JButton linabutton;
    private JButton wyjdzbutton;
    private Client client;
    private JLabel label3;
    public ClientGraphical() {
        setTitle("Client gry lina");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 170);
        setLayout(new BorderLayout());

        JPanel uppanel=new JPanel();
        uppanel.setLayout(new GridLayout(2, 1));

        progressBar = new JProgressBar(-50, 50);
        progressBar.setValue(0);
        progressBar.setStringPainted(false);
        uppanel.add(progressBar);

        label3=new JLabel("");
        uppanel.add(label3);

        add(uppanel, BorderLayout.NORTH);


        JPanel centerpanel = new JPanel();
        centerpanel.setLayout(new GridLayout(2, 2));


        JLabel label1=new JLabel("Username");
        centerpanel.add(label1);


        JTextField username=new JTextField();
        centerpanel.add(username);


        JLabel label2 = new JLabel("UserTeam");
        centerpanel.add(label2);


        String[] options = {"RED", "BLUE"};
        JComboBox<String> comboBox = new JComboBox<>(options);
        centerpanel.add(comboBox);


        add(centerpanel, BorderLayout.CENTER);
        JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(new GridLayout(1, 3));
        connectbutton = new JButton("Dolacz");
        connectbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Thread clientThread = new Thread(() -> {
                    client = new Client(username.getText(), (String) comboBox.getSelectedItem());
                    client.run();
                });
                clientThread.start();
            }
        });
        buttonpanel.add(connectbutton);

        linabutton = new JButton("Przeciagnij line");
        linabutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                client.out.println("/ciagnijline");
            }
        });
        buttonpanel.add(linabutton);

        wyjdzbutton = new JButton("Wyjdz z rozgrywki");
        wyjdzbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                client.out.println("/wyjdz");
            }
        });
        buttonpanel.add(wyjdzbutton);


        add(buttonpanel, BorderLayout.SOUTH);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ClientGraphical frame = new ClientGraphical();
                frame.setVisible(true);
            }
        });
    }
    public class Client implements Runnable {
        Socket client;
        BufferedReader in;
        PrintWriter out;
        String zespol;
        String username;
        boolean running = true;
        public Client(String username,String zespol) {
            this.username = username;
            this.zespol = zespol;
        }
        @Override
        public void run() {
            try{
                client = new Socket("127.0.0.1", 8000);
                out = new PrintWriter(client.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                InputHandler inHandler = new InputHandler();
                out.println("Username:"+username);
                out.println("UserTeam:"+zespol);
                Thread thread = new Thread(inHandler);
                thread.start();
                String inMesage;
                while((inMesage = in.readLine()) != null) {
                    String[] parts=inMesage.split(":");
                    if(parts[0].equals("statusliny")) {
                        if(Integer.parseInt(parts[1])>=50 || Integer.parseInt(parts[1])<=-50){
                            progressBar.setValue(0);
                            progressBar.update(progressBar.getGraphics());
                        }else {
                            progressBar.setValue(Integer.parseInt(parts[1]));
                            progressBar.update(progressBar.getGraphics());
                        }
                    }else {
                        System.out.println(inMesage);
                        label3.setText(AnsiHandler.removecolor(inMesage));
                    }
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
                        }else {
                                out.println(message);
                            }
                    }
                }catch (IOException e){
                    shutdown();
                }
            }
        }
    }
}
