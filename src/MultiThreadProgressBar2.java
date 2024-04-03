package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class MultiThreadProgressBar2 extends JFrame {
    private static JProgressBar[] progressBars;
    private JButton startButton;
    private int numThreads = 8;

    public MultiThreadProgressBar2() {
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
                startThreads();
            }
        });
        add(startButton, BorderLayout.SOUTH);
    }

    private void startThreads() {
        start(numThreads);
    }
        public static void start(int watki){
            String sciezka_do_folderu="C:\\Users\\games\\OneDrive\\Pulpit\\Studia\\rozproszone\\Laby_rozproszone\\pliki";
            File folder=new File(sciezka_do_folderu);
            File[] tablicaplikow=folder.listFiles();
            Thread[] watki=new Thread[tablicaplikow.length];
            for(int i=0;i< tablicaplikow.length;i++) {
                final int finali=i;
                watki[i]=new Thread(()->szyfruj(tablicaplikow[finali], finali%4));
                watki[i].start();
            }
            try {
                for (Thread watek : watki) {
                    watek.join();
                }
            }
            catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }
        public static void szyfruj(File plik,int nr_watku){
            try {
                FileReader odczyt=new FileReader(plik);
                StringBuilder zaszyfrowanytekst = new StringBuilder();
                int znakint;
                int iloscznakow=0;
                int progres=0;
                while((znakint=odczyt.read())!=-1){
                    char znak=(char)znakint;
                    char zaszyfrowanyznak=szyfrowaniecezara(znak);
                    zaszyfrowanytekst.append(zaszyfrowanyznak);
                    iloscznakow++;
                    int procent=(int) Math.round(((double) (iloscznakow) / (double) plik.length()) * 100);
                    if (procent%10==0) {
                        if(procent!=progres) {
                            progres=procent;
                            System.out.println("Postęp dla watku nr" + nr_watku + ": " + progres + "%");
                        }
                    }
                    Thread.sleep(1);
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
            } catch (InterruptedException e) {
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
                MultiThreadProgressBar2 frame = new MultiThreadProgressBar2();
                frame.setVisible(true);
            }
        });
    }
}