/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fortranformattests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sheldon.chi
 */
public class FortranFormatTests {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        
        Model model = new Model(200,200,4);

        
        
        /*
        String input = "-1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1";
        String specificationString = "(40I2)";
        try {
            ArrayList<Object> a = FortranFormat.read(input, specificationString);
            for (Object o:a)
            {
                int i = Integer.parseInt(o.toString());
                System.out.println(i);
            }
        } catch (ParseException | IOException ex) {
            Logger.getLogger(FortranFormatTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        */

      
    }
}
