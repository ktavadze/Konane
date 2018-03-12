package edu.ramapo.ktavadze.konane;

import java.util.Random;

/**
 * Board class.
 */

public class Board {
    public static final int SIZE = 6;
    public Square[][] table;

    /**
     Board class constructor.
     */
    public Board() {
        table = new Square[SIZE][SIZE];

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

        // Update destination.
        square2.color = square1.color;
        square2.isEmpty = false;

        // Update midpoint.
        if (a_r1 == a_r2) {
            if (a_c1 - 2 == a_c2) {
                table[a_r1][a_c1 - 1].empty();
            }
            else if (a_c1 + 2 == a_c2) {
                table[a_r1][a_c1 + 1].empty();
            }
        }
        else if (a_c1 == a_c2) {
            if (a_r1 + 2 == a_r2) {
                table[a_r1 + 1][a_c1].empty();
            }
            else if (a_r1 - 2 == a_r2) {
                table[a_r1 - 1][a_c1].empty();
            }
        }

        // Update origin.
        square1.empty();
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
                        table[i][j] = new Square('B', i, j);
                    } else {
                        table[i][j] = new Square('W', i, j);
                    }
                }
                // Populate odd rows.
                else {
                    if (j % 2 == 0) {
                        table[i][j] = new Square('W', i, j);
                    } else {
                        table[i][j] = new Square('B', i, j);
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
