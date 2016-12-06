package fam;

import java.io.RandomAccessFile;

/**
 *
 * @author MiguelRam
 */
public class IndexFile {

    String clave;
    int pos;
    StringBuffer buffer;
    RandomAccessFile archiIn;

    public IndexFile() throws Exception {
        archiIn = new RandomAccessFile("indice.txt", "rw");
    }

    public void escribir(String cve, int posi) throws Exception{     
        StringBuffer buffer = null;
        
        buffer = new StringBuffer(cve);
        buffer.setLength(20);
        archiIn.writeUTF(buffer.toString());
        
        archiIn.writeInt(posi);
    }
    
    public void cerrarArchivo()throws Exception{
        archiIn.close();
    }
}
