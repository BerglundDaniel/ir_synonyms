/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Hedvig Kjellström, 2012
 */  

package ir;

import java.util.LinkedList;
import java.util.StringTokenizer;

public class Query {
    
    public LinkedList<String> terms = new LinkedList<String>();
    public LinkedList<Double> weights = new LinkedList<Double>();

    /**
     *  Creates a new empty Query 
     */
    public Query() {
	}
	
    /**
     *  Creates a new Query from a string of words
     */
    public Query( String queryString  ) {
		StringTokenizer tok = new StringTokenizer( queryString );
		while ( tok.hasMoreTokens() ) {
			terms.add( tok.nextToken() );
			weights.add( new Double(1) );
		}    
	}
	
    /**
     *  Returns the number of terms
     */
    public int size() {
		return terms.size();
	}
	
    /**
     *  Returns a shallow copy of the Query
     */
    public Query copy() {
		Query queryCopy = new Query();
		queryCopy.terms = (LinkedList<String>) terms.clone();
		queryCopy.weights = (LinkedList<Double>) weights.clone();
		return queryCopy;
	}
	
    /**
     *  Expands the Query using Relevance Feedback
     */
	 // results contain the ranked list from the current search
	// docIsRelevant contains the users feedback on which of the 10 first hits are relevant
    public void relevanceFeedback( PostingsList results, boolean[] docIsRelevant, Indexer indexer ) {
	/*
		double alpha = 1.0;
		double beta = 0.75;
		for(int i = 0; i > results.length(); i++){
			results.getList().get(i).score	= alpha*results.getList().get(i).score+(beta*(1/results.size())*sum(results));*/
    }
	/*
	public double sum(PostingsList docs){
		for
	*/
}

    
