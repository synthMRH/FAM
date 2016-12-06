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
public class Performance {

    BaseFile baFile;
    ValuesFile vaFile;
    RulesFile rulFile;
    Scanner in = new Scanner(System.in);
    GestorArbol gest;
    FileReader fr;
    BufferedReader br;

    Handler handler;
    ArrayList<Integer> inSet;
    ArrayList<Double> finalSet;
    Weight weight;

    public Performance() throws Exception {
        handler = new Handler();
        vaFile = new ValuesFile();
        rulFile = new RulesFile();
        weight = new Weight();
    }

    public void entrada() throws Exception {
        //System.out.println("Escribe el concepto");
        //String concepto=in.next();
        ArrayList<String> conceptos;
        ArrayList<Float> conValues = new ArrayList<>();
        ArrayList<String> setsIN = new ArrayList<>();
        String goalCo=getGoalConcep();
        conceptos = getConceptos();//     saca los conceptos del archivo de reglas, mediante los antecedentes

        for (int i = 0; i < conceptos.size(); i++) {
            System.out.println("Escribe el valor de entrada para " + conceptos.get(i));
            conValues.add(Float.parseFloat(in.next()));

        }
        //String concepto = conceptos.get(0);

        //======================================================================================================
        vaFile.openReader();
        for (int i = 0; i < conceptos.size(); i++) {
            //  obtener los valores del concepto dado
            ArrayList<Double> values = vaFile.getValuesPLine(vaFile.readLine(), conceptos.get(i));
            inSet = getInBinarySet(values, getIndex(values, conValues.get(i)));// se obtiene el conjunto binario de entrada a partir del concepto dado y el valor de entrada
            String cad = "";
            for (int j = 0; j < inSet.size(); j++) {
                cad += inSet.get(j) + ",";
            }
            setsIN.add(cad);
        }

        // obetener el conjunto final
        finalSet = getFinalSet(setsIN);
        System.out.println("VECTOR FINAL ");
//        for (int i = 0; i < finalSet.size(); i++) {
//            System.out.println(finalSet.get(i));
//        }
        System.out.println("tamaño del vector final "+finalSet.size());
        //  desdifuzificar
        Double result = getDesFuzzy(finalSet, goalCo);
        System.out.println("La "+goalCo+" resultante es: " + result);
    }

    private ArrayList<String> getConceptos() throws Exception {
        ArrayList<String> concep = new ArrayList<>();

        rulFile.openReader();
        String rule = rulFile.readLine();
        String[] compRule;
        if (rule != null) {
            compRule = rule.split("/");
            if (compRule[0].contains("=")) {
                String[] lol = compRule[0].split("=");
                for (int i = 0; i < lol.length; i++) {
                    String[] a = lol[i].split("-");
                    concep.add(a[0]);
                }
            } else {
                String[] a = compRule[0].split("-");
                concep.add(a[0]);
            }
        }
        rulFile.closeReader();
        return concep;
    }
    
    private String getGoalConcep() throws Exception {
        ArrayList<String> concep = new ArrayList<>();

        rulFile.openReader();
        String rule = rulFile.readLine();
        String[] compRule;
        String concepGoal="";
        if (rule != null) {
            compRule = rule.split("/");
            if (compRule[1]!=null) {
                String[] lol = compRule[1].split("-");
                concepGoal=lol[0];
            } else {
                concepGoal="nulo";
            }
        }
        rulFile.closeReader();
        return concepGoal;
    }

    private Double getDesFuzzy(ArrayList<Double> set, String concep) throws Exception {
        vaFile.closeReader();
        ArrayList<Double> values;
        Double sumDown = 0.0, sumUp = 0.0;

        vaFile.openReader();
        values = vaFile.getValuesPLine(vaFile.readLine(), concep);
//        for (int i = 0; i < values.size(); i++) {
//            System.out.println("values "+values.get(i));
//        }
        for (int i = 0; i < set.size(); i++) {
            sumDown += set.get(i);
            sumUp += set.get(i) * values.get(i);
        }
        //System.out.println("lol");
        return sumUp / sumDown;
    }

