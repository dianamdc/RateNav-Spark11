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

    private int V; //num of vertices
    private ArrayList<ArrayList<Edge>> terminals;
    private boolean[] hasAlternatePaths;
    private double ratingThreshold;
    private ArrayList<String> destinationNames;
    private HashSet<String> terminalNames;
    private int[] pathsToDest; //number of paths going to a destination of index i
    //private ArrayList<Set<String>> hasPrefMode; //checks if there is a

    static Comparator<Edge> timeComparator = (Edge e1, Edge e2) -> Double.compare(e1.computeTravelTime(), e2.computeTravelTime());
    static Comparator<Edge> fareComparator = (Edge e1, Edge e2) -> Double.compare(e1.getFare(), e2.getFare());
    static Comparator<Edge> ratingComparator = (Edge e1, Edge e2) -> -Double.compare(e1.getRating(), e2.getRating());
    static Comparator<Edge> distComparator = (Edge e1, Edge e2) -> Double.compare(e1.getDistance(), e2.getDistance());

    //node V is the destination
    public AdjListGraph(int V) {
        this.V = V;
        terminals = new ArrayList<>();
        destinationNames = new ArrayList<>();
        terminalNames = new HashSet<>();
        //hasPrefMode = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            terminals.add(new ArrayList<>());
            //hasPrefMode.add(new HashSet<>());
        }
        hasAlternatePaths = new boolean[V + 5];
        ratingThreshold = 0;
        pathsToDest = new int[V];
    }

    //if a certain path has a value lower than the rating threshold, it is avoided
    public void setRatingThreshold(double rate) {
        ratingThreshold = rate;
        for (int i = 0; i < V; i++) {
            this.alternatePathsPass(i, ratingThreshold);
        }
    }

    public void setRating(Edge e, double rating) {
        e.setRating(rating);
        int dest = e.getDestination();
        if (rating >= ratingThreshold && !hasAlternatePaths[dest] && pathsToDest[dest] > 1)
            hasAlternatePaths[dest] = true;
    }

    public void addEdge(String name, String mode, int src, int dest, double dist, double fare, double spd) {
        if (!terminalNames.contains(name)) {
            terminals.get(src).add(new Edge(name, mode, src, dest, dist, fare, spd));
            pathsToDest[dest] += 1;
        } else {
            System.out.println("Terminal named " + name + " already exists.");
        }
        //if (!hasPrefMode.get(dest).contains(mode)) hasPrefMode.get(dest).add(mode);
    }

    public void addEdge(String name, String mode, String src, String dest, double dist, double fare, double spd) {
        if (!terminalNames.contains(name)) {
            int dest_i = destinationNames.indexOf(dest);
            int src_i = destinationNames.indexOf(src);
            if (dest_i == -1 || src_i == -1) {
                System.out.println("Terminal does not exist.");
                return;
            }
            terminals.get(src_i).add(new Edge(name, mode, src_i, dest_i, dist, fare, spd));
            pathsToDest[dest_i] += 1;
        } else {
            System.out.println("Terminal named " + name + " already exists.");
        }
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

    public ArrayList<String> getDestinationNames() {
        return destinationNames;
    }

    public void setDestinationNames(ArrayList<String> t) {
        this.destinationNames = t;
    }

    public void setTerminalName(String t, int i) {
        this.destinationNames.add(i, t);
    }

    public int getV() {
        return V;
    }

    //checks if there are alternative paths to the destination that passes the rating threshold
    public boolean alternatePathsPass(int dest, double ratingThreshold) {
        int pathsNum = 0;
        boolean passes = false;
        for (ArrayList<Edge> a : terminals) {
            for (Edge e : a) {
                if (e.getDestination() == dest) {
                    pathsNum++;
                    if (e.getRating() >= ratingThreshold)
                        if (pathsNum > 1) return true;
                }
            }
        }

        return false;
    }

    //dijkstra
    public void findShortestPath(String str, int src, int dest) {
        PriorityQueue<Edge> pq;

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

        dist[0][0] = 0;

        //initializes pq with all paths coming from src
        switch (str) {

            case "fare":
                pq = new PriorityQueue(fareComparator);
                for (Edge e : terminals.get(src)) {
                    dist[src][e.getDestination()] = e.getFare();
                    pq.add(e);
                }
                break;

            case "rating":
                pq = new PriorityQueue(ratingComparator);
                for (Edge e : terminals.get(src)) {
                    dist[src][e.getDestination()] = e.getRating();
                    pq.add(e);
                }
                break;

            case "distance":
                pq = new PriorityQueue(distComparator);
                for (Edge e : terminals.get(src)) {
                    dist[src][e.getDestination()] = e.getDistance();
                    //System.out.println(dist[0][e.getDestination()]);
                    pq.add(e);
                }
                break;

            case "time":
            default:
                pq = new PriorityQueue(timeComparator);
                for (Edge e : terminals.get(src)) {
                    dist[src][e.getDestination()] = e.computeTravelTime();
                    pq.add(e);
                }
                break;
        }

        //edge going to destination that has the lowest distance
        //makes sure that all other paths have been exhausted first and pq is empty
        //saves the edge so that it can be accessed outside the loop
        Edge min = new Edge("", "", V, V, 1_000_000_000, 1_000_000_000, 1_000_000_000);
        int countToDest = 0;

        while (!pq.isEmpty()) {
            Edge curr = pq.poll();
            //System.out.println(curr.getMode());

            if (curr.getDestination() == dest) {
                if (dist[min.getSource()][min.getDestination()] >= dist[curr.getSource()][curr.getDestination()])
                    min = curr;
                countToDest++;

                //if all paths going to destination have been visited, loop terminates
                if (countToDest == pathsToDest[dest]) break;
                continue;
            }

            //if (!curr.getMode().equals(prefMode) && hasPrefMode.get(dest).contains(prefMode)) continue;
            //main dijksra algos
            //only includes paths with ratings already
            //is separated because rating looks for the highest ratings while the others looks for the lowest values
            if (str.equals("rating")) {
                if (curr.getNumberOfRatings() == 0 || curr.getRating() < dist[curr.getSource()][curr.getDestination()]
                        || this.hasAlternatePaths[curr.getDestination()]) continue;

                for (Edge adj : terminals.get(curr.getDestination())) {
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
                    case "fare":
                        valueToCompare = curr.getFare();
                        break;
                    case "distance":
                        valueToCompare = curr.getDistance();
                        break;
                    case "time":
                    default:
                        valueToCompare = curr.computeTravelTime();
                        break;
                }

                if (valueToCompare > dist[curr.getSource()][curr.getDestination()]
                        || this.hasAlternatePaths[curr.getDestination()]) continue;

                for (Edge adj : terminals.get(curr.getDestination())) {
                    double valueToAdd;

                    switch (str) {
                        case "fare":
                            valueToAdd = adj.getFare();
                            break;
                        case "distance":
                            valueToAdd = adj.getDistance();
                            break;
                        case "time":
                        default:
                            valueToAdd = adj.computeTravelTime();
                            break;
                    }

                    if (dist[curr.getDestination()][adj.getDestination()] > dist[curr.getSource()][curr.getDestination()] + valueToAdd) {
                        dist[curr.getDestination()][adj.getDestination()] = dist[curr.getSource()][curr.getDestination()] + valueToAdd;
                        pq.add(adj);
                        pred[curr.getDestination()][adj.getDestination()] = curr;
                    }
                }
            }
        }

        if (!min.getName().equals("")) {
            System.out.println("Found destination.");
            ArrayList<Edge> path = new ArrayList<>();
            double[] values = new double[5];

            //gets the overall path
            Edge c = min;
            path.add(c);
            while (c.getSource() != src) {
                c = pred[c.getSource()][c.getDestination()];
                path.add(c);
            }

            switch (str) {
                case "fare":
                    System.out.print("Cheapest Path");
                    break;
                case "rating":
                    System.out.print("Best Rated Path");
                    break;
                case "distance":
                    System.out.print("Shortest Path");
                    break;
                case "time":
                default:
                    System.out.print("Fastest Path");
                    break;
            }

            System.out.println("from " + destinationNames.get(src) + " to " + destinationNames.get(dest) + ": ");

            //prints all the values for each path visited
            for (int i = path.size() - 1; i >= 0; i--) {
                Edge e = path.get(i);
                values[0] += e.getSpeed();
                values[1] += e.getDistance();
                values[2] += e.computeTravelTime();
                values[3] += e.getFare();
                values[4] += e.getRating();
                System.out.printf("    %-10s src: %10s, dest: %10s, distance: %5.2f, "
                        + "speed: %5.2f, travel time: %5.2f, fare: %5.2f, rating: %5.2f (%2d)",
                        e.getMode(), destinationNames.get(e.getSource()), destinationNames.get(e.getDestination()), e.getDistance(),
                        e.getSpeed(), e.computeTravelTime(), e.getFare(), e.getRating(), e.getNumberOfRatings());
                System.out.println(", warnings: " + (e.getWarnings().isEmpty() ? "none" : e.getWarnings()));
            }

            //prints all the total values
            System.out.printf("Total distance: %6.2f%n", values[1]);
            System.out.printf("Total time:     %6.2f%n", values[2]);
            System.out.printf("Total fare:     %6.2f%n", values[3]);
            System.out.printf("Average rating: %6.2f%n", values[4] / path.size());

            return;
        }

        System.out.println("Could not find route to destination.");
    }

}

class Edge {

    private String name, mode;
    private int src, dest, numOfRatings;
    private double fare, dist, spd, rating;
    private Set warnings = new HashSet();

    public Edge(String name, String mode, int src, int dest, double dist, double fare, double spd) {
        this.name = name;
        this.mode = mode;
        this.src = src;
        this.dest = dest;
        this.fare = fare;
        this.dist = dist;
        this.spd = spd;
        numOfRatings = 0;
        rating = 0;
        warnings.add("No ratings yet.");
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

    public int getNumberOfRatings() {
        return numOfRatings;
    }

    public String getName() {
        return name;
    }

    public void setRating(double rating) {
        if (numOfRatings == 0) warnings.remove("No ratings yet.");
        this.rating = (rating + this.rating) / ++numOfRatings;
    }

}
