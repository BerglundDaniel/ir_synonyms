/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Johan Boye, 2010
 *   Second version: Johan Boye, 2012
 */  

package ir;

import java.util.LinkedList;
import java.util.HashMap;
import java.io.Serializable;

/**
 *   A list of Posting for a given word.
 */
public class PostingsList implements Serializable , Comparable<PostingsList>{
    
    /** The Posting list as a linked list. */
    private LinkedList<Posting> list = new LinkedList<Posting>();
	private HashMap<Integer,Posting> docIds = new HashMap<Integer,Posting>();

    /**  Number of Posting in this list  */
    public int size() {
		return list.size();
    }
	
	public LinkedList<Posting> getList(){ return list;}
    /**  Returns the ith posting */
    public Posting getPosting( int i ) {
		return list.get( i );
    }
	
	public int getDocumentFrequency() { return list.size(); }
	
	public void addRanked(Posting pE){
		if(!docIds.containsKey(pE.docID)){
			docIds.put(pE.docID, pE);
			list.add(pE);
		}
		else{ 
			//docIds.get(pE.docID).score+=pE.score;
			int i = 0;
			while(getPosting(i).docID!=pE.docID) i++;
			if(getPosting(i).docID==pE.docID) getPosting(i).score+=pE.score;
		}	
		
	}
    public void add(int docId, int offset){
		if(!docIds.containsKey(docId)){
			Posting pE = new Posting();
			pE.setDocId(docId);
			pE.addOffset(offset);
			docIds.put(docId, pE);
			list.add(pE);
		}
		else{ 
			Posting pE = docIds.get(docId);
			pE.addOffset(offset);
		}	
		
	}
	
	
	public int compareTo(PostingsList other){
		return (this.size()-other.size());
	}
}
	

			   
