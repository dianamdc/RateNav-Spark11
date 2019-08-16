
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
        this.V = V + 1;
        terminals = new ArrayList<>();
        for (int i = 0; i < V + 1; i++) {
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

    public void findShortestDistance() {
        PriorityQueue<Edge> pq = new PriorityQueue(distComparator);

        int dist[][] = new int[V + 5][V + 5];
        Edge pred[][] = new Edge[V + 5][V + 5];

        for (int i = 0; i < V + 5; i++) {
            for (int j = 0; j < V + 5; j++) {
                dist[i][j] = 1_000_000_000;
            }
        }

        //pq.add(terminals.get(0).get(0));
        dist[0][0] = 0;

        for (Edge e : terminals.get(0)) {
            dist[0][e.getDest()] = e.getDist();
            pq.add(e);
        }

        while (!pq.isEmpty()) {
            Edge curr = pq.poll();
            //System.out.println(curr.getMode());

            if (curr.getDest() == V - 1) {
                System.out.println("Found destination.");
                ArrayList<Edge> path = new ArrayList<>();
                Edge c = curr;
                path.add(c);
                while (c.getSrc() != 0) {
                    c = pred[c.getSrc()][c.getDest()];
                    path.add(c);
                }

                System.out.println("Shortest Path: ");
                for (int i = path.size() - 1; i >= 0; i--) {
                    Edge e = path.get(i);
                    System.out.println("    " + e.getMode() + ", src: " + e.getSrc() + ", dest: " + e.getDest() + ", dist: " + e.getDist());
                }

                System.out.println("Total Distance: " + dist[curr.getSrc()][curr.getDest()]);
                break;
            }

            if (curr.getDest() > dist[curr.getSrc()][curr.getDest()]) continue;

            for (Edge adj : terminals.get(curr.getDest())) {
                //System.out.println(dist[curr.getDest()][adj.getDest()]);
                if (dist[curr.getDest()][adj.getDest()] > dist[curr.getSrc()][curr.getDest()] + adj.getDist()) {
                    dist[curr.getDest()][adj.getDest()] = dist[curr.getSrc()][curr.getDest()] + adj.getDist();
                    pq.add(adj);
                    pred[curr.getDest()][adj.getDest()] = curr;
                }
            }

        }
    }

    public ArrayList<ArrayList<Edge>> getTerminals() {
        return terminals;
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

    public String getMode() {
        return mode;
    }

}
