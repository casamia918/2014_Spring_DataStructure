import java.io.*;
import java.util.*;
import java.util.Map.*;



public class Subway {
	
	private static Graph allStation;
	private static Graph transferStation;
	
	
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
	
	private static void fileRead(String fileName) throws Exception {
		BufferedReader brText = new BufferedReader (new FileReader(fileName) );
		String line;
		String[] args;
		
		ArrayList<String[]> stationList = new ArrayList<String[]>();
		ArrayList<String[]> edgeList = new ArrayList<String[]>();
		
		//------------------------- declare ----------------------//
		
		//stationMap is temp storage map for input from text file
		//key : id, value : station
		TreeMap<String, Station> stationMap = new TreeMap<String, Station>();
		
		//stationMap by its name (for pick up transfer station)
		//key : name, value : station 
		TreeMap<String, Station> stationMap_name = new TreeMap<String, Station>();
		
		//transferStatinMap is stroage map for transfer stations
		//key : id, value : transferStationMap
		TreeMap<String, Station> transferStationMap = new TreeMap<String, Station>();
		
		//transferStationMap by its name (for store same transfer stations)
		//key : name, value : linkedlist of same station name id
		TreeMap<String, LinkedList<Station> > transferStationMap_name = 
				new TreeMap<String, LinkedList<Station>>();
		
		//lineMap is 2 dimensional tree, first lineNum, second stationMap of its line
		//first key : lineNum, first value : stationMap of that line
		//second key : id, second value : station
		TreeMap<String, TreeMap<String,Station> > lineMap = 
							new TreeMap<String, TreeMap<String,Station> > ();
		
		
		
		
		//--------- read station list & draw stationMap & draw trasnferStationMap -------------//
		
		//read station data from text file
		while(  ( (line=brText.readLine()) != null ) && !line.equals("") )  {
			
			args = line.split(" "); // 0:id, 1:name 2:subwayLine
			Station newStation = new Station(args[0], args[1], args[2]);
			stationMap.put(args[0], newStation);
			
			
			// if there is a transferStation with same Name, put method returns the privious contatining value
			Station poppedStation = stationMap_name.put(args[1], newStation);
			
			//draw transfersStation map
			if( poppedStation != null) { 
				
				// assign -> update -> put into trasferStationMap 
				if ( !poppedStation.isTransferStation() ) {

					poppedStation.setTransferStation();
					String poppedId = poppedStation.getId();
					stationMap.put(poppedId, poppedStation);
					transferStationMap.put(poppedId, poppedStation);
				}
				
				newStation.setTransferStation();
				String newId = newStation.getId();
				stationMap.put(newId, newStation);
				transferStationMap.put(newId, newStation);
				
				stationMap_name.put(args[1], newStation);
				
			}
			
			stationList.add(args);
		}
		
		int numTransferStation = transferStationMap.size();
		int numStation = stationMap.size();

		
		
		
		
		//------------------ draw lineMap -----------------------//
		for(Map.Entry<String,Station> entry : stationMap.entrySet()) {
			String id = entry.getKey();
			Station station = entry.getValue();
			String subwayLine = station.getLine();
			
			if( !lineMap.containsKey(subwayLine) ) {
				TreeMap<String,Station> newLineStation = new TreeMap<String,Station>();
				newLineStation.put(id, station);
				lineMap.put(subwayLine, newLineStation);
				
			} else {
				lineMap.get(subwayLine).put(id, station);
			}
		
		}
		
		//--------------- draw transfeStationMap_name ------------------//
		for(Map.Entry<String,Station> entry : transferStationMap.entrySet()) {
			Station station = entry.getValue();
			String name = station.getName();
			
			if( !transferStationMap_name.containsKey(name) ) {
				LinkedList<Station> sameStationList = new LinkedList<Station>();
				sameStationList.add(station);
				transferStationMap_name.put(name, sameStationList);
				
			} else {
				transferStationMap_name.get(name).add(station);
			}
		
		}
		
		
		
		//-------------draw allStation graph from stationMap ------------//
		allStation = new Graph( numStation ); 
		
		//read edge list from text file
		while ( ( line=brText.readLine() )!= null) {
			args = line.split(" "); // 0:station1 Id, 1:station2 Id, 2:time weight
			Station s1 = stationMap.get(args[0]);
			Station s2 = stationMap.get(args[1]);
			int weight = Integer.parseInt(args[2]) ;
			
			allStation.addEdge(s1, s2, weight);
			edgeList.add(args);
			
		}
		
		//connect transfer station from transferStationMap_name
		for(Map.Entry<String,LinkedList<Station> > entry : transferStationMap_name.entrySet()) {
			String name = entry.getKey();
			LinkedList<Station> sameStationList = entry.getValue();
			int transferWeight = 5;
			
			for(Station sameNameStation : sameStationList ){
				for(Station otherIdStation : sameStationList  ) {
					if( sameNameStation.compareTo(otherIdStation) != 0) {
						allStation.addEdge(sameNameStation, otherIdStation, transferWeight);
					}
					
				}
				
			}
			
			
		}		
		
		
		
		//------------ draw transferStation graph from allStation graph --------------//
//		transferStation = new Graph(numTransferStation);
//		Entry<String,Station> firstTransferEntry = transferStationMap.firstEntry();

		
		brText.close();

		
	}

	
	

	

	
	
	
	
	
	
	

	private static void search(String input){
		String[] args = input.split(" ");
		String depStation = args[0];
		String arrStation = args[1];
		Station[] route;
		
		if( args[2] != null && args[2] == "!" ) {
			route = findFastest(depStation, arrStation);
		} else {
			route = findLeastTransfer(depStation, arrStation);
		}
			
		
	}
	
	private static Station[] findFastest(String depStation, String arrStation)  {
		
		LinkedList<Station> stationSet = new LinkedList<Station>();
		stationSet.push(depStation);
		
		for(int i=0; i<allStation.getNumStations(); i++) {
			
			
		}
		
		
	}
	
	
	private static Station[] findLeastTransfer(String depStation, String arrStation)  {
		
		return null;
		
	}

	
	
	
}















class Station implements Comparable<Station> {
	
	private String id;
	private String name;
	private String line;
	private boolean transferStation;
	private boolean lastStation;
	
	public Station(String id, String name, String line) {
		this.id = id;
		this.name = name;
		this.line = line;
		this.transferStation = false;
		this.lastStation = false;
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
	
	public boolean isLastStation() {
		return lastStation;
	}
	
	public void setLastStation() {
		this.lastStation = true;
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

	public Station getDepartureStation() {
		return departureStation;
	}

	public Station getArrivalStation() {
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
	private TreeMap<Station, TreeMap<Station,Integer> > adjList;
	
	public Graph(int n) {
		numStations = n;
		numEdges = 0;
		adjList = new TreeMap<Station, TreeMap<Station,Integer> >();
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
	
	TreeMap<Station, Integer> getStationTree(Station hubStation) {
		return adjList.get(hubStation);
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
		Station depStation = e.getDepartureStation();
		Station arrStation = e.getArrivalStation();
		int weight = e.getWeight();
		this.addEdge(depStation, arrStation, weight);
	}
	
}























































