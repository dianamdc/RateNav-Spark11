/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RateNavSpark11Project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
                ArrayList<String> terminals = new ArrayList<>();

                for (int j = 0; j < V; j++) {
                    terminals.add(sc.next());
                }

                graph.setDestinationNames(terminals);

                while (true) {
                    String str = sc.next();
                    if (str.equals("end")) break;
                    //gets data from input file
                    graph.addEdge(str,
                            sc.next(),
                            sc.nextInt(),
                            sc.nextInt(),
                            sc.nextDouble(),
                            sc.nextDouble(),
                            sc.nextDouble());
                    graph.getEdge(str).setRating(sc.nextDouble());
                }

                int S = sc.nextInt();
                for (int j = 0; j < S; j++) {
                    graph.findShortestPath(sc.next(), sc.nextInt(), sc.nextInt());
                    System.out.println();
                }

                System.out.println("Test case " + (i + 1) + " end.");
            }

            sc.close();

        } catch (FileNotFoundException ex) {
            System.out.println("File not found.");
        }
    }

}
