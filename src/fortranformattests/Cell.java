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
    
    public Cell()
    {
        this.ibound = 0;
    }
    
    public Cell(int j, int i, int k)
    {
        this.j=j;
        this.i=i;
        this.k=k;
        this.ibound=0;
        this.iboundChanged=false;
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
    
}
