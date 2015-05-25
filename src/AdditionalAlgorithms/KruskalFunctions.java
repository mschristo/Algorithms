package AdditionalAlgorithms;

import java.util.ArrayList;

/** *
 * @author Christos Boutsikas
 * @aem 2473
 * @email mschristo@csd.auth.gr
 */
public class KruskalFunctions {
    
 public KruskalFunctions(){}

    /**
     * @param x The first number
     * @param y The second number
     * @param z The third number
     * @return Which number is the minimum
     */
    public int min(double x,double y, double z){
        if ((x<=y && x<z) || (x<y && x<=z)){
            return 1;
        }else if (y<x && y<=z) {
            return 2;
        }else if (z<x && z<y){
            return 3;
        }else return 1;
    }

    /**
     *
     * @param x The first number
     * @param y The second number
     * @return Which number is the minimum
     */
    public int min(double x,double y){
        if ( x<=y ) return 1;
        return 2;
    }

    /**
     * @return 1 if the the edge has imported to mst, 2 if the edge is skipped and 0 if the process is completed
     */
    public int disjoin(int i, int j, ArrayList<Integer> disjoinSet){
        int x=find(i,disjoinSet);
        int y=find(j,disjoinSet);
        if(x==y) return 2; // Skip edge
        if(disjoinSet.get(x)<=disjoinSet.get(y)){
            union(y,x,disjoinSet);
            if(disjoinSet.get(x) == disjoinSet.size()*(-1)){
                return 0; // Terminal Condition
            }
        }else{
            union(x,y,disjoinSet);
            if(disjoinSet.get(y)== disjoinSet.size()*(-1)){
                return 0; // Terminal Condition
            }
        }
        return 1;
    }

    int find(int x, ArrayList<Integer> disjoinSet){
        if(disjoinSet.get(x)<0) return x;
        return find(disjoinSet.get(x),disjoinSet);
    }

    void union(int x,int y,ArrayList<Integer> disjoinSet){
        int meion = disjoinSet.get(x);
        disjoinSet.set(x,new Integer(y));
        disjoinSet.set(y,new Integer(disjoinSet.get(y)+meion));
    }
}


