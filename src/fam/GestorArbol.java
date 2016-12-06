package fam;

import java.io.RandomAccessFile;

/**
 *
 * @author MiguelRam
 */
public class GestorArbol {

    Arbol arbol;
    RandomAccessFile archiIn;
    String cve;
    int posicion;

    public void crearArbol() throws Exception {
        archiIn = new RandomAccessFile("indice.txt", "r");
        arbol = new Arbol();

        //  ciclo para leer los registros del archivo indice e ingresarlos al árbol de uno en uno
        while (archiIn.getFilePointer() != archiIn.length()) {
            cve = archiIn.readUTF();
            posicion = archiIn.readInt();
            arbol.insertar(cve, posicion);//    insertamos en el árbol
        }
        archiIn.close();
    }

    public int buscar(String cve) {
        return arbol.buscar(cve);
    }
    public void cerrar() throws Exception {
        archiIn.close();
    }
    
   
    
    //===========================================================================
    
}