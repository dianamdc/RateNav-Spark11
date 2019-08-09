/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 *
 * @author diana
 */
public class AdjListGraph {

    private int V;
    private ArrayList<ArrayList<Edge>> terminals;

    Comparator<Edge> speedComparator = (Edge e1, Edge e2) -> e1.computeTravelTime() - e2.computeTravelTime();
    Comparator<Edge> fareComparator = (Edge e1, Edge e2) -> e1.getFare() - e2.getFare();
    Comparator<Edge> ratingComparator = (Edge e1, Edge e2) -> e1.getRating() - e2.getRating();
    Comparator<Edge> distComparator = (Edge e1, Edge e2) -> e1.getDist() - e2.getDist();

    //node V is the destination
    public AdjListGraph(int V) {
        this.V = V;
        terminals = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            terminals.add(new ArrayList<>());
        }
    }

    public void addEdge(String mode, int src, int dest, int fare, int dist, int spd) {
        terminals.get(src).add(new Edge(mode, src, dest, fare, dist, spd));
    }

    public List<Integer> getAdjacentVertices(int src) {
        List<Integer> list = new ArrayList<>();
        for (Edge x : terminals.get(src)) {
            list.add(x.getDest());
        }
        return list;
    }

    //dijsktra
    public void findShortest() {
        int[][] dist = new int[V][V];
        Edge[][] pred = new Edge[V][V];

        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                dist[i][j] = -1;
            }
        }

        PriorityQueue<Edge> pq = new PriorityQueue<>(distComparator);
        pq.add(terminals.get(0).get(0));

        while (!pq.isEmpty()) {
            Edge curr = pq.poll();
            if (curr.getDist() > dist[curr.getSrc()][curr.getDest()]) continue;

            //if destination is reached
            if (curr.getDest() == V - 1) {
                ArrayList<Edge> path = new ArrayList<>();
                Edge c = curr;

                while (c.getSrc() != 0) {
                    c = pred[c.getSrc()][c.getDest()];
                    path.add(c);
                }

                System.out.println("Shortest path: ");
                for (int i = path.size(); i >= 0; i--) {
                    System.out.print(path.get(i) + ", ");
                }

                System.out.println("\nDistance: " + dist[curr.getSrc()][curr.getDest()]);
                //return dist[curr.getSrc()][curr.getDest()];
            }

            //for each destination of curr check distance
            for (Edge e : terminals.get(curr.getDest())) {
                if (dist[curr.getDest()][e.getDest()] > dist[curr.getSrc()][curr.getDest()] + e.getDist()) {
                    dist[curr.getDest()][e.getDest()] = dist[curr.getSrc()][curr.getDest()] + e.getDist();
                    pred[curr.getDest()][e.getDest()] = curr;

                    pq.add(e);
                }
            }
        }

        //return -1;
    }
}

class Edge {

    private String mode;
    private int rating;
    private int src, dest, fare, dist, spd, numOfRatings;

    public Edge(String mode, int src, int dest, int fare, int dist, int spd) {
        this.mode = mode;
        this.src = src;
        this.dest = dest;
        this.fare = fare;
        this.dist = dist;
        this.spd = spd;
        numOfRatings = 0;
        rating = -1;
    }

    public int getDest() {
        return dest;
    }

    public int getSrc() {
        return src;
    }

    public int getDist() {
        return dist;
    }

    public int getFare() {
        return fare;
    }

    public int getRating() {
        return rating;
    }

    public void setSpd(int spd) {
        this.spd = spd;
    }

    public int computeTravelTime() {
        return dist / spd;
    }

}
