/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fam;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

/**
 *
 * @author MiguelRam
 */
public class BaseFile {

    static int pos = 1;
    String clave;
    Scanner entrada;
    //  ARCHIVO INDICE
    IndexFile objIndice;
    RandomAccessFile archi;
    StringBuffer buffer;
    float inicio, fin, temp;
    int cont = 0, iniDom, finDom;
    //Registro objRegistro;

    public BaseFile(String file) throws Exception {
        entrada = new Scanner(System.in);
        archi = new RandomAccessFile(file + ".txt", "rw");
        objIndice = new IndexFile();
        //objRegistro = new Registro();
    }

    public BaseFile() throws Exception {
        entrada = new Scanner(System.in);
        //archi = new RandomAccessFile(file+".txt", "rw");
        objIndice = new IndexFile();
        //objRegistro = new Registro();
    }

    public void openFile(String file) throws Exception {
        archi = new RandomAccessFile(file + ".txt", "rw");
    }

    public void escribirArchivoMaestro() throws Exception {
        int h;
        //entrada = new Scanner(System.in);
        do {
            //  Ingresar CLAVE
            System.out.println("Ingresa concepto");
            clave = entrada.next();
            buffer = new StringBuffer(clave);
            buffer.setLength(20);
            archi.writeChars(buffer.toString());

            //escribir en el archivo índice
            objIndice.escribir(buffer.toString(), pos);
            pos++;

            //  Inicio del dominio
            System.out.println("Ingresa el inicio del dominio");
            iniDom = Integer.parseInt(entrada.next());

            //  Ingresar etiquetas y puntos criticos
            inicio = iniDom;
            fin = 0;
            temp = 0;
            System.out.println("Cuantas conjuntos son?");
            int noEtiquetas = entrada.nextInt();
            String[] inter;
//  -------------------ESCRIBIR EL NÚMERO DE ETIQUETAS DADAS POR EL USUARIO Y SUS PUNTOS CRITICOS 
            for (int i = 0; i < noEtiquetas; i++) {
                System.out.println("Ingresa nombre del conjunto");
                String tag = entrada.next();
                String tipoFUN;

                do {
                    System.out.println("Ingresa su tipo de función TRA=Trapezoidal; TRI=Triangular; SIG=Sigmoidal; GAU=Gauus");
                    tipoFUN = entrada.next();
                } while (!(tipoFUN.equalsIgnoreCase("TRI")) && !(tipoFUN.equalsIgnoreCase("TRA")) && !(tipoFUN.equalsIgnoreCase("SIG")) && !(tipoFUN.equalsIgnoreCase("GAU")));

                int cantPCs = 0;
                int noPCs = 0;
                int q = 0;
                float pc1 = 0, pc2;
                do {
                    System.out.println("Ingresa punto critico");
                    String o = entrada.next();
                    Float punCri = Float.parseFloat(o);
                    // archi.writeFloat(punCri);//----escribir pC
                    if (cantPCs != 1) {
                        System.out.println("Quieres ingresar otro punto crítico? 1=SI;2=NO");
                        q = entrada.nextInt();
                        pc1 = punCri;
                    }
                    pc2 = punCri;
                    cantPCs++;
                } while (q == 1 && cantPCs < 2);

                inter = getIntervalo(cantPCs, pc1, pc2);

                //  Write Tag in File
                System.out.println("Conjunto " + tag);
                buffer = new StringBuffer(tag);
                buffer.setLength(20);
                archi.writeChars(buffer.toString());

                //  Write Type in File
                System.out.println("Tipo funcion " + tipoFUN);
                buffer = new StringBuffer(tipoFUN);
                buffer.setLength(5);
                archi.writeChars(buffer.toString());

                System.out.println("Puntos Criticos:" + pc1 + "   p2=" + pc2);
                System.out.println("INTERVALOS:");

                //  Write Intervalo
                for (int j = 0; j < inter.length; j++) {
                    float v = Float.parseFloat(inter[j]);
                    archi.writeFloat(v);
                    System.out.print(v + ", ");
                }
                System.out.println("\n");
            }

//  -------------------SI EL USUARIO INGRESÓ MENOS DE 7 ETIQUETAS, RELLENAR EL ESPACIO EN REGISTRO CON ETIQUETAS VACÍAS             
            if (noEtiquetas < 7) {
                for (int i = noEtiquetas; i < 7; i++) {
                    //  Escribir etiqueta vacía
                    buffer = new StringBuffer(".");
                    buffer.setLength(20);
                    archi.writeChars(buffer.toString());

                    //  Write Type in File
                    buffer = new StringBuffer(".");
                    buffer.setLength(5);
                    archi.writeChars(buffer.toString());
                    //  Write Intervalo
                    for (int j = 0; j < 4; j++) {
                        float v = -1;
                        archi.writeFloat(v);
                    }
                }
            }
            //  -------------------ESCRIBIR EL UNIVERSO DE DISCURSO DE LA ETIQUETA
            System.out.println("Ingresa el fin  del dominio");
            finDom = entrada.nextInt();
            archi.writeInt(finDom);

            System.out.println("Quieres ingresar otro concepto? Teclea 1 para SI o algún otro número para NO");
            h = entrada.nextInt();

        } while (h == 1);
        System.out.println("ESCRITURA FINALIZADA");
        archi.close();
        objIndice.cerrarArchivo();
    }

