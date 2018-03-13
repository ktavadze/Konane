package edu.ramapo.ktavadze.konane;

/**
 * Player class.
 */

public class Player {
    public char color;
    public int score;

    /**
     Player class constructor.
     @param a_color - Char color value of the new player.
     */
    public Player(char a_color) {
        color = a_color;
        score = 0;
    }

    /**
     Determines whether the calling player has any available moves.
     @return Boolean value depending on the player's available moves.
     */
    public boolean canMove() {
        Square[][] table = Game.board.table;
        for (int i = 0; i < Game.board.size; i++) {
            for (int j = 0; j < Game.board.size; j++) {
                // Check if square matches player's color and is not empty.
                if (table[i][j].color == color && !table[i][j].isEmpty) {
                    // Check if square can move up.
                    if (canMoveUp(i, j)) {
                        return true;
                    }
                    // Check if square can move right.
                    if (canMoveRight(i, j)) {
                        return true;
                    }
                    // Check if square can move down.
                    if (canMoveDown(i, j)) {
                        return true;
                    }
                    // Check if square can move left.
                    if (canMoveLeft(i, j)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     Determines whether the calling player can move a particular square.
     @param a_row - Integer row value of the square.
     @param a_col - Integer column value of the square.
     @return Boolean value depending on whether the player can move the specified square.
     */
    public boolean canMove(int a_row, int a_col) {
        Square[][] table = Game.board.table;
        // Check if square matches player's color and is not empty.
        if (table[a_row][a_col].color == color && !table[a_row][a_col].isEmpty) {
            // Check if square can move up.
            if (canMoveUp(a_row, a_col)) {
                return true;
            }
            // Check if square can move right.
            if (canMoveRight(a_row, a_col)) {
                return true;
            }
            // Check if square can move down.
            if (canMoveDown(a_row, a_col)) {
                return true;
            }
            // Check if square can move left.
            if (canMoveLeft(a_row, a_col)) {
                return true;
            }
        }
        return false;
    }

    /**
     Determines whether the calling player can make a particular move.
     @param a_r1 - Integer row value of the origin.
     @param a_c1 - Integer column value of the origin.
     @param a_r2 - Integer row value of the destination.
     @param a_c2 - Integer column value of the destination.
     @return Boolean value depending on whether the player can make the specified move.
     */
    public boolean canMove(int a_r1, int a_c1, int a_r2, int a_c2) {
        Square[][] table = Game.board.table;
        // Check if origin matches player's color and is not empty.
        if (table[a_r1][a_c1].color == color && !table[a_r1][a_c1].isEmpty &&
                table[a_r2][a_c2].isEmpty) {
            // Check vertical moves.
            if (a_c1 == a_c2) {
                // Check move up.
                if (a_r1 - 2 == a_r2 && canMoveUp(a_r1, a_c1)) {
                    return true;
                }
                // Check move down.
                if (a_r1 + 2 == a_r2 && canMoveDown(a_r1, a_c1)) {
                    return true;
                }
            }
            // Check horizontal moves.
            if (a_r1 == a_r2) {
                // Check move right.
                if (a_c1 + 2 == a_c2 && canMoveRight(a_r1, a_c1)) {
                    return true;
                }
                // Check move left.
                if (a_c1 - 2 == a_c2 && canMoveLeft(a_r1, a_c1)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     Determines whether the calling player can move up a particular square.
     @param a_row - Integer row value of the square.
     @param a_col - Integer column value of the square.
     @return Boolean value depending on whether the player can move up the specified square.
     */
    public boolean canMoveUp(int a_row, int a_col) {
        Square[][] table = Game.board.table;
        // Check if square can move up.
        if (a_row - 2 >= 0) {
            if (table[a_row - 2][a_col].isEmpty && !table[a_row - 1][a_col].isEmpty) {
                return true;
            }
        }
        return false;
    }

    /**
     Determines whether the calling player can move right a particular square.
     @param a_row - Integer row value of the square.
     @param a_col - Integer column value of the square.
     @return Boolean value depending on whether the player can move right the specified square.
     */
    public boolean canMoveRight(int a_row, int a_col) {
        Square[][] table = Game.board.table;
        // Check if square can move right.
        if (a_col + 2 < Game.board.size) {
            if (table[a_row][a_col + 2].isEmpty && !table[a_row][a_col + 1].isEmpty) {
                return true;
            }
        }
        return false;
    }

    /**
     Determines whether the calling player can move down a particular square.
     @param a_row - Integer row value of the square.
     @param a_col - Integer column value of the square.
     @return Boolean value depending on whether the player can move down the specified square.
     */
    public boolean canMoveDown(int a_row, int a_col) {
        Square[][] table = Game.board.table;
        // Check if square can move down.
        if (a_row + 2 < Game.board.size) {
            if (table[a_row + 2][a_col].isEmpty && !table[a_row + 1][a_col].isEmpty) {
                return true;
            }
        }
        return false;
    }

    /**
     Determines whether the calling player can move left a particular square.
     @param a_row - Integer row value of the square.
     @param a_col - Integer column value of the square.
     @return Boolean value depending on whether the player can move left the specified square.
     */
    public boolean canMoveLeft(int a_row, int a_col) {
        Square[][] table = Game.board.table;
        // Check if square can move left.
        if (a_col - 2 >= 0) {
            if (table[a_row][a_col - 2].isEmpty && !table[a_row][a_col - 1].isEmpty) {
                return true;
            }
        }
        return false;
    }
}
