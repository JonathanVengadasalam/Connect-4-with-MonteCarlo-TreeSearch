package connect4;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

public class node {
	
    public int move;
    public node parentNode;
    public List<node> childNodes;
    public double wins;
    public short visits;
    public List<Integer> untriedMoves;
    public int playerJustMoved;

    public node(int playerjustmoved, int move, node parentnode, List<Integer> untriedmoves){
        this.playerJustMoved = playerjustmoved;
        this.move = move;
        this.parentNode = parentnode;
        this.childNodes = new ArrayList<node>();
        this.untriedMoves = untriedmoves;
        this.wins = 0;
        this.visits = 0;
    }
    
    public node(int playerjustmoved, List<Integer> untriedmoves){
        this.playerJustMoved = playerjustmoved;
        this.childNodes = new ArrayList<node>();
        this.untriedMoves = untriedmoves;
        this.wins = 0;
        this.visits = 0;
    }
    
    public node uctSelect(){
        List<node> childs = this.childNodes;
        node res = childs.get(0);
        double max = res.upper_confident_bounce(this.visits);
        double tmp;
        if(childs.size()>1){
            for(int i=1; i<childs.size(); i++){
                tmp = childs.get(i).upper_confident_bounce(this.visits);
                if(max < tmp){
                    max = tmp;
                    res = childs.get(i);
                }
            }
        }
        return res;
    }
    
    public node visitsSelect(){
        List<node> childs = this.childNodes;
        node res = childs.get(0);
        double max = res.visits;
        double tmp;
        if(childs.size()>1){
            for(int i=1; i<childs.size(); i++){
                tmp = childs.get(i).visits;
                if(max < tmp){
                    max = tmp;
                    res = childs.get(i);
                }
            }
        }
        return res;
    }

    public String toString(){
        return "[:" + String.valueOf(this.move) + " W/V:" + String.valueOf(this.wins) + "/" + String.valueOf(this.visits) + " U:" + String.valueOf(this.untriedMoves);
    }

    public String treetoString(int indent){
        List<node> childs = this.childNodes;
        String res = indentString(indent) + toString();
        if(!childs.equals(new ArrayList<node>())){
            for(int i=0; i<childs.size(); i++){
                if(childs.get(i).visits > 0){
                    res = res + childs.get(i).treetoString(indent + 1);
                }
            }
        }
        return res;
    }

    public double upper_confident_bounce(short V){
        return this.wins/this.visits + 0.5*Math.sqrt(V)/this.visits;
    }

    private String indentString(int indent){
        String res = "\n";
        for(int i=1; i<indent+1; i++){
            res = res + "| ";
        }
        return res;
    }
}