/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author diana
 */
public class AdjListGraph {

    private int V;
    private ArrayList<ArrayList<Edge>> terminals;

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
    private int src, dest, fare, dist, spd;

    public Edge(String mode, int src, int dest, int fare, int dist, int spd) {
        this.mode = mode;
        this.src = src;
        this.dest = dest;
        this.fare = fare;
        this.dist = dist;
        this.spd = spd;
    }

    public int getDest() {
        return dest;
    }

    public void setSpd(int spd) {
        this.spd = spd;
    }

    public int computeTravelTime() {
        return dist / spd;
    }
}
