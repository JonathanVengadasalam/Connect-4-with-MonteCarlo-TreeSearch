package connect4;
import java.util.List;

import java.util.ArrayList;

import java.util.Iterator;

import java.lang.Math;



public class connect {
	final static int Target = 4;
	final static int Height = 6;
	final static int Width = 7;
	final static int InitialPlayerMove = -1;
	
	public boolean terminated;
	public int playerJustMoved;
	public int state[][];
	public List<Integer> validmoves;
	public int indexes[];

	

	public connect() {
		playerJustMoved = InitialPlayerMove;
		terminated = false;
		state = new int[Height][Width];
		validmoves = new ArrayList<Integer>();
		for(int i=0; i<Width; i++) {
			validmoves.add(i);
		}
		indexes = new int[Width];
		for(int i=0; i<Width; i++) {
			indexes[i] = Height - 1;
		}
	}

	public connect(int playerjustmoved, boolean pterminated, int pstate[][], List<Integer> pvalidmoves, int pindexes[]) {
		playerJustMoved = playerjustmoved;
		terminated = pterminated;
		state = pstate;
		validmoves = pvalidmoves;
		indexes = pindexes;
	}

	// public functions
	public connect clone() {
		return new connect(this.playerJustMoved, this.terminated, copystate(), copymoves(), this.indexes.clone());
	}

	public int convertmove(String col) {
		int res;
		try{
			res = Integer.valueOf(col) - 1;
			if(this.indexes[res] < 0) {
				System.out.print("Choix impossible\n");
				res = -1;
			}
		}catch(NumberFormatException e){
			System.out.print("Le choix n'est pas un nombre\n");
			return -1;
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.print("Choix impossible\n");
			return -1;
		}
		return res;
	}

	public void domove(int col){
		int ind = this.indexes[col];
		this.playerJustMoved = -1*this.playerJustMoved;
		this.state[ind][col] = this.playerJustMoved;
		this.indexes[col] = this.indexes[col] - 1;

		if(this.indexes[col] < 0)
			removemove(col);
		
		if(testalignment(ind, col)){
			this.validmoves = new ArrayList<Integer>();
			this.terminated = true;
		}
	}

	public List<Integer> getmoves(){
		return copymoves();
	}

	public String show(){
		return "Justplayed:" + String.valueOf(this.playerJustMoved) + "\n" + this.toString() + "\n";
	}

	public double value(){
		double res = this.terminated ? 0.5 : 0;
		return 0.5 + res;
	}

	public String toString() {
		String st = this.convert(this.state[0][0]);
		String line = new String();


		for(int i=0; i<(4*Width-1); i++)
			line = line + "-";

		for(int i=1; i<Width; i++)
			st = st + "|" + this.convert(this.state[0][i]);
		
		for(int i=1; i<Height; i++) {
			st = st + "\n" + line + "\n" + this.convert(this.state[i][0]);
			for(int j=1; j<Width; j++)
				st = st + "|" + this.convert(this.state[i][j]);
		}
		st = st + "\n 1 ";
		for(int i=1; i<Width; i++)
			st = st + "  " + String.valueOf(i + 1) + " ";
		return st;
	}

	// private functions
	private String convert(int x) {
		String res = new String();
		if(x == 1)
			res = " X ";
		if(x == -1)
			res = " O ";
		if(x == 0)
			res = "   ";
		return res;
	}

	private int[][] copystate(){
		int res[][] = new int[Height][Width];
		for(int i=0; i<Height; i++){
			for(int j=0; j<Width; j++){
				res[i][j] = this.state[i][j];
			}
		}
		return res;
	}

	private List<Integer> copymoves(){
		List<Integer> res = new ArrayList<Integer>();
		for(int i=0; i<this.validmoves.size(); i++){
			res.add(this.validmoves.get(i));
		}
		return res;
	}
	
	private int countneighbors(int ind, int col, int imax, int a, int b) {
		int count = 0;
		int item = this.state[ind][col];
		for(int i=1; i<imax; i++) {
			if(item != this.state[ind+a*i][col+b*i]) {
				return count;
			}
			count = count + 1;
		}
		return count;
	}

	private void removemove(int move){
		Iterator<Integer> iterator = this.validmoves.iterator();
		while(iterator.hasNext())
		{
			int value = iterator.next();
			if (move == value)
			{
				iterator.remove();
				break;
			}
		}
	}

	private boolean testalignment(int ind, int col) {
		int ind1 = ind + 1;
		int ind2 = Height - ind;
		int col1 = col + 1;
		int col2 = Width - col;

		if(countneighbors(ind, col, ind2, 1, 0) + countneighbors(ind, col, ind1, -1, 0) + 1 > Target - 1)
			return true;
		if(this.countneighbors(ind, col, col2, 0, 1) + this.countneighbors(ind, col, col1, 0, -1) + 1 > Target - 1)
			return true;
		if(this.countneighbors(ind, col, Math.min(col2,ind2), 1, 1) + this.countneighbors(ind, col, Math.min(col1,ind1), -1, -1) + 1 > Target - 1)
			return true;
		if(this.countneighbors(ind, col, Math.min(col1,ind2), 1, -1) + this.countneighbors(ind, col, Math.min(col2,ind1), -1, 1) + 1 > Target - 1)
			return true;
		return false;
	}
}
