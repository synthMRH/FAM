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
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author synthM
 */
public class ValuesFile {

    Scanner entrada;
    FileReader fr;
    BufferedReader br;
    String fileLine;
    File f;
    BufferedWriter bw;
    FileWriter fw;
    PrintWriter pw;

    public ValuesFile(String File) throws Exception {
        entrada = new Scanner(System.in);
        f = new File("values.txt");
        fw = new FileWriter(f);
        bw = new BufferedWriter(fw);
        pw = new PrintWriter(bw);
    }
    

    public ValuesFile() throws Exception {
        entrada = new Scanner(System.in);
    }

    public void writeInFile(String cad) throws Exception {
        pw.print(cad + "-");
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
        fr = new FileReader("values.txt");
        br = new BufferedReader(fr);
    }

    public void closeReader() throws Exception {
        br.close();
        fr.close();
    }

    public String readLine() throws Exception {
        return br.readLine();
    }

    public ArrayList<Double> getValuesPLine(String line, String concepto)throws Exception{
        ArrayList<Double> list = new ArrayList<>();
        String[] li = line.split("-");
        while (!concepto.equalsIgnoreCase(li[0])) {            
            String le=readLine();
            li = le.split("-");
        }
        if (concepto.equalsIgnoreCase(li[0])) {
            for (int i = 1; i < li.length - 1; i++) {
                list.add(Double.parseDouble(li[i]));
            }
        } else {
            list.add(-1.0);
        }
        return list;
    }
}
