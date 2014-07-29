import java.io.*;
import java.util.*;
import java.util.Map.*;



public class SubwayNew {
	
	
	public static void main(String args[]) {
		BufferedReader br = new BufferedReader (new InputStreamReader(System.in) );
		
		String fileName = args[0];
		try {
			fileRead(fileName);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		while (true) {
			try {
				String input = br.readLine();
				if (input.compareTo("QUIT") == 0) {
					break;
				}
				search(input);
			}
			catch (Exception e) {
				System.out.println("Error : " + e);
			}
		}
		
		

	}
	
	private static void fileRead(String fileName){
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	private static void search(String input) {
		String[] args = input.split(" ");
		String depStation = args[0];
		String arrStation = args[1];
		SearchReturn result;
		
		if( args[2] != null && args[2] == "!" ) {
			result = findLeastTransfer(depStation, arrStation);
		} else {
			result = findFastest(depStation, arrStation);
		}
		
		printResult(result);
		
	}
	
	
	private class SearchReturn {
		public int time;
		public Station[] route;
		
		SearchReturn(int time_, Station[] route_) {
			this.time = time_;
			this.route = route_;
		}
	}
	
	private static void printResult(SearchReturn result) {
		
		Station[] route = result.route;

		for(int i=0; i<route.length-1; i++){
			if( route[i].isTransferStation() && 
				route[i].getLine().compareTo(route[i+1].getLine() ) != 0 ) {
				System.out.print( "[" + route[i].getName() + "]" + " ");
			} else {
				System.out.print( route[i].getName() + " ");
			}
		}
		System.out.println( route[route.length-1].getName() );
		System.out.println( result.time );
		
	}
	
	
	
	
	
	
	
	private static SearchReturn findFastest(String depStation, String arrStation) {
		
		int time;
		Station[] route;
		
		
		
		
		
		
		
		
		
		return new SearchReturn(time,route);
		
		
	}
	
	private static SearchReturn findLeastTransfer(String depStation, String arrStation) {
		
		int time;
		Station[] route;
		
		
		return new SearchReturn(time,route);	
	}
	
	
}















class Station implements Comparable<Station> {
	
	private String id;
	private String name;
	private String line;
	private boolean transferStation;
	
	public Station(){
	}
	
	public Station(String id, String name, String line) {
		this.id = id;
		this.name = name;
		this.line = line;
		this.transferStation = false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public boolean isTransferStation() {
		return transferStation;
	}

	public void setTransferStation() {
		this.transferStation = true;
	}
	
	public int compareTo(Station other) {
		return id.compareTo(other.getId());
	}
	
}




class Edge {
	private Station departureStation, arrivalStation;
	private int weight;
	
	public Edge (Station s1, Station s2, int edgeWeight) {
		departureStation = s1;
		arrivalStation = s2;
		weight = edgeWeight;
	}

	public Station getDepStation() {
		return departureStation;
	}

	public Station getArrStation() {
		return arrivalStation;
	}

	public int getWeight() {
		return weight;
	}
	
}


//refer to the textbook p.810
class Graph {
	
	private int numStations;
	private int numEdges;
	
	//key1 : departure station
	//key2 : arrive station, Integer : time interval btw station
	//station is comparable by its own id string
	private Hashtable<Station, TreeMap<Station,Integer> > adjList;
	
	public Graph(int n) {
		numStations = n;
		numEdges = 0;
		adjList = new Hashtable<Station, TreeMap<Station,Integer> >();
	}
	
	
	// getters
	
	public int getNumStations() {
		return numStations;
	}
	
	public int getNumEdges() {
		return numEdges;
	}
	
	public int getEdgeWeight(Station depStation, Station arrStation) {
		return adjList.get(depStation).get(arrStation);
	}
	
	public Edge getEdge(Station depStation, Station arrStation){
		int weight = adjList.get(depStation).get(arrStation);
		return new Edge(depStation, arrStation, weight);
	}
	
	TreeMap<Station, Integer> getStationTree(Station searchStation) {
		return adjList.get(searchStation);
	}
	
	
	// add methods
	
	public void addEdge(Station depStation, Station arrStation, int weight) {
		if ( !adjList.containsKey(depStation) ) {
			adjList.put(depStation, new TreeMap<Station, Integer>() );
		}
		adjList.get(depStation).put(arrStation, weight);
		numEdges++;
	}
	
	public void addEdge(Edge e) {
		Station depStation = e.getDepStation();
		Station arrStation = e.getArrStation();
		int weight = e.getWeight();
		if ( !adjList.containsKey(depStation) ) {
			adjList.put(depStation, new TreeMap<Station, Integer>() );
		}
		adjList.get(depStation).put(arrStation, weight);
		numEdges++;
	}
	
}


























































