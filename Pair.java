// Pair Class; used to store a coordinate (x,y)
// William Lin and Matthew Liu
// Jan 21, 2022

public class Pair implements Comparable<Pair>{
	private int x, y;
  //constructor method for the pair class
	public Pair (int x0, int y0){
		x=x0;
		y=y0;
	}

  //compare function used to compare values 
	public int compareTo(Pair e){
		if(x == e.x){
			if(y<e.y) return -1;
			else return 1;
		}
		else {
			if(x < e.x) return -1;
			else return 1;
		}
	}

  //accessor methods
	public int getX() {return x;}
	public int getY() {return y;}
  
	//check if two pair objects are equal or not
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (getClass()!= obj.getClass()) return false;
		Pair other = (Pair)obj;
		return this.x == other.x && this.y == other.y;
	}
	
	//generate a unique hash code for a given pair; used for HashSets
	public int hashCode() {
		return (int) 1e6 * x + y;
	}
}

