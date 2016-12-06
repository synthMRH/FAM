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
public class Arbol {

    Nodo raiz = null;

    public void insertar(String llave, int p) {
        Nodo nuevo;
        Nodo ant = null;
        Nodo rec;

        if (raiz == null) {
            raiz = new Nodo(llave, p);
            //System.out.println("Valor insertado " + llave);
        } else {
            nuevo = new Nodo(llave, p);
            //System.out.println("Valor insertado " + llave);
            rec = raiz;
            while (rec != null) {
                ant = rec;
                if (rec.clave.compareTo(nuevo.clave) > 0) {
                    rec = rec.izq;
                } else {
                    rec = rec.der;
                }
            }

            if (ant.clave.compareTo(nuevo.clave) > 0) {
                ant.izq = nuevo;
            } else {
                ant.der = nuevo;
            }
        }
    }
    
    public int buscar(String cve){
        int pos=0;
        Nodo rec,ant;
        
        if (raiz==null) {
            System.out.println("El árbol está vacío");
        }else{
            rec=raiz;
            ant=raiz;
            
            while(!(ant.clave.trim().equals(cve))&&rec!=null){
                ant=rec;
                if (rec.clave.trim().compareTo(cve) > 0) 
                    rec = rec.izq;
                 else 
                    rec = rec.der;
            } 
                if (ant.clave.trim().equals(cve)) {
                    System.out.println("El valor si se encuentra "+ant.clave);
                    pos=ant.dl;
                    //System.out.println("la posicion es "+pos);
                }else
                    System.out.println("El valor "+cve+" no se encuetra");
            }
            return pos;
        }//fin de buscar
    }

    
    

