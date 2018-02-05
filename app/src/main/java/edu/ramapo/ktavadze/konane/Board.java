package edu.ramapo.ktavadze.konane;

import java.util.Random;

/**
 * Board class.
 */

public class Board {
    public static final int SIZE = 6;
    public Square[][] table = new Square[SIZE][SIZE];

    /**
     Board class constructor.
     */
    public Board() {
        populate();

        emptyRandomTwo();
    }

    /**
     Swaps the squares at the specified locations.
     @param a_r1 - Integer row value of the first square.
     @param a_c1 - Integer column value of the first square.
     @param a_r2 - Integer row value of the second square.
     @param a_c2 - Integer column value of the second square.
     */
    public void move(int a_r1, int a_c1, int a_r2, int a_c2) {
        Square square1 = table[a_r1][a_c1];
        Square square2 = table[a_r2][a_c2];

        table[a_r1][a_c1] = square2;
        table[a_r2][a_c2] = square1;
    }

    /**
     Populates the entire table with the appropriate pattern of squares.
     */
    private void populate() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                // Populate even rows.
                if (i % 2 == 0) {
                    if (j % 2 == 0) {
                        table[i][j] = new Square('B');
                    } else {
                        table[i][j] = new Square('W');
                    }
                }
                // Populate odd rows.
                else {
                    if (j % 2 == 0) {
                        table[i][j] = new Square('W');
                    } else {
                        table[i][j] = new Square('B');
                    }
                }
            }
        }
    }

    /**
     Empties two random squares of opposite types.
     */
    private void emptyRandomTwo() {
        Random rand = new Random();

        int randRow = rand.nextInt(SIZE);
        int randCol = rand.nextInt(SIZE);

        // Empty the first square
        char emptied = table[randRow][randCol].color;
        table[randRow][randCol].empty();

        while (true) {
            randRow = rand.nextInt(SIZE);
            randCol = rand.nextInt(SIZE);

            // Empty the second square.
            if (table[randRow][randCol].color != emptied && !table[randRow][randCol].isEmpty) {
                table[randRow][randCol].empty();
                break;
            }
        }
    }
}
