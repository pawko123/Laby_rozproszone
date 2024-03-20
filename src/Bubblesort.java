package src;

import java.util.Arrays;
import java.util.Random;

public class Bubblesort{
    public static void main(String[] args) throws InterruptedException {
        int dlugosc=1000000;
        Random losuj=new Random();
        int[] tablica=new int[dlugosc];
        for(int i=0;i<dlugosc;i++){
            tablica[i]= losuj.nextInt(100)+1;
        }
        //System.out.println(Arrays.toString(tablica));
        int ilosc_watkow=4;
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
        //System.out.println(Arrays.deepToString(tablicatablic));
        //int ilosc_powtorzen=(int)(Math.log(ilosc_watkow) / Math.log(2));
        final int[][] wyniki= new int[ilosc_watkow/2][];

        for (int i=0;i<ilosc_watkow/2;i++){
            final int finali=i;
            tablica_watkow[finali]=new Thread(()->wyniki[finali]=Bubblesort.polacz_tablice(tablicatablic[finali*2],tablicatablic[finali*2+1]));
            tablica_watkow[finali].start();
        }
        for(int i=0;i<ilosc_watkow/2;i++){
            tablica_watkow[i].join();
        }

        int [] wynikowa_tablica=polacz_tablice(wyniki[0],wyniki[1]);
        System.out.println(Arrays.toString(wynikowa_tablica));
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
        int[][] tablicatablic = new int[ilosctablic][];
        for (int i = 0; i < ilosctablic; i++) {
            tablicatablic[i] = new int[dlugosc_tablic];
            System.arraycopy(tablica, i*dlugosc_tablic, tablicatablic[i], 0, dlugosc_tablic);
        }
        return tablicatablic;
    }
    public static int[] polacz_tablice(int[] tab1,int[] tab2){
        int dlugosc_nowej=tab1.length+tab2.length;
        int indeks_drugiej=0;
        int indeks_pierwszej=0;
        int[] polaczona_tablica=new int[dlugosc_nowej];
        for(int i=0;i<dlugosc_nowej;i++){
            if(indeks_pierwszej==tab1.length){
                polaczona_tablica[i]=tab2[indeks_drugiej];
                indeks_drugiej++;
            }
            else if (indeks_drugiej==tab2.length) {
                polaczona_tablica[i]=tab1[indeks_pierwszej];
                indeks_pierwszej++;
            }
            else {
                if (tab2[indeks_drugiej] > tab1[indeks_pierwszej]) {
                    polaczona_tablica[i] = tab1[indeks_pierwszej];
                    indeks_pierwszej++;
                }
                else {
                    polaczona_tablica[i] = tab2[indeks_drugiej];
                    indeks_drugiej++;
                }
            }
        }
        return polaczona_tablica;
    }
}