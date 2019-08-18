package RateNavSpark11Project;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 *
 * @author diana
 */
public class AdjListGraph {

    private int V;
    private ArrayList<ArrayList<Edge>> terminals;

    static Comparator<Edge> timeComparator = (Edge e1, Edge e2) -> Double.compare(e1.computeTravelTime(), e2.computeTravelTime());
    static Comparator<Edge> speedComparator = (Edge e1, Edge e2) -> Double.compare(e1.getSpeed(), e2.getSpeed());
    static Comparator<Edge> fareComparator = (Edge e1, Edge e2) -> Double.compare(e1.getFare(), e2.getFare());
    static Comparator<Edge> ratingComparator = (Edge e1, Edge e2) -> -Double.compare(e1.getRating(), e2.getRating());
    static Comparator<Edge> distComparator = (Edge e1, Edge e2) -> Double.compare(e1.getDistance(), e2.getDistance());

    //node V is the destination
    public AdjListGraph(int V) {
        this.V = V + 1;
        terminals = new ArrayList<>();
        for (int i = 0; i < V + 1; i++) {
            terminals.add(new ArrayList<>());
        }
    }

    public void addEdge(String mode, int src, int dest, double dist, double fare, double spd) {
        //Edge e = new Edge(mode, src, dest, fare, dist, spd);
        terminals.get(src).add(new Edge(mode, src, dest, dist, fare, spd));
        //System.out.println(e.computeTravelTime());
    }

    public List<Integer> getAdjacentVertices(int src) {
        List<Integer> list = new ArrayList<>();
        for (Edge x : terminals.get(src)) {
            list.add(x.getDestination());
        }
        return list;
    }

    public ArrayList<ArrayList<Edge>> getTerminals() {
        return terminals;
    }

    public int getV() {
        return V;
    }

    public boolean alternatePathsPass(int dest, double ratingThreshold) {
        ArrayList<Edge> paths = new ArrayList<>();
        boolean passes = false;
        for (ArrayList<Edge> a : terminals) {
            for (Edge e : a) {
                if (e.getDestination() == dest) {
                    paths.add(e);
                    if (e.getRating() > ratingThreshold) passes = true;
                }
            }
        }

        return passes && paths.size() > 1;
    }

