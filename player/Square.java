/* Square.java */

package player;

/**
 * A class representing a single location on the GameBoard.
 * Keeps track of whether or not there is a chip on "this" square and
 * the x,y coordinates of "this" square.
 **/

public class Square {

    private Chip chip = null;
    private int x;
    private int y;

    // Creates a Square at the location x, y on the GameBoard
    public Square(int x, int y) {
        this.chip = null;
        this.x = x;
        this.y = y;
    }

    // Returns the x coordinate of "this" Square on the GameBoard
    public int getX() {
        return this.x;
    }

    // Returns the y coordinate of "this" Square on the GameBoard
    public int getY() {
        return this.y;
    }

    // Returns the chip located on "this" Square. Returns null if no chip exists.
    public Chip getChip() {
        return chip;
    }

    // Places a chip at "this" Square's location
    public void add(Chip chip) {
        this.chip = chip;
        chip.setLocation(this);
    }

    // Removes a chip from "this" Square's location
    public void remove() {
        chip = null;
    }

    // String representation of this square
    public String toString() {
        if (chip == null) {
            return "[ ]";
        } else if (chip.getColor() == GameBoard.BLACK) {
            return "[B]";
        } else if (chip.getColor() == GameBoard.WHITE) {
            return "[W]";
        } else {
            System.out.println("trying to print an invalid square with chip "+chip);
            return "";
        }
    }

}