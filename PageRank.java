/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Johan Boye, 2012
 */  

import java.util.*;
import java.io.*;
import java.lang.Math;

public class PageRank{

    /**  
     *   Maximal number of documents. We're assuming here that we
     *   don't have more docs than we can keep in main memory.
     */
    final static int MAX_NUMBER_OF_DOCS = 2000000;

    /**
     *   Mapping from document names to document numbers.
     */
    Hashtable<String,Integer> docNumber = new Hashtable<String,Integer>();

    /**
     *   Mapping from document numbers to document names
     */
    String[] docName = new String[MAX_NUMBER_OF_DOCS];

    /**  
     *   A memory-efficient representation of the transition matrix.
     *   The outlinks are represented as a Hashtable, whose keys are 
     *   the numbers of the documents linked from.<p>
     *
     *   The value corresponding to key i is a Hashtable whose keys are 
     *   all the numbers of documents j that i links to.<p>
     *
     *   If there are no outlinks from i, then the value corresponding 
     *   key i is null.
     */
    Hashtable<Integer,Hashtable<Integer,Boolean>> link = new Hashtable<Integer,Hashtable<Integer,Boolean>>();

    /**
     *   The number of outlinks from each node.
     */
    int[] out = new int[MAX_NUMBER_OF_DOCS];

    /**
     *   The number of documents with no outlinks.
     */
    int numberOfSinks = 0;

    /**
     *   The probability that the surfer will be bored, stop
     *   following links, and take a random jump somewhere.
     */
    final static double BORED = 0.15;

    /**
     *   Convergence criterion: Transition probabilities do not 
     *   change more that EPSILON from one iteration to another.
     */
    final static double EPSILON = 0.00001;

    /**
     *   Never do more than this number of iterations regardless
     *   of whether the transistion probabilities converge or not.
     */
    final static int MAX_NUMBER_OF_ITERATIONS = 1000;

    
    /* --------------------------------------------- */


    public PageRank( String filename, int algorithm) {
		int noOfDocs = readDocs( filename );
		computePagerank( noOfDocs, algorithm );
    }


    /* --------------------------------------------- */


    /**
     *   Reads the documents and creates the docs table. When this method 
     *   finishes executing then the @code{out} vector of outlinks is 
     *   initialised for each doc, and the @code{p} matrix is filled with
     *   zeroes (that indicate direct links) and NO_LINK (if there is no
     *   direct link. <p>
     *
     *   @return the number of documents read.
     */
    int readDocs( String filename ) {
	int fileIndex = 0;
	try {
	    System.err.print( "Reading file... " );
	    BufferedReader in = new BufferedReader( new FileReader( filename ));
	    String line;
	    while ((line = in.readLine()) != null && fileIndex<MAX_NUMBER_OF_DOCS ) {
		int index = line.indexOf( ";" );
		String title = line.substring( 0, index );
		Integer fromdoc = docNumber.get( title );
		//  Have we seen this document before?
		if ( fromdoc == null ) {	
		    // This is a previously unseen doc, so add it to the table.
		    fromdoc = fileIndex++;
		    docNumber.put( title, fromdoc );
		    docName[fromdoc] = title;
		}
		// Check all outlinks.
		StringTokenizer tok = new StringTokenizer( line.substring(index+1), "," );
		while ( tok.hasMoreTokens() && fileIndex<MAX_NUMBER_OF_DOCS ) {
		    String otherTitle = tok.nextToken();
		    Integer otherDoc = docNumber.get( otherTitle );
		    if ( otherDoc == null ) {
			// This is a previousy unseen doc, so add it to the table.
			otherDoc = fileIndex++;
			docNumber.put( otherTitle, otherDoc );
			docName[otherDoc] = otherTitle;
		    }
		    // Set the probability to 0 for now, to indicate that there is
		    // a link from fromdoc to otherDoc.
		    if ( link.get(fromdoc) == null ) {
			link.put(fromdoc, new Hashtable<Integer,Boolean>());
		    }
		    if ( link.get(fromdoc).get(otherDoc) == null ) {
			link.get(fromdoc).put( otherDoc, true );
			out[fromdoc]++;
		    }
		}
	    }
	    if ( fileIndex >= MAX_NUMBER_OF_DOCS ) {
		System.err.print( "stopped reading since documents table is full. " );
	    }
	    else {
		System.err.print( "done. " );
	    }
	    // Compute the number of sinks.
	    for ( int i=0; i<fileIndex; i++ ) {
		if ( out[i] == 0 )
		    numberOfSinks++;
	    }
	}
	catch ( FileNotFoundException e ) {
	    System.err.println( "File " + filename + " not found!" );
	}
	catch ( IOException e ) {
	    System.err.println( "Error reading file " + filename );
	}
	System.err.println( "Read " + fileIndex + " number of documents" );
	return fileIndex;
    }


    /* --------------------------------------------- */


    /*
     *   Computes the pagerank of each document.
     */
    void computePagerank( int numberOfDocs, int algorithm ) {
		long start = System.currentTimeMillis();
		if(algorithm==0) doPageRank(numberOfDocs);
		else if(algorithm==1) doMcEndPoint(numberOfDocs);
		else if(algorithm==2) doMcCyclicStart(numberOfDocs);
		long end = System.currentTimeMillis();
		long time = (end-start);
		System.out.println("Time executed: " + time+"ms");
    }
	
