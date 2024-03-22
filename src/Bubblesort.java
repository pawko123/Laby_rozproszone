package src;

import java.util.Arrays;
import java.util.Random;

public class Bubblesort{
    public static void main(String[] args) throws InterruptedException {
        int dlugosc=100000;
        int ilosc_watkow=16;
        Random losuj=new Random();
        int[] tablica=new int[dlugosc];
        for(int i=0;i<dlugosc;i++){
            tablica[i]= losuj.nextInt(100)+1;
        }
        System.out.println(Bubblesort.test_multithread(tablica,ilosc_watkow));
    }
    public static long test_multithread(int[] tablica,int ilosc_watkow) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        int[][] tablicatablic = podziel_tablice(tablica, ilosc_watkow,tablica.length/ilosc_watkow);
        Thread[] tablica_watkow=new Thread[ilosc_watkow];
        for (int i=0;i<ilosc_watkow;i++) {
            int finalI = i;
            tablica_watkow[i]= new Thread(() -> Bubblesort.sort(tablicatablic[finalI]));
            tablica_watkow[i].start();
        }
        for(Thread watek:tablica_watkow){
            watek.join();
        }
        int[] wynikowa_tablica = merging(tablicatablic);


       //System.out.println(Arrays.toString(wynikowa_tablica));
        return System.currentTimeMillis() - startTime;
    }
    public static int[] merging(int[][] tab) throws InterruptedException {
        if(tab.length!=2) {
            final int[][] wyniki = new int[tab.length/2][];
            Thread[] tablica_watkow = new Thread[tab.length/2];
            for (int i = 0; i < tab.length/2; i++) {
                final int finali = i;
                tablica_watkow[i] = new Thread(() -> wyniki[finali] = merge_tables(tab[finali * 2], tab[finali * 2 + 1]));
                tablica_watkow[i].start();
            }
            for (Thread watek : tablica_watkow) {
                watek.join();
            }
            return merging(wyniki);
        }
        else{

            return merge_tables(tab[0],tab[1]);
        }
    }
    public static void sort(int[] tablica){
        for(int i=0;i<tablica.length-1;i++){
            for(int j=0;j<tablica.length-i-1;j++){
                if(tablica[j]>tablica[j+1]){
                    Bubblesort.swap(j,j+1,tablica);
                }
            }
        }
    }
    public static void swap(int i,int j,int[] tab){
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
}