package recommender.memoryBased;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

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
	
	private HashMap<Integer, Node[]> itemNeighborhood;
		
	private SparseMatrix trainMatrix;
	
	public ItemBasedKNN(int k, int n) {
		this.k = k;
		this.n = n;
		itemNeighborhood = new HashMap<Integer, Node[]>();
		trainMatrix = null;
		
	}
	
	public void train(SparseMatrix trainMatrix){
		/*
		 * TODO
		 * - Speedup by considering bidirectional distance equivalency
		 * - look for tradeoff between Time (object creation) and memory 
		 * 
		 * Overhead:
		 * - Creation of node object in each iteration
		 * 
		 * Advantage
		 * - Stores only top k item neighbors
		 */
		
		SparseVector vec1, vec2;
		Double sim;
		int columns, count = 0;
		this.trainMatrix = trainMatrix;
		columns = trainMatrix.getColumnCount();  //no of users
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
			Node [] neighbors = new Node[k];
			count = 0;
			for(Node node: boundedPQ){
				neighbors[count++] = node;
			}
			itemNeighborhood.put(i, neighbors);	
		}
	}
	
	public HashMap<String, Integer[]> getRecommendations(SparseMatrix testMatrix){
		HashMap<String, Integer[]> recommendations = 
				new HashMap<String, Integer[]>();
				
		Node[] neighbors;
		int idx = 0;
		Integer item;
		double rating = 0;
		
		BoundedPriorityQueue<Node> pq = 
				new BoundedPriorityQueue<Node>(Collections.reverseOrder(), n);
		
		for(int i = 0; i < testMatrix.getRowCount(); i++){ //iterate over users
			HashSet<Integer> userPurchasedItems = 
					trainMatrix.getRow(i).getIndicesHashSet();
			HashSet<Integer> testItems = 
					testMatrix.getRow(i).getIndicesHashSet();
			for(Integer j: testItems){ //iterate over items
				neighbors = itemNeighborhood.get(j);
				for(Node neighbor: neighbors){
					item = neighbor.getKey();
					if(userPurchasedItems.contains(item)) continue;
					rating = predict(i, j, itemNeighborhood.get(item),
							 		 userPurchasedItems);
					if(rating == 0) continue;
					pq.offer(new Node(item, rating));
				}
				
				if(pq.size() == n){
					Integer[] recommendation = new Integer[n];
					idx = 0;
					for(Node node: pq){
						recommendation[idx++] = node.getKey();
					}
					recommendations.put("" + i + ":" + "j", recommendation);
				}
				pq.clear();
			}	
		}
		return recommendations;
	}
	
	public double predict(int user, int item, Node[] neighbors, HashSet<Integer> userPurchasedItems){
		double rating = 0;
		double total = 0;
		double userPref = 0;
		double value;
		for(Node neighbor: neighbors){
			value = neighbor.getValue();
			if (userPurchasedItems.contains(neighbor.getKey()))
				userPref += value * 1; // 1 is rating
			total += value;
		}
		rating = userPref/total;
		return rating;
		
	}
}
