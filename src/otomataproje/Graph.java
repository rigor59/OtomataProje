/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otomataproje;

import java.util.ArrayList;
import java.util.List;


public class Graph {
      
    public List<Kenar> kenarlar;    //Oluşan kenarlar buraya kaydedilecek.
    public Node start;
    public Node end;
    public String dot_kod;      
    
    Regex regex2 = new Regex();
    
    public void setDot_kod(String dot_kod) {    //Görselin oluşması için gereken kod.
        this.dot_kod = "digraph \"\" {\n"
                + "start [shape= point]\n"
                + "start ->" + getStart() + "\n"
                + getEnd() + " [shape= doublecircle]\n" +
                regex2.transformNFA() + "\n}";
    }
    
    public Graph(){
        kenarlar = new ArrayList<Kenar>();      //Kenarlar bu listede tutulacak.
    }

    public List<Kenar> getKenarlar() {
        return kenarlar;
    }

    public Node getStart() {    //Stackten çekilen NFA graphların başlangıcını verir.
        return start;
    }

    public void setStart(Node start) {
        this.start = start;
    }

    public Node getEnd() {      //Stackten çekilen NFA graphların sonunu verir.
        return end;
    }

    public void setEnd(Node end) {
        this.end = end;
    }
    
    public void reset(){
        Node.reset();              // id = 0 işlemini yapıyor.
    }
//      ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
   
    
    public void Star(Object obj){   //Bunun yazılma amacı alınan objeyi manuel olarak GRAPH objesine dönüşümünü sağlamaktır.
        addStar((Graph) obj);       
    }

//      ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

    public void Union(Object obj1, Object obj2){    //Bunun yazılma amacı alınan objeleri manuel olarak GRAPH objesine dönüşümünü sağlamaktır.
        addUnion((Graph) obj1, (Graph) obj2);                  
		}
	
    
//      ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    
    
    public void Concat(Object obj1, Object obj2){   //Bunun yazılma amacı alınan objeleri manuel olarak GRAPH objesine dönüşümünü sağlamaktır.
        addConcat((Graph) obj1, (Graph) obj2);                
		}
       

//      ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||


    
    public static boolean KarakterveyaSayiMi(char c){       //Girilen ifadenin sayı veya alfabi mi yoksa sembol mü olduğunu kontrol ediyoruz.
        if(Character.isLetterOrDigit(c)){
            return true;
        }
        else{
            return false;       //Karakter veya sayı değilse sembol olur ve sembol gelirse ona göre farklı kodlar çalışmaktadır.
        }
    }  
    
    /////////////////////////////////////////////////////////////////////////
    
    public void alfabeNFA(char c){  //Girilen ifade a b gibi bir ifade ise bunu grapha dönüştürüyoruz. İki tane düğüm oluşturup        
        Node begNode = new Node();  //arasındaki çizgiye de girilen karakteri yazıyoruz.
        Node endNode = new Node();
        Kenar kenar = new Kenar(begNode, endNode, Character.toString(c));
        this.kenarlar.add(kenar); 
        this.start= begNode;    //Başlangıçlar ve sonlar tekrardan düzenlenir.
        this.end=endNode;
    }
    
    /////////////////////////////////////////////////////////////////////////
    
    public void addStar(Graph graph){   //Buraya bir adet graph gelecek ve onu belirli kurallara göre düğümlere bağlıyoruz.
        Node begNode = new Node();
        Node endNode = new Node();
        Kenar kenar1 = new Kenar(begNode, endNode, ":E:");
        Kenar kenar2 = new Kenar(begNode, graph.getStart(), ":E:");
        Kenar kenar3 = new Kenar(graph.getEnd(), endNode, ":E:");
        Kenar kenar4 = new Kenar(graph.getEnd(), graph.getStart(), ":E:");
        for(int i = 0; i<graph.getKenarlar().size(); i++){  //Listedeki tüm elemanların adedi kadar gez ve
            this.kenarlar.add(graph.getKenarlar().get(i));  //O anki kenarı kenarlar listesine ekle.
        }
        this.kenarlar.add(kenar1);  //Burada ve aşağıdaki 3 satırda oluşturduğumuz kenarları da listeye ekliyoruz.
        this.kenarlar.add(kenar2);
        this.kenarlar.add(kenar3);
        this.kenarlar.add(kenar4);
        this.start = begNode;
        this.end = endNode;
    }
    
   
    /////////////////////////////////////////////////////////////////////////


    
    public void addUnion(Graph graph1, Graph graph2){   //Buraya bir adet graph gelecek ve onu belirli kurallara göre düğümlere bağlıyoruz.
        Node begNode = new Node();
        Node endNode = new Node();   
        Kenar kenar1 = new Kenar(begNode, graph1.getStart(), ":E:");
        Kenar kenar2 = new Kenar(begNode, graph2.getStart(), ":E:");
        Kenar kenar3 = new Kenar(graph1.getEnd(), endNode, ":E:");
        Kenar kenar4 = new Kenar(graph2.getEnd(), endNode, ":E:");
        this.start = begNode;
        this.end = endNode;
        for(int i=0; i<graph1.getKenarlar().size(); i++){   //Listedeki tüm elemanların adedi kadar gez ve
            this.kenarlar.add(graph1.getKenarlar().get(i)); //O anki kenarı kenarlar listesine ekle.
        }
        for(int i=0; i<graph2.getKenarlar().size(); i++){   //Her iki graph için bunu yapıyoruz.
            this.kenarlar.add(graph2.getKenarlar().get(i));
        }        
        this.kenarlar.add(kenar1);
        this.kenarlar.add(kenar2);
        this.kenarlar.add(kenar3);
        this.kenarlar.add(kenar4);
    }        
    

////////////////////////////////////////////////////////////////////////////////////
 
    public void addConcat(Graph graph1, Graph graph2) {     //addUnion işleminde yaptığımızın aynısını yaapıyoruz.
	Kenar kenar1 = new Kenar(graph1.getEnd(), graph2.getStart(), ":E:"); //Concat işlemi için bir tane kenar yeterlidir.
	this.start = graph1.getStart();
	this.end = graph2.getEnd();
	for (int i = 0; i < graph1.getKenarlar().size(); i++) {
		this.kenarlar.add(graph1.getKenarlar().get(i));
	}
	for (int i = 0; i < graph2.getKenarlar().size(); i++) {
		this.kenarlar.add(graph2.getKenarlar().get(i));
	}
            this.kenarlar.add(kenar1);
	}
 
/////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        /*String yazdir = "\nBaşlangıç: " + "s" +this.start + " Son: " + "s" + this.end + "\n";     //normal bir gösterim için bunu yazdırıyorduk ama graphın görselleştirilmesi için 
        for(int i = 0; i < kenarlar.size();i++){                                                    //aşağıdaki bir şekilde yazdırmam gerekmektedir.
            yazdir += kenarlar.get(i) + "\n";
        }
        return yazdir;*/
        
        String yazdir = "digraph \"graph\" {\nrankdir = LR;\nstart [shape=point]" + "\nstart " + "-> s" +this.start + "\n" + "s" + this.end + "[shape= doublecircle]\n";
        for(int i = 0; i < kenarlar.size();i++){
            yazdir += kenarlar.get(i) + "\n";
        }
        yazdir += "}";
        return yazdir;  //Burada graphı görselleştiren kod yazdırılmaktadır.
    }

/////////////////////////////////////////////////////////////////////////////////////
    
}
