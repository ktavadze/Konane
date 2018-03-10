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
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                // Check if current square matches the player's color and is not empty.
                if (table[i][j].color == color && !table[i][j].isEmpty) {
                    // Check if current square can move down.
                    if (i + 2 < Board.SIZE) {
                        if (table[i + 2][j].isEmpty && !table[i + 1][j].isEmpty) {
                            return true;
                        }
                    }
                    // Check if current square can move up.
                    if (i - 2 >= 0) {
                        if (table[i - 2][j].isEmpty && !table[i - 1][j].isEmpty) {
                            return true;
                        }
                    }
                    // Check if current square can move right.
                    if (j + 2 < Board.SIZE) {
                        if (table[i][j + 2].isEmpty && !table[i][j + 1].isEmpty) {
                            return true;
                        }
                    }
                    // Check if current square can move left.
                    if (j - 2 >= 0) {
                        if (table[i][j - 2].isEmpty && !table[i][j - 1].isEmpty) {
                            return true;
                        }
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
        // Check if square matches the player's color and is not empty.
        if (table[a_row][a_col].color == color && !table[a_row][a_col].isEmpty) {
            // Check if square can move down.
            if (a_row + 2 < Board.SIZE) {
                if (table[a_row + 2][a_col].isEmpty && !table[a_row + 1][a_col].isEmpty) {
                    return true;
                }
            }
            // Check if square can move up.
            if (a_row - 2 >= 0) {
                if (table[a_row - 2][a_col].isEmpty && !table[a_row - 1][a_col].isEmpty) {
                    return true;
                }
            }
            // Check if square can move right.
            if (a_col + 2 < Board.SIZE) {
                if (table[a_row][a_col + 2].isEmpty && !table[a_row][a_col + 1].isEmpty) {
                    return true;
                }
            }
            // Check if square can move left.
            if (a_col - 2 >= 0) {
                if (table[a_row][a_col - 2].isEmpty && !table[a_row][a_col - 1].isEmpty) {
                    return true;
                }
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
        if (table[a_r1][a_c1].color != color || !table[a_r2][a_c2].isEmpty) {
            return false;
        }
        // Check vertical moves.
        else if (a_r1 == a_r2) {
            // Check move down.
            if (a_c1 + 2 == a_c2 && !table[a_r1][a_c1 + 1].isEmpty) {
                return true;
            }
            // Check move up.
            else if (a_c1 - 2 == a_c2 && !table[a_r1][a_c1 - 1].isEmpty) {
                return true;
            }
        }
        // Check horizontal moves.
        else if (a_c1 == a_c2) {
            // Check move right.
            if (a_r1 + 2 == a_r2 && !table[a_r1 + 1][a_c1].isEmpty) {
                return true;
            }
            // Check move left.
            else if (a_r1 - 2 == a_r2 && !table[a_r1 - 1][a_c1].isEmpty) {
                return true;
            }
        }
        return false;
    }

    /**
     Makes a particular move.
     @param a_r1 - Integer row value of the origin.
     @param a_c1 - Integer column value of the origin.
     @param a_r2 - Integer row value of the destination.
     @param a_c2 - Integer column value of the destination.
     */
    public void makeMove(int a_r1, int a_c1, int a_r2, int a_c2) {
        Board board = Game.board;
        // Make vertical move.
        if (a_r1 == a_r2) {
            // Make move down.
            if (a_c1 + 2 == a_c2 && !board.table[a_r1][a_c1 + 1].isEmpty) {
                board.move(a_r1, a_c1, a_r2, a_c2);
                board.table[a_r1][a_c1 + 1].empty();
                score++;
            }
            // Make move up.
            else if (a_c1 - 2 == a_c2 && !board.table[a_r1][a_c1 - 1].isEmpty) {
                board.move(a_r1, a_c1, a_r2, a_c2);
                board.table[a_r1][a_c1 - 1].empty();
                score++;
            }
        }
        // Make horizontal move.
        else if (a_c1 == a_c2) {
            // Make move right.
            if (a_r1 + 2 == a_r2 && !board.table[a_r1 + 1][a_c1].isEmpty) {
                board.move(a_r1, a_c1, a_r2, a_c2);
                board.table[a_r1 + 1][a_c1].empty();
                score++;
            }
            // Make move left.
            else if (a_r1 - 2 == a_r2 && !board.table[a_r1 - 1][a_c1].isEmpty) {
                board.move(a_r1, a_c1, a_r2, a_c2);
                board.table[a_r1 - 1][a_c1].empty();
                score++;
            }
        }
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
        if (a_col + 2 < Board.SIZE) {
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
        if (a_row + 2 < Board.SIZE) {
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
