package finalproject;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SearchEngine {
	public HashMap<String, ArrayList<String> > wordIndex;   // this will contain a set of pairs (String, LinkedList of Strings)	
	public MyWebGraph internet;
	public XmlParser parser;

	public SearchEngine(String filename) throws Exception{
		this.wordIndex = new HashMap<String, ArrayList<String>>();
		this.internet = new MyWebGraph();
		this.parser = new XmlParser(filename);
	}
	
	/* 
	 * This does a graph traversal of the web, starting at the given url.
	 * For each new page seen, it updates the wordIndex, the web graph,
	 * and the set of visited vertices.
	 * 
	 * 	This method will fit in about 30-50 lines (or less)
	 */
	public void crawlAndIndex(String url) throws Exception {
		// TODO : Add code here
		Queue<String> queue=new LinkedList<>();
		queue.add(url);
		while (!queue.isEmpty()){
			String currentUrl=queue.poll();
			if(internet.getVisited(currentUrl)){
				continue;
			}
			ArrayList<String> urls = parser.getLinks(currentUrl);
			ArrayList<String> contents = parser.getContent(currentUrl);
			updateWordIndex(contents,currentUrl);
			internet.addVertex(currentUrl);
			internet.setVisited(currentUrl,true);
			for(String u:urls){
				internet.addVertex(u);
				internet.addEdge(currentUrl,u);
				queue.add(u);
			}
		}

	}

	private void updateWordIndex(ArrayList<String> contents,String url){
		for(String content:contents){
			ArrayList<String> index=wordIndex.getOrDefault(content,new ArrayList<>());
			index.add(url);
			wordIndex.put(content,index);
		}
	}
	
	
	/* 
	 * This computes the pageRanks for every vertex in the web graph.
	 * It will only be called after the graph has been constructed using
	 * crawlAndIndex(). 
	 * To implement this method, refer to the algorithm described in the 
	 * assignment pdf. 
	 * 
	 * This method will probably fit in about 30 lines.
	 */
	public void assignPageRanks(double epsilon) {
		// TODO : Add code here
		ArrayList<String> vertices=internet.getVertices();
		List<Double> currentRank=Arrays.asList(new Double[vertices.size()]);
		Collections.fill(currentRank,1.0);

		do{
			for(int i=0;i<vertices.size();i++){
				internet.setPageRank(vertices.get(i),currentRank.get(i));
			}
			currentRank=computeRanks(vertices);
		}while (!stopRank(epsilon,currentRank,vertices));

	}

	private boolean stopRank(double epsilon,List<Double> lastRank,List<String> vertices){
		for(int i=0;i<vertices.size();i++){
			double abs=Math.abs(internet.getPageRank(vertices.get(i))-lastRank.get(i));
			if(abs>=epsilon){
				return false;
			}
		}
		return true;
	}

	/*
	 * The method takes as input an ArrayList<String> representing the urls in the web graph 
	 * and returns an ArrayList<double> representing the newly computed ranks for those urls. 
	 * Note that the double in the output list is matched to the url in the input list using 
	 * their position in the list.
	 */
	public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
		// TODO : Add code here
		ArrayList<Double> result=new ArrayList<>();
		for(int i=0;i<vertices.size();i++){
			String url=vertices.get(i);
			ArrayList<String> edges=internet.getEdgesInto(url);
			double sum= edges.stream().mapToDouble(edge -> internet.getPageRank(edge) / internet.getOutDegree(edge)).sum();
			double rank=0.5+0.5*sum;
			result.add(i,rank);
		}
		return result;
	}


	/* Returns a list of urls containing the query, ordered by rank
	 * Returns an empty list if no web site contains the query.
	 *
	 * This method should take about 25 lines of code.
	 */
	public ArrayList<String> getResults(String query) {
		// TODO: Add code here
		HashMap<String,Double> map=new HashMap<>();
		ArrayList<String> urls=wordIndex.get(query);
		if(urls==null){
			return new ArrayList<>();
		}
		for(String url:urls){
			map.put(url,internet.getPageRank(url));
		}
		return Sorting.fastSort(map);
	}
}
