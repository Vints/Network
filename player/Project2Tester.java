/** Project2Tester.java

package player;

/**
 *  The Project2Tester class is a program that tests the functionality of the
 *  GameBoard class for Project 2.
 *	@author Youri Park.
 *

public class Project2Tester {



    private static void testIsDiagonal(GameBoard game) {
        Chip a = new Chip(0);
        Chip b = new Chip(0);
        Chip c = new Chip(0);
        Chip d = new Chip(0);
        Chip g = new Chip(0);
        game.addChip(a, 1, 1);
        game.addChip(b, 4, 4);
        game.addChip(c, 3, 2);
        game.addChip(d, 4, 3);
        game.addChip(g, 2, 6);

        if (game.isDiagonal(a, b) == true) {
            System.out.println("isDiagonal Test 1 Passed");
        } else {
            System.out.println("isDiagonal Test 1 Did NOT Pass");
        }

        if (game.isDiagonal(a, c) != true) {
            System.out.println("isDiagonal Test 2 Passed");
        } else {
            System.out.println("isDiagonal Test 2 Did NOT Pass");
        }

        if (game.isDiagonal(c, d) == true) {
            System.out.println("isDiagonal Test 3 Passed");
        } else {
            System.out.println("isDiagonal Test 3 Did NOT pass");
        }

        if (game.isDiagonal(b, a) == true) {
            System.out.println("isDiagonal Test 4 Passed");
        } else {
            System.out.println("isDiagonal Test 4 Did NOT pass");
        }

        if (game.isDiagonal(b, g) == true) {
            System.out.println("isDiagonal Test 5 Passed");
        } else {
            System.out.println("isDiagonal Test 5 Did NOT pass");
        }

        if (game.isDiagonal(g, b) == true) {
            System.out.println("isDiagonal Test 6 Passed");
        } else {
            System.out.println("isDiagonal Test 6 Did NOT pass");
        }


    }


    private static void testIsConnected(GameBoard game) {
        //should include a test to see if a chip blocking another chip returns false for isConnected()
        Chip a = new Chip(0);
        Chip b = new Chip(0);
        Chip c = new Chip(0);
        Chip d = new Chip(0);
        Chip e = new Chip(0);
        game.addChip(a, 1, 1);
        game.addChip(b, 4, 4);
        game.addChip(c, 3, 2);
        game.addChip(d, 4, 3);
        game.addChip(e, 1, 2);

        //Test 1, checking two chips that are diagonal.
        if (game.isConnected(a, b) == true) {
            System.out.println("isConnected Test 1 Passed");
        } else {
            System.out.println("isConnected Test 1 Did NOT Pass");
        }

        //Test 2, making sure two chips not connected are not connected
        if (game.isConnected(a, c) != true) {
            System.out.println("isConnected Test 2 Passed");
        } else {
            System.out.println("isConnected Test 2 Did NOT Pass");
        }

        //Test 3, checking two chips that are vertical
        if (game.isConnected(b, d) == true) {
            System.out.println("isConnected Test 3 Passed");
        } else {
            System.out.println("isConnected Test 3 Did NOT Pass");
        }

        //Test 4, checking two chips that are diagonal.
        if (game.isConnected(c, d) == true) {
            System.out.println("isConnected Test 4 Passed");
        } else {
            System.out.println("isConnected Test 4 Did NOT Pass");
        }

        //Test 5, checking two chips that are horizontal.
        if (game.isConnected(c, e) == true) {
            System.out.println("isConnected Test 5 Passed");
        } else {
            System.out.println("isConnected Test 5 Did NOT Pass");
        }
        Chip f = new Chip(1);
        game.addChip(f, 3, 3);
        //Test 6, checking two diagonal chips blocked by a chip of opposite color
        if (game.isConnected(a, b) != true) {
            System.out.println("isConnected Test 6 Passed");
        } else {
            System.out.println("isConnected Test 6 Did NOT Pass");
        }


    }

    private static void testConnectedChips(GameBoard game) {


    }



    public static void main(String[] args) {

        GameBoard game = new GameBoard();
        if (game.getSquare(3, 3) == null) {
            System.out.println("When a GameBoard is created, spaces in board[][] are null.");
        }

        testIsDiagonal(game);
        game = new GameBoard();
        testIsConnected(game);
        game = new GameBoard();

    }


}
*/