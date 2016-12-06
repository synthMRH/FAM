/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fam;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 *
 * @author synthM
 */
public class RulesFile {

    FileReader fr;
    BufferedReader br;

    public void openReader() throws Exception {
        fr = new FileReader("rules.txt");
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
