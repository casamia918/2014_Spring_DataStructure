import java.io.*;

public class MovieDatabase {

	public static ListReferenceBased db = new ListReferenceBased();

	public static void main(String args[]) {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			try {
				String input = br.readLine();
				if (input.equals("QUIT"))
					break;

				new Scanner(input);

			} catch (Exception e) {
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}

	}

}

class Scanner {
	private String input, command, genre, movie, keyword;

	public Scanner(String input) throws Exception {
		this.input = input;

		parshing();
		operation(command);
	}

	private void parshing() {

		int wordOccur = 0;

		for (int i = 0; i < input.length(); i++) {

			if (wordOccur == 0 && isChar(input.charAt(i))) {

				int wordLength = scanWordLength(input, i, "command");
				command = input.substring(i, i + wordLength);
				wordOccur++;
				i += wordLength - 1;

			} else if (wordOccur == 1 && isPerc(input.charAt(i))) {

				int wordLength = scanWordLength(input, i + 1, "word with %");

				if (command.equals("SEARCH")) {
					keyword = input.substring(i + 1, i + 1 + wordLength);
					return;
				} else if (command.equals("INSERT") || command.equals("DELETE")) {
					genre = input.substring(i + 1, i + 1 + wordLength);
				}

				wordOccur++;
				i += wordLength + 1;

			} else if (wordOccur == 2 && isPerc(input.charAt(i))) {

				int wordLength = scanWordLength(input, i + 1, "word with %");

				if (command.equals("INSERT") || command.equals("DELETE")) {
					movie = input.substring(i + 1, i + 1 + wordLength);
				}

				return;
			}

		}

	}

	private int scanWordLength(String input, int startIndex, String type) {

		int length = 0;

		switch (type) {
		case "command":
			for (int j = 0; isChar(input.charAt(startIndex + j)); j++) {
				length++;

				if (input.substring(startIndex, startIndex + length).equals(
						"PRINT")) {
					break;
				}

			}
			break;
		case "word with %":
			for (int j = 0; input.charAt(startIndex + j) != '%'; j++) {
				length++;
			}
			break;
		}

		return length;

	}

	private void operation(String command) throws Exception {
		DB_Manager dbManager = new DB_Manager();

		switch (command) {
		case "INSERT":
			dbManager.insert(genre, movie);
			break;

		case "DELETE":
			dbManager.delete(genre, movie);
			break;

		case "SEARCH":
			dbManager.search(keyword);
			break;

		case "PRINT":
			dbManager.print();
			break;

		default:
			break;
		}

	}

	private static boolean isChar(char c) {
		if (c >= 65 && c <= 90) {
			return true;
		} else
			return false;
	}

	private static boolean isPerc(char c) {
		if (c == '%')
			return true;
		else
			return false;
	}

}

class DB_Manager {

	ListReferenceBased db = MovieDatabase.db;

	public void insert(String genre, String movie) throws Exception {

		if (db.size() == 0) {
			this.insertNewGenre(genre, movie);
			return;
		}

		for (int i = 1; i <= db.size(); i++) {

			ListReferenceBased currGenreList = (ListReferenceBased) db.get(i);
			String currGenreName = currGenreList.getListInfo().getGenre();

			int genreComparison = genre.compareTo(currGenreName);
			
			if ( genreComparison == 0 ){
				
				for (int j =1; j<= currGenreList.size(); j++) {
					String currMovie = (String) currGenreList.get(j);
					int movieComparison = movie.compareTo(currMovie);
					
					if ( movieComparison == 0 ) {
						return; 
					} else if ( movieComparison > 0 ) {
						if (j == currGenreList.size()
								&& (movie.compareTo(currMovie)!=0 ) ) {
							currGenreList.add(currGenreList.size() + 1, movie);
							return;
						}
						continue;
					} else {
						currGenreList.add(j, movie);
						return;
					}
				}
				
			} else if ( genreComparison > 0 ) {
				if (i == db.size() && genre.compareTo(currGenreName)!=0 ) {
					this.insertNewGenre(genre, movie);
					return;
				}
				continue;
			} else {
				ListReferenceBased newGenre = new ListReferenceBased(new ListInfo(genre) );
				newGenre.add(1, movie);
				db.add(i, newGenre);
				return;
			}
		
		
		}
	}

