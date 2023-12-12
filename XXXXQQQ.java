import java.util.Iterator;

public class XXXXQQQ {

	public static void main(String[] args) {
		GraphInput GG = new GraphInput();
		SimpleGraph G = new SimpleGraph();
		StringBuffer stringBuffer = RandomGraph.graphBuilder(200, 60, 1, 5);
		RandomGraph.toFile(stringBuffer, "/Users/agampanesar/Downloads/L75R125v1.txt");
		GG.LoadSimpleGraph(G, "/Users/agampanesar/Downloads/L75R125v1.txt");
		System.out.println(GG);
		Vertex source=null;
		Vertex sink=null;
		
		Iterator<Vertex> vertices = G.vertices();
		while(vertices.hasNext()) {
			Vertex v = vertices.next();
			if(v.getName().equals("s")){
				source=v;
			}else if(v.getName().equals("t")){
				sink = v;
			}
		}
		long startTime = System.nanoTime();
		FordFulkerson ff= new FordFulkerson(G,source,sink);
		System.out.println("Max Flow by FordFulkerson:"+ff.getMaxFlow());
		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1000000;
		System.out.println("Duration for FordFulkerson: " + duration + " millisecond");

		startTime = System.nanoTime();
		ScalingFordFulkerson sff= new ScalingFordFulkerson(G,source,sink);
		System.out.println("Max Flow by ScalingFordFulkerson:"+sff.getMaxFlow());
		endTime = System.nanoTime();
		duration = (endTime - startTime) / 1000000;
		System.out.println("Duration for ScalingFordFulkerson: " + duration + " millisecond");

		startTime = System.nanoTime();
		PreFlowPush preFlowPush = new PreFlowPush(G);
		System.out.println("Max Flow by PreFlowPushAlgorithm:" + preFlowPush.getMaxFlow());
		endTime = System.nanoTime();
		duration = (endTime - startTime) / 1000000;
		System.out.println("Duration for PreFlowPushAlgorithm: " + duration + " millisecond");

	}

}
