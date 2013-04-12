import java.io.Serializable;
public class Doc implements Comparable<Doc>, Serializable {
    
    public int id;
    public double score;
    public int compareTo( Doc other ) {return Double.compare( other.score, score ); }
	
}