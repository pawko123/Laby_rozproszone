package src;

import java.io.*;

public class Szyfrowanie {
    public static void main(String[] args){
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
}
