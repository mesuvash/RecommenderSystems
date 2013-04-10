package lib.datasetReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

import lib.matrix.SparseMatrix;

public class UserItemRatingReader{
	
	
	public static Integer[] getUserItemCount(String filePath, String delim) 
			throws Exception{
		
		Integer[] count = new Integer[2];		
		String line;
		HashSet<Integer> users = new HashSet<Integer>();
		HashSet<Integer> items = new HashSet<Integer>();
		
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		while(true){
			line = br.readLine();
			if(line == null) break;
			String[] tokens = line.split(delim);
			Integer uid = Integer.parseInt(tokens[0]); 
			Integer item_id = Integer.parseInt(tokens[1]); 
			users.add(uid);
			items.add(item_id);
		}
		count[0] = users.size();
		count[1] = items.size();
		return count;
	}
	
	public static boolean isRatingAvailable (String filePath, String delim)
		throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		if(line == null) throw new RuntimeException("Empty dataset");
		String[] tokens = line.split(delim);
		if (tokens.length == 3)
			return true;
		else
			return false;
			
	}

	public static SparseMatrix read(String filePath,String delim) 
			throws Exception {
		
		String line;
		Integer uid, item_id;
		Double rating;
		Integer[] userItemCount = 
				UserItemRatingReader.getUserItemCount(filePath,delim);
		System.out.println("Users : " + userItemCount[0]);
		System.out.println("Items : " + userItemCount[1]);

		SparseMatrix userItemRating = 
				new SparseMatrix(userItemCount[0], userItemCount[1]);
		boolean isRatingAvailable = UserItemRatingReader.isRatingAvailable(filePath, delim);
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		while(true){
			line = br.readLine();
			if(line == null) break;
			String[] tokens = line.split(delim);
			uid = Integer.parseInt(tokens[0]); 
			item_id = Integer.parseInt(tokens[1]); 
			if(isRatingAvailable != true)
				rating = 1.0;
			else{
				rating = Double.parseDouble(tokens[2]);
			}			
			// out of bound error may occur if the size
			// of the matrix doesn't match to max(uids) - 1
			// or max(item_ids) -1
			userItemRating.set(uid-1, item_id-1, rating);	
		}
		
		return userItemRating;
	}
	
	public static void main(String[] args) throws Exception {
		SparseMatrix ratings = 
				UserItemRatingReader.read("testfile", ",");
		System.out.println(ratings);
		
	}

}
