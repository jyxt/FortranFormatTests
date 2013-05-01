/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fortranformattests;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author sheldon.chi
 */
public class ModelPanel extends NavigableImagePanel{
    
    private Model theModel;
    private int layer;
    private ModelProperty property;
    
    public ModelPanel()
    {
        property = ModelProperty.IBOUND;
    }
    
    public ModelPanel(Model aModel)
    {
        theModel = aModel;
       // setLayer(1);
        property = ModelProperty.IBOUND;
      //  this.setImage(model2Image(theModel,layer));
    }

    /**
     * @return the theModel
     */
    public Model getTheModel() {
        return theModel;
    }

    /**
     * @param theModel the theModel to set
     */
    public void setTheModel(Model theModel) {
        this.theModel = theModel;
    }

    /**
     * @return the layer
     */
    public int getLayer() {
        return layer;
    }

    /**
     * @param layer the layer to set
     */
    public void setLayer(int layer) { // offset by 1
        this.layer = layer;
        this.setImage(this.model2Image(getTheModel()));
        repaint();
    }

    /**
     * @return the property
     */
    public ModelProperty getProperty() {
        return property;
    }

    /**
     * @param property the property to set
     */
    public void setProperty(ModelProperty property) {
        this.property = property;
    }
    
    
    public BufferedImage model2Image(Model aModel)
    {
        int numberOfRows = aModel.getI();
        int numberOfColumns = aModel.getJ();
        
        double ModelWidth= aModel.x[numberOfColumns]-aModel.x[1]; //changing 0 to 1 fixed shallow lines
     //   double ModelHeight = aModel.y[0]-aModel.y[numberOfRows];
        double ModelHeight = aModel.y[numberOfRows] - aModel.y[1];
                
        int panelWidth = 5000;
        int panelHeight = (int)Math.ceil((panelWidth*(ModelHeight/ModelWidth))); //.height based on relative to width
        
        
        double x_factors[] = new double[numberOfColumns];
        double y_factors[] = new double[numberOfRows];
        int x[] = new int[numberOfColumns];
        int w[]= new int[numberOfColumns];
        int y[] = new int[numberOfRows];
        int h[] = new int[numberOfRows];
        
        for (int c = 1; c < numberOfColumns; c++) {
            x_factors[c] = (aModel.x[c + 1] - aModel.x[c]) / ModelWidth;
            x[c] = (int)((aModel.x[c]-aModel.x[1])/ModelWidth*panelWidth);  
            w[c] = (int)Math.ceil(panelWidth*x_factors[c]);  
        }        
        for(int r=1;r<numberOfRows;r++)
        {
            y_factors[r] = (aModel.y[r+1]-aModel.y[r])/ModelHeight;
    //        y[r] = (int)((aModel.y[0]-aModel.y[r])/ModelHeight*panelHeight);
            y[r] = (int)((aModel.y[r]- aModel.y[1])/ModelHeight*panelHeight);
            h[r] = (int)Math.ceil(panelHeight*y_factors[r]);
        }        
        
                
        final BufferedImage img = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) img.createGraphics();
        
        System.out.println("about to enter loop");
        
        HashSet<JIK> iboundChanged = aModel.getIboundChanged();//ibound changed hashset
        
