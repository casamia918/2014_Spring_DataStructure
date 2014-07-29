import java.io.*;
import java.util.*;

public class Matching {

	private static ArrayList<String> stringArr;
	private static HashTable hashTable;

	public static void main(String args[]) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			try {
				String input = br.readLine();

				if (input.compareTo("QUIT") == 0)
					break;

				char command = input.charAt(0);
				String param = input.substring(2);
				// input.charAt(1) is space

				switch (command) {

				case '<':
					stringArr = new ArrayList<String>();
					readText(param);
					hashTable = new HashTable(stringArr);

					break;

				case '@':

					int slotIndex = Integer.parseInt(param);
					hashTable.printSlot(slotIndex);

					break;

				case '?':

					hashTable.searchPattern(param);

					break;

				// /////////////////////////////////////////// delete before
				// summit
				// /////////////////////////////////////////////

				case '$':
					testPrint(param);

				default:
				}

			} catch (Exception e) {
				System.out.println("오류 : " + e.toString());
			}
		}
	}

	private static void readText(String fileName) throws IOException {

		BufferedReader brText = new BufferedReader(new FileReader(fileName));

		String line;
		while ((line = brText.readLine()) != null) {
			stringArr.add(line);
		}

		brText.close();

		// /////////////////////////////////////////// delete before summit
		// /////////////////////////////////////////////

		// for (int i = 0; i < stringArr.size(); i++) {
		// System.out.println(stringArr.get(i));
		// }

	}

	private static void testPrint(String slotAtoB) throws IOException, InterruptedException {

		String[] slotAtoB_arg = slotAtoB.split(" ");
		int a = Integer.parseInt(slotAtoB_arg[0]);
		int b = Integer.parseInt(slotAtoB_arg[1]);

		for (int i = a; i <= b; i++) {
			System.out.println("/////////////////////////////////");
			System.out.println("slotIndex is :  " + i);
			hashTable.printSlot(i);

		}

	}
}




class HashTable {

	static final int tableSize = 100;
	static final int keySize = 6;

	private AVLTree[] AVLArr;
	private ArrayList<String> text;
	
	public HashTable() {
	}

	public HashTable(ArrayList<String> newStringArr) throws Exception {

		AVLArr = new AVLTree[tableSize];
		text = newStringArr;

		for (int i = 0; i < tableSize; i++) {
			AVLArr[i] = new AVLTree();
		}

		for (int i = 0; i < newStringArr.size(); i++) {

			for (int j = 0; j < newStringArr.get(i).length() - keySize + 1; j++) {

				// mapping each substring to hash slot
				// each substring are key of the AVLtree
				// line number and horizontal index at the substring are values
				// of the AVLtree

				String sub = newStringArr.get(i).substring(j, j + keySize);
				Position pos = new Position(i + 1, j + 1);

				int slotIndex = hashFunction(sub);

				System.out.println("i : " + i + "  j : " + j + "  slotIndex : " + slotIndex) ;
				
//				if( i == 0 && j == 86) {
//				 System.out.println("stop");
//				 }
//				
				if(slotIndex == 13) {
					System.out.println("stop");
				}

				
				
				AVLArr[slotIndex].insert(sub, pos); // sub : key, pos : value
				
				AVLArr[slotIndex] = AVLArr[slotIndex].searchTopTree(AVLArr[slotIndex]);

				// ///////////////////////////////////////// delete before
				// summit
				// /////////////////////////////////////////////
				 
			}

		}
	}

	private int hashFunction(String value) {

		int sumASCII = 0;
		for (int i = 0; i < value.length(); i++) {
			sumASCII += value.charAt(i);
		}

		return (sumASCII % 100);
	}

	public void printSlot(int slotIndex) throws InterruptedException {

		AVLTree targetSlot = AVLArr[slotIndex];

		if (targetSlot.getHeight() == 0) {
			System.out.println("EMPTY");
			return;
		} else {
			
			StringBuilder output = new StringBuilder();
			targetSlot.getAllNodes(targetSlot, output);
			output.setLength(output.length()-1);
			System.out.println(output);

		}

	}

