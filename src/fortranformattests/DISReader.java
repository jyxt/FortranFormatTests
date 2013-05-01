/*
 * To change model template, choose Tools | Templates
 * and open the template in the editor.
 */
package fortranformattests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sheldon.chi
 */
public class DISReader{
    Model model;
    private String dis;
    
    public DISReader(Model model)
    {
        this.model = model;
    }
    
    
    public void read(String dis) throws IOException
    {
        String specificationString = "(10E16.9)";
        String bottomTopFormat = "(10E14.7)";
        
      BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(dis));
            // skip #'s
            in.readLine();
            in.readLine();
            in.readLine();
            in.readLine();

            String strRead;
            

            int c = 1;
            int r = 1;

            in.readLine();
            while (c <= model.getJ()) {
                strRead = in.readLine();
                ArrayList<Object> ColofIbounds = FortranFormat.read(strRead, specificationString);
                for (Object o : ColofIbounds) {
                    if (o != null) {
                        //             System.out.println(c + " " + r + " " + l + " " + o);
                        model.delx[c] = Double.parseDouble(o.toString());
                        c++;
                    }
                }
            }            
            in.readLine(); // skip format line
            while (r <= model.getI()) {
                strRead = in.readLine();
                ArrayList<Object> RowofIbounds = FortranFormat.read(strRead, specificationString);
                for (Object o : RowofIbounds) {
                    if (o != null) {
                        //             System.out.println(c + " " + r  + " " + o);
                        model.dely[r] = Double.parseDouble(o.toString());
                        r++;
                    }
                }
            }  

          for(int cc=1;cc<=model.getJ();cc++)
          {
              model.x[cc] += (model.x[cc-1] + model.delx[cc]); // cumulate
          }
          for(int rr=1;rr<=model.getI();rr++)
          {
              model.y[rr] +=(model.y[rr-1] + model.dely[rr]); // cumulate
          }

          in.readLine();
          // read top
          r = 1;
          c = 1;
          while (r <= model.getI()) {

                if (c > model.getJ()) {
                    c = 1;
                }
                while (c <= model.getJ()) {
                    strRead = in.readLine();
                    ArrayList<Object> RowofIbounds = FortranFormat.read(strRead, bottomTopFormat);
                    for (Object o : RowofIbounds) {
                        if (o != null) {
                            double top = Double.parseDouble(o.toString());                            
                            model.modelTop[c][r] = top;
                            c++;
                        }
                    }
                }
                r++;
            }
          
            BlockDataReader br = new BlockDataReader(model);
            br.readBlockData(in, BlockDataReader.BlockDataType.DIS_TopBottom, bottomTopFormat);
            
            in.close();

        } 
         catch (ParseException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }


    /**
     * @return the dis
     */
    public String getDis() {
        return dis;
    }

    /**
     * @param dis the dis to set
     */
    public void setDis(String dis) {
        this.dis = dis;
    }
}
