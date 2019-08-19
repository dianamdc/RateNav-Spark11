/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RateNavSpark11Project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author diana
 */
public class RateNavSpark11Project {

    /**
     * @param args the command line arguments
     */
    static String[] terminals = {"a", "b", "c", "d", "e", "f"};

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

                graph.getTerminals().get(0).get(0).setRating(10);
                graph.setTerminalNames(terminals);

                graph.findShortestPath("distance", 1, 4);
                System.out.println();
                graph.findShortestPath("fare", 0, 5);
                System.out.println();
                graph.findShortestPath("x", 0, 5);
                System.out.println();
                graph.findShortestPath("time", 0, 5);
                System.out.println();
                graph.findShortestPath("rating", 0, 5);
                System.out.println();
            }

            sc.close();

        } catch (FileNotFoundException ex) {
            System.out.println("File not found.");
        }
    }

}