	public void searchPattern(String word) throws Exception {

		String searchKey;
		if( word.length() >= 6) {
			searchKey = word.substring(0, 6);
		} else {
			throw new Exception("too short search word");
		}	
		
		int slotIndex = hashFunction(searchKey);
		AVLTree resultTree = AVLArr[slotIndex].searchKey(AVLArr[slotIndex], searchKey);
		
		if(resultTree == null) {
			System.out.println("(0, 0)");
		} else {
			ListReferenceBased resultList = resultTree.getRoot().getPositionList();
			boolean find = false;
			StringBuilder output = new StringBuilder();
			
			while(!resultList.isEmpty()) {
				Position nowPos = (Position) resultList.pop();
				
				int vIndex = nowPos.getVerIndex()-1;
				int hIndex = nowPos.getHorIndex()-1;
				
				String targetString = text.get(vIndex);
				
				if(hIndex + word.length() <= targetString.length() ) {
					String targetWord = targetString.substring(hIndex, hIndex+word.length());
					if( word.compareTo(targetWord) == 0 ){
						//System.out.print
						output.append
						("("+ nowPos.getVerIndex() + ", " + nowPos.getHorIndex() + ") ");
						find = true;
					}
				}
				
			}
			
			if(find == true) {
				output.setLength(output.length()-1);
				System.out.println( output );
			} else {
				System.out.println("(0, 0)");

			}
			
			
			
		}
		
		
		
		
	}

}




class AVLTree {

	private AVLTreeNode root;
	private AVLTree parent;
	private AVLTree leftTree;
	private AVLTree rightTree;
	private int height;

	// /////// constructors ///////////

	public AVLTree() {
		root = null;
		leftTree = rightTree = parent = null;
		height = 0;
	}

	public AVLTree(AVLTreeNode newRoot) {
		root = newRoot;
		leftTree = rightTree = parent = null;
		if (newRoot != null) {
			height = 1;
		}
	}

	public AVLTree(AVLTree newTree) throws Exception {
		if (newTree != null) {
			this.root = newTree.getRoot();
			this.parent = newTree.getParent();
			this.leftTree = newTree.getLeftTree();
			this.rightTree = newTree.getRightTree();
			this.height = newTree.getHeight();
		} else {
			this.root = null;
			this.parent = null;
			this.leftTree = null;
			this.rightTree = null;
			this.height = 0;
		}
	}

	public void copy(AVLTree sourceTree) throws Exception {
		this.root = sourceTree.getRoot();
		this.parent = sourceTree.getParent();
		this.leftTree = sourceTree.getLeftTree();
		this.rightTree = sourceTree.getRightTree();
		this.height = sourceTree.getHeight();
	}

	// /////////getter, setter ////////////

	// root
	public AVLTreeNode getRoot() {
		return root;
	}

	public void setRoot(AVLTreeNode root) {
		this.root = root;
	}

	// parent
	public AVLTree getParent() {
		return parent;
	}

	public void setParent(AVLTree newParent) throws Exception {
		this.parent = newParent;
	}

	public AVLTree setParent(AVLTree nowTree, AVLTree newParent)
			throws Exception {

		if (nowTree == null && newParent == null) {
			return null;
		} else if (nowTree == null && newParent != null) {
			throw new Exception();
		} else if (nowTree != null && newParent == null) {
			nowTree.setParent(null);
			return nowTree;
		} else {
			nowTree.setParent(newParent);
			return nowTree;
		}

	}

	// / child
	public void setChild(AVLTree subTree) throws Exception {

		if (subTree != null) {

			String subKey = subTree.getRoot().getKey();
			String thisKey = this.root.getKey();

			if (subKey.compareTo(thisKey) < 0) {
				this.leftTree = subTree;
			} else if (subKey.compareTo(thisKey) > 0) {
				this.rightTree = subTree;
			} else {
				throw new Exception();
			}
		}
	}

	public AVLTree setChild(AVLTree nowTree, AVLTree subTree) throws Exception {

		if (nowTree == null && subTree == null) {
			return null;
		} else if (nowTree == null && subTree != null) {
			throw new Exception();
		} else if (nowTree != null && subTree == null) {
			return nowTree;
		} else {
			nowTree.setChild(subTree);
			return nowTree;
		}
	}

	// leftTree
	public AVLTree getLeftTree() {
		return leftTree;
	}

	public void setLeftTree(AVLTree newLeftTree) throws Exception {
		this.leftTree = newLeftTree;
	}

	// rightTree
	public AVLTree getRightTree() {
		return rightTree;
	}

