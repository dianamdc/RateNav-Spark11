/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author diana
 */
public class SamsungProjectPrototype {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        AdjListGraph graph = new AdjListGraph(5);
        graph.addEdge("bus", 0, 1, 10, 5, 30);
        graph.addEdge("bus", 0, 1, 5, 2, 30);
        graph.addEdge("bus", 0, 2, 15, 10, 30);
        graph.addEdge("tricycle", 1, 2, 10, 3, 30);
        graph.addEdge("bus", 1, 2, 10, 10, 30);
        //graph.addEdge("train", 2, 1, 20, 5, 30);
        graph.addEdge("uv", 2, 3, 34, 12, 30);
        graph.addEdge("uv", 2, 5, 34, 50, 30);
        graph.addEdge("bus", 3, 4, 10, 5, 30);
        graph.addEdge("bus", 4, 5, 10, 5, 30);

        AdjListGraph.findShortestPath("speed", graph.getTerminals(), graph.getV());
        System.out.println();
        AdjListGraph.findShortestPath("time", graph.getTerminals(), graph.getV());
        System.out.println();
        //AdjListGraph.findShortestPath("speed", graph.getTerminals(), graph.getV());
        //System.out.println();
        AdjListGraph.findShortestPath("fare", graph.getTerminals(), graph.getV());
        System.out.println();
        AdjListGraph.findShortestPath("distance", graph.getTerminals(), graph.getV());
        //System.out.println(graph.getTerminals());
    }

}
