/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fortranformattests;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author sheldon.chi
 */
public class ListDataReader {
    Model model;

    ListDataReader(Model model) {
        this.model = model;
    }

    public void readListData(String fname, ListDataType dataType) {
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(fname));
            Pattern pattern = Pattern.compile("[ ,\t]+");
            String strRead;
            try {
                while ((strRead = in.readLine()) != null) {
                    String[] splitarray = pattern.split(strRead);
                    int r = Integer.parseInt(splitarray[0]);
                    int c = Integer.parseInt(splitarray[1]);
                    int l = Integer.parseInt(splitarray[2]);
                    if (dataType == ListDataType.Other) { // this is only temp
                        
                        String head = splitarray[3];
                                                
                        if (head.contains("E") && head.contains("-") ) {
                            model.cell[c][r][l].setIbound(3); //dry                               
                       //     model.cell[c][r][l].setIbound(2);// cells dry but head above bottom
                            model.cell[c][r][l].setInitHead(-999.99);
                        }
                    }
                }
             in.close();   
            } catch (IOException ex) {
                Logger.getLogger(ListDataReader.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ListDataReader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public enum ListDataType {

        Other
    }
}
