/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Johan Boye, 2010
 *   Second version: Johan Boye, 2012
 */  

package ir;

import java.io.Serializable;
import java.util.LinkedList;


public class Posting implements Comparable<Posting>, Serializable {
    
    public int docID;
    public double score;
	public LinkedList<Integer> offsets = new LinkedList<Integer>();
	

    /**
     *  PostingEntries are compared by their score (only relevant 
     *  in ranked retrieval).
     *
     *  The comparison is defined so that entries will be put in 
     *  descending order.
     */
    public int compareTo( Posting other ) {return Double.compare( other.score, score ); }
    
	public void setDocId(int newDocId){ docID = newDocId; }
	
	public void addOffset(int offset){ offsets.add(offset); }
	
	public int getFrequency() { return offsets.size(); }
	
}

    
