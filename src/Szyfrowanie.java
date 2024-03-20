package src;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Szyfrowanie {
    public static void main(String[] args){
        String sciezka_do_pliku="C:\\Users\\games\\OneDrive\\Pulpit\\Studia\\rozproszone\\Laby_rozproszone\\pliki\\plik1.txt";

    }
    public static void szyfruj(String sciezka){
        try {
            FileReader odczyt=new FileReader(sciezka);
            int znak;
           // while(znak=odczyt.read())
        }
        catch (FileNotFoundException e){
            System.out.println("Plik nie znaleziony");
        }
    }
}
