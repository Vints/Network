/* Chip.java */

package player;

/**
 *  An implementation of a piece on the board.  Keeps track of the team it is on
 *  and which Square it is located on the GameBoard.  
 */

public class Chip {

    private int color;
    private Square location = null;


    // Creates a Chip with the given color and location.  Color is either 0 (black)
    // or 1 (white).  (White has the first move.) Location is a Square on the GameBoard.
    public Chip(int color) {
        this.color = color;
    }


    //Returns the color of "this". Either 0 (black) or 1 (white).
    public int getColor() {
        return color;
    }


    //Returns the Square that this chip is located in.
    public Square getLocation() {
        return location;
    }

    //Sets the Square that the chip is being moved to.
    //Does not check if the new location isValid.
    public void setLocation(Square location) {
        this.location = location;
    }

    //Returns a String with the coordinates of this in (x,y) form.
    public String toString() {
        return "(" + color + "@" +  location.getX() + "," + location.getY() + ")";
    }


    //Returns a boolean that returns true if this and Chip "a" are located on the same Square, which
    //by default means that the chips are equal because only one chip can be on a Square.
    public boolean equals(Chip a) {
        int x = this.getLocation().getX();
        int y = this.getLocation().getY();
        int x1 = a.getLocation().getX();
        int y1 = a.getLocation().getY();
        if (x == x1 && y == y1) {
            return true;
        }
        return false;
    }



}