        for (int jj = 1;jj <numberOfColumns; jj++)
        {
            for (int ii=1;ii<numberOfRows;ii++)
            {   
        
                
                int ibound = aModel.cell[jj][ii][getLayer()].getIbound();   // get color form ibound, black or white              
                if(ibound==0)
                {
                    g.setColor(new Color(50, 150, 100));
                }else if(ibound==1)
                {
                    g.setColor(new Color(255,255,255));
                }else if(ibound==-1)
                {
                    g.setColor(new Color(255,255,255));
                  //  g.setColor(new Color(50,50,150));                  // show BC
                }

                
                /*
                else 
                {
                    JOptionPane.showMessageDialog(null, "invalid ibound found");
                    return null;
                }
                */ 
                
                /*
                //temp
                if(aModel.cell[jj][ii][getLayer()].getInitHead()<230)
                {
                    
                    g.setColor(new Color(255,25,50)); // red
                }
                if (aModel.cell[jj][ii][getLayer()].getInitHead() < 230 && aModel.cell[jj][ii][getLayer()].getIbound()==1) {

                    System.out.println("Cell " + jj+" " + ii + " "+getLayer()+ " initial head "+  aModel.cell[jj][ii][getLayer()].getInitHead() +"<230 and ibound is 1");
                    if(aModel.cell[jj+1][ii][getLayer()].getInitHead() > 230 ) System.out.println("Neighbour cell "+(jj+1)+" "+ii+" "+ getLayer()+" is "+ aModel.cell[jj+1][ii][getLayer()].getInitHead());
                    if(aModel.cell[jj-1][ii][getLayer()].getInitHead() > 230 ) System.out.println("Neighbour cell "+(jj-1)+" "+ii+" "+ getLayer()+" is "+ aModel.cell[jj+1][ii][getLayer()].getInitHead());
                    if(aModel.cell[jj][1+ii][getLayer()].getInitHead() > 230 ) System.out.println("Neighbour cell "+ jj +" "+(ii+1)+" "+ getLayer()+" is "+ aModel.cell[jj+1][ii][getLayer()].getInitHead());
                    if(aModel.cell[jj][ii-1][getLayer()].getInitHead() > 230 ) System.out.println("Neighbour cell "+jj+" "+(ii-1)+" "+ getLayer()+" is "+ aModel.cell[jj+1][ii][getLayer()].getInitHead());

                 }
                 // temp over
                 */

                // temp2
                
                if (ibound == 3) {
                    g.setColor(new Color(120, 150, 10)); // shit color
                    
                }
                if (ibound == 2) {
                    g.setColor(new Color(255, 25, 50));     // red                
                }
                 
                double bottom = aModel.cell[jj][ii][getLayer()].getBottom();   // get bottom 
                double initHead = aModel.cell[jj][ii][getLayer()].getInitHead();   // get head 
                if (initHead < -900.0)
                {
                    g.setColor(new Color(222, 150, 150)); 
                }
                
                //temp2



                if (iboundChanged.contains(new JIK(jj, ii, getLayer()))) {
                    g.setColor(new Color(255, 25, 50)); // red
                    System.out.println("contains");
                }
                //    g.setColor(new Color(1,1,1));
                
                int CellX = x[jj];                
                int CellY = y[ii];
                
                int CellWidth = w[jj];               
                int CellHeight = h[ii];                                                
                
                g.fillRect(CellX, CellY, CellWidth, CellHeight);

            }
            //     System.out.println(jj);
        }

        return img;


    }

    public static void main(String args[]) {
//        Model model = new Model(534,292,49);
//        model.readBAS("C:\\0-Modeling projects\\4-Hemlo\\copy_9\\COPY9.BAS", true);
//        model.getXYfromDIS("C:\\0-Modeling projects\\4-Hemlo\\copy_9\\COPY9.DIS");
        Model model = new Model(200,200,4);
        model.readBAS("C:\\0-Modeling projects\\22-PathBud\\PATHBUD1.bas", true);
        model.getXYfromDIS("C:\\0-Modeling projects\\22-PathBud\\PATHBUD1.dis");
        ModelPanel panel = new ModelPanel(model);
        final JFrame frame;
        frame = new JFrame("IBOUND");
        frame.setLocationRelativeTo(null);
        int w = 1000;
        int h = 1000;
        frame.setSize(w + 100, 200 + h);
        JPanel viewer = new Viewer(panel);        
        frame.getContentPane().add(viewer);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.setVisible(false);
                frame.removeAll();
                frame.dispose();
            }
        });
    }
    public enum ModelProperty {

        IBOUND, K, RECHARGE
    }
}
