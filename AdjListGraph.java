
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

    Comparator<Edge> timeComparator = (Edge e1, Edge e2) -> Double.compare(e1.computeTravelTime(), e2.computeTravelTime());
    Comparator<Edge> speedComparator = (Edge e1, Edge e2) -> Double.compare(e1.getSpd(), e2.getSpd());
    Comparator<Edge> fareComparator = (Edge e1, Edge e2) -> Double.compare(e1.getFare(), e2.getFare());
    Comparator<Edge> ratingComparator = (Edge e1, Edge e2) -> -Double.compare(e1.getRating(), e2.getRating());
    Comparator<Edge> distComparator = (Edge e1, Edge e2) -> Double.compare(e1.getDist(), e2.getDist());

    //node V is the destination
    public AdjListGraph(int V) {
        this.V = V + 1;
        terminals = new ArrayList<>();
        for (int i = 0; i < V + 1; i++) {
            terminals.add(new ArrayList<>());
        }
    }

    public void addEdge(String mode, int src, int dest, double fare, double dist, double spd) {
        //Edge e = new Edge(mode, src, dest, fare, dist, spd);
        terminals.get(src).add(new Edge(mode, src, dest, fare, dist, spd));
        //System.out.println(e.computeTravelTime());
    }

    public List<Integer> getAdjacentVertices(int src) {
        List<Integer> list = new ArrayList<>();
        for (Edge x : terminals.get(src)) {
            list.add(x.getDest());
        }
        return list;
    }

    public void findShortestPath(String str) {
        PriorityQueue<Edge> pq;

        double dist[][] = new double[V + 5][V + 5];
        Edge pred[][] = new Edge[V + 5][V + 5];

        for (int i = 0; i < V + 5; i++) {
            for (int j = 0; j < V + 5; j++) {
                dist[i][j] = 1_000_000_000;
            }
        }

        //pq.add(terminals.get(0).get(0));
        dist[0][0] = 0;

        switch (str) {
            case "speed":
                pq = new PriorityQueue(speedComparator);
                for (Edge e : terminals.get(0)) {
                    dist[0][e.getDest()] = e.getSpd();
                    pq.add(e);
                }
                break;

            case "fare":
                pq = new PriorityQueue(fareComparator);
                for (Edge e : terminals.get(0)) {
                    dist[0][e.getDest()] = e.getFare();
                    pq.add(e);
                }
                break;

            case "rating":
                pq = new PriorityQueue(ratingComparator);
                for (Edge e : terminals.get(0)) {
                    dist[0][e.getDest()] = e.getRating();
                    pq.add(e);
                }
                break;

            case "time":
                pq = new PriorityQueue(timeComparator);
                for (Edge e : terminals.get(0)) {
                    dist[0][e.getDest()] = e.computeTravelTime();
                    //System.out.println(dist[0][e.getDest()]);
                    pq.add(e);
                }
                break;

            case "distance":
            default:
                pq = new PriorityQueue(distComparator);
                for (Edge e : terminals.get(0)) {
                    dist[0][e.getDest()] = e.getDist();
                    pq.add(e);
                }
                break;
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
                    System.out.print("    " + e.getMode() + ", src: " + e.getSrc() + ", dest: " + e.getDest());
                    switch (str) {
                        case "speed":
                            System.out.println(", speed: " + e.getSpd());
                            break;
                        case "fare":
                            System.out.println(", fare: " + e.getFare());
                            break;
                        case "rating":
                            System.out.println(", rating: " + e.getRating());
                            break;
                        case "time":
                            System.out.println(", travel time: " + e.computeTravelTime());
                            break;
                        case "distance":
                        default:
                            System.out.println(", distance: " + e.getDist());
                    }
                }

                switch (str) {
                    case "speed":
                        System.out.println("Average speed: " + dist[curr.getSrc()][curr.getDest()] / path.size());
                        break;
                    case "fare":
                        System.out.println("Total fare: " + dist[curr.getSrc()][curr.getDest()] + ".");
                        break;
                    case "rating":
                        System.out.println("Average rating: " + dist[curr.getSrc()][curr.getDest()] / path.size());
                        break;
                    case "time":
                        System.out.println("Total travel time: " + dist[curr.getSrc()][curr.getDest()]);
                        break;
                    case "distance":
                    default:
                        System.out.println("Total distance: " + dist[curr.getSrc()][curr.getDest()] + ".");
                        break;
                }
                //System.out.println("Total Distance: " + dist[curr.getSrc()][curr.getDest()]);
                break;
            }

            if (str.equals("rating")) {
                if (curr.getRating() < dist[curr.getSrc()][curr.getDest()]) continue;

                for (Edge adj : terminals.get(curr.getDest())) {
                    //System.out.println(dist[curr.getDest()][adj.getDest()]);
                    double valueToAdd = adj.getRating();

                    if (dist[curr.getDest()][adj.getDest()] < dist[curr.getSrc()][curr.getDest()] + valueToAdd) {
                        dist[curr.getDest()][adj.getDest()] = dist[curr.getSrc()][curr.getDest()] + valueToAdd;
                        pq.add(adj);
                        pred[curr.getDest()][adj.getDest()] = curr;
                    }
                }

            } else {
                double valueToCompare;
                switch (str) {
                    case "speed":
                        valueToCompare = curr.getSpd();
                        break;
                    case "fare":
                        valueToCompare = curr.getFare();
                        break;
                    case "time":
                        valueToCompare = curr.computeTravelTime();
                        break;
                    case "distance":
                    default:
                        valueToCompare = curr.getDist();
                }

                if (valueToCompare > dist[curr.getSrc()][curr.getDest()]) continue;

                for (Edge adj : terminals.get(curr.getDest())) {
                    //System.out.println(dist[curr.getDest()][adj.getDest()]);
                    double valueToAdd;

                    switch (str) {
                        case "speed":
                            valueToAdd = adj.getSpd();
                            break;
                        case "fare":
                            valueToAdd = adj.getFare();
                            break;
                        case "time":
                            valueToAdd = adj.computeTravelTime();
                            break;
                        case "distance":
                        default:
                            valueToAdd = adj.getDist();
                    }

                    if (dist[curr.getDest()][adj.getDest()] > dist[curr.getSrc()][curr.getDest()] + valueToAdd) {
                        dist[curr.getDest()][adj.getDest()] = dist[curr.getSrc()][curr.getDest()] + valueToAdd;
                        pq.add(adj);
                        pred[curr.getDest()][adj.getDest()] = curr;
                    }
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
    private int src, dest;
    private double fare, dist, spd, numOfRatings, rating;

    public Edge(String mode, int src, int dest, double fare, double dist, double spd) {
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

    public double getDist() {
        return dist;
    }

    public double getFare() {
        return fare;
    }

    public double getRating() {
        return rating;
    }

    public void setSpd(double spd) {
        this.spd = spd;
    }

    public double getSpd() {
        return spd;
    }

    public double computeTravelTime() {
        return dist / (spd);
    }

    public String getMode() {
        return mode;
    }

    public void setRating(double rating) {
        numOfRatings++;
        this.rating = (rating + this.rating) / numOfRatings;
    }

}
