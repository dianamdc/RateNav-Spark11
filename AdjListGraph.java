/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
