package finalproject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry; // You may need it to implement fastSort

public class Sorting {

	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2) as it uses bubble sort, where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable> ArrayList<K> slowSort (HashMap<K, V> results) {
        ArrayList<K> sortedUrls = new ArrayList<K>();
        sortedUrls.addAll(results.keySet());	//Start with unsorted list of urls

        int N = sortedUrls.size();
        for(int i=0; i<N-1; i++){
			for(int j=0; j<N-i-1; j++){
				if(results.get(sortedUrls.get(j)).compareTo(results.get(sortedUrls.get(j+1))) <0){
					K temp = sortedUrls.get(j);
					sortedUrls.set(j, sortedUrls.get(j+1));
					sortedUrls.set(j+1, temp);					
				}
			}
        }
        return sortedUrls;                    
    }
    
    
	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable> ArrayList<K> fastSort(HashMap<K, V> results) {
    	// ADD YOUR CODE HERE
    	Entry[] entries=results.entrySet().toArray(new Entry[0]);
    	Entry[] tmp=entries.clone();
		mergeSort(entries,tmp,0,entries.length-1);
		ArrayList<K> result=new ArrayList<>();
		for(Entry<K,V> entry:entries){
			result.add(entry.getKey());
		}
		return result;
    }


	private static <K, V extends Comparable> void mergeSort(Entry<K,V>[] nums, Entry<K,V>[] temp, int start, int end) {
		if (start >= end){
			return ;
		}
		int mid = (start + end)/2;
		mergeSort(temp, nums, start, mid);
		mergeSort(temp, nums, mid + 1, end);

		merge(nums, temp, start, mid+1, end);
	}

	private static <K, V extends Comparable> void merge(Entry<K,V>[] nums, Entry<K,V>[] temp, int left, int right, int end) {
		int index = left;
		int leftSize = right;

		while (index <= end) {
			if (left==leftSize){
				nums[index] = temp[right++];
			} else if (right==end+1){
				nums[index] = temp[left++];
			} else if (temp[left].getValue().compareTo(temp[right].getValue()) <0 ) {
				nums[index] = temp[right++];
			} else{
				nums[index] = temp[left++];
			}
			index++;
		}

	}


}