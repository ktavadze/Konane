package edu.ramapo.ktavadze.konane;

/**
 * Square class
 */

public class Square {
    public char color;
    public boolean isEmpty = false;

    /**
     Square class constructor.
     @param a_color - Char color value of the new square.
     */
    public Square(char a_color){
        color = a_color;
    }

    /**
     Empties the calling square.
     */
    public void empty() {
        color = ' ';
        isEmpty = true;
    }
}
