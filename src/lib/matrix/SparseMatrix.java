//Author: Suvash  Sedhain

package lib.matrix;

import lib.vector.SparseVector;

public class SparseMatrix {
	
	private int M;
	private int N;
	
	private SparseVector[] rows;
	private SparseVector[] cols;
	
	public SparseMatrix(int m, int n) {
		M = m;
		N = n;
		rows = new SparseVector[m];
		cols = new SparseVector[n];
		
		for (int i = 0; i < M; i++) {
			rows[i] = new SparseVector(N);
		}
		for (int j = 0; j < N; j++) {
			cols[j] = new SparseVector(M);
		}
	}
	
	/*
	 * Todo:
	 * - Construct sparse matrix from another sparse matrix 
	 * 	 public SparseMatrix(SparseMatrix mat)
	 */
	
	// Properties of the matrices
	public int[] dim(){
		int[] dims = new int[2];
		dims[0] = M;
		dims[1] = N;
		return dims;
	}
	
	public int getRowCount(){
		return M;
	}
	
	public int getColumnCount(){
		return N;
	}
	
	public int countNonZero(){
		int count = 0;
		
		for(int i = 0; i < M; i++){
			count += rows[i].itemCount();
		}
		return count;
	}
	
	/*
	 * Getters/Setters methods
	 */
	
	public Double get(Integer i, Integer j){
		return rows[i].get(j);
	}
	
	public void set(Integer i, Integer j, Double value){
		rows[i].set(j, value);
		cols[j].set(i, value);
	}
	
	public SparseVector getRow(Integer i){
		return rows[i];
	}
	
	public SparseVector getColumn(Integer i){
		return cols[i];
	}
	
	/*
	 * Matrix operations
	 * Todo:
	 * - matrix - matrix
	 * 		- addition/subtraction
	 * 		- multiplication
	 * - matrix - vector
	 * 		+ multiplication
	 * - matrix - scalar
	 * 		+ addition/subtraction
	 * 		+ scaling
	 * 
	 * - think about all possible inplace operations
	 */
	
	/*
	 * Matrix Scalar operations
	 */
	public SparseMatrix scale(double x){
		SparseMatrix mat = new SparseMatrix(M, N);
		
		for (int i = 0; i < mat.M; i++) {
			mat.rows[i] = rows[i].scale(x);
		}
		for (int j = 0; j < mat.N; j++) {
			mat.cols[j] = cols[j].scale(x);
		}
		return mat;
	}
	
	public SparseMatrix add(double value){
		SparseMatrix mat = new SparseMatrix(M, N);
		
		for (int i = 0; i < mat.M; i++) {
			mat.rows[i] = rows[i].add(value);
		}
		for (int j = 0; j < mat.N; j++) {
			mat.cols[j] = cols[j].add(value);
		}
		return mat;
	}
	
	public SparseMatrix subtract(double value){
		return this.add(-value);
	}
	
	/*
	 * Matrix Vector operations
	 */
	
	public SparseVector times(SparseVector x){
		// y = A x
		if ( this.getRowCount() != x.size()) 
			throw new RuntimeException("Matrix - Vector Dimension disagree");
		SparseMatrix A = this;
		SparseVector y = new SparseVector(M);
		
		for (int i = 0; i < M; i++) {
			y.set(i, A.rows[i].innerProduct(x));
		}
		return y;
	}
	
	/*
	 * Matrix Matrix operations
	 */
 
	
	/*
	 * Linear algebra routines
	 * - Inverse
	 * - LU decomposition
	 */
	
	public SparseMatrix transpose() {
		SparseMatrix T = new SparseMatrix(this.N, this.M);
		
		T.cols = this.rows;
		T.rows = this.cols;
		
		return T;
	}
	
	/*
	 * String representation of the matrix
	 */
	public String toString() {
		
        StringBuffer strBuf = new StringBuffer();
        for (int i = 0; i < this.M; i++) {
        	SparseVector row = this.rows[i];
        	for (int j : row.getIndices()) {
        		
        		strBuf.append("(" + i + ", " + j + ": " + this.get(i, j) + ")" + " ");
        	}
        	strBuf.append("\n");
        }
        
        return strBuf.toString();
    }
	
	

}