    private ArrayList<Double> getFinalSet(ArrayList<String> setsEntrada) throws Exception {
        weight.openReader();
        String linea;
        ArrayList<String> vectoresC = new ArrayList<>();
        String[] lineaW;

        //  para cada Matriz de pesos
        while ((linea = weight.readLine()) != null) {
            //System.out.println("----");
            lineaW = linea.split("/");
            if (lineaW.length == 1) {//   Si hay un solo antecedente
                vectoresC.add(getVectorC(setStringTOInt(setsEntrada.get(0)), lineaW[0]));
            } else {//    si hay varios antecedentes desde matrices W
                ArrayList<String> vectsCPreliminares = new ArrayList<>();
                for (int i = 0; i < lineaW.length; i++) {
                    vectsCPreliminares.add(getVectorC(setStringTOInt(setsEntrada.get(i)), lineaW[i]));
                }
                vectoresC.add(getMinSet(vectsCPreliminares));
                //jm;
            }
        }

        System.out.println("Vectores FINALES");
        for (int i = 0; i < vectoresC.size(); i++) {
            System.out.println(vectoresC.get(i));
        }
        return summation(vectoresC);
    }

    private String getMinSet(ArrayList<String> lista) {
        //ArrayList<Double> result = new ArrayList<>();
        String result="";
        ArrayList<Double> temp = new ArrayList<>();
        String[] re = lista.get(0).split(",");
        
        for (int i = 0; i < re.length; i++) {
            double min = 110.0;
            for (int j = 0; j < lista.size(); j++) {
                temp=setStringTODouble(lista.get(j));
                if (temp.get(i)<min) {
                    min=temp.get(i);
                }
            }
            result+=min+",";
            //result.add(min);
        }
        //  formato de cada elemento de lista .8,.9,.8 | .4,.5,.6
        return result;
    }

    private ArrayList<Integer> setStringTOInt(String set) {
        ArrayList<Integer> lel = new ArrayList<>();
        String[] lol = set.split(",");
        for (int i = 0; i < lol.length; i++) {
            lel.add(Integer.parseInt(lol[i]));
        }
        return lel;
    }
    
    private ArrayList<Double> setStringTODouble(String set) {
        ArrayList<Double> lel = new ArrayList<>();
        String[] lol = set.split(",");
        for (int i = 0; i < lol.length; i++) {
            lel.add(Double.parseDouble(lol[i]));
        }
        return lel;
    }

    private ArrayList<Double> summation(ArrayList<String> vectoresC) {
        ArrayList<Double> vectFinal = new ArrayList<>();
        String vecTo = "";
        String[] vectorTotal;

        for (int i = 0; i < vectoresC.size(); i++) {
            vecTo += vectoresC.get(i);
        }
        vectorTotal = vecTo.split(",");
        int noCols=vectorTotal.length/vectoresC.size();

        for (int i = 0; i < noCols; i++) {
            Double sum = 0.0;
            for (int j = 0; j < vectoresC.size(); j++) {
                sum += Double.parseDouble(vectorTotal[j * noCols + i]);
            }
            vectFinal.add(sum);
        }
        /*for (int i = 0; i < vectFinal.size(); i++) {
            System.out.println("ññ "+vectFinal.get(i));
        }*/
        return vectFinal;
    }

    private String getVectorC(ArrayList<Integer> set, String lineW) {
        String vector = "";
        String[] peso = lineW.split("-");

        int noCols = Integer.parseInt(peso[peso.length - 1]);

        int lengWeiMa = (peso.length - 1) / noCols;
        //System.out.println("long--- "+leng);
        for (int i = 0; i < noCols; i++) {//    el numero de columnas de la matriz W
            Double suma = 0.0;
            for (int j = 0; j < set.size(); j++) {//    numero de renglones
                suma += set.get(j) * Double.parseDouble(peso[j * noCols + i]);
            }
            vector += suma + ",";
            //System.out.println("*******SUMA=" + suma);
        }
        return vector;
    }

    private int getIndex(ArrayList<Double> val, Float vaIn) throws Exception {
        Double menor = 2000.0, diferencia;
        int index = -1;
        for (int i = 0; i < val.size(); i++) {
            if (Math.abs((val.get(i) - vaIn)) < menor) {
                menor = Math.abs((val.get(i) - vaIn));
                index = i;
            }
        }
        return index;
    }

    private ArrayList<Integer> getInBinarySet(ArrayList<Double> val, int ind) throws Exception {
        ArrayList<Integer> set = new ArrayList<>();
        for (int i = 0; i < val.size(); i++) {
            if (ind == i) {
                set.add(1);
            } else {
                set.add(0);
            }
        }
        return set;
    }
}