	public void setRightTree(AVLTree newRightTree) throws Exception {
		this.rightTree = newRightTree;
	}

	// height
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	// ///////// search method /////////

	public AVLTree searchKey(AVLTree nowTree, String key) {
		// return AVLtree which has only same key

		if (nowTree == null || nowTree.isEmpty()) {
			return null;
		}

		if (key.compareTo(nowTree.getRoot().getKey()) == 0) {
			return nowTree;
		} else if (key.compareTo(nowTree.getRoot().getKey()) < 0) {
			return searchKey(nowTree.getLeftTree(), key);
		} else {
			return searchKey(nowTree.getRightTree(), key);
		}

	}

	public AVLTree searchTarget(AVLTree nowTree, String key) {
		// return AVLtree which will be parent tree of the key
		// when nowTree is root and also null value, return AVLTree is null

		if (nowTree != null && !nowTree.isEmpty()) {
			if (key.compareTo(nowTree.getRoot().getKey()) < 0) {

				if (nowTree.getLeftTree() != null) {
					return nowTree.searchTarget(nowTree.getLeftTree(), key);
				}

			} else if (key.compareTo(nowTree.getRoot().getKey()) > 0) {

				if (nowTree.getRightTree() != null) {
					return nowTree.searchTarget(nowTree.getRightTree(), key);
				}

			}
		}

		return nowTree;

	}
	
	public AVLTree searchTopTree(AVLTree nowTree) {
		if(nowTree != null && !nowTree.isEmpty()) {
			
			AVLTree nowParent = nowTree.getParent();
			
			if(nowParent == null) {
				return nowTree;
			} else {
				return searchTopTree(nowParent);
			}

		} else {
			return null;
		}
		
	}

	// ///////// relation mathod /////////

	public void makeRelation(AVLTree upTree, AVLTree subTree) throws Exception {

		if (upTree != null && !upTree.isEmpty()) {
			if (subTree != null) {
				subTree.setParent(upTree);
				upTree.setChild(subTree);
			} else {
				upTree.setChild(null);
			}
		} else {
			if (subTree != null) {
				subTree.setParent(null);
			}
		}

	}

	public void refreshHeight(AVLTree nowTree) {

		if (nowTree == null || nowTree.isEmpty()) {
			return;
		}

		nowTree.setHeight(1 + biggerHeight(nowTree.leftTree, nowTree.rightTree));
		refreshHeight(nowTree.getParent());
	}

	// ///// public methods /////////

	public boolean isEmpty() {
		return root == null;
	}

	public void insert(String newkey, Position newPos) throws Exception {

		// insert newTree to the present tree
		AVLTreeNode newNode = new AVLTreeNode(newkey, newPos);
		AVLTree newTree = new AVLTree(newNode);

		AVLTree targetTree = this.searchTarget(this, newkey);

		if (targetTree != null && !targetTree.isEmpty()) {

			// same key -> add position list
			if (newkey.compareTo(targetTree.getRoot().getKey()) == 0) {

				targetTree.getRoot().addPosition(newPos);
				return;
			}

			this.makeRelation(targetTree, newTree);
			this.refreshHeight(targetTree);

			// fix tree shape by rotation
			AVLTree faultTree = examineFault(targetTree);

			if (faultTree != null) {
				faultTree.rotate();
			}

		} else {
			// insert is occured at top tree
			targetTree.copy(newTree);
		}

	}
	
	public void getAllNodes(AVLTree nowTree, StringBuilder basket) {
		
		if ((nowTree == null) || nowTree.isEmpty()) {
			return;
		} else {

			String thisKey = nowTree.getRoot().getKey();
			basket.append(thisKey + " ");
			
			getAllNodes(nowTree.getLeftTree(), basket);
			getAllNodes(nowTree.getRightTree(), basket);
		}
		
		
	}
	

	// ////// private operation methods /////////

	private int biggerHeight(AVLTree t1, AVLTree t2) {
		if (t1 == null && t2 == null) {
			return 0;
		} else if (t1 == null && t2 != null) {
			return t2.getHeight();

		} else if (t2 == null && t1 != null) {
			return t1.getHeight();

		} else {
			if (t1.getHeight() >= t2.getHeight()) {
				return t1.getHeight();
			} else {
				return t2.getHeight();
			}
		}
	}

