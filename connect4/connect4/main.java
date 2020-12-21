package connect4;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;

public class main {

	public static void main(String[] args) {

		// TODO Auto-generated method stub
		/*
		*/
		connect connect = new connect();
		play(connect,(short)5000);
	}

	private static void play(connect rootenv, short itermax){
		connect env = rootenv.clone();
		Scanner sc = new Scanner(System.in);
		Random rand = new Random();
		node node;
		int manturn = 2*rand.nextInt(2) - 1;
		boolean continue_game = true;
		boolean ask_order;
		String order = new String();
		int move = -1;
		System.out.print("Vous etes le joueur numero : " + String.valueOf(manturn) + "\nL'ordinateur est le joueur numero : " + String.valueOf(manturn*-1) + "\n\nPour jouer il faut choisir une colonne entre 1 et 7\n" + env.toString() + "\n");

		while (continue_game){
			if(env.playerJustMoved != manturn){
				ask_order = true;
				while(ask_order){
					System.out.print("Saisissez une commande : ");
					order = sc.nextLine();
					if(order.equals("q")){
						continue_game = false;
						ask_order = false;
					}
					else{
						move = env.convertmove(order);
						if(move != -1){
							env.domove(move);
							ask_order = false;
						}
					}
				}
			}
			else{
				node = mcts(env, itermax);
				move = node.move;
				env.domove(move);
			}
			if(order.equals("q")){
				System.out.print("Vous avez quitté");
			}
			else{
				String str = (manturn == env.playerJustMoved) ? "Vous venez" : "L'ordi viens";
				System.out.print("\n" + env.show(str, move + 1) + "\n");
			}
			if(env.getmoves().equals(new ArrayList<Integer>())){
				continue_game = false;
			}
		}

		if(!order.equals("q")){
			if(env.value() == 1.0){
				String str = (manturn == env.playerJustMoved) ? "Man" : "Computer";
				System.out.print(str + " gagné");
			}
			else{
				System.out.print("Personne n'a gagné");
			}
		}
		sc.close();
	}

	private static node mcts(connect rootenv, short itermax){
		node rootnode = new node(rootenv.playerJustMoved, rootenv.getmoves());
		Random rand = new Random();
		List<Integer> emptymoves = new ArrayList<Integer>();
		List<node> emptychilds = new ArrayList<node>();
		List<Integer> moves;
		connect env;
		node node;
		node child;
		double value;
		int move;
		int pjm;

		for(short i=0; i<itermax; i++){
			node = rootnode;
			env = rootenv.clone();

			// select
			while(node.untriedMoves.equals(emptymoves) && !node.childNodes.equals(emptychilds)){
				node = node.uctSelect();
				env.domove(node.move);
			}

			// expand
			if(!node.untriedMoves.equals(emptymoves)){
				move = node.untriedMoves.get(rand.nextInt(node.untriedMoves.size()));
				env.domove(move);
				child = new node(env.playerJustMoved, move, node, env.getmoves());
				removemove(node, move);
				node.childNodes.add(child);
				node = child;
			}

			// rollout
			moves = env.getmoves();
			while(!moves.equals(emptymoves)){
				move = moves.get(rand.nextInt(moves.size()));
				env.domove(move);
				moves = env.getmoves();
			}

			// backpropagate
			value = env.value();
			pjm = env.playerJustMoved;
			while(node instanceof node){
				node.wins = node.wins + 0.5 + (value - 0.5)*pjm*node.playerJustMoved;
				node.visits = (short)(node.visits + 1);
				node = node.parentNode;
			}
		}
		return rootnode.visitsSelect();
	}

	private static void removemove(node node, int move){
		Iterator<Integer> iterator = node.untriedMoves.iterator();
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
}
