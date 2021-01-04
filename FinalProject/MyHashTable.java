package finalProject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;


public class MyHashTable<K,V> implements Iterable<HashPair<K,V>>{
    // num of entries to the table
    private int numEntries;
    // num of buckets 
    private int numBuckets;
    // load factor needed to check for rehashing 
    private static final double MAX_LOAD_FACTOR = 0.75;
    // ArrayList of buckets. Each bucket is a LinkedList of HashPair
    private ArrayList<LinkedList<HashPair<K,V>>> buckets; 
    
    // constructor
    public MyHashTable(int initialCapacity) {
    	
    	this.numBuckets = initialCapacity; //initializing the fields
    	this.numEntries = 0;			   
    	    	
    	this.buckets = new ArrayList<>();  //empty arrayList created
    	
    	for (int index = 0; index < this.numBuckets; index++) {
    		this.buckets.add(new LinkedList<>());   //every index in the arrayList has a linkedList
    		
    	}
    }
    
    public int size() {
        return this.numEntries;
    }
    
    public boolean isEmpty() {
        return this.numEntries == 0;
    }
    
    public int numBuckets() {
        return this.numBuckets;
    }
    
    /**
     * Returns the buckets variable. Useful for testing  purposes.
     */
    public ArrayList<LinkedList< HashPair<K,V> > > getBuckets(){
        return this.buckets;
    }
    
    /**
     * Given a key, return the bucket position for the key. 
     */
    public int hashFunction(K key) {
        int hashValue = Math.abs(key.hashCode())%this.numBuckets;
        return hashValue;
    }
    
    /**
     * Takes a key and a value as input and adds the corresponding HashPair
     * to this HashTable. Expected average run time  O(1)
     */
    public V put(K key, V value) {
        
    	int hashNum;
    	hashNum = this.hashFunction(key); //generates hashed number for the key given
    	
    	for (HashPair<K,V> entryPtr : this.buckets.get(hashNum)) {
    		
    		if(entryPtr.getKey().equals(key)) {  //if the key pairs match
    			    			
    			entryPtr.setValue(value);
       			return entryPtr.getValue();
    		}
    	}
    	
    	this.buckets.get(hashNum).add(new HashPair<>(key, value));
    	//new key value pairing added
    	this.numEntries = this.numEntries + 1;  //incremented when new entry added
    	
    	if((double) this.numEntries/this.numBuckets > MAX_LOAD_FACTOR) {
    		this.rehash();
    	}
    	
    	return null;
    }
    
    
    /**
     * Get the value corresponding to key. Expected average runtime O(1)
     */
    
    public V get(K key) {
    	
    	//generate hash number for the key
    	
    	int hashNum = this.hashFunction(key);
    	
    	for (HashPair<K,V> ptr : this.buckets.get(hashNum)) {
    		if(ptr.getKey().equals(key)) {
    			V valToReturn = ptr.getValue();  //return the value associated with the key
    			return valToReturn;
    		}
    	}
    	
    	return null;  //if no matches found
    }
    
    /**
     * Remove the HashPair corresponding to key . Expected average runtime O(1) 
     */
    public V remove(K key) {
        
    	int index; //used to iterate
    	
    	//size of the current bucket
    	
    	int buckLength = buckets.get(hashFunction(key)).size();  
    	int hashNum = this.hashFunction(key);
    	
    	for (index = 0; index < buckLength; index++) { 
    		//for every item in that bucket
    		
    		if(buckets.get(hashNum).get(index).getKey().equals(key)) {  //if search found
    			
    			V tmpVal = buckets.get(hashNum).get(index).getValue();
    			buckets.get(hashNum).remove(index);
    			this.numEntries = this.numEntries - 1;  //the entry is removed and size decremented
    			
    			return tmpVal;
    		}
    	}
    	
    	return null; //returns null if nothing is removed
    	
    }
    
    
    /** 
     * Method to double the size of the hashtable if load factor increases
     * beyond MAX_LOAD_FACTOR.
     * Made public for ease of testing.
     * Expected average runtime is O(m), where m is the number of buckets
     */
    public void rehash() {
    	
    	this.numBuckets *=2;  //access parent node
    	
    	ArrayList<LinkedList<HashPair<K,V>>> container = this.buckets;
    	
    	this.buckets = new ArrayList<LinkedList<HashPair<K,V>>>(this.numBuckets);
    	
    	int bucketNum = this.numBuckets;
        
    	for (int index = 0; index < bucketNum; index++) {  //for every entry in bucket
    		this.buckets.add(new LinkedList<HashPair<K,V>>()); //new linked list is added
    	}
    	
    	for (LinkedList<HashPair<K,V>> bucketPtr : container) {
    		for (HashPair<K,V> entry : bucketPtr) {
    			this.buckets.get(this.hashFunction(entry.getKey())).add(entry);
    		}
    	}
    }
    
    
    /**
     * Return a list of all the keys present in this hashtable.
     * Expected average runtime is O(m), where m is the number of buckets
     */
    
    public ArrayList<K> keys() {
        
    	ArrayList<K> returnKey;
    	int entryNum = this.numEntries;
    	returnKey = new ArrayList<K>(entryNum);
    	
    	for(LinkedList<HashPair<K,V>> bucketPtr : this.buckets) {
    		for (HashPair<K,V> entry : bucketPtr) {  
    			returnKey.add(entry.getKey()); 
    			//key is retrieved for every entry in every bucket
    		}
    	}
    	
    	return returnKey;
        
    }
    
