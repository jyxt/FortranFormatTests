/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fortranformattests;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 *
 * @author sheldon.chi
 */
public class BlockDataReader {

    Model model;

    public BlockDataReader(Model model) {
        this.model = model;
    }

    public void readBlockData(BufferedReader in, BlockDataType type, String fortranFormat) throws IOException, ParseException {
        String strRead;
        int c = 1;
        int r = 1;
        int l = 1;
        while (l <= model.getK()) {
            // skip the line with format specifier
            in.readLine();
            if (r > model.getI()) {
                r = 1;
            }
            while (r <= model.getI()) {

                if (c > model.getJ()) {
                    c = 1;
                }
                while (c <= model.getJ()) {
                    strRead = in.readLine();
                    ArrayList<Object> RowofIbounds = FortranFormat.read(strRead, fortranFormat);
                    for (Object o : RowofIbounds) {
                        if (o != null) {
                            if (type == BlockDataType.BAS_ibound) {
                                int newIbound = Integer.parseInt(o.toString());
                                model.cell[c][r][l].setIbound(newIbound);
                            } else if (type == BlockDataType.DIS_TopBottom) {
                                double bottom = Double.parseDouble(o.toString());
                                model.cell[c][r][l].setBottom(bottom);
                            }
                            c++;
                        }
                    }
                }
                r++;
            }
            l++;
        }
    }


    
    private void readXYfromDIS(BufferedReader in, String fortranFormat) throws IOException, ParseException {
        int c,r,l;
        c = 1;
        r = 1;
        l = 1;

        in.readLine();//SKIP 1.0E30

        while (l <= model.getK()) {
            // skip the line with format specifier
            in.readLine();
            if (r > model.getI()) {
                r = 1;
            }
            while (r <= model.getI()) {

                if (c > model.getJ()) {
                    c = 1;
                }
                while (c <= model.getJ()) {
                    String strRead = in.readLine();
                    ArrayList<Object> RowofInitHeads = FortranFormat.read(strRead, fortranFormat);
                    for (Object o : RowofInitHeads) {
                        if (o != null) {
                            //             System.out.println(c + " " + r + " " + l + " " + o);
                            double newInitHead = Double.parseDouble(o.toString());
                            model.cell[c][r][l].setInitHead(newInitHead);
                            c++;
                        }
                    }
                }
                r++;
            }
            l++;
        }
    }

    public enum BlockDataType {

        BAS_ibound, BAS_initHead, DIS_XY, DIS_TopBottom
    }
}
