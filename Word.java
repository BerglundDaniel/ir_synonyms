package ir;
import java.io.Serializable;
public class Word implements Comparable<Word>, Serializable{
	private String name;
	private int occurences;
	public Word(String word, int x){
		name = word;
		occurences = x;
	}
	public String getName(){
		return name;
	}
	public int getOccurences(){
		return occurences;
	}
	public int compareTo( Word other ) {return Integer.compare( other.occurences, occurences ); }
}