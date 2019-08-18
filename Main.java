/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author diana
 */
public class RateNavSpark11Project {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(new File("src/testdata/input.txt")); //file path here
            int T = sc.nextInt();

            for (int i = 0; i < T; i++) {
                System.out.println("Case " + (i + 1) + ": ");
                int V = sc.nextInt();
                AdjListGraph graph = new AdjListGraph(V);

                while (true) {
                    String str = sc.next();
                    if (str.equals("end")) break;
                    graph.addEdge(str, sc.nextInt(), sc.nextInt(), sc.nextDouble(), sc.nextDouble(), sc.nextDouble());
                }

                AdjListGraph.findShortestPath("distance", graph.getTerminals(), graph.getV());
                System.out.println();
                AdjListGraph.findShortestPath("fare", graph.getTerminals(), graph.getV());
                System.out.println();
                AdjListGraph.findShortestPath("speed", graph.getTerminals(), graph.getV());
                System.out.println();
                AdjListGraph.findShortestPath("time", graph.getTerminals(), graph.getV());
                System.out.println();
                AdjListGraph.findShortestPath("rating", graph.getTerminals(), graph.getV());
                System.out.println();
            }

        } catch (FileNotFoundException ex) {
            System.out.println("File not found.");
        }

//        AdjListGraph graph = new AdjListGraph(5);
//        graph.addEdge("bus", 0, 1, 10, 5, 30);
//        graph.addEdge("bus", 0, 1, 5, 2, 30);
//        graph.addEdge("bus", 0, 2, 15, 10, 30);
//        graph.addEdge("tricycle", 1, 2, 10, 3, 30);
//        graph.addEdge("bus", 1, 2, 10, 10, 30);
//        //graph.addEdge("train", 2, 1, 20, 5, 30);
//        graph.addEdge("uv", 2, 3, 34, 12, 30);
//        graph.addEdge("uv", 2, 5, 34, 50, 30);
//        graph.addEdge("bus", 3, 4, 10, 5, 30);
//        graph.addEdge("bus", 4, 5, 10, 5, 30);
//
//        AdjListGraph.findShortestPath("speed", graph.getTerminals(), graph.getV());
//        System.out.println();
//        AdjListGraph.findShortestPath("time", graph.getTerminals(), graph.getV());
//        System.out.println();
//        AdjListGraph.findShortestPath("rating", graph.getTerminals(), graph.getV());
//        System.out.println();
//        AdjListGraph.findShortestPath("speed", graph.getTerminals(), graph.getV());
//        System.out.println();
//        AdjListGraph.findShortestPath("fare", graph.getTerminals(), graph.getV());
//        System.out.println();
//        AdjListGraph.findShortestPath("distance", graph.getTerminals(), graph.getV());
    }

}
