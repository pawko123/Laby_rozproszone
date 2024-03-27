package src;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Random;
import javax.swing.*;

public class MultiThreadProgressBar extends JFrame {
    private static JProgressBar[] progressBars;
    private JButton startButton;
    private int numThreads = 16;

    public MultiThreadProgressBar() {
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
        int[] tablica=stworz_tablice(200000);
        long[] tablica_czasow=new long[10];
        tablica_czasow[0]=test_multithread(tablica,1);
        tablica_czasow[1]=test_multithread(tablica,2);
        tablica_czasow[2]=test_multithread(tablica,4);
        tablica_czasow[3]=test_multithread(tablica,8);
        tablica_czasow[4]=test_multithread(tablica,16);
        tablica_czasow[5]=test_multithread(tablica,32);
        tablica_czasow[6]=test_multithread(tablica,64);
        tablica_czasow[7]=test_multithread(tablica,128);
        tablica_czasow[8]=test_multithread(tablica,256);
        tablica_czasow[9]=test_multithread(tablica,512);

        System.out.println(Arrays.toString(tablica_czasow));
    }
    public static int[] stworz_tablice(int dlugosc){
        Random losuj=new Random();
        int[] tablica=new int[dlugosc];
        for(int i=0;i<dlugosc;i++){
            tablica[i]= losuj.nextInt(100)+1;
        }
        return tablica;
    }
    public static long test_multithread(int[] tablica, int ilosc_watkow){
        long startTime = System.currentTimeMillis();
        int[][] tablicatablic = podziel_tablice(tablica, ilosc_watkow,tablica.length/ilosc_watkow);
        Thread[] tablica_watkow=new Thread[ilosc_watkow];
        for (int i=0;i<ilosc_watkow;i++) {
            int finalI = i;
            tablica_watkow[i]= new Thread(() -> sort(tablicatablic[finalI],finalI));
            tablica_watkow[i].start();
        }
        for(Thread watek:tablica_watkow){
            try {
                watek.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        //System.out.println(Arrays.deepToString(tablicatablic));
        int[] wynikowa_tablica = merging(tablicatablic);
        //System.out.println(Arrays.toString(wynikowa_tablica));
        return System.currentTimeMillis() - startTime;
    }
    public static int[] merging(int[][] tab){
        if(tab.length==1){
            return tab[0];
        }
        if(tab.length!=2) {
            final int[][] wyniki = new int[tab.length/2][];
            Thread[] tablica_watkow = new Thread[tab.length/2];
            for (int i = 0; i < tab.length/2; i++) {
                final int finali = i;
                tablica_watkow[i] = new Thread(() -> wyniki[finali] = merge_tables(tab[finali * 2], tab[finali * 2 + 1]));
                tablica_watkow[i].start();
            }
            for (Thread watek : tablica_watkow) {
                try {
                    watek.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            return merging(wyniki);
        }
        else{
            return merge_tables(tab[0],tab[1]);
        }
    }
    public static void sort(int[] tablica,int nr_watku) {
        for (int i = 0; i < tablica.length - 1; i++) {
            for (int j = 0; j < tablica.length - i - 1; j++) {
                if (tablica[j] > tablica[j + 1]) {
                    swap(j, j + 1, tablica);
                }
            }
            int procent=(int) Math.ceil(((double) (i + 2) / (double) tablica.length) * 100);


            //System.out.println(progressBars[nr_watku].getValue());
            if(procent%10==0) {
                if(procent!=progressBars[nr_watku].getValue()) {
                    progressBars[nr_watku].setValue(procent);
                    progressBars[nr_watku].update(progressBars[nr_watku].getGraphics());
                }
            }
        }
    }
    public static void swap(int i,int j,int[] tab)  {
        int temp=tab[i];
        tab[i]=tab[j];
        tab[j]=temp;
    }
    public static int[][] podziel_tablice(int[] tablica, int ilosctablic,int dlugosc_tablic) {
        int[][] arrayOfArrays = new int[ilosctablic][];
        for (int i = 0; i < ilosctablic; i++) {
            arrayOfArrays[i] = new int[dlugosc_tablic];
            System.arraycopy(tablica, i*dlugosc_tablic, arrayOfArrays[i], 0, dlugosc_tablic);
        }

        return arrayOfArrays;
    }
    public static int[] merge_tables(int[] tablica1,int[] tablica2){
        int dlugosc_nowej_tabeli=tablica1.length+tablica2.length;
        int[] nowa_tablica=new int[dlugosc_nowej_tabeli];
        int indeks_pierwszej=0;
        int indeks_drugiej=0;
        for(int i=0;i<nowa_tablica.length;i++){
            if(indeks_pierwszej==tablica1.length){
                nowa_tablica[i]=tablica2[indeks_drugiej];
                indeks_drugiej++;
            }
            else if (indeks_drugiej==tablica2.length) {
                nowa_tablica[i]=tablica1[indeks_pierwszej];
                indeks_pierwszej++;
            }
            else {
                if (tablica1[indeks_pierwszej] > tablica2[indeks_drugiej]) {
                    nowa_tablica[i] = tablica2[indeks_drugiej];
                    indeks_drugiej++;
                } else {
                    nowa_tablica[i] = tablica1[indeks_pierwszej];
                    indeks_pierwszej++;
                }
            }
        }

        return nowa_tablica;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MultiThreadProgressBar frame = new MultiThreadProgressBar();
                frame.setVisible(true);
            }
        });
    }
}