	void doMcEndPoint(int numberOfDocs){
		double N = numberOfDocs*50;
		double x[] = new double[numberOfDocs];
		Random randomGenerator = new Random();
		int randomDoc = randomGenerator.nextInt(numberOfDocs);

		for(int i = 0; i<N; i++){
			int jump = randomGenerator.nextInt(numberOfDocs);
			for(int j = 0; j<8;j++){
				if(link.get(jump)==null||(randomGenerator.nextInt(100)<10)){
					jump = randomGenerator.nextInt(numberOfDocs);
				}
				else{
					jump = randomJump(jump, numberOfDocs);
				}
			}
			x[jump] += 1.0/N;
		}
		ArrayList<Doc> results = new ArrayList<Doc>();
		for(int i = 0; i < x.length; i++) {
			Doc tmp = new Doc();
			tmp.id=i;
			tmp.score=x[i];
			results.add(tmp);
		}
		Collections.sort(results);
		System.out.println("Rank"+"\t"+"Id"+"\t"+"Score");
		for(int i = 0; i<50;i++) System.out.println(i+1+"\t"+ docName[results.get(i).id] +"\t"+ String.format("%.5f",results.get(i).score));
	}
	
	void doMcCyclicStart(int numberOfDocs){
		double N = numberOfDocs*50;
		double x[] = new double[numberOfDocs];
		Random randomGenerator = new Random();
		int randomDoc = randomGenerator.nextInt(numberOfDocs);
		int counter = 0;
		for(int i = 0; i<N; i++){
			if(counter>=numberOfDocs) counter = 0;
			int jump = counter;
			counter++;
			for(int j = 0; j<5;j++){
				if(link.get(jump)==null||(randomGenerator.nextInt(100)<10)){
					jump = randomGenerator.nextInt(numberOfDocs);
				}
				else{
					jump = randomJump(jump, numberOfDocs);
				}
			}
			x[jump] += 1.0/N;
		}
		ArrayList<Doc> results = new ArrayList<Doc>();
		for(int i = 0; i < x.length; i++) {
			Doc tmp = new Doc();
			tmp.id=i;
			tmp.score=x[i];
			results.add(tmp);
		}
		Collections.sort(results);
		System.out.println("Rank"+"\t"+"Id"+"\t"+"Score");
		for(int i = 0; i<50;i++) System.out.println(i+1+"\t"+ docName[results.get(i).id] +"\t"+ String.format("%.5f",results.get(i).score));
	}
	
	int randomJump(int docId, int numberOfDocs){
		Hashtable<Integer,Boolean> tmp = link.get(docId);
		Iterator<Map.Entry<Integer, Boolean>> it = tmp.entrySet().iterator();
		int[] links = new int[numberOfDocs];
		int i = 0;
		while (it.hasNext()) {
			Map.Entry<Integer, Boolean> entry = it.next();
			if (entry.getKey() != null) {
				links[i] = entry.getKey();
				i++;
			}
		}
		int item = new Random().nextInt(i);
		return links[item];
	}
	
	void doPageRank(int numberOfDocs) {
		double P[][] = new double[numberOfDocs][numberOfDocs];
		double x[] = new double[numberOfDocs];
		double xNext[] = new double[numberOfDocs];
		//Step 1 and 2.
		for(int i = 0; i<numberOfDocs; i++){
			if(link.get(i)!=null) {
				double size = link.get(i).size();
				for(int k = 0; k<numberOfDocs;k++){
					if(link.get(i).get(k)!=null) {P[i][k] = 1.0/size;}
					else{
						//if(i==k) P[i][k] = 0.0;
					}
				}
			}
			else for(int k = 0; k<numberOfDocs;k++) {
				if(i!=k)P[i][k] = 1.0/(numberOfDocs-1);	
				else if(i==k) P[i][k] = 0.0;
			}
		}
		//Step 3 and 4.
		for(int a = 0; a<numberOfDocs; a++){
			for(int b = 0; b<numberOfDocs;b++){
				P[a][b] = (1-BORED)*P[a][b];
				P[a][b] += (BORED/numberOfDocs);
			}
		}
		//set first state of xNext.
		xNext[0] = 1.0;
		ArrayList<Doc> results = new ArrayList<Doc>();
		int iter = 0;
		while(diff(xNext, x)){
			for(int i = 0; i < x.length; i++) {x[i] = xNext[i]; xNext[i]=0.0;}
			
			for(int i = 0; i<numberOfDocs; i++){
				for(int j = 0; j<numberOfDocs;j++){
					xNext[i] += x[j]*P[j][i];
				}
			}
			iter++;
		}
		System.out.println("Number of iterations: " + iter);
		for(int i = 0; i < x.length; i++) {
			Doc tmp = new Doc();
			tmp.id=i;
			tmp.score=xNext[i];
			results.add(tmp);
		}
		Collections.sort(results);
		System.out.println("Rank"+"\t"+"Id"+"\t"+"Score");
		for(int i = 0; i<50;i++) System.out.println(i+1+"\t"+ docName[results.get(i).id] +"\t"+ String.format("%.5f",results.get(i).score));
	}
	
	boolean diff(double first[], double second[]){
		int i = 0;
		double euclideanLength = 0.0;
		while(i < first.length){
			euclideanLength += (first[i]-second[i])*(first[i]-second[i]);
			i++;
		}
		euclideanLength = Math.sqrt(euclideanLength);
		if(euclideanLength<EPSILON) return false;
		else return true;
	}


    /* --------------------------------------------- */


    public static void main( String[] args ) {
	if ( args.length < 1 ) {
	    System.err.println( "Please give the name of the link file" );
	}
	else {
		int i = Integer.parseInt(args[1]);
	    new PageRank( args[0] , i);
	}
	}
}
	