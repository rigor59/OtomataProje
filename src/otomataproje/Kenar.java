/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otomataproje;



//DÜĞÜMLERİ BİRBİRİNE BAĞLAYAN KENARLARIN CLASS'I

public class Kenar {
    
    private Node begin;     //Node türünden begin ve end oluşturduk.
    private Node end;
    private String etiket;
    private Kenar kenar;

    
    public Kenar(){ //Gerek kalmadı.
    }
    
    public Kenar(Node begin, Node end) {    //Gerek kalmadı.
        this.begin = begin;
        this.end = end;
    }

    
    public Kenar(Node begin, Node end, String etiket) {
        this.begin = begin;
        this.end = end;
        this.etiket = etiket;
    }

    
    public Node getBegin() {
        return begin;
    }

    public void setBegin(Node begin) {
        this.begin = begin;
    }

    public Node getEnd() {
        return end;
    }

    public void setEnd(Node end) {
        this.end = end;
    }

    public String getEtiket() {
        return etiket;
    }

    public void setEtiket(String etiket) {
        this.etiket = etiket;
    }

    public Kenar getKenar() {
        return kenar;
    }

    public void setKenar(Kenar kenar) {
        this.kenar = kenar;
    }
   
	
    @Override
    public String toString() {
        return "s" + begin+ " " + "->" + " " + "s" +end + " [label= " + "\"" + etiket+ "\"" +"]";
	}  
    
    
    

}

