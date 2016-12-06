/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fam;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author synthM
 */
public class Weight {

    FileReader fr;
    BufferedReader br;

    File f;
    BufferedWriter bw;
    FileWriter fw;
    PrintWriter pw;

    public Weight(String file) throws Exception {
        f = new File("weightFile.txt");
        fw = new FileWriter(f);
        bw = new BufferedWriter(fw);
        pw = new PrintWriter(bw);
    }
    
    public Weight() throws Exception {
        //f = new File("weightFile.txt");
        //fw = new FileWriter(f);
        //bw = new BufferedWriter(fw);
        //pw = new PrintWriter(bw);
        int i=0;
    }

    public void writeInFile(String cad) throws Exception {
        //System.out.println("weight "+cad);
        
        pw.print(cad + "-");
    }

    public void writeWeightM(ArrayList<Double> setA, ArrayList<Double> setC) throws Exception {
        for (int i = 0; i < setA.size(); i++) {
            for (int j = 0; j < setC.size(); j++) {
                if (setA.get(i) < setC.get(j)) {
                    writeInFile(setA.get(i) + "");
                } else {
                    //pw.print(setC.get(j)+"");
                    writeInFile(setC.get(j) + "");
                }
            }
        }
        //*pw.println(setC.size()+"");
        pw.print(setC.size()+"/");
    }

    public void openWriter() throws Exception {
        bw = new BufferedWriter(fw);
        pw = new PrintWriter(bw);
    }

    public void closeWriter() throws Exception {
        pw.close();
        bw.close();
    }

    public void openReader() throws Exception {
        fr = new FileReader("weightFile.txt");
        br = new BufferedReader(fr);
    }

    public void closeReader() throws Exception {
        br.close();
        fr.close();
    }

    public String readLine() throws Exception {
        return br.readLine();
    }
}
