package recommender.memoryBased;

import java.util.Collections;
import java.util.HashMap;

import recommender.Recommender;
import lib.boundedPriorityQueue.BoundedPriorityQueue;
import lib.boundedPriorityQueue.Node;
import lib.matrix.SparseMatrix;
import lib.vector.SparseVector;

public class ItemBasedKNN implements Recommender{
	
	//neighborhood size
	private int k;
	
	//number of recommendation per user/item pair
	private int n;
	
	private HashMap<Integer, BoundedPriorityQueue<Node>>itemNeighborhood;
	
	public ItemBasedKNN(int k, int n) {
		this.k = k;
		this.n = n;
		itemNeighborhood = new HashMap<Integer, BoundedPriorityQueue<Node>>();
	}
	
	public void train(SparseMatrix trainMatrix){
		/*
		 * TODO
		 * - Speedup by considering bidirectional distance equivalency
		 * - look for tradeoff between object creating and memory size
		 * 
		 * Overheads
		 * - Creation of node object in each iteration
		 * 
		 * Advantage
		 * - Stores only top k item neighbors
		 */
		SparseVector vec1, vec2;
		Double sim;
		int columns = trainMatrix.getColumnCount();
		for(Integer i = 0; i < columns; i++ ){
			BoundedPriorityQueue<Node> boundedPQ = 
					new BoundedPriorityQueue<Node>(Collections.reverseOrder(), k);
			vec1 = trainMatrix.getColumn(i);
			for(Integer j = 0; j < columns; j++){
				if (i == j) continue;
				vec2 = trainMatrix.getColumn(j);
				sim = vec1.innerProduct(vec2);
				boundedPQ.offer(new Node(j,sim));
			}
			itemNeighborhood.put(i, boundedPQ);	
		}
	}
	
	public int[] predict(){
		int []ids = new int[n];
		return ids;
	}
}
