import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class PreFlowPush {
    private SimpleGraph graph;
    private HashMap<String, Integer> height;
    private HashMap<Edge, Double>flow;

    private HashMap<Vertex, Double>excessFlow;
    private HashMap<Edge, Boolean> isBackward;
    private int numVertices;

    PreFlowPush(SimpleGraph graph) {
        height = new HashMap<>();
        flow = new HashMap<>();
        excessFlow = new HashMap<>();
        isBackward = new HashMap<>();
        numVertices = graph.numVertices();
        this.graph = graph;
    }
    private void preflow(SimpleGraph graph) {
        Iterator itVertices = graph.vertices();
        while (itVertices.hasNext()) {
            Vertex u = (Vertex) itVertices.next();
            height.put((String) u.getName(), 0);
            excessFlow.put(u, (double) 0);
        }
        height.put("s", numVertices);
        LinkedList copyList = (LinkedList) graph.edgeList.clone();
        Iterator it = copyList.iterator();
        //
        while (it.hasNext()) {
            Edge edge = (Edge) it.next();
            if (edge.getFirstEndpoint().getName().equals("s")) {
                flow.put(edge,(Double) edge.getData());
                excessFlow.put(edge.getSecondEndpoint(), (Double) edge.getData());
                Edge newEdge = graph.insertEdge(edge.getSecondEndpoint(), edge.getFirstEndpoint(),0.0, "reverse" + edge.getName());
                isBackward.put(newEdge, true);
                flow.put(newEdge, (Double) edge.getData());
            } else {
                flow.put(edge, (double) 0);
            }
        }
    }

    private Vertex overFlowVertex(SimpleGraph graph) {
        Iterator it = graph.vertices();
        while (it.hasNext()) {
            Vertex v = (Vertex) it.next();
            if(v.getName().equals("t") || v.getName().equals("s")) {
                continue;
            }
            if(excessFlow.containsKey(v) && excessFlow.get(v) > 0) {
                return v;
            }
        }
        return null;
    }

    private void updateReverseEdgeFlow(Edge edge, double fl, SimpleGraph graph) {
        Vertex v = edge.getFirstEndpoint();
        Vertex u = edge.getSecondEndpoint();
        Iterator it = graph.edges();
        while (it.hasNext()) {
            if (!(fl > 0)) {
                return;
            }
            Edge curEdge = (Edge) it.next();
            if(curEdge.getFirstEndpoint() == u && curEdge.getSecondEndpoint() == v) {
                if(isBackward.containsKey(edge) && isBackward.get(edge)) {
                    if(!isBackward.containsKey(curEdge) || !isBackward.get(curEdge)) {
                        if(fl > flow.get(curEdge)) {
                            fl -= flow.get(curEdge);
                            flow.put(curEdge, 0.0);
                        } else {
                            flow.put(curEdge, flow.get(curEdge) - fl);
                            fl = 0.0;
                        }
                    }
                } else {
                    if(isBackward.containsKey(curEdge) && isBackward.get(curEdge)) {
                        flow.put(curEdge, flow.get(curEdge) + fl);
                        fl = 0.0;
                    }
                }
            }
        }
        Edge newEdge = graph.insertEdge(u, v, 0.0, null);
        isBackward.put(newEdge, true);
        flow.put(newEdge, fl);
    }

    private boolean push(Vertex u, SimpleGraph graph) {
        Iterator it = graph.edges();
        while (it.hasNext()) {
            Edge edge = (Edge)it.next();
            if (edge.getFirstEndpoint() == u) {
                if ((Math.abs(flow.get(edge)- (Double) edge.getData()) <= 0.000001)) {
                    continue;
                }
                if (height.get((String)u.getName()) > height.get((String)edge.getSecondEndpoint().getName())) {
                    if (isBackward.containsKey(edge) && isBackward.get(edge)) {
                        //backward edge
                        double fl = Math.min(flow.get(edge), excessFlow.get(u));
                        excessFlow.put(u, excessFlow.get(u) - fl);
                        excessFlow.put(edge.getSecondEndpoint(), excessFlow.get(edge.getSecondEndpoint()) + fl);
                        flow.put(edge, flow.get(edge) - fl);
                        updateReverseEdgeFlow(edge, fl, graph);
                    } else {
                        //forward edge
                        double fl = Math.min((Double) edge.getData() - flow.get(edge), excessFlow.get(u));
                        excessFlow.put(u, excessFlow.get(u) - fl);
                        excessFlow.put(edge.getSecondEndpoint(), excessFlow.get(edge.getSecondEndpoint()) + fl);
                        flow.put(edge, flow.get(edge) + fl);
                        updateReverseEdgeFlow(edge, fl, graph);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void relabel(Vertex u, SimpleGraph graph) {
        Iterator it = graph.edges();
        int minHeight = Integer.MAX_VALUE;
        while(it.hasNext()) {
            Edge edge = (Edge) it.next();
            if(edge.getFirstEndpoint() == u) {
                if((Math.abs(flow.get(edge) - (Double) edge.getData()) <= 0.000001)) {
                    continue;
                }
                if(height.get((String) edge.getSecondEndpoint().getName()) >= height.get((String) u.getName())) {
                    minHeight = Math.min(minHeight, height.get((String) edge.getSecondEndpoint().getName()));
//                    mh = height.get((String) edge.getSecondEndpoint().getName());

                }
            }
        }
        height.put((String)u.getName(), minHeight + 1);
    }

    public double getMaxFlow() {
        preflow(graph);
        while (overFlowVertex(graph) != null)
        {
            Vertex u = overFlowVertex(graph);
            if (!push(u, graph))
                relabel(u, graph);
        }
        double ans = 0.0;
        for (Vertex vertex : excessFlow.keySet()) {
            if(vertex.getName().equals("t")) {
                ans = excessFlow.get(vertex);
            }
        }
        return ans;
    }

}