    /**
     * Returns an ArrayList of unique values present in this hashtable.
     * Expected average runtime is O(m) where m is the number of buckets
     */
    public ArrayList<V> values() {
        
    	int entryNum = this.numEntries;
    	ArrayList<V> returnVal;
    	returnVal = new ArrayList<V>(entryNum);
    	
    	int bucketNum = this.numBuckets;
    	MyHashTable<V,K> tableOfValues = new MyHashTable<V,K>(bucketNum);
    	
    	for (K keyPtr : this.keys()) {
    		V tempKey = this.get(keyPtr);
    		tableOfValues.put(tempKey, keyPtr);
    	}
    	
    	ArrayList<LinkedList<HashPair<V,K>>> listOfBuckets = tableOfValues.getBuckets();
    	LinkedList<HashPair<V,K>> bucketPtr;
    	
    	for (V valueKeyPtr : tableOfValues.keys()) {
    		bucketPtr = listOfBuckets.get(tableOfValues.hashFunction(valueKeyPtr));
    		
    		int lenOfBucket = bucketPtr.size();
    		
    		if (lenOfBucket == 1) {
    			returnVal.add(valueKeyPtr);
    			continue;
    		}
    		
    		else if(lenOfBucket == 0) {
    			continue;
    		}
    		
    		else {
    			returnVal.add(valueKeyPtr);
    			
    			int index;
    			for (index = 0; index< bucketPtr.size(); index++) {
    				if (bucketPtr.get(index).getKey().equals(valueKeyPtr)) {
    					bucketPtr.remove(index);
    					index = index - 1;
    				}
    			}
    		}
    	}
    	
    	return returnVal;
    	
    }
    
    
	/**
	 * This method takes as input an object of type MyHashTable with values that 
	 * are Comparable. It returns an ArrayList containing all the keys from the map, 
	 * ordered in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2), where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable<V>> ArrayList<K> slowSort (MyHashTable<K, V> results) {
        ArrayList<K> sortedResults = new ArrayList<>();
        for (HashPair<K, V> entry : results) {
			V element = entry.getValue();
			K toAdd = entry.getKey();
			int i = sortedResults.size() - 1;
			V toCompare = null;
        	while (i >= 0) {
        		toCompare = results.get(sortedResults.get(i));
        		if (element.compareTo(toCompare) <= 0 )
        			break;
        		i--;
        	}
        	sortedResults.add(i+1, toAdd);
        }
        return sortedResults; //b
    }
    
    
	/**
	 * This method takes as input an object of type MyHashTable with values that 
	 * are Comparable. It returns an ArrayList containing all the keys from the map, 
	 * ordered in descending order based on the values they mapped to.
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 */
    
    public static <K, V extends Comparable<V>> ArrayList<K> fastSort(MyHashTable<K, V> results) {
        
    	ArrayList<K> keySort = results.keys();
    	ArrayList<K> listOfSortedResults = new ArrayList<>();
    	
    	int keySortSize = keySort.size();
    	
    	if(keySortSize > 1) {
    		quickSort(results, keySort, 0, keySortSize - 1);
    	}
    	
    	return keySort;

    }
    
    private static <K,V extends Comparable<V>> void quickSort(MyHashTable<K,V> myTable, ArrayList<K> keySort, int lI, int rI){
    	
    	boolean rightBig = lI < rI;
    	if(rightBig) {
    		V centre = myTable.get(keySort.get(rI));
    		
    		int wall = lI - 1;
    		
    		for (int index = lI; index < rI; index++) {
    			
    			K curInd = keySort.get(index);
    			if (myTable.get(curInd).compareTo(centre) > 0){
    				wall = wall + 1; //increment wall
    				
    				K tmp = keySort.get(wall);
    				
    				keySort.set(wall,  keySort.get(index));
    				keySort.set(index, tmp);
    			}
    		}
    		
    		K tmp = keySort.get(rI);
    		
    		int cenLoc = wall + 1;
    		
    		keySort.set(rI, keySort.get(wall+1));
    		keySort.set(cenLoc, tmp);
    		
    		
    		
    		quickSort(myTable, keySort, lI, cenLoc - 1);
    		quickSort(myTable, keySort, cenLoc + 1, rI);
    	}
    	
    }
    
    
                
    
    @Override
    public MyHashIterator iterator() {
        return new MyHashIterator();
    }   
    
    private class MyHashIterator implements Iterator<HashPair<K,V>> {
    	
    	Iterator iter;
        
    	ArrayList<HashPair<K,V>> newList = new ArrayList<>();
    	
    	
    	/**
    	 * Expected average runtime is O(m) where m is the number of buckets
    	 */
        private MyHashIterator() {
            
        	for (LinkedList<HashPair<K,V>> index: buckets) {
        		for (HashPair<K,V> counterI : index) {
        			newList.add(counterI);
        		}
        	}
        	
        	iter = newList.iterator();
        }
        
        @Override
        /**
         * Expected average runtime is O(1)
         */
        public boolean hasNext() {
            
        	 boolean valToReturn = iter.hasNext();
        	 return valToReturn;
        }
        
        @Override
        /**
         * Expected average runtime is O(1)
         */
        public HashPair<K,V> next() {
            return (HashPair<K,V>) iter.next();
        }
        
    }
}

