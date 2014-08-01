/* GameBoard.java */

package player;
import java.lang.Math;

/**
 *  An implementation of a GameBoard. 
 */

public class GameBoard {

    private Square[][] board;
    private Chip[] blackChips = new Chip[10];
    private Chip[] whiteChips = new Chip[10];
    private int numBlack = 0;
    private int numWhite = 0;
    public static final int BLACK = 0;
    public static final int WHITE = 1;


    //Creates an empty GameBoard with all Squares initialized to a new Square with null Chip.
    public GameBoard() {
        board = new Square[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                //need to initialize the Squares in board so they are not null.
                board[i][j] = new Square(i, j);
            }
        }
    }

    //Returns the Square at location x, y.
    public Square getSquare(int x, int y) {
        return board[x][y];
    }


    //Adds a chip to the board and updates the blackChips[] or whiteChips[], and numBlack or numWhite.
    public void addChip(Chip chip, int x, int y) {
        board[x][y].add(chip);
        if (chip.getColor() == GameBoard.BLACK) {
            blackChips[numBlack] = chip;
            numBlack++;
        } else {
            whiteChips[numWhite] = chip;
            numWhite++;
        }
    }

    //Moves a chip from its old location to the new Square at x, y. Square.add(chip) takes care of updating
    //the chip's location.
    public void moveChip(Chip chip, int x, int y) {
        Square old = chip.getLocation();
        board[x][y].add(chip);
        old.remove();

    }

    //Makes a move, whether the move requires an addition of a chip or movement of a chip.
    //Assumes that you only input a Move "move" that isValidMove, the method makeMove does not
    //Check if it is a valid move for you.
    public void makeMove(int color, Move move) {
        if (move.moveKind == Move.ADD) {
            //if a Chip needs to be added.
            addChip(new Chip(color), move.x1, move.y1);
        } else if (move.moveKind == Move.STEP) {
            //if Chip needs to be moved
            moveChip(board[move.x2][move.y2].getChip(), move.x1, move.y1);
        } else {
            return;
        }
    }

    //Returns the number of chips on the board of "color".
    public int numChips(int color) {
        if (color == GameBoard.BLACK) {
            return numBlack;
        } else {
            return numWhite;
        }
    }



    //Returns true if "this" GameBoard has a network for
    //the "color".
    public boolean hasNetwork(int color) {
        Chip[] starts = new Chip[6];
        int count = 0;
        if (color == GameBoard.BLACK) {
            for (int i = 1; i < 7; i++) {
                if (board[i][0].getChip() != null) {
                    starts[count] = board[i][0].getChip();
                    count++;
                }
            }
        } else {
            for (int i = 1; i < 7; i++) {
                if (board[0][i].getChip() != null) {
                    starts[count] = board[0][i].getChip();
                    count++;
                }
            }
        }

        for (Chip c : starts) {
            if (c != null) {
                Chip[] potentialNetwork = new Chip[10];
                potentialNetwork[0] = c;
                if (leadsToNetwork(potentialNetwork, 1)) {
                    return true;
                }
            }
        }
        return false;
    }


    //A helper function to hasNetwork that recursively solves whether
    //or not a path of Chips[] may lead to a network. Checks for
    //chips that have already been added to chipsSoFar, whether
    //the next chip is in the same direction as the two before,
    //and only adds on chips that are connected.
    private boolean leadsToNetwork(Chip[] chipsSoFar, int len) {
        Chip lastChip = chipsSoFar[len-1];
        Chip[] connected = connectedChips(lastChip);
        for (Chip c : connected) {
            if (c != null) {
                //check if unvisited
                boolean visited = false;
                for (Chip visitedChip : chipsSoFar) {
                    if (visitedChip != null) {
                        if (c.equals(visitedChip)) {
                            //if visited, return false;
                            visited = true;
                            break;
                        }
                    }
                }
                if (visited) {
                    continue;
                }

                boolean sameDirection = false;
                //check for different slope, if there are at least 2 chips already in chipsSoFar
                if (len > 1) {
                    Chip lastLastChip = chipsSoFar[len - 2];
                    if (slope(c, lastChip) == slope(lastChip, lastLastChip)) {
                        // slope is the same, so we don't want this neighbor
                        sameDirection = true;
                    }
                }
                if (sameDirection) {
                    continue;
                }


                //connected chip (c) is unvisited and has diff slope! great!
                int newLen = len + 1;
                if (c.getColor() == GameBoard.BLACK) {
                    //Check if in other Black goal
                    if (c.getLocation().getY() == 0) {
                        continue;
                    }
                    if (c.getLocation().getY() == 7) {
                        if (newLen >= 6) {
                            return true;
                        } else {
                            //cannot be a potential network if not 6 chips or more and already at goal state.
                            continue;
                        }
                    }
                    chipsSoFar[newLen - 1] = c;
                    return leadsToNetwork(chipsSoFar, newLen);
                } else {
                    //if color is white, check other White goal.
                    if (c.getLocation().getX() == 0) {
                        continue;
                    }
                    if (c.getLocation().getX() == 7) {
                        if (newLen >= 6) {
                            return true;
                        } else {
                            continue;
                        }
                    }
                    chipsSoFar[newLen - 1] = c;
                    return leadsToNetwork(chipsSoFar, newLen);
                }
            }


        }

        return false;
    }

    
    //Returns the slope between chip a and chip b.
    private double slope(Chip a, Chip b) {
        double x1 = a.getLocation().getX();
        double y1 = a.getLocation().getY();
        double x2 = b.getLocation().getX();
        double y2 = b.getLocation().getY();
        return (x2 - x1) / (y2 - y1);
    }


    //A method to see if a move for player "color" to Square "location" is valid. Returns true if
    //the location does not already have a chip or is not in the other player's goal.
    //Cannot put a chip if there is a cluster or it will create a cluster.
    public boolean isValidMove(int color, Move move) {
        int x = move.x1;
        int y = move.y1;
        //black player
        if (color == GameBoard.BLACK) {
            //check if in white goal
            if (x == 0 || x == 7) {
                return false;
            }
            

        } else {
            //white player
            //check if in black goal
            if (y == 0 || y == 7) {
                return false;
            }
            
        }

        if (board[x][y].getChip() != null) {
            return false;
        }

        //Checks for clusters, not very fast..
        GameBoard copy = this.makeCopy();
        if (move == null) {
            return false;
        }
        copy.makeMove(color, move);
        if (copy.hasCluster(color)) {

            return false;
        }

        return true;

    }

    //returns True if player "color" has a cluster at this given GameBoard state, otherwise return false.
    private boolean hasCluster(int color) {
        Chip[] chips;
        if (color == GameBoard.BLACK) {
            chips = blackChips;
        } else {
            chips = whiteChips;
        }

        for (Chip chip : chips) {
            if (chip != null) {
                int neighbors = 0;
                int x1 = chip.getLocation().getX();
                int y1 = chip.getLocation().getY();
                for (Chip chipp : chips) {
                    if (chipp != null) {
                        int x2 = chipp.getLocation().getX();
                        int y2 = chipp.getLocation().getY();
                        if (!chip.equals(chipp)) {
                            if (Math.abs(x1-x2) < 2 && Math.abs(y1-y2) < 2) {
                                neighbors++;
                            }
                        }
                    }
                }
                if (neighbors > 1) {
                    return true;
                }
            }
        }
        return false;
    }

    //Goes through entire GameBoard and returns all locations that are valid moves for player of "color".
    //Returns a Square[] of length 48 with the first n elements filled with validMoves and the rest with null.
    public Move[] validMoves(int color) {
        Move[] sq;
        int count = 0;
        if (numChips(color) < 10) {
            sq = new Move[48];
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    Move move = new Move(i, j);
                    if (isValidMove(color, move)){
                        sq[count] = move;
                        count++;
                    }
                }
            }
        } else {
            //implement valid moves for moving an existing piece to another location
            sq = new Move[300];
            Chip[] chips;
            if (color == 0) {
                chips = blackChips;
            } else {
                chips = whiteChips;
            }
            for (Chip chip : chips) {
                if (chip != null) {
                    int x = chip.getLocation().getX();
                    int y = chip.getLocation().getY();
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            Move move = new Move(i, j, x, y);
                            if (isValidMove(color, move)) {
                                sq[count] = move;
                                count++;
                            }
                        }
                    }
                }
            }

        }


        return sq;

    }

    //Makes a copy of "this" GameBoard and returns the copy.
    public GameBoard makeCopy() {
        GameBoard copy = new GameBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Chip c = this.board[i][j].getChip();
                if (c != null) {
                    int color = c.getColor();
                    Chip temp = new Chip(color);
                    copy.board[i][j].add(temp);
                    if (color == GameBoard.BLACK) {
                        copy.blackChips[copy.numBlack] = temp;
                        copy.numBlack++;
                    } else {
                        copy.whiteChips[copy.numWhite] = temp;
                        copy.numWhite++;
                    }
                }
            }
        }

        return copy;
    }



    //Goes through the entire board to find chips of the same color as "chip" and then checks
    //to see if "chip" and the other chips of the same color are connected. connectedChips()
    //returns a Chip[] that only contains the chips that "chip" is connected to.
    public Chip[] connectedChips(Chip chip) {
        int x = chip.getLocation().getX();
        int y = chip.getLocation().getY();
        Chip[] connected = new Chip[9];
        int count = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Chip check = board[i][j].getChip();
                if (check != null && !(x == i && y == j)) {
                    //System.out.println("found non null chip at ("+i+","+j+")");
                    if (check.getColor() == chip.getColor() && isConnected(chip, check)) {
                        connected[count] = check;
                        count++;
                    }
                }
            }
        }


        return connected;

    }


    //Checks if Chip a and Chip b are connected horizontally, vertically, or diagonally. Returns
    //true if they are connected in any of those ways and are not blocked by a chip of an opposing
    //color. Returns false otherwise.
    private boolean isConnected(Chip a, Chip b) {
        //remember that Chip has a getLocation() and getColor(), and Square has getX(), getY().
        int a_X = a.getLocation().getX();
        int a_Y = a.getLocation().getY();
        int b_X = b.getLocation().getX();
        int b_Y = b.getLocation().getY();
        int c, d;

        //Checks for case when a and b have same X coordinate.
        if (a_X == b_X) {
            if (a_Y < b_Y) {
                c = a_Y;
                d = b_Y;
            } else {
                c = b_Y;
                d = a_Y;
            }
            for (int i = c + 1; i < d; i++) {
                if (board[a_X][i].getChip() != null) {
                    return false;
                }
            }
            return true;
        } else if (a_Y == b_Y) {
            if (a_X < b_X) {
                c = a_X;
                d = b_X;
            } else {
                c = b_X;
                d = a_X;
            }
            for (int i = c + 1; i < d; i++) {
                if (board[i][a_Y].getChip() != null) {
                    return false;
                }
            }
            return true;
        } else {
            return isDiagonal(a, b);
        }

    }

    //Checks if Chip a and Chip b are connected diagonally. This is a helper method for isConnected
    //and thus should not be called from anywhere besides isConnected. Returns true if Chip a and b
    //are connected diagonally, false otherwise.
    private boolean isDiagonal(Chip a, Chip b) {
        if (Math.abs(slope(a, b)) != 1.0 ) {
            return false;
        }
        int a_X = a.getLocation().getX();
        int a_Y = a.getLocation().getY();
        int b_X = b.getLocation().getX();
        int b_Y = b.getLocation().getY();
        if (a_X < b_X) {
            if (a_Y < b_Y) {
                for (int i = 1; i < b_X - a_X; i++) {

                    if (board[a_X+i][a_Y+i].getChip() != null) {
                        return false;
                    }
                }
                if ((double)((b_X - a_X)/(b_Y - a_Y)) != 1.0) {
                    return false;
                }
                return true;
            } else {
                for (int i = 1; i < b_X - a_X; i++) {
                    if (board[a_X+i][a_Y-i].getChip() != null) {
                        return false;
                    }
                }
                if ((double)((b_X - a_X)/(a_Y - b_Y)) != 1.0) {
                    return false;
                }
                return true;
            }

        } else {
            if (b_Y < a_Y) {
                for (int i = 1; i < b_X - a_X; i++) {
                    if (board[a_X-i][a_Y+i].getChip() != null) {
                        return false;
                    }
                }
                if ((double)((a_X - b_X)/(a_Y - b_Y)) != 1.0) {
                    return false;
                }
                return true;
            } else {
                for (int i = 1; i < b_X - a_X; i++) {
                    if (board[a_X-i][a_Y-i].getChip() != null) {
                        return false;
                    }
                }
                if ((double)((a_X - b_X)/(b_Y - a_Y)) != 1.0) {
                    return false;
                }

                return true;
            }
        }
    }

    //A toString method for GameBoard, prints out the entire board based on what the
    //contents of the Sqares in the board are.
    public String toString() {
        String output = "";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                output+=board[j][i]+" ";
            }
            output+="\n\n";
        }
        return output;
    }


    //Returns an int that describes the longest length of current connections
    //for "color" that could possibly lead to a Network.
    public int longestLength(int color) {
        Chip[] starts = new Chip[6];
        int count = 0;
        //Setting up starts
        if (color == GameBoard.BLACK) {
            for (int i = 1; i < 7; i++) {
                if (board[i][0].getChip() != null) {
                    starts[count] = board[i][0].getChip();
                    count++;
                }
            }
        } else {
            for (int i = 1; i < 7; i++) {
                if (board[0][i].getChip() != null) {
                    starts[count] = board[0][i].getChip();
                    count++;
                }
            }
        }

        int longestLen = 0;
        for (Chip c : starts) {
            if (c != null) {
                Chip[] potentialNetwork = new Chip[10];
                potentialNetwork[0] = c;
                int networkLength = networkLength(potentialNetwork, 1);
                if (networkLength > longestLen) {
                  longestLen = networkLength;
                }
            }
        }
        return longestLen;
    }

    //Helper method for longestLength, returns the longest potential
    //network length recursively.
    private int networkLength(Chip[] chipsSoFar, int len) {
        int bestLength = 0;
        Chip lastChip = chipsSoFar[len-1];
        Chip[] connected = connectedChips(lastChip);
        for (Chip c : connected) {
            if (c != null) {
                //check if unvisited
                boolean visited = false;
                for (Chip visitedChip : chipsSoFar) {
                    if (visitedChip != null) {
                        if (c.equals(visitedChip)) {
                            //if visited, return false;
                            visited = true;
                            break;
                        }
                    }
                }
                if (visited) {
                    continue;
                }

                boolean sameDirection = false;
                //check for different slope, if there are at least 2 chips already in chipsSoFar
                if (len > 1) {
                    Chip lastLastChip = chipsSoFar[len - 2];
                    if (slope(c, lastChip) == slope(lastChip, lastLastChip)) {
                        // slope is the same, so we don't want this neighbor
                        sameDirection = true;
                    }
                }
                if (sameDirection) {
                    continue;
                }

                //connected chip (c) is unvisited and has diff slope! great!
                int newLen = len + 1;
                if (c.getColor() == GameBoard.BLACK) {
                    //Check if in other Black goal
                    if (c.getLocation().getY() == 0) {
                        continue;
                    }
                    if (c.getLocation().getY() == 7) {
                        if (newLen >= 6) {
                            return 20;
                        } else {
                            //cannot be a potential network if not 6 chips or more and already at goal state.
                            continue;
                        }
                    }
                    chipsSoFar[newLen - 1] = c;
                    return networkLength(chipsSoFar, newLen);
                } else {
                    //if color is white, check other White goal.
                    if (c.getLocation().getX() == 0) {
                        continue;
                    }
                    if (c.getLocation().getX() == 7) {
                        if (newLen >= 6) {
                            return 20;
                        } else {
                            continue;
                        }
                    }
                    chipsSoFar[newLen - 1] = c;
                    return networkLength(chipsSoFar, newLen);
                }
            }


        }

        return len;
    }


    //returns an integer with the total number of connections in "color".
    public int totalConnections(int color) {
        Chip[] chips = null;
        Chip[] seenChips = new Chip[10];
        int index = 0;
        int count = 0;
        if (color == GameBoard.BLACK) {
            chips = blackChips;
        } else {
            chips = whiteChips;
        }
        for (Chip c : chips) {
            //for each chip of a color
            if (c != null) {
                Chip[] connected = connectedChips(c);
                for (Chip connect : connected) {
                    if (connect != null) {
                        //see if b is in seenChips
                        boolean skip = false;
                        for (Chip seen : seenChips) {
                            if (seen != null) {
                                if (seen.equals(connect)) {
                                    skip = true;
                                    break;
                                }
                            }
                        }
                        if (skip) {
                            continue;
                        }
                        //if connect is not equal to null, and we have not seen it
                        count++;
                    }

                }
                //add c to list of seenChips to prevent double counting 
                seenChips[index] = c;
                index++;
            }

        }
        return count;
    }


    //If currentTotalConnections is more than the newly calculated totalConnections, 
    //the int returned is the number of connections that were blocked. The higher
    //the number returned, the more connections are blocked. If connections were
    //made, then it returns a negative number.
    public int blockedConnections(int myColor, int otherColor) {
        int currentTotalConnections = totalConnections(otherColor);
        GameBoard copy = this.makeCopy();
        if (myColor == GameBoard.BLACK) {
            for (Chip c : blackChips) {
                if (c != null) {
                    Square s = c.getLocation();
                    copy.getSquare(s.getX(), s.getY()).remove();
                }
            }
        } else {
            for (Chip c : whiteChips) {
                if (c != null) {
                    Square s = c.getLocation();
                    copy.getSquare(s.getX(), s.getY()).remove();
                }
            }
        }
        return copy.totalConnections(otherColor) - currentTotalConnections;
    }

}