/* Best.java */

package player;

/**
 *  A class that keeps track of a player's best move and its score.
 *  The best move will be updated.
 */
public class Best{

    protected Move move;
    protected double score;

    // Constructs an empty Best object with no specified move or score yet
    public Best() {
        move = null;
        score = 0;
    }
    
    // Constructs a Best object with the specified move and score
    public Best(Move move, double score) {
        this.move = move;
        this.score = score;
    }

    // Returns the Best score
    public double getScore() {
        return score;
    }

    // Returns the Best move
    public Move getMove() {
        return move;
    }

}