	public void delete(String genre, String movie) throws Exception {

		for (int i = 1; i <= db.size(); i++) {

			ListReferenceBased currGenreList = (ListReferenceBased) db.get(i);
			

			if ( genre.compareTo( currGenreList.getListInfo().getGenre() ) == 0 ) {
				for (int j = 1; j <= currGenreList.size(); j++) {
					String currMovie = (String) currGenreList.get(j);
					if ( movie.compareTo(currMovie) == 0 ) {
						currGenreList.remove(j);
					}
				}
				if (currGenreList.isEmpty()) {
					db.remove(i);
					return;
				}
				return;

			}
		}
	}

	public void search(String keyword) throws Exception {

		boolean occur = false;

		for (int i = 1; i <= db.size(); i++) {
			ListReferenceBased currGenreList = (ListReferenceBased) db.get(i);
			for (int j = 1; j <= currGenreList.size(); j++) {
				String currMovie = (String) currGenreList.get(j);
				if (this.isLeftWordContainRightWord(currMovie, keyword)) {
					this.printGenreMovie(currGenreList, currMovie);
					occur = true;
				}
			}
		}

		if (!occur) {
			System.out.println("EMPTY");
		}
	}

	public void print() throws Exception {

		boolean occur = false;

		for (int i = 1; i <= db.size(); i++) {
			ListReferenceBased currGenreList = (ListReferenceBased) db.get(i);
			for (int j = 1; j <= currGenreList.size(); j++) {
				String currMovie = (String) currGenreList.get(j);
				this.printGenreMovie(currGenreList, currMovie);
				occur = true;
			}
		}

		if (!occur) {
			System.out.println("EMPTY");
		}
	}


	private boolean isLeftWordContainRightWord(String lWord, String rWord) {
		for (int i = 0; i <= lWord.length() - rWord.length(); i++) {
			String trimmedWord = lWord.substring(i, i + rWord.length());
			if (trimmedWord.equals(rWord)) {
				return true;
			}
		}
		return false;
	}

	private void printGenreMovie(ListReferenceBased Genre, String Movie) {
		System.out.println("(" + Genre.getListInfo().getGenre() + ", " + Movie
				+ ")");

	}

	private void insertNewGenre(String genre, String movie) throws Exception {

		ListReferenceBased newGenreList = new ListReferenceBased(new ListInfo(genre));
		newGenreList.add(1, movie);
		db.add(db.size() + 1, newGenreList);

	}

}

class Node {
	private Object item;
	private Node next;

	public Node() {
	}

	public Node(Object newItem) {
		this.item = newItem;
		next = null;
	}

	public Node(Object newItem, Node nextNode) {
		this.item = newItem;
		this.next = nextNode;
	}

	public Object getItem() {
		return item;
	}

	public void setItem(Object item) {
		this.item = item;
	}

	public Node getNext() {
		return next;
	}

	public void setNext(Node next) {
		this.next = next;
	}

}

interface ListInterface {
	boolean isEmpty();

	int size();

	void add(int index, Object item) throws Exception;

	void remove(int index) throws Exception;

	Object get(int index) throws Exception;
	
}

class ListReferenceBased implements ListInterface {

	private Node head;
	private int numItems;


	private Node getNode(int index) {
		Node curr = head;
		for (int i = 1; i <= index; i++) {
			curr = curr.getNext();
		}
		return curr;
	}
	
	public ListReferenceBased() {
		numItems = 0;
		head = new Node();
		head.setNext(null);
	}

	public ListReferenceBased(ListInfo info) {
		numItems = 0;
		head = new Node(info);
		head.setNext(null);
	}

	public boolean isEmpty() {
		return numItems == 0;
	}

	public int size() {
		return numItems;
	}

	public void add(int index, Object item) throws Exception {
		// if 1<=index<=numItems -> insert item btw existing items
		// if index == numItems+1 -> add new item to the last
		if (index >= 1 && index <= numItems + 1) {
			Node prev = getNode(index - 1);
			Node newNode = new Node(item, prev.getNext());
			prev.setNext(newNode);
			numItems++;
		} else {
			throw new Exception();
		}
	}

	public Object get(int index) throws Exception {
		if (index >= 1 && index <= numItems) {
			Node curr = getNode(index);
			return curr.getItem();
		} else {
			throw new Exception();
		}
	}

	public ListInfo getListInfo() {
		return (ListInfo) head.getItem();

	}

	public void remove(int index) throws Exception {
		if (index >= 1 && index <= numItems) {
			Node prev = getNode(index - 1);
			Node curr = prev.getNext();
			prev.setNext(curr.getNext());
			numItems--;
		} else {
			throw new Exception();
		}
	}
	
}

class ListInfo {
	private String genre;

	public ListInfo(String str) {
		this.genre = str;
	}

	public String getGenre() {
		return genre;
	}

}
