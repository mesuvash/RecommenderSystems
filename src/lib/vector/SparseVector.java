package lib.vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SparseVector {
	
	private int length;
	private HashMap<Integer, Double> container;
	
	public SparseVector() {
		this.length = 0;
		this.container = new HashMap<Integer, Double>();
	}
	
	public SparseVector copy() {
		SparseVector newVec = new SparseVector(this.size());	
		for (int i : container.keySet()) {
			newVec.set(i, this.get(i));
		}
		return newVec;
	}
	

	public SparseVector(int length){
		this.length = length;
		container = new HashMap<Integer, Double>();
	}
	
	public void set(Integer key, Double value){
		if (key >= length) throw new RuntimeException("Out of bound index ");
		if(value ==  0.0){
			container.remove(key);
		}else{
			container.put(key, value);	
		}
	}
	
	public Double get(Integer key){
		if (key >= length) throw new RuntimeException("Out of bound index ");
		if(container.containsKey(key)){
			return container.get(key);
		}
		return 0.0;
	}
	
	public void remove(Integer key){
		this.set(key, 0.0);
	}
	
	/*
	 * Vector Properties
	 */
	
	public int size(){
		return length;
	}
	
	public int itemCount(){
		return container.keySet().size();
	}
	
	public HashSet<Integer> getIndicesHashSet(){
		return (HashSet<Integer>) container.keySet();
	}
	
	public int[] getIndices(){
		int itemCount = this.itemCount();
		int[] indices = new int[itemCount];
		int i = 0;
		for(Integer value: container.keySet()){
			indices[i] = value;
			i++;
		}
		return indices;
	}
	
	/*
	 * Vector Statistics
	 */
	
	public Double sum(){
		Double result = 0.0;
		for(Integer key : container.keySet()){
			result += container.get(key);
		}
		return result;
	}
	
	public Double average(){
		return this.sum() / this.itemCount();
	}
	
	public Double norm(){
	    SparseVector a = this;
		return Math.sqrt(this.innerProduct(a));
	}
	
	/*
	 * Scalar Arithmetic operations 
	 */
	// Inplace operations
	public void addInplace(Double value){
		for(Integer key: container.keySet()){
			this.set(key, container.get(key) + value);
		}
	}
	
	public void subtractInplace(Double value){
		this.addInplace(-value);
	}
	
	public void scaleInplace(Double value){
		for(Integer key: container.keySet()){
			this.set(key, container.get(key) * value);
		}
		
	}
	
	//Non-Inplace operations
	public SparseVector add(Double value){
		SparseVector result = new SparseVector(length);
		for(Integer key: container.keySet()){
			result.set(key, container.get(key) + value);
		}
		return result;
	}
	
	public SparseVector subtract(Double value){
		return this.add(-value);
	}
	
	public SparseVector scale(Double value){
		SparseVector result = new SparseVector(length);
		for(Integer key: container.keySet()){
			result.set(key, container.get(key) * value);
		}
		return result;
	}
	
	
	/*
	 * Vector Operations
	 */
	
	public Double innerProduct(SparseVector x){
		Double result = 0.0;
		if (x.size() != this.size()) throw new RuntimeException("Vector lengths disagree");
		// iterate over small
		if(x.size() < this.size()){
			for(Integer key : container.keySet()){
				if(x.container.containsKey(key))
					result += container.get(key) * x.get(key);
			}
		}
		else{
			for(Integer key : x.container.keySet()){
				if(this.container.containsKey(key))
					result += container.get(key) * x.get(key);
			}
		}
		return result;
	}


	private SparseVector _add(SparseVector vec, int sign){
		SparseVector _vec = this;
		if (_vec.size() != vec.size()) throw new RuntimeException("Vector lengths disagree");
		SparseVector newVec = new SparseVector(_vec.length);
		for(Integer key: _vec.container.keySet()){
			newVec.set(key, _vec.get(key));
		}
		for(Integer key: vec.container.keySet()){
			newVec.set(key,  newVec.get(key) + sign *vec.get(key)) ;
		}
		return newVec;
	}
	
	public  SparseVector add(SparseVector vec){
		return this._add(vec,1);
	}
	
	public SparseVector sub(SparseVector vec){
		return this._add(vec,-1);
	}
	
	// return a string representation
    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        for (Integer key : container.keySet()) {
        	strBuf.append( "(" + key.toString() + " : " + container.get(key).toString() + ")\n");
        }
        return strBuf.toString();
    }
	
    public static void main(String[] args) {
		SparseVector vec = new SparseVector(10);
		vec.set(1, 2.0);
		vec.set(4,3.0);
		
		SparseVector vec1 = new SparseVector(10);
		vec1.set(0, 2.0);
		vec1.set(2,3.0);
		
		System.out.println(vec);
		
		vec.scaleInplace(5.0);
		System.out.println(vec);
		
		System.out.println(vec.norm()); //18.027756377319946

		System.out.println(vec.innerProduct(vec1)); //45.0
	}
	
}
