
package com.mycompany.distribucionequipos;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class Main{
    public final String[] orden = {"Ingenieria", "Humanas", "Medicina", "Artes"};
    List<Facultad> facultades = new ArrayList();    
    ColaEnlazada<Lote> lotes = new ColaEnlazada();
    int i = 0;
    //DISTRIBUCION
    
    //Nos da la facultad que sigue en el orden de distribucion
    public Facultad ordenDis(){
        Facultad fac = this.facultades.get(0);
        for(int i=1; i<this.facultades.size(); i++){
            Facultad x = this.facultades.get(i);
            if(x.getEst() > fac.getEst() ){
                fac = x;
            }else if(x.getEst() == fac.getEst()){
                //Comparamos por nombres
                String nom = fac.getNombre();
                int ordFac = orden(nom);
                int ordX = orden(x.getNombre());
                //La facultad que este primera en orden sera la asignada
                if(ordX<ordFac){
                    fac = x;
                }
            }
        }
        
        return fac;
    }
    
    //Nos da la posicion de orden, nos dice cual debemos entregar si la cantidad de estudiantes son iguales
    public int orden(String nombre){
        int i = 0;
        while(!(nombre.equals(this.orden[i])) ){
            i ++;
        }
        return i;
    }
    
    //Distriuye un lote en una facultad
    public Lote distribucion(Facultad fac, Lote lot){
        int j = this.i; //COntrolador del ciclo, nos dice si debemos poner com, lap o tab
        while(fac.getEst() > 0 && lot.getTotal() > 0){
            int est = fac.getEst();
            Lote loteFac = fac.getLote();
            switch (j%3) {
                case 0:
                    //Deberiamos aignar computadores
                    if(lot.getCom()>0){
                        //Asignamos computador
                        fac.setEst(est-1); //Quitamos un estudiante
                        
                        int comFac = loteFac.getCom();
                        loteFac.setCom( comFac + 1 ); // La facultad a obtenido 1 computador mas
                        
                        int com = lot.getCom();
                        lot.setCom(com-1); //El lote tiene una compu menps                     
                        
                    }   break;
                case 1:
                    // Asignamos laptop
                    if(lot.getLap()>0){
                        fac.setEst(est-1); //Quitamos un estudiante
                        
                        int lapFac = loteFac.getLap();
                        loteFac.setLap( lapFac + 1 ); // La facultad a obtenido 1 laptop mas
                        
                        int lap = lot.getLap();
                        lot.setLap(lap-1); //El lote tiene una laptop menps
                    }   break;
                case 2:
                    //Asiganmos tablet
                    if(lot.getTab()>0){
                        fac.setEst(est-1); //Quitamos un estudiante
                        
                        int TabFac = loteFac.getTab();
                        loteFac.setTab( TabFac + 1 ); // La facultad a obtenido 1 tablet mas
                        
                        int tab = lot.getTab();
                        lot.setTab(tab-1); //El lote tiene una compu menps
                    }   break;
            }
            
            
            j++;
        }
        this.i = j;
        //System.out.println(fac.StringTo());
        return lot;
    }
    
    //Distribuye un lote para la facultad a la que le toca 
    public void distribuirLote(Lote lot){
        limpiarFacu();
        Facultad fac = ordenDis();
        //Antes de distribuir un lote limpiamos los registros        
        //System.out.println(fac.getLote().StringTo());
        //distribucion(fac,lot);
        while(lot.getTotal() != 0 && fac.getEst()>0){            
            lot = distribucion(fac,lot);
            //System.out.println("Esta aqui");
            //System.out.println("Lote: " + lot.StringTo());
            //System.out.println(fac.StringTo());
            fac = ordenDis();
        }
        if(lot.getTotal() != 0){ //Osea, si el ciclo se rompio fue pq sobraron equipos
            Node element = new Node(lot);
            this.lotes.Encolar(element);
        }
    }
    
    //Antes de cada nueva distribucion los contadores de lo que le toco a cada facultad debe ser cero
    public void limpiarFacu(){
        for(Facultad x: this.facultades){
            Lote loteN = new Lote(0,0,0);
            x.setLote(loteN);
        }
    }
    
    // OUTPUT
    
     //Ordenamos la lista de facultades para la impresion
    public List<Facultad> OrdenarBurbuja(List<Facultad> list, int n, int interc){
        if(interc == 0){
            return list;
        }else{
            interc = 0;
            for(int i=0; i<n-1; i++){
                if( list.get(i).getEst()>list.get(i+1).getEst() ){
                    Facultad aux1 = list.get(i+1);
                    list.set(i+1,list.get(i));
                    list.set(i,aux1);
                    interc += 1;
                }else if( list.get(i).getEst()==list.get(i+1).getEst() ){
                    int ordI = orden(list.get(i).getNombre());
                    int ordX = orden(list.get(i+1).getNombre());
                    if(ordI>ordX){
                        Facultad aux1 = list.get(i+1);
                        list.set(i+1,list.get(i));
                        list.set(i,aux1);
                        interc += 1;
                    }
                }
            }
            
            //System.out.println("List: " + list + " wrap: " + wrap);
            
            return OrdenarBurbuja(list,n,interc);
        }
      
    }
    
    public void imprimir(){
        List<Facultad> ordenada = OrdenarBurbuja(this.facultades, this.facultades.size(),1);
        for(Facultad x: ordenada){
            System.out.println(x.StringTo());
        }
    }
    
    // INPUT
    public void leerFacultades(String[] facu){
        for(int i=0; i<4; i++){
            Facultad x = new Facultad(facu[2*i],Integer.parseInt(facu[(2*i)+1])); 
            this.facultades.add(x);
        }
    }
    
    public void leerLotes(String[] lotes){

        Lote lote = new Lote(Integer.parseInt(lotes[2]), Integer.parseInt(lotes[4]), Integer.parseInt(lotes[6]));
        
        Node n = new Node(lote);
        this.lotes.Encolar(n);
    }
    
    /*
    public void prueba(Facultad prueba){
        prueba.setNombre("xd");
    }*/
    
    public static void main(String[] args) {
        Main xd = new Main();
        Scanner sc = new Scanner(System.in);
        String facultades = sc.nextLine();
        String[] facu = facultades.split(" ");
        xd.leerFacultades(facu);
        while(sc.hasNext()){
            String input = sc.nextLine();
            if(input.equals("Distribuir lote")){
                Lote lot = xd.lotes.Desencolar();
                xd.distribuirLote(lot);
            }else if(input.equals("Imprimir")){
                xd.imprimir();          
            }else{
                String[] lotes = input.split(" ");
                xd.leerLotes(lotes);
            }
        }
    }
}