    static public void findShortestPath(String str, AdjListGraph graph, int V) {
        PriorityQueue<Edge> pq;
        ArrayList<ArrayList<Edge>> terminals = graph.getTerminals();
        int ratingThreshold = 6;

        double dist[][] = new double[V + 5][V + 5];
        Edge pred[][] = new Edge[V + 5][V + 5];

        if (str.equals("rating")) {
            for (int i = 0; i < V + 5; i++) {
                for (int j = 0; j < V + 5; j++) {
                    dist[i][j] = -1_000_000_000;
                }
            }
        } else {
            for (int i = 0; i < V + 5; i++) {
                for (int j = 0; j < V + 5; j++) {
                    dist[i][j] = 1_000_000_000;
                }
            }
        }

        //pq.add(terminals.get(0).get(0));
        dist[0][0] = 0;

        switch (str) {
            case "speed":
                pq = new PriorityQueue(speedComparator);
                for (Edge e : terminals.get(0)) {
                    dist[0][e.getDestination()] = e.getSpeed();
                    pq.add(e);
                }
                break;

            case "fare":
                pq = new PriorityQueue(fareComparator);
                for (Edge e : terminals.get(0)) {
                    dist[0][e.getDestination()] = e.getFare();
                    pq.add(e);
                }
                break;

            case "rating":
                pq = new PriorityQueue(ratingComparator);
                for (Edge e : terminals.get(0)) {
                    dist[0][e.getDestination()] = e.getRating();
                    pq.add(e);
                }
                break;

            case "time":
                pq = new PriorityQueue(timeComparator);
                for (Edge e : terminals.get(0)) {
                    dist[0][e.getDestination()] = e.computeTravelTime();
                    //System.out.println(dist[0][e.getDest()]);
                    pq.add(e);
                }
                break;

            case "distance":
            default:
                pq = new PriorityQueue(distComparator);
                for (Edge e : terminals.get(0)) {
                    dist[0][e.getDestination()] = e.getDistance();
                    pq.add(e);
                }
                break;
        }

        while (!pq.isEmpty()) {
            Edge curr = pq.poll();
            //System.out.println(curr.getMode());

            if (curr.getDestination() == V - 1) {
                System.out.println("Found destination.");
                ArrayList<Edge> path = new ArrayList<>();
                Edge c = curr;
                path.add(c);
                while (c.getSource() != 0) {
                    c = pred[c.getSource()][c.getDestination()];
                    path.add(c);
                }

                System.out.println("Shortest Path: ");
                for (int i = path.size() - 1; i >= 0; i--) {
                    Edge e = path.get(i);
                    System.out.printf("    %-10s src: %2d, dest: %2d", e.getMode(), e.getSource(), e.getDestination());
                    //System.out.print("    " + e.getMode() + ", src: " + e.getSource() + ", dest: " + e.getDestination());
                    switch (str) {
                        case "speed":
                            System.out.printf(", speed: %5.2f", e.getSpeed());
                            break;
                        case "fare":
                            System.out.printf(", fare: %5.2f", e.getFare());
                            break;
                        case "rating":
                            System.out.printf(", rating: %5.2f", e.getRating());
                            break;
                        case "time":
                            System.out.printf(", travel time: %5.2f", e.computeTravelTime());
                            break;
                        case "distance":
                        default:
                            System.out.printf(", distance: %5.2f", e.getDistance());
                    }
                    System.out.println(", warnings: " + (e.getWarnings().isEmpty() ? "none" : e.getWarnings()));
                }

                double valueToDisplay = dist[curr.getSource()][curr.getDestination()];

                switch (str) {
                    case "speed":
                        System.out.printf("Average speed: %.2f%n", valueToDisplay / path.size());
                        break;
                    case "fare":
                        System.out.printf("Total fare: %.2f%n", valueToDisplay);
                        break;
                    case "rating":
                        System.out.printf("Average rating: %.2f%n", valueToDisplay / path.size());
                        break;
                    case "time":
                        System.out.printf("Total time: %.2f%n", valueToDisplay);
                        break;
                    case "distance":
                    default:
                        System.out.printf("Total distance: %.2f%n", valueToDisplay);
                        break;
                }
                return;
            }

            if (str.equals("rating")) {
                if (curr.getRating() < dist[curr.getSource()][curr.getDestination()]
                        || graph.alternatePathsPass(curr.getDestination(), ratingThreshold)) continue;

                for (Edge adj : terminals.get(curr.getDestination())) {
                    //System.out.println(dist[curr.getDest()][adj.getDest()]);
                    double valueToAdd = adj.getRating();

                    if (dist[curr.getDestination()][adj.getDestination()] < dist[curr.getSource()][curr.getDestination()] + valueToAdd) {
                        dist[curr.getDestination()][adj.getDestination()] = dist[curr.getSource()][curr.getDestination()] + valueToAdd;
                        pq.add(adj);
                        pred[curr.getDestination()][adj.getDestination()] = curr;
                    }
                }

            } else {
                double valueToCompare;
                switch (str) {
                    case "speed":
                        valueToCompare = curr.getSpeed();
                        break;
                    case "fare":
                        valueToCompare = curr.getFare();
                        break;
                    case "time":
                        valueToCompare = curr.computeTravelTime();
                        break;
                    case "distance":
                    default:
                        valueToCompare = curr.getDistance();
                }

                if (valueToCompare > dist[curr.getSource()][curr.getDestination()]
                        || graph.alternatePathsPass(curr.getDestination(), ratingThreshold)) continue;

                for (Edge adj : terminals.get(curr.getDestination())) {
                    //System.out.println(dist[curr.getDest()][adj.getDest()]);
                    double valueToAdd;

                    switch (str) {
                        case "speed":
                            valueToAdd = adj.getSpeed();
                            break;
                        case "fare":
                            valueToAdd = adj.getFare();
                            break;
                        case "time":
                            valueToAdd = adj.computeTravelTime();
                            break;
                        case "distance":
                        default:
                            valueToAdd = adj.getDistance();
                    }

                    if (dist[curr.getDestination()][adj.getDestination()] > dist[curr.getSource()][curr.getDestination()] + valueToAdd) {
                        dist[curr.getDestination()][adj.getDestination()] = dist[curr.getSource()][curr.getDestination()] + valueToAdd;
                        pq.add(adj);
                        pred[curr.getDestination()][adj.getDestination()] = curr;
                    }
                }
            }

        }

        System.out.println("Could not find route to destination.");
    }
}

class Edge {

    private String mode;
    private int src, dest;
    private double fare, dist, spd, numOfRatings, rating;
    private Set warnings = new HashSet();

    public Edge(String mode, int src, int dest, double dist, double fare, double spd) {
        this.mode = mode;
        this.src = src;
        this.dest = dest;
        this.fare = fare;
        this.dist = dist;
        this.spd = spd;
        numOfRatings = 0;
        rating = -1;
    }

    public int getDestination() {
        return dest;
    }

    public int getSource() {
        return src;
    }

    public double getDistance() {
        return dist;
    }

    public double getFare() {
        return fare;
    }

    public double getRating() {
        return rating;
    }

    public void setSpeed(double spd) {
        this.spd = spd;
    }

    public double getSpeed() {
        return spd;
    }

    public void addWarning(String str) {
        warnings.add(str);
    }

    public void removeWarning(String str) {
        warnings.remove(str);
    }

    public Set getWarnings() {
        return warnings;
    }

    public double computeTravelTime() {
        return dist / (spd);
    }

    public String getMode() {
        return mode;
    }

    public void setRating(double rating) {
        //numOfRatings++;
        this.rating = (rating + this.rating) / ++numOfRatings;
    }

}
