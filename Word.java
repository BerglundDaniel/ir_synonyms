package ir;
import java.io.Serializable;
public class Word implements Comparable<Word>, Serializable{
	private String name;
	private Integer occurences;
	public Word(String word, int x){
		name = word;
		occurences = new Integer(x);
	}
	public String getName(){
		return name;
	}
	public int getOccurences(){
		return occurences.intValue();
	}
	public int compareTo( Word other ) {return other.occurences.compareTo(occurences); }
}