    //===========================================================================

    public void closeFile() throws Exception {
        archi.close();
    }

    //===========================================================================
    public String[] getIntervalo(int PCs, float p1, float p2) {
        String[] intervalo = new String[4];

        if (PCs == 1) {
            if (cont == 0) {
                inicio = iniDom;
                fin = (p1 - inicio) * 2;
                intervalo[0] = inicio + "";
                intervalo[1] = p1 + "";
                intervalo[2] = p2 + "";
                intervalo[3] = fin + "";
                temp = fin - p1;
            } else {
                inicio = (float) (fin - (temp * .4));
                fin = (p1 - inicio) + p1;

                intervalo[0] = inicio + "";
                intervalo[1] = p1 + "";
                intervalo[2] = p2 + "";
                intervalo[3] = fin + "";

                temp = fin - p1;
            }
            cont++;

        } else if (PCs == 2) {
            if (cont == 0) {
                inicio = iniDom;
                fin = p1 + p2;
                intervalo[0] = inicio + "";
                intervalo[1] = p1 + "";
                intervalo[2] = p2 + "";
                intervalo[3] = fin + "";
                temp = fin - p2;
            } else {
                inicio = (float) (fin - (temp * .4));
                fin = (p1 - inicio) + p2;
                intervalo[0] = inicio + "";
                intervalo[1] = p1 + "";
                intervalo[2] = p2 + "";
                intervalo[3] = fin + "";

                temp = fin - p2;
            }
            cont++;
        }
        //System.out.println("---------   fkfkfkfkf   PUTNOS "+inicio+"===="+p1+"====="+p2+"====="+fin);
        return intervalo;
    }

    public int getNOVar() throws Exception {
        long lreg;
        int NO = 0;
        //archi = new RandomAccessFile("base.txt", "r");
        long size = archi.length();
        //System.out.println("ei " + size);

        do {
            String clave = readChars(20);

            for (int i = 0; i < 7; i++) {
                String tag = readChars(20);
                String tipo = readChars(5);

                //  PUNTOS
                float punto1 = archi.readFloat();
                float punto2 = archi.readFloat();
                float punto3 = archi.readFloat();
                float punto4 = archi.readFloat();
            }

            int a = archi.readInt();
            NO++;
            lreg = archi.getFilePointer();
            //System.out.println("lon " + lreg);

        } while (lreg != size);
        //archi.close();
        return NO;
    }

    public String readChars(int length) throws IOException {
        String ret = "";
        for (int i = 0; i < length; i++) {
            ret += archi.readChar();
        }
        return ret;
    }
}
