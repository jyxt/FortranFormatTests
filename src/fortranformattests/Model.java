/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fortranformattests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

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
    private String path;
    private String name;
    private String BAS, DIS;
    private double[] delx;
    private double[] dely;
    public double[] x,y;
    private HashSet<JIK> iboundChanged;
    
    
    Model(int j, int i, int k)
    {
        this.j=j;this.i=i;this.k=k;
        this.cell = new Cell[this.j+1][this.i+1][this.k+1]; // 0,0,0 is empty so that same as Fortran
        this.oldIbounds = new int[k+1]; // 0 is empty
        this.delx = new double[this.j+1];
        this.dely = new double[this.i+1];
        this.x = new double[this.j+1];
        this.y = new double[this.i+1];
        this.iboundChanged = new HashSet<>();
                
        for (int c = 0; c <= this.j; c++) {
            for (int r = 0; r <= this.i; r++) {
                for (int l = 0; l <= this.k; l++) {
                    this.cell[c][r][l] = new Cell(c,r,l);
                }
                this.dely[r]=0.0;
                this.y[r]=0.0;
            }
            this.delx[c]=0.0;
            this.x[c]=0.0;
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
         if(changed) return stats; else return "nothing changed";
    }
    
    public  void readBAS(String BAS, boolean firstTime) {

            getOldIbound();
            getIboundChanged().clear();
            
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
                                    int newIbound = Integer.parseInt(o.toString());
                                    if(!firstTime) 
                                    {
                                        if(this.cell[c][r][l].getIbound()!=newIbound)
                                        {
                                            getIboundChanged().add(new JIK(c,r,l));
                                            System.out.println(c+" "+r+" "+l);
                                        }                                        
                                    }
                                    
                                        this.cell[c][r][l].setIbound(newIbound);
                                    
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

    public void getXYfromDIS(String dis) {
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(dis));
            // skip #'s
            in.readLine();
            in.readLine();
            in.readLine();
            in.readLine();

            String strRead;
            String specificationString = "(10E16.9)";

            int c = 1;
            int r = 1;

            in.readLine();
            while (c <= this.getJ()) {
                strRead = in.readLine();
                ArrayList<Object> ColofIbounds = FortranFormat.read(strRead, specificationString);
                for (Object o : ColofIbounds) {
                    if (o != null) {
                        //             System.out.println(c + " " + r + " " + l + " " + o);
                        this.delx[c] = Double.parseDouble(o.toString());
                        c++;
                    }
                }
            }            
            in.readLine(); // skip format line
            while (r <= this.getI()) {
                strRead = in.readLine();
                ArrayList<Object> RowofIbounds = FortranFormat.read(strRead, specificationString);
                for (Object o : RowofIbounds) {
                    if (o != null) {
                        //             System.out.println(c + " " + r  + " " + o);
                        this.dely[r] = Double.parseDouble(o.toString());
                        r++;
                    }
                }
            }  

          for(int cc=1;cc<=this.j;cc++)
          {
              this.x[cc] += (this.x[cc-1] + this.delx[cc]); // cumulate
  //            System.out.println(x[cc]);
          }
          for(int rr=1;rr<=this.i;rr++)
          {
              this.y[rr] +=(this.y[rr-1] + this.dely[rr]); // cumulate
  //            System.out.println(y[rr]);
          }

            in.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(SimpleUI.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Cannot find DIS file"); 
        } catch (IOException ex) {
            Logger.getLogger(SimpleUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * @return the BAS
     */
    public String getBAS() {
        return path + File.separator + name + ".BAS";
    }


    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the DIS
     */
    public String getDIS() {
        return path + File.separator + name + ".DIS";
    }

    /**
     * @return the iboundChanged
     */
    public HashSet<JIK> getIboundChanged() {
        return iboundChanged;
    }

        
    
}