class Facultad{
    private String nombre;
    private int estudiantes;
    private Lote lote; // cantidad de entregas que se han hecho
    
    
    public Facultad(String nombre, int estudiantes){
        this.estudiantes= estudiantes;
        this.nombre = nombre;
        this.lote = new Lote(0,0,0);
    }
    
    public Facultad(){
        this("",-1);
    }
    
    public Lote getLote(){
        return this.lote;
    }
    
    public void setLote(Lote lot){
        this.lote = lot;
    }
    
    public String getNombre(){
        return this.nombre;
    }
    
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
     public int getEst(){
        return this.estudiantes;
    }
    
    public void setEst(int est){
        this.estudiantes = est;
    }
    
    public String StringTo(){
        //String cad = "Facultad: " + this.nombre + " est: " + this.estudiantes +this.lote.StringTo();
        String cad = this.nombre + " " + this.estudiantes +" - " +this.lote.StringTo();
        return cad;
    
    }
    
}

class Lote{
    private int computers;
    private int laptops;
    private int tablets;
    private int total;
    
    public Lote(int com, int lap, int tab){
        this.computers = com;
        this.laptops = lap;
        this.tablets = tab;
        this.total = com+lap+tab;
    }
    
    public Lote(){
        this(0,0,0);
    }
    
    public int getTotal(){
        return this.computers + this.laptops + this.tablets;
    }
    
    public void setTotal(int total){
        this.total = total;
    }
    
    public int getCom(){
        return this.computers;
    }
    
    public void setCom(int com){
        this.computers = com;
    } 
    public int getLap(){
        return this.laptops;
    }
    
    public void setLap(int lap){
        this.laptops = lap;
    } 
    public int getTab(){
        return this.tablets;
    }
    
    public void setTab(int tab){
        this.tablets = tab;
    }  
    
    public String StringTo(){
        String cad = "Computers " + this.computers + " Laptops " + this.laptops + " Tablets " + this.tablets;
        return cad;
    }
    
}

class ColaEnlazada <T>{
    Node rear;
    Node front;
   
    public boolean empty(){
        return rear == null;
    }
    
    
    public void Encolar(Node<Character> element){
        if(!empty()){
            this.rear.setNext(element); //Seteamos a que apunte a este nuevo elemento
            this.rear = element; //Hacemos que ahora la referencia vaya a este nuevo elemento para que ahora sea el nuevo rear
        }
        else{
            this.rear = element;
            this.front = element;
        }
    }
    
    public T Desencolar(){
        if(empty()){
            throw new IllegalArgumentException("Esta vacío pa");
        }
        T data =  (T) front.getData();
        if(this.rear == this.front){ //Si solo hay un elemento  
            front = null;
            rear = null;           
        }else{
             front = front.getNext();
        }      
        return data;
    }
    
    public ColaEnlazada(){
           this.rear = null;
           this.front = null;
       }
    
    public void StringTo(){
        String cad = "[";
            if(empty()){
                cad += "]";
            }else{
                Node m = this.front;
                while(m != this.rear){
                    cad +=  m.getData() + ",";
                    m = m.getNext();
                }
                cad += m.getData() + "]";
            }     
            System.out.println(cad);
    }
    
}

class Node<T>{
    private T data;
    private Node next;
    
   public Node(){    
        this(null);
    }
    public Node(T data) {
        this.data = data;
        next = null;
    }
    
    public void setNext(Node n){
        this.next =n;
    }
    
    public Node getNext(){
        return this.next;
    }
    
    public void setData(T data){
        this.data = data;
    }
    
    public T getData(){
        return this.data;
    }
}