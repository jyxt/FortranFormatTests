/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fortranformattests;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sheldon.chi
 */
public class Model {
    private int j;
    private int i;
    private int k;
    Cell[][][] cell;
    int oldIbounds[];
    private String BAS;
    
    Model(int j, int i, int k)
    {
        this.j=j;this.i=i;this.k=k;
        this.cell = new Cell[this.j+1][this.i+1][this.k+1]; // 0,0,0 is empty so that same as Fortran
        this.oldIbounds = new int[k+1]; // 0 is empty
                
        for (int c = 0; c <= this.j; c++) {
            for (int r = 0; r <= this.i; r++) {
                for (int l = 0; l <= this.k; l++) {
                    this.cell[c][r][l] = new Cell(c,r,l);
                }
            }
        }
    }

    public void getOldIbound() {
        Arrays.fill(oldIbounds,0);
        for (int l = 1; l <= this.k; l++) {
            for (int r = 1; r <= this.i; r++) {
                for (int c = 1; c <= this.j; c++) {
                    oldIbounds[l] += Math.abs(this.cell[c][r][l].getIbound());
                }
            }
        }
    }

    /**
     * @return the j
     */
    public int getJ() {
        return j;
    }

    /**
     * @param j the j to set
     */
    public void setJ(int j) {
        this.j = j;
    }

    /**
     * @return the i
     */
    public int getI() {
        return i;
    }

    /**
     * @param i the i to set
     */
    public void setI(int i) {
        this.i = i;
    }

    /**
     * @return the k
     */
    public int getK() {
        return k;
    }

    /**
     * @param k the k to set
     */
    public void setK(int k) {
        this.k = k;
    }

    public String iboundStats()
    {
        boolean changed = false; // if all layers are same
        String stats="";        
        int currentIbounds[] = new int[this.k+1];
         for (int l = 1; l <= this.k; l++){
            for (int r = 1; r <= this.i; r++) {
                 for (int c = 1; c <= this.j; c++){
                    currentIbounds[l] += Math.abs(this.cell[c][r][l].getIbound());
                }
            }
        }       
         
         for (int l=1;l<=this.k;l++)
         {
             if((currentIbounds[l]-oldIbounds[l])==0)
             {
                 stats +="\nLayer "+l+" has same amout of active cells";
             }else
             {
                 changed = true;
                 stats +="\nLayer "+l+" active cells number has changed from "+oldIbounds[l]+" to "+currentIbounds[l];
             }
         }
         if(changed) return stats; else return "";
    }
    
    public synchronized void readBAS(String BAS, boolean firstTime) {

            this.getOldIbound();
//!!!!! need to update for non-vmod
            String specificationString = "(40I2)";
           

            BufferedReader in;
            try {
                in = new BufferedReader(new FileReader(BAS)); 

                String strRead;
                /*
                 while ((strRead = in.readLine()) != null) {
                 // Item 0 [#Text] in MODFLOW manual. Can be multiple line
                 // if it's not item 0 then it is Item 1 [Options], can be blank line. We skip it here
                 //!!!!! need to update for non-vmod                    
                 if (!strRead.equals("\n")) {
                 break;
                 }
                 }
                 */

                in.readLine();
                in.readLine();
                in.readLine();

                int c = 1;
                int r = 1;
                int l = 1;


                while (l <= this.getK()) {
                    // skip the line with format specifier for now since it is always (40I2) for V. Should be made dynamic in the future                
                    in.readLine();
                    if (r > this.getI()) {
                        r = 1;
                    }
                    while (r <= this.getI()) {

                        if (c > this.getJ()) {
                            c = 1;
                        }
                        while (c <= this.getJ()) {
                            strRead = in.readLine();
                            ArrayList<Object> RowofIbounds = FortranFormat.read(strRead, specificationString);
                            for (Object o : RowofIbounds) {
                                if (o != null) {
                       //             System.out.println(c + " " + r + " " + l + " " + o);
                                    this.cell[c][r][l].setIbound(Integer.parseInt(o.toString()));
                                    c++;
                                }

                            }
                        }
                        r++;
                    }
                    l++;

                }

                
          //      if(!firstTime) this.dir.updateUI(model.iboundStats());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException | ParseException ex) {
                Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    /**
     * @return the BAS
     */
    public String getBAS() {
        return BAS;
    }

    /**
     * @param BAS the BAS to set
     */
    public void setBAS(String BAS) {
        this.BAS = BAS;
    }
        
    
}
