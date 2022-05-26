
package otomataproje;

import java.util.Stack;

public class Regex {

    private String ayristilan = "";
    private Stack operatorStack;        
    private Stack operandStack;  
    private int [][] oncelik = {
		{ 1, 1, 1, -1, 1, 1 },// *&|()#         peek(), anlik   EXCEL'de tablo şekilde hazırlandı.
		{ -1, 1, 1, -1, 1, 1 },
                { -1, -1, 1, -1, 1, 1 },
		{ -1, -1, -1, -1, 0, 2 },
                { 1, 1, 1, 1, 1, 1 }, 
		{ -1, -1, -1, -1, -1, -1 }        
    };
    
    public int oncelikOperatoru(Character c1, Character c2){    //sembollerin önceliklerini karşılaştırarak bir değer döndürüyoruz.
        String oncelikString = "*&|()#";
        return this.oncelik[oncelikString.indexOf(c1.toString())][oncelikString.indexOf(c2.toString())];
    }    

    public Regex() {   
	ayristilan = "";
	operatorStack = new Stack();
	operandStack = new Stack();          
    }
 
    public Regex(String girdi) {    //İçine regex verilirse constructer çalışır ve fonksiyona yollanır.
	ayristilan = "";
	operatorStack = new Stack();
	operandStack = new Stack();
	parcala(girdi);            
    }

    public String getAyristilan() { //Parse edilen stringi döndürür.
	return ayristilan;
    }
 
    public void setAyristilan(String girdi) {
        parcala(girdi);       
    }    
 
////////////////////////////////////////////////////////////    
    
    public static boolean KarakterveyaSayiMi(char c){       //Girilen ifadenin sayı veya alfabi mi yoksa sembol mü olduğunu kontrol ediyoruz.
        if(Character.isLetterOrDigit(c)){
            return true;
        }
        else{
            return false;       //Karakter veya sayı değilse sembol olur.
        }
        //return Character.isLetterOrDigit(c);
    }     

////////////////////////////////////////////////////////////    

	public Graph transformNFA() {   //Ayrıştırılan (parse edilen) regex'i buraya yollarız.
		if (ayristilan.length() == 0)   //regex girilmediyse null değer dönmektedir.
			return null;
		else {
			int i = 0;      //while döngüsü için kullanıldı.
			operatorStack.push('#');    //operatorStack'in en alt kısmına # koyuyoruz. (İlerleyen kodda döngüyü bitirmek için kullanacağız.)
			char[] alfabe = (ayristilan + "#").toCharArray();   //Burada # işareti alfabe harf dizimizin en sonuna eklenmektedir. Örnek: a&b -> a&b#
                        
			while (alfabe[i] != '#' || (Character) (operatorStack.peek()) != '#') { //o anki eleman # değilse veya operatorStack'in en üstündeki # değilse
				if (KarakterveyaSayiMi(alfabe[i])) {    //karakter veya sayi mi diye kontrol ediyoruz.
                                    Graph graph0 = new Graph();
                                    graph0.alfabeNFA(alfabe[i]);        //girilen karakter grapha dönüştürülür.
					operandStack.push(graph0);      //graphı stack'e ekliyoruz.
					i++;
				} else {                                            
					int deger=oncelikOperatoru((Character)(operatorStack.peek()), alfabe[i]);   //operatörlerin önceliklerini karşılaştır.
					switch (deger) {
					case 1:
						Character character=(Character)operatorStack.pop(); //stackten bir eleman çıkar
						switch (character) {
						case '*':
							Object obj=operandStack.pop();
							Graph graph1=new Graph();
							graph1.Star(obj);
							operandStack.push(graph1);
							break;
						case '&':
							Object obj2=operandStack.pop();
							Object obj1=operandStack.pop();
							Graph graph2=new Graph();                                                        
							graph2.Concat(obj1, obj2);      //obj1 ile obj2'nin yeri çok önemli. ilk obj1 yollanmalıdır.
							operandStack.push(graph2);
							break;
						case '|':
							Object obj4=operandStack.pop();
							Object obj3=operandStack.pop();
							Graph graph3=new Graph();
							graph3.Union(obj3, obj4);   //obj3 ile obj4'ün yeri çok önemli. ilk obj3 yollanmalıdır.
							operandStack.push(graph3);
							break;
						default:
							break;
						}
						break;
					case 0:
						operatorStack.pop();    //Parantezleri silme işleminde kullanılıyor.
						i++;    //i 1 artınca ) işareti de otomatik olarak geçildi.
						break;
					case -1:    //Matrisin kesişen ifadesi -1 ise (GENELLİKLE operatör üstünlüğü için kullanılır.)
						operatorStack.push(alfabe[i]);      //Bunu operatorStack kısmına  ekle. 
						i++;
						break;
					default:
						break;
					}
				}
			}
			return (Graph) operandStack.pop();  //en son kalan graphı döndür.
		}
	}


///////////////////////////////////////////////////////////////////////////////////
    public void reset(){
	Node.reset();
	operandStack.clear();
	operatorStack.clear();
	}
 
//////////////////////////////////////////////////////////////////////////////////

    public void parcala(String girdi) { //PARSE İŞLEMİ
    char[] regexChar = girdi.replaceAll(" ", "").toCharArray();       // boşlukları siliyoruz ve orada yazan tüm elemanları regexChar aldı bir dizinin içine atıyoruz. (CHAR tipinde)
    	for (int i = 0; i < regexChar.length; i++) {       // tüm elemanlari tek tek geziyoruz.
    		if (i == 0){
                        ayristilan += regexChar[i];     // dizinin ilk elemanını stringe ekledik. bu adım tek bir kez koşulur. geri kalan elemanlar belirli koşullardan geçirilerek eklenir.
                    }				
    		else {  //ilk eleman değilse
    			if (regexChar[i] == '|' || regexChar[i] == '*' || regexChar[i] == ')') { // gezdiği eleman | * veya ) ise  
    				ayristilan += regexChar[i]; //onları da stringe ekliyoruz
    			} else {    // eğer gezilen elemanlar | * veya ) lerden biri değilse
    				if (regexChar[i - 1] == '(' || regexChar[i - 1] == '|')   // gezilen bir önceki eleman ( veya | ise
    					ayristilan += regexChar[i]; // bunu da stringe ekle
                                        else{
                                           ayristilan += ("&" + regexChar[i]);  // eğer onlardan hiçbiri değilse yani ab gibi bir veri girildiyse onun önüne & sembolünü koyarak stringe ekle.
                                        }				
    			}
    		}
            }   
            
        }    
    
    
}
 
    