	private int heightDiff(AVLTree nowTree) {

		AVLTree leftTree = nowTree.getLeftTree();
		AVLTree rightTree = nowTree.getRightTree();

		if (leftTree != null) {
			if (rightTree != null) {
				return leftTree.getHeight() - rightTree.getHeight();
			} else {
				return leftTree.getHeight();
			}
		} else {

			if (rightTree != null) {
				return -rightTree.getHeight();
			} else {
				return 0;
			}
		}

	}

	private AVLTree examineFault(AVLTree nowTree) {

		if (nowTree == null) {
			return null;
		}

		if (heightDiff(nowTree) == 2 || heightDiff(nowTree) == -2) {
			return nowTree;
		}

		return examineFault(nowTree.getParent());

	}

	public void rotate() throws Exception {

		int checkHeight2 = this.heightDiff(this);
		int checkHeight1;

		if (checkHeight2 == 2) {
			checkHeight1 = this.heightDiff(this.getLeftTree());

			if (checkHeight1 == 1) {

				this.rightRotate();

			} else if (checkHeight1 == -1) {

				this.getLeftTree().leftRotate();
				this.rightRotate();

			}

		} else if (checkHeight2 == -2) {
			checkHeight1 = this.heightDiff(getRightTree());

			if (checkHeight1 == 1) {

				this.getRightTree().rightRotate();
				this.leftRotate();

			} else if (checkHeight1 == -1) {

				this.leftRotate();
			}
		}

	}

	public void rightRotate() throws Exception {

		if(this.getLeftTree()==null || this.getLeftTree().isEmpty() ){
			throw new Exception();
		}

		AVLTree ceilingTree = this.getParent();
		AVLTree leftSubTree = this.getLeftTree();
		AVLTree temp_LR = this.getLeftTree().getRightTree();
		
		this.makeRelation(ceilingTree, leftSubTree);
		this.makeRelation(leftSubTree, this);
		if(temp_LR == null) {
			this.setLeftTree(temp_LR);
		} else {
			this.makeRelation(this,temp_LR);
		}
		
		this.refreshHeight(this);
		
	}
	
	
	
	

	public void leftRotate() throws Exception {

		if(this.getRightTree()==null || this.getRightTree().isEmpty() ){
			throw new Exception();
		}

		AVLTree ceilingTree = this.getParent();
		AVLTree rightSubTree = this.getRightTree();
		AVLTree temp_RL = this.getRightTree().getLeftTree();
		
		this.makeRelation(ceilingTree, rightSubTree);
		this.makeRelation(rightSubTree, this);
		if(temp_RL == null) { 
			this.setRightTree(temp_RL);
		} else {
			this.makeRelation(this,temp_RL);
		}
		
		this.refreshHeight(this);
		
	}
	
	
	
}

class AVLTreeNode {

	private String key;
	private ListReferenceBased positionList;

	public AVLTreeNode(String newKey, Position pos) throws Exception {
		key = newKey;
		positionList = new ListReferenceBased(pos);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public ListReferenceBased getPositionList() {
		return positionList;
	}

	public void setPositionList(ListReferenceBased positionList) {
		this.positionList = positionList;
	}

	public void addPosition(Position newPos) throws Exception {
		this.positionList.addLast(newPos);
	}

	public Position getPosition(int index) throws Exception {
		if (index >= 1 && index <= positionList.size()) {
			return (Position) positionList.get(index);
		} else {
			throw new Exception("wrong index");
		}
	}

}

class Position {

	private int vIndex, hIndex;

	public Position(int LineNum, int indexInLine) {
		vIndex = LineNum;
		hIndex = indexInLine;
	}

	public int getVerIndex() {
		return vIndex;
	}

	public int getHorIndex() {
		return hIndex;
	}

	public void printPos() {
		System.out.print("(" + hIndex + ", " + vIndex + ")");
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

	void addLast(Object item) throws Exception;

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

	public ListReferenceBased(Object newItem) {
		numItems = 1;
		head = new Node();
		head.setNext(new Node(newItem));
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

	public void addLast(Object item) throws Exception {
		if (!isEmpty()) {
			Node last = getNode(numItems);
			Node newNode = new Node(item);
			last.setNext(newNode);
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
	
	public Object pop() throws Exception {
		if(!isEmpty()) {
			Node pop = getNode(1);
			head.setNext(pop.getNext());
			numItems--;
			return pop.getItem();
		} else {
			throw new Exception();
		}
	}

}
