package src;

import java.util.Arrays;
import java.util.Random;

public class Bubblesort{
    public static void main(String[] args) throws InterruptedException {
        int dlugosc=10000;
        Random losuj=new Random();
        int[] tablica=new int[dlugosc];
        for(int i=0;i<dlugosc;i++){
            tablica[i]= losuj.nextInt(100)+1;
        }

        System.out.println(Bubblesort.test_bubblesort(tablica));
        System.out.println(Bubblesort.test_multithread(tablica));
    }
    public static long test_bubblesort(int[] tablica){
        long startTime = System.currentTimeMillis();
        Bubblesort.sort(tablica);
        //System.out.println(Arrays.toString(tablica));
        return System.currentTimeMillis() - startTime;
    }
    public static long test_multithread(int[] tablica) throws InterruptedException {
        int ilosc_watkow=4;
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
        final int[][] wynik1 = {new int[1]};
        final int[][] wynik2 = {new int[1]};
        tablica_watkow[0]=new Thread(()-> wynik1[0] =merge_tables(tablicatablic[0],tablicatablic[1]));
        tablica_watkow[1]=new Thread(()-> wynik2[0] =merge_tables(tablicatablic[2],tablicatablic[3]));
        tablica_watkow[0].start();
        tablica_watkow[1].start();
        tablica_watkow[0].join();
        tablica_watkow[1].join();

        int[] wynikowa_tablica=merge_tables(wynik1[0],wynik2[0]);
        //System.out.println(Arrays.toString(wynikowa_tablica));
        return System.currentTimeMillis() - startTime;
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