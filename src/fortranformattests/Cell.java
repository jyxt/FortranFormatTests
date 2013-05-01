/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fortranformattests;

/**
 *
 * @author sheldon.chi
 */
public class Cell {
    private int ibound,j,i,k;
    private boolean iboundChanged;
    private double initHead;
    private double bottom;
    
    public Cell() {
        this.ibound = 0;
        this.initHead = 0;
        this.iboundChanged = false;
        this.bottom = 0.0;
    }

    public Cell(int j, int i, int k) {
        this();
        this.j = j;
        this.i = i;
        this.k = k;
    }

    /**
     * @return the ibound
     */
    public int getIbound() {
        return ibound;
    }

    /**
     * @param ibound the ibound to set
     */
    public void setIbound(int ibound) {
        this.ibound = ibound;
        
    }

    /**
     * @return the iboundChanged
     */
    public boolean isIboundChanged() {
        return iboundChanged;
    }

    /**
     * @param iboundChanged the iboundChanged to set
     */
    public void setIboundChanged(boolean iboundChanged) {
        this.iboundChanged = iboundChanged;
    }

    /**
     * @return the initHead
     */
    public double getInitHead() {
        return initHead;
    }

    /**
     * @param initHead the initHead to set
     */
    public void setInitHead(double initHead) {
        this.initHead = initHead;
    }

    /**
     * @return the bottom
     */
    public double getBottom() {
        return bottom;
    }

    /**
     * @param bottom the bottom to set
     */
    public void setBottom(double bottom) {
        this.bottom = bottom;
    }
    
}
