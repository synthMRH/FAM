/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author synthM
 */
public class Handler {

    BaseFile baFile;
    ValuesFile vaFile;
    Scanner in;
    GestorArbol gest;
    FileReader fr;
    BufferedReader br;
    ArrayList<Double> values, fuzzySet;

    public Handler() throws Exception {

        // Si no existe
        //baFile = new BaseFile("base");
        //vaFile = new ValuesFile("values");
        //  Si ya existe
        vaFile = new ValuesFile();
        baFile = new BaseFile();

        fr = new FileReader("rules.txt");
        br = new BufferedReader(fr);

        in = new Scanner(System.in);
        gest = new GestorArbol();
    }

    public void entrenar() throws Exception {
        //  Escribir los conceptos y sus conjuntos en el archivo base binario
        //baFile.escribirArchivoMaestro();

        //  Escribir los valores para cada concepto en un archivo plano 
        //writeValFile();
        difusificar();
        //  elegir regla
        //  sacar vector A y C de la regla
        // sacar vectores A y C evaluando valores en cada conjunto A y C
    }

    public int getPosConcep(String concepto) throws Exception {
        gest.crearArbol();
        //System.out.println("Ingresa la clave a buscar");
        //String cve2 = "temperatura";
        int posiLogica = gest.buscar(concepto);// llamamos al metodo de buscar la clave en el árbol y nos devuelve la posición del registro
        //System.out.println("Se encuentra en la posición " + posiLogica);
        return posiLogica - 1;
    }

    public void writeValFile() throws Exception {
        String cad;
        baFile.openFile("base");

        System.out.println("jo " + baFile.getNOVar());
        baFile.archi.seek(0);

        int num = baFile.getNOVar();
        for (int i = 0; i < num; i++) {
            baFile.archi.seek(0);
            //System.out.println("Ingresa los valores para " + baFile.readChars(20));
            long index = baFile.archi.length() / baFile.getNOVar();
            //System.out.println("index " + index);
            baFile.archi.seek(index * i);

            String conce = baFile.readChars(20).trim();
            vaFile.writeInFile(conce);// escribe el concepto
            System.out.println("Ingresa los valores para " + conce);
            do {
                System.out.println("Valor");
                cad = in.next();
                vaFile.writeInFile(cad);// ingresar valores
                System.out.println("Agregar otro?si;no");
                cad = in.next();
            } while (cad.equalsIgnoreCase("si"));
            vaFile.pw.println("/");
        }
        vaFile.closeWriter();
    }

    public ArrayList<Double> getIntervalSet(String conjunto) throws Exception {
        ArrayList<Double> intervalo = new ArrayList<>();

        while (!baFile.readChars(20).trim().equalsIgnoreCase(conjunto)) {
            baFile.readChars(5);//   leer tipo función
            baFile.archi.readFloat();//    leer puntos
            baFile.archi.readFloat();
            baFile.archi.readFloat();
            baFile.archi.readFloat();
        }
        baFile.readChars(5);//   leer tipo función
        //  guardar los puntos del intrevalo
        //System.out.println("En el conjunto " + conjunto);
        for (int i = 0; i < 4; i++) {
            Double e = Double.parseDouble(baFile.archi.readFloat() + "");
          //  System.out.println("punto " + i + " " + e);
            intervalo.add(e);
        }
        return intervalo;
    }

    private ArrayList<Double> getFuzzySet(ArrayList<Double> intervalo, ArrayList<Double> valores) {
        //System.out.println("valoressssss "+valores.size());
        ArrayList<Double> FuzzySet = new ArrayList<>();
        for (int i = 0; i < valores.size(); i++) {
            if (valores.get(i) > intervalo.get(0) && valores.get(i) < intervalo.get(1)) {
                FuzzySet.add(sacarGradoMembresia(1, 0, valores.get(i), Float.parseFloat(intervalo.get(1) + ""),
                        Float.parseFloat(intervalo.get(0) + "")));
            } else if (valores.get(i) > intervalo.get(2) && valores.get(i) < intervalo.get(3)) {
                FuzzySet.add(sacarGradoMembresia(0, 1, valores.get(i), Float.parseFloat(intervalo.get(3) + ""),
                        Float.parseFloat(intervalo.get(2) + "")));
            } else if (valores.get(i) <= intervalo.get(0) || valores.get(i) >= intervalo.get(3)) {
                FuzzySet.add(0.0);
            } else if (valores.get(i) >= intervalo.get(1) || valores.get(i) <= intervalo.get(2)) {
                FuzzySet.add(1.0);
            }
        }
        return FuzzySet;
    }

    public void difusificar() throws Exception {
        Weight weight = new Weight("d");
        //vaFile = new ValuesFile();
        ArrayList<Double> setA, setC;
        String rule;
        String[] antec;
        String[] cons;
        

        //baFile.openFile("base");
        while ((rule = getRule()) != null) {// leer regla del archivo rules
            String[] ru = rule.split("/");
            
            //      Si sólo es un antecedente y un consecuente
            if (!rule.contains("=")) {
                antec = ru[0].split("-");
                cons = ru[1].split("-");
                System.out.println(antec[0] + ".." + antec[1]);
                setA = getVector(antec);//    get set A
                //System.out.println(cons[0] + ".." + cons[1]);
                setC = getVector(cons);//    get set C
                //System.out.println("consecuente " + cons[0] + ".." + cons[1]);
                weight.writeWeightM(setA, setC);

            } else {
                String[] antecedentes = ru[0].split("=");
                cons = ru[1].split("-");
                
                for (int i = 0; i < antecedentes.length; i++) {
                    antec = antecedentes[i].split("-");
                    
                    //System.out.println(antec[0] + ".." + antec[1]);
                    setA = getVector(antec);//    get set A
                    //System.out.println(cons[0] + ".." + cons[1]);
                    setC = getVector(cons);//    get set C
                    //System.out.println("consecuente " + cons[0] + ".." + cons[1]);
                    weight.writeWeightM(setA, setC);
                    System.out.println("escrito "+antec[0] + ".." + antec[1]);
                }
                weight.pw.println();
            }
            //weight.closeWriter();
        }
        weight.closeWriter();
        //weight.closeReader();
    }

    private ArrayList<Double> getVector(String[] ante) throws Exception {
        baFile.openFile("base");
        baFile.archi.seek((baFile.archi.length() / baFile.getNOVar()) * getPosConcep(ante[0]));//buscar el antecedente en el archivo base
        vaFile.openReader();

        String conce = baFile.readChars(20).trim();
        //System.out.println("El concepto " + conce);
        values = vaFile.getValuesPLine(vaFile.readLine(), conce);

        //  buscar los valores del concepto que se pasa como parámetro en archivo plano
        while (values.get(0) < 0) {
            values = vaFile.getValuesPLine(vaFile.readLine(), conce);
        }

        //for (int i = 0; i < values.size(); i++) {
        // System.out.println("concepto " + values.get(i));
        //}
        //  difusificar con el intervalo seleccionado los valores encontrados 
        fuzzySet = getFuzzySet(getIntervalSet(ante[1]), values);// tiene el intervalo del conjunto a difusificar y el vector de valores
        /*for (int i = 0; i < fuzzySet.size(); i++) {
            System.out.println("valor " + i + "=" + fuzzySet.get(i));
        }*/
        baFile.closeFile();
        //vaFile.closeReader();*/
        return fuzzySet;
    }

    public String getRule() throws Exception {
        return br.readLine();
    }

    public double sacarGradoMembresia(int y2, int y1, double x, float x2, float x1) {
        return (y2 - y1) * ((x - x1) / (x2 - x1)) + y1;
    }
}
