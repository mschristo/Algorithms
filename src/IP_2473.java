import AdditionalAlgorithms.KruskalFunctions;
import integrationproject.algorithms.Algorithms;
import integrationproject.model.BlackAnt;
import integrationproject.model.Edge;
import integrationproject.model.RedAnt;
import integrationproject.utils.InputHandler;
import integrationproject.utils.Visualize;

import java.util.ArrayList;
import java.util.Collections;


/** *
 * @author Christos Boutsikas
 * @aem 2473
 * @email mschristo@csd.auth.gr
 */
public class IP_2473 extends Algorithms {

    public static void main(String[] args) {
        checkParameters(args);

        //create Lists of Red and Black Ants
        int flag = Integer.parseInt(args[1]);
        ArrayList<RedAnt> redAnts = new ArrayList<>();
        ArrayList<BlackAnt> blackAnts = new ArrayList<>();
        if (flag == 0) {
            InputHandler.createRandomInput(args[0], Integer.parseInt(args[2]));
        }
        InputHandler.readInput(args[0], redAnts, blackAnts);


        IP_2473 algs = new IP_2473();

        //debugging options
        boolean visualizeMST = true;
        boolean visualizeSM = false;
        boolean printCC = false;
        boolean evaluateResults = true;

        if(visualizeMST){
            int[][] mst = algs.findMST(redAnts, blackAnts);
            if (mst != null) {
                Visualize sd = new Visualize(redAnts, blackAnts, mst, null, "Minimum Spanning Tree");
                sd.drawInitialPoints();
            }
        }

        if(visualizeSM){
            int[][] matchings = algs.findStableMarriage(redAnts, blackAnts);
            if (matchings != null) {
                Visualize sd = new Visualize(redAnts, blackAnts, null, matchings, "Stable Marriage");
                sd.drawInitialPoints();
            }
        }

        if(printCC){
            int[] coinChange = algs.coinChange(redAnts.get(0), blackAnts.get(0));
            System.out.println("Capacity: " + redAnts.get(0).getCapacity());
            for(int i = 0; i < blackAnts.get(0).getObjects().length; i++){
                System.out.println(blackAnts.get(0).getObjects()[i] + ": " + coinChange[i]);
            }
        }

        if(evaluateResults){
            System.out.println("\nEvaluation Results");
            algs.evaluateAll(redAnts, blackAnts);
        }
    }

