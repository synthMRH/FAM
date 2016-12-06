/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fam;

/**
 *
 * @author MiguelRam
 */
public class Nodo {
    String clave;
    int dl;
    Nodo izq;
    Nodo der;
    
    public Nodo(String ele,int dir,Nodo I,Nodo D){
        clave=ele;
        dl=dir;
        izq=I;
        der=D;
    }
    
    public Nodo(String ele,int dir){
        this(ele,dir,null,null);
    }
}
