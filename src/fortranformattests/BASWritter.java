/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fortranformattests;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sheldon.chi
 */
public class BASWritter {

    Model model;

    public BASWritter(Model model) {
        this.model = model;
    }

    public void write(String fname) {
        int len = 12;
        int digits = 5;
        int timesRepeated = 10;
        String format = "%" + len + "." + digits + "e";
        try {
            try (PrintWriter out = new PrintWriter(new FileOutputStream(fname))) {
                int c = 1;
                int r = 1;
                int l = 1;
                while (l <= model.getK()) {
                    out.println("        10        1. (10G12.5)                  -1 Starting Head for Layer "+l);
                    if (r > model.getI()) {
                        r = 1;
                    }
                    while (r <= model.getI()) {

                        if (c > model.getJ()) {
                            c = 1;
                        }                        
                        while (c <= model.getJ()) {
                            double head = model.cell[c][r][l].getInitHead();
                            double bottom = model.cell[c][r][l].getBottom();
                            if ((head < bottom) && (l<=4)) {
                                out.printf(format, -999.999);
                                
                            } else  {
                                out.printf(format, head);
                                
                        //        System.out.println(c+" "+r+" "+l+ " " +head+" "+bottom);
                            }
                            if(c%timesRepeated==0 || c==model.getJ()) out.println();
                            c++;                                                        
                        }
                        r++;
                    }
                    l++;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BASWritter.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
