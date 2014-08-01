/* MachinePlayer.java */

package player;

/**
 *  An implementation of an automatic Network player.  Keeps track of moves
 *  made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {

  public int compColor;
  public int oppColor;
  private int depth;
  public GameBoard board;
  private int totalDepth = 0;

  // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).  (White has the first move.)
  public MachinePlayer(int color) {
    board = new GameBoard(); // Initialize the GameBoard
    compColor = color;
    depth = 3;
    // Assigns the game piece colors to players
    if (compColor == GameBoard.WHITE) {
      oppColor = GameBoard.BLACK;
    } else {
      oppColor = GameBoard.WHITE;
    }
  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth) {
    this(color);
    depth = searchDepth;
  }

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  public Move chooseMove() {
    Double alpha = -50000.0;
    Double beta = 50000.0;
    Move bestMove = getValue(board, true, depth, alpha, beta).getMove();
    board.makeMove(compColor, bestMove);
    totalDepth++;
    return bestMove;
  } 



  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m) {
    if (board.isValidMove(oppColor, m)) {
      totalDepth++;
      board.makeMove(oppColor, m);
      return true;
    } else {
      return false;
    }
  }

  // If the Move m is legal, records the move as a move by "this" player
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method is used to help set up "Network problems" for your
  // player to solve.
  public boolean forceMove(Move m) {
    if (board.isValidMove(compColor, m)) {
      totalDepth++;
      board.makeMove(compColor, m);
      return true;
    } else {
      return false;
    }
  }


  // getValue is a helper function for alphaBeta. This function deals with the
  // terminal cases, such as depth == 0 and if either side has a Network. Otherwise,
  // it goes through the alphaBeta algorithm and returns the Best from it. Boolean side
  // is true if and only if we are on the side of the computer (compColor).
  private Best getValue(GameBoard board, boolean side, int depth, double alpha, double beta) {

    if (depth == 0  || board.hasNetwork(GameBoard.BLACK) || board.hasNetwork(GameBoard.WHITE)) {
      double resultScore = evalFxn(board);
      return new Best(null, resultScore);
    }
    return alphaBeta(board, side, depth, alpha, beta);
  }

  /**
  * alphaBeta() returns a Best object containing the best move for a side and the
  * corresponding score for the move based on an evaluation function evalFxn().
  * Makes use of the minimax algorithm with alpha beta pruning.
  * @param board: the current state of the game board
  * @param side: whose turn it is
  * @param depth: search depth of the algorithm
  * @param alpha: alpha value for pruning
  * @param beta: beta value for pruning
  * @return a Best object with the best move and score for a side
  **/
  private Best alphaBeta(GameBoard board, boolean side, int depth, double alpha, double beta) {
    int player; // The current player
    Best myBest = new Best(); // My best move
    Best reply; // Opponent's best move
    
    if (side) {
      myBest.score = alpha;
      player = compColor;
    } else {
      myBest.score = beta;
      player = oppColor;
    }
    // Generate list of all valid moves for current player
    Move[] allMoves = board.validMoves(player);

    // Need to return an arbitrary legal move if every move leads to loss;
    myBest.move = allMoves[0];
    for (Move m : allMoves) {
      if (m != null) {
        GameBoard copy = board.makeCopy(); // Make copy of current board
        copy.makeMove(player, m);
        totalDepth++;
        reply = getValue(copy, !side, depth-1, alpha, beta);
        totalDepth--;
        if (side) {
          if (reply.getScore() > myBest.getScore()) {
            myBest.move = m;
            myBest.score = reply.getScore();
            alpha = reply.getScore();
          }
        } else {
          if (reply.getScore() < myBest.getScore()) {
            myBest.move = m;
            myBest.score = reply.getScore();
            beta = reply.getScore();
          }
        }
        if (alpha >= beta) {
          return myBest;
        }
      } else {
        break;
      }
    }
    return myBest;
  }


  
  /**
  * evalFxn() computes a score for a given GameBoard board. The more 
  * positive the score, the higher the chance of winning (vice versa for negative).
  * @param board: The current GameBoard being evaluated for a score
  * @return a number that indicates how confidently a win can be achieved.
  **/
  private double evalFxn(GameBoard board) {
    double score = 0.0;
    if (board.hasNetwork(oppColor)) {
      return -300000.0 / totalDepth;
    } else if (board.hasNetwork(compColor)) {
      return 300000.0 / totalDepth;
    } else {}
    return score;
  }


  //Makes a copy of the move "m" and returns it.
  private Move makeCopyOfMove(Move m) {
    if (m.moveKind == Move.ADD) {
      return new Move(m.x1, m.y1);
    } else {
      return new Move(m.x1, m.y1, m.x2, m.y2);
    }
  }


  //Returns the number of chips in the left most goal (player white's goal).
  private int leftGoal() {
    int total = 0;
    for (int i = 1; i < 7; i ++) {
      if (board.getSquare(0, i).getChip() != null) {
        total++;
      }
    }
    return total;
  }


  //Returns the number of chips in the top most goal (player black's goal).
  private int topGoal() {
    int total = 0;
    for (int i = 1; i < 7; i ++) {
      if (board.getSquare(i, 0).getChip() != null) {
        total++;
      }
    }
    return total;
  }

}
