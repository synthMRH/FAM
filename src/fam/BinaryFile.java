/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fam;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Arturo
 */
public class BinaryFile {
    
    protected RandomAccessFile archi;
    public String readChars(int length) throws IOException
    {
        String ret = "";
        for (int i = 0; i < length; i++) {
          ret+=archi.readChar();
        }
        return ret;
    }
    
}
