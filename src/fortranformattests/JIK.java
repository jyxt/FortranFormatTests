/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fortranformattests;

/**
 *
 * @author sheldon.chi
 */
public class JIK {
    int j,i,k;
    JIK(int j, int i, int k)
    {
        this.j=j;
        this.i = i;
        this.k = k;
    }

    @Override
    public boolean equals(Object arg0) {
        if (arg0 instanceof JIK) {
            JIK that = (JIK) arg0;
            return (this.j == that.j && this.i == that.i && this.k == that.k);
        }else
        {
            return false;
        }

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.j;
        hash = 71 * hash + this.i;
        hash = 71 * hash + this.k;
        return hash;
    }
}
