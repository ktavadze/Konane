package edu.ramapo.ktavadze.konane;

/**
 * Square class
 */

public class Square {
    public boolean isEmpty = false;
    public char color;
    public int row;
    public int col;

    /**
     Square class constructor.
     @param a_color - Char color value of the new square.
     @param a_row - Integer row value of the new square.
     @param a_col - Integer column value of the new square.
     */
    public Square(char a_color, int a_row, int a_col) {
        color = a_color;
        row = a_row;
        col = a_col;
    }

    /**
     Compares the calling square with another.
     @param a_square - Square to be compared to.
     @return Boolean value depending on whether the squares match.
     */
    @Override
    public boolean equals(Object a_square) {
        Square square = (Square) a_square;
        if (row == square.row && col == square.col) {
            return true;
        }
        return false;
    }
}
