package firstproject;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;

public class firstclass {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		*/
		Connect connect = new Connect();
		play(connect,(short)5000);
	}

	private static void play(Connect rootenv, short itermax){
		Connect env = rootenv.clone();
		Scanner sc = new Scanner(System.in);
		Random rand = new Random();
		Node node;
		int manturn = 2*rand.nextInt(2) - 1;
		boolean continue_game = true;
		boolean ask_order;
		String order = new String();
		int move = -1;
		System.out.print("Man is the player " + String.valueOf(manturn) + "\n" + env.show());
		
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
				System.out.print("\nMove: " + String.valueOf(move+1) + "\n" + env.show() + "\n");
			}
			if(env.getmoves().equals(new ArrayList<Integer>())){
				continue_game = false;
			}
		}

		if(!order.equals("q")){
			if(env.value() == 1.0){
				String str = (manturn == env.playerJustMoved) ? "Man" : "Computer";
				System.out.print(str + " wins");
			}
			else{
				System.out.print("Nobody wins");
			}
		}
		sc.close();
	}

	private static Node mcts(Connect rootenv, short itermax){
		Node rootnode = new Node(rootenv.playerJustMoved, rootenv.getmoves());
		Random rand = new Random();
		List<Integer> emptymoves = new ArrayList<Integer>();
		List<Node> emptychilds = new ArrayList<Node>();
		List<Integer> moves;
		Connect env;
		Node node;
		Node child;
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
				child = new Node(env.playerJustMoved, move, node, env.getmoves());
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
			while(node instanceof Node){
				node.wins = node.wins + 0.5 + (value - 0.5)*pjm*node.playerJustMoved;
				node.visits = (short)(node.visits + 1);
				node = node.parentNode;
			}
		}
		return rootnode.visitsSelect();
	}

	private static void removemove(Node node, int move){
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
