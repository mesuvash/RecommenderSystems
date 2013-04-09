package recommender;

import lib.matrix.SparseMatrix;

public interface Recommender {
	
	public void train(SparseMatrix trainMatrix);
	
	//public Metrics evaluate();

}
