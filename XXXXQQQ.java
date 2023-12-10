package graphCode;

import java.util.Iterator;

public class XXXXQQQ {

	public static void main(String[] args) {
		GraphInput GG = new GraphInput();
		SimpleGraph G = new SimpleGraph();
		GG.LoadSimpleGraph(G, "C:\\Users\\c1339\\OneDrive\\Desktop\\L75R125v1.txt");
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
		ScalingFordFulkerson ss= new ScalingFordFulkerson(G,source,sink);
		
		System.out.println(ss.getMaxFlow());
	}

}
