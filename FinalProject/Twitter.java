package finalProject;

import java.util.ArrayList;

public class Twitter {
	
	MyHashTable<String, Integer> sortingTrend = new MyHashTable(1);
	MyHashTable<String, ArrayList<Tweet>> sortingDate = new MyHashTable(1);
	MyHashTable<String, Tweet> sortingAuthor;
	
	private ArrayList<String> stopWords;
	private ArrayList<Tweet> tweets;
	
	 
	
	// O(n+m) where n is the number of tweets, and m the number of stopWords
	public Twitter(ArrayList<Tweet> tweets, ArrayList<String> stopWords) {
		
		this.stopWords = stopWords;
		this.tweets = tweets;
	    
		initHashMap();
		
	}
	
	
    /**
     * Add Tweet t to this Twitter
     * O(1)
     */
	public void addTweet(Tweet t) {
		tweets.add(t);
		initHashMap(); 
	}
	
	private void initHashMap() {
		
		int sizeOfTweets = tweets.size();
		
		sortingAuthor = new MyHashTable(sizeOfTweets);
		
		for (Tweet myTweet: tweets) {
			
			String curAuthor = myTweet.getAuthor();
			
			int limit = 0;
			if (sortingAuthor.get(curAuthor) == null || sortingAuthor.get(curAuthor).getDateAndTime().compareTo(myTweet.getDateAndTime()) < limit) {
				sortingAuthor.put(curAuthor, myTweet);
			}
			
			String[] tweetDate = myTweet.getDateAndTime().split(" ");
			
			String firstTweetDate = tweetDate[0];
			
			if(sortingDate.get((firstTweetDate)) != null) {
				
				sortingDate.get(firstTweetDate).add(myTweet);
			}
			
			else {
				sortingDate.put(firstTweetDate, new ArrayList<>());
				sortingDate.get(firstTweetDate).add(myTweet);
			}
		}
	}
	

    /**
     * Search this Twitter for the latest Tweet of a given author.
     * If there are no tweets from the given author, then the 
     * method returns null. 
     * O(1)  
     */
    public Tweet latestTweetByAuthor(String author) {
      
    	Tweet authorToReturn = sortingAuthor.get(author);
    	return authorToReturn;
    	
        
    }


    /**
     * Search this Twitter for Tweets by `date' and return an 
     * ArrayList of all such Tweets. If there are no tweets on 
     * the given date, then the method returns null.
     * O(1)
     */
    public ArrayList<Tweet> tweetsByDate(String date) {
    	
    	ArrayList<Tweet> dateToReturn = sortingDate.get(date);
        
    	return dateToReturn;
    }
    
	/**
	 * Returns an ArrayList of words (that are not stop words!) that
	 * appear in the tweets. The words should be ordered from most 
	 * frequent to least frequent by counting in how many tweet messages
	 * the words appear. Note that if a word appears more than once
	 * in the same tweet, it should be counted only once. 
	 */
    public ArrayList<String> trendingTopics() {
        
    	for (Tweet myTweet: tweets) {
    		
    		String myMessage = myTweet.getMessage();
    		ArrayList<String> tempW = getWords(myMessage);
    		tempW.removeAll(stopWords);
    		
    		int adjust = 1;
    		
    		for (String str: tempW) {
    			if (!sortingTrend.get(str).equals(null)) {
    				sortingTrend.put(str, sortingTrend.get(str) + adjust);
    			}
    			
    			else if (sortingTrend.get(str).equals(null)) {
    				sortingTrend.put(str, adjust);
    			}
    		}    		
    	}
    	
       	return MyHashTable.fastSort(sortingTrend);
    	
    }
    
    
    
    /**
     * An helper method you can use to obtain an ArrayList of words from a 
     * String, separating them based on apostrophes and space characters. 
     * All character that are not letters from the English alphabet are ignored. 
     */
    private static ArrayList<String> getWords(String msg) {
    	msg = msg.replace('\'', ' ');
    	String[] words = msg.split(" ");
    	ArrayList<String> wordsList = new ArrayList<String>(words.length);
    	for (int i=0; i<words.length; i++) {
    		String w = "";
    		for (int j=0; j< words[i].length(); j++) {
    			char c = words[i].charAt(j);
    			if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))
    				w += c;
    			
    		}
    		wordsList.add(w);
    	}
    	return wordsList;
    }

    

}
