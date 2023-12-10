package graphCode;

import java.util.Iterator;

public class XXXXQQQ {

	public static void main(String[] args) {
		GraphInput GG = new GraphInput();
		SimpleGraph G = new SimpleGraph();
		GG.LoadSimpleGraph(G, "/Users/yangliying/Downloads/543group2/graph1.txt");
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
		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1000000;
		System.out.println("Duration for FordFulkerson: " + duration + " millisecond");
		System.out.println("FordFulkerson:"+ff.getMaxFlow());

		startTime = System.nanoTime();
		ScalingFordFulkerson sff= new ScalingFordFulkerson(G,source,sink);
		endTime = System.nanoTime();
		duration = (endTime - startTime) / 1000000;

		System.out.println("Duration for ScalingFordFulkerson: " + duration + " millisecond");
		System.out.println("ScalingFordFulkerson:"+sff.getMaxFlow());
	}

}