    @Override
    public int[][] findMST(ArrayList<RedAnt> redAnts, ArrayList<BlackAnt> blackAnts) {
        int[][] MST = new int[redAnts.size()+blackAnts.size()-1][4];
        boolean isFinished = false;
        int red=0;
        int black=0;
        int mixed=0;
        int firstId = -1;
        int firstType = -1;
        int secondId = -1;
        int secondType = -1;
        int a=-1;
        int b=-1;
        int rowMST=0;

        KruskalFunctions func = new KruskalFunctions();
        ArrayList<Edge> redEdges = new ArrayList<>();
        ArrayList<Edge> blackEdges = new ArrayList<>();
        ArrayList<Edge> redBlackEdges = new ArrayList<>();

        // The auxiliary array for Kruskal-UnionFInd Algorithm
        ArrayList<Integer> disjoinSet = new ArrayList<>();
        for(int z=0; z<redAnts.size()+blackAnts.size(); z++){
            disjoinSet.add(new Integer(-1));
        }

        //Edges between red ants only
        for (int i=0; i<redAnts.size(); i++){
            for (int j=i+1; j<redAnts.size(); j++){
                double distance = redAnts.get(i).getDistanceFrom(redAnts.get(j));
                redEdges.add(new Edge(redAnts.get(i).getID(),redAnts.get(j).getID(),distance));
            }
        }
        //Edges between black ants only
        for (int i=0; i<blackAnts.size(); i++){
            for (int j=i+1; j<blackAnts.size(); j++){
                double distance = blackAnts.get(i).getDistanceFrom(blackAnts.get(j));
                blackEdges.add(new Edge(blackAnts.get(i).getID(),blackAnts.get(j).getID(),distance));
            }
        }
        //Edges between black and red ants mixed
        for (int i=0; i<redAnts.size(); i++){
            for (int j=0; j<blackAnts.size(); j++){
                double distance = redAnts.get(i).getDistanceFrom(blackAnts.get(j));
                redBlackEdges.add(new Edge(redAnts.get(i).getID(),blackAnts.get(j).getID(),distance));
            }
        }
        Collections.sort(redEdges, new Edge());
        Collections.sort(blackEdges, new Edge());
        Collections.sort(redBlackEdges, new Edge());


        while(isFinished==false) {
            if (red < redEdges.size() && black < blackEdges.size() && mixed < redBlackEdges.size()) {
                // Select between all edges
                switch (func.min(redEdges.get(red).getDistance(), blackEdges.get(black).getDistance(), redBlackEdges.get(mixed).getDistance())){
                    case 1:
                        firstId=redEdges.get(red).getFrom();
                        firstType=0;
                        secondId=redEdges.get(red).getTo();
                        secondType=0;
                        red++;
                        break;
                    case 2:
                        firstId=blackEdges.get(black).getFrom();
                        firstType=1;
                        secondId=blackEdges.get(black).getTo();
                        secondType=1;
                        black++;
                        break;
                    case 3:
                        firstId=redBlackEdges.get(mixed).getFrom();
                        firstType=0;
                        secondId=redBlackEdges.get(mixed).getTo();
                        secondType=1;
                        mixed++;
                        break;
                    default:
                        break;
                }
            } else if (red == redEdges.size() && black < blackEdges.size() && mixed < redBlackEdges.size()) {
                // Select edges between only black or red-black ants
                switch (func.min(blackEdges.get(black).getDistance(), redBlackEdges.get(mixed).getDistance())){
                    case 1: // black
                        firstId=blackEdges.get(black).getFrom();
                        firstType=1;
                        secondId=blackEdges.get(black).getTo();
                        secondType=1;
                        black++;
                        break;
                    case 2: // mixed
                        firstId=redBlackEdges.get(mixed).getFrom();
                        firstType=0;
                        secondId=redBlackEdges.get(mixed).getTo();
                        secondType=1;
                        mixed++;
                        break;
                    default:
                        break;
                }
            } else if (red < redEdges.size() && black == blackEdges.size() && mixed < redBlackEdges.size()) {
                // Select edges between only red or red-black ants
                switch (func.min(redEdges.get(red).getDistance(), redBlackEdges.get(mixed).getDistance())){
                    case 1: // red
                        firstId=redEdges.get(red).getFrom();
                        firstType=0;
                        secondId=redEdges.get(red).getTo();
                        secondType=0;
                        red++;
                        break;
                    case 2: // mixed
                        firstId=redBlackEdges.get(mixed).getFrom();
                        firstType=0;
                        secondId=redBlackEdges.get(mixed).getTo();
                        secondType=1;
                        mixed++;
                        break;
                    default:
                        break;
                }
            } else if (red < redEdges.size() && black < blackEdges.size() && mixed == redBlackEdges.size()) {
                // Select edges between only red or only black ants
                switch (func.min(redEdges.get(red).getDistance(),blackEdges.get(black).getDistance())){
                    case 1: // red
                        firstId=redEdges.get(red).getFrom();
                        firstType=0;
                        secondId=redEdges.get(red).getTo();
                        secondType=0;
                        red++;
                        break;
                    case 2: // black
                        firstId=blackEdges.get(black).getFrom();
                        firstType=1;
                        secondId=blackEdges.get(black).getTo();
                        secondType=1;
                        black++;
                        break;
                    default:
                        break;
                }
            } else if (red == redEdges.size() && black == blackEdges.size() && mixed < redBlackEdges.size()) {
                // Select only from mixed edges between red and black ants
                firstId=redBlackEdges.get(mixed).getFrom();
                firstType=0;
                secondId=redBlackEdges.get(mixed).getTo();
                secondType=1;
                mixed++;
            } else if (red == redEdges.size() && black < blackEdges.size() && mixed == redBlackEdges.size()) {
                // Select only from edges between black ants
                firstId=blackEdges.get(black).getFrom();
                firstType=1;
                secondId=blackEdges.get(black).getTo();
                secondType=1;
                black++;
            } else if (red < redEdges.size() && black == blackEdges.size() && mixed == redBlackEdges.size()) {
                // Select only from edges between red ants
                firstId=redEdges.get(red).getFrom();
                firstType=0;
                secondId=redEdges.get(red).getTo();
                secondType=0;
                red++;
            } else {
                // No other edges
                break;
            }

            // Check if there is any black ant, if so change its id due to implementation of disjoin function , which take only one
            // arraylist with all ants . The first redAnts.size() positions are for red ants, the rest are for black ants.
            if(firstType==1){
                a=firstId+redAnts.size();
            }else{
                a=firstId;
            }
            if(secondType==1){
                b=secondId+redAnts.size();
            }else{
                b=secondId;
            }

            // Call the disjoin function which decides if an edge will be added on the MST
            switch (func.disjoin(a,b,disjoinSet)){
                case 0:
                    MST[rowMST][0]=firstId;
                    MST[rowMST][1]=firstType;
                    MST[rowMST][2]=secondId;
                    MST[rowMST][3]=secondType;
                    isFinished=true;
                    break;
                case 1:
                    MST[rowMST][0]=firstId;
                    MST[rowMST][1]=firstType;
                    MST[rowMST][2]=secondId;
                    MST[rowMST][3]=secondType;
                    rowMST++;
                    break;
                case 2:
                    break;
                default:
                    break;
            }


        }
        return MST;
    }

    @Override
    public int[][] findStableMarriage(ArrayList<RedAnt> redAnts, ArrayList<BlackAnt> blackAnts) {
        //You should implement this method.
        return null;
    }

    @Override
    public int[] coinChange(RedAnt redAnt, BlackAnt blackAnt) {
        //You should implement this method.
        return null;
    }

    private static void checkParameters(String[] args) {
        if (args.length == 0 || args.length < 2 || (args[1].equals("0") && args.length < 3)) {
            if (args.length > 0 && args[1].equals("0") && args.length < 3) {
                System.out.println("3rd argument is mandatory. Represents the population of the Ants");
            }
            System.out.println("Usage:");
            System.out.println("1st argument: name of filename");
            System.out.println("2nd argument: 0 create random file, 1 input file is given as input");
            System.out.println("3rd argument: number of ants to create (optional if 1 is given in the 2nd argument)");
            System.exit(-1);
        }
    }




}
