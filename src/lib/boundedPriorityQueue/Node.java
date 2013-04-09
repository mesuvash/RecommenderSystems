package lib.boundedPriorityQueue;

public class Node implements Comparable<Node>{
	private Integer key;
	private Double value;
	
	public Node(Integer key, Double value) {
		this.key = key;
		this.value = value;
	}
	
	public Integer getKey(){
		return key;
	}
	
	public Double getValue(){
		return value;
	}
	
	@Override
	public int compareTo(Node x) {
		return value.compareTo(x.getValue());
	}
	
	
	

}
