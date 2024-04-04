package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Szyfrowanie extends JFrame {
    private static JProgressBar[] progressBars;
    private JButton startButton;
    private int numThreads = 5;

    public Szyfrowanie() {
        setTitle("Multi-Thread Progress Bar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(numThreads, 1));
        progressBars = new JProgressBar[numThreads];

        for (int i = 0; i < numThreads; i++) {
            progressBars[i] = new JProgressBar(0, 100);
            progressBars[i].setValue(0);
            progressBars[i].setStringPainted(true);
            panel.add(progressBars[i]);
        }

        add(panel, BorderLayout.CENTER);

        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                start(numThreads);
            }
        });
        add(startButton, BorderLayout.SOUTH);
    }
        public static void start(int iloscwatkow){
        //laptop - C:\Users\games\OneDrive\Pulpit\Studia\rozproszone\Laby_rozproszone\pliki
        //komputer - C:\Users\Paweł\IdeaProjects\Laby_rozproszone\pliki
            String sciezka_do_folderu="C:\\Users\\Paweł\\IdeaProjects\\Laby_rozproszone\\pliki";
            File folder=new File(sciezka_do_folderu);
            File[] tablicaplikow=folder.listFiles();
            Thread[] watki=new Thread[iloscwatkow];
            BlockingQueue<File> kolejka = new ArrayBlockingQueue<>(tablicaplikow.length);
            kolejka.addAll(Arrays.asList(tablicaplikow));
            for(int i=0;i< iloscwatkow;i++) {
                int finalI = i;
                watki[i]=new Thread(()-> {
                    while(!kolejka.isEmpty()) {
                        try {
                            szyfruj(kolejka.take(), finalI);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                watki[i].start();
            }
            System.gc();
        }
        public static void szyfruj(File plik,int nr_watku){
            try {
                FileReader odczyt=new FileReader(plik);
                StringBuilder zaszyfrowanytekst = new StringBuilder();
                int znakint;
                int iloscznakow=0;
                while((znakint=odczyt.read())!=-1){
                    char znak=(char)znakint;
                    char zaszyfrowanyznak=szyfrowaniecezara(znak);
                    zaszyfrowanytekst.append(zaszyfrowanyznak);
                    iloscznakow++;
                    int procent=(int) Math.round(((double) (iloscznakow) / (double) plik.length()) * 100);
                    if (procent%10==0) {
                        if(procent!=progressBars[nr_watku].getValue()) {
                            progressBars[nr_watku].setValue(procent);
                            progressBars[nr_watku].update(progressBars[nr_watku].getGraphics());
                            System.out.println("Postęp dla watku nr" + nr_watku + ": " + progressBars[nr_watku].getValue() + "%");
                        }
                    }
                }
                odczyt.close();
                BufferedWriter zapis=new BufferedWriter(new FileWriter(plik));
                String finalnytekst = zaszyfrowanytekst.toString();
                //System.out.println(finalnytekst);
                zapis.write(finalnytekst);
                zapis.close();
            }
            catch (FileNotFoundException e){
                System.out.println("Plik nie znaleziony");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        public static char szyfrowaniecezara(char znak){
            if (!Character.isAlphabetic(znak)) {
                return znak;
            }
            if(znak=='X' || znak=='x'){
                return Character.isUpperCase(znak) ? 'A' : 'a';
            }
            if(znak=='Y' || znak=='y'){
                return Character.isUpperCase(znak) ? 'B' : 'b';
            }
            if(znak=='Z' || znak=='z'){
                return Character.isUpperCase(znak) ? 'C' : 'c';
            }
            else{
                return znak+=3;
            }
        }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Szyfrowanie frame = new Szyfrowanie();
                frame.setVisible(true);
            }
        });
    }
}