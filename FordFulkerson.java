package graphCode;


import java.util.*;

public class FordFulkerson {
    private SimpleGraph graph;
    private Vertex source;
    private Vertex sink;
    private Map<Vertex, Vertex> parent;
    private double maxFlow;
    private Map<Edge, Double> edgeFlows; // It is used to track the flow of each edge

    public FordFulkerson(SimpleGraph graph, Vertex source, Vertex sink) {
        this.graph = graph;
        this.source = source;
        this.sink = sink;
        this.parent = new HashMap<>();
        this.maxFlow = 0;
        this.edgeFlows = new HashMap<>(); // Initialize the flow map of the edges
        initializeEdgeFlows();
    }

    private void initializeEdgeFlows() {
        // The flow of all edges is initialized to be 0
        for (Object e : graph.edgeList) {
            Edge edge = (Edge) e;
            edgeFlows.put(edge, 0.0);
        }
    }

    public double getMaxFlow() {
        while (bfs(1)) {
            double pathFlow = Double.MAX_VALUE;
            for (Vertex v = sink; v != source; v = parent.get(v)) {
                Vertex u = parent.get(v);
                Edge edge = findEdge(u, v);
                double edgeCapacity = (double) edge.getData();
                double edgeFlow = edgeFlows.get(edge);
                pathFlow = Math.min(pathFlow, edgeCapacity - edgeFlow);
            }

            for (Vertex v = sink; v != source; v = parent.get(v)) {
                Vertex u = parent.get(v);
                Edge edge = findEdge(u, v);
                edgeFlows.put(edge, edgeFlows.get(edge) + pathFlow); // Update the flow
                Edge residual = findEdge(v, u);
                if (residual != null) {
                    edgeFlows.put(residual, edgeFlows.get(residual) - pathFlow); // Update the flow of the reverse edge
                }
            }

            maxFlow += pathFlow;
        }
        return maxFlow;
    }

    private boolean bfs(double scalingFactor) {
        Queue<Vertex> queue = new LinkedList<>();
        Set<Vertex> visited = new HashSet<>();
        queue.add(source);
        visited.add(source);
        parent.clear();

        while (!queue.isEmpty()) {
            Vertex u = queue.poll();
            Iterator<Edge> edgeIterator = graph.incidentEdges(u);
            while (edgeIterator.hasNext()) {
                Edge edge = edgeIterator.next();
                Vertex v = edge.getSecondEndpoint();
                double edgeCapacity = (double) edge.getData();
                double edgeFlow = edgeFlows.get(edge);
                if (!visited.contains(v) && edgeCapacity - edgeFlow >= scalingFactor) {
                    queue.add(v);
                    visited.add(v);
                    parent.put(v, u);
                    if (v == sink) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Edge findEdge(Vertex u, Vertex v) {
        Iterator<Edge> edgeIterator = graph.incidentEdges(u);
        while (edgeIterator.hasNext()) {
            Edge edge = edgeIterator.next();
            if (edge.getSecondEndpoint() == v) {
                return edge;
            }
        }
        return null;
    }
}
