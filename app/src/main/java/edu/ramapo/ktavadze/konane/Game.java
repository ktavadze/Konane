package edu.ramapo.ktavadze.konane;

/**
 * Game class.
 */

public class Game {
    public static Board board = new Board();
    public Player black = new Player('B');
    public Player white = new Player('W');
    public char turn = 'B';

    /**
     Game class constructor.
     */
    public Game() {}

    /**
     Processes each move to determine the appropriate course of action.
     @param a_r1 - Integer row value of the origin.
     @param a_c1 - Integer column value of the origin.
     @param a_r2 - Integer row value of the destination.
     @param a_c2 - Integer column value of the destination.
     @return String value depending on the type of move being attempted.
     */
    public String processMove(int a_r1, int a_c1, int a_r2, int a_c2) {
        // Process black player's turn.
        if (turn == 'B') {
            // Check if black player can make any move.
            if (black.canMove()) {
                // Check if black player can make specified move.
                if (black.canMove(a_r1, a_c1, a_r2, a_c2)) {
                    // Black player makes specified move.
                    black.makeMove(a_r1, a_c1, a_r2, a_c2);

                    // Check if black player should make another move.
                    if (black.canMove(a_r2, a_c2)) {
                        return "Combo";
                    }

                    turn = 'W';
                    return "Basic";
                }
                return "Illegal";
            }
            // Skip black player's turn
            turn = 'W';
        }
        // Process white player's turn.
        else if (turn == 'W') {
            // Check if white player can make any move.
            if (white.canMove()) {
                // Check if white player can make specified move.
                if (white.canMove(a_r1, a_c1, a_r2, a_c2)) {
                    // White player makes specified move.
                    white.makeMove(a_r1, a_c1, a_r2, a_c2);

                    // Check if white player should make another move.
                    if (white.canMove(a_r2, a_c2)) {
                        return "Combo";
                    }

                    turn = 'B';
                    return "Basic";
                }
                return "Illegal";
            }
            // Skip white player's turn.
            turn = 'B';
        }
        return "Error";
    }

    /**
     Determines whether the game is over.
     @return Boolean value depending on whether either player can make a move.
     */
    public boolean isOver() {
        if (black.canMove() || white.canMove()) {
            return false;
        }
        return true;
    }

    /**
     Determines the outcome of the game.
     @return String value depending on the final scores.
     */
    public String getResults() {
        if (black.score > white.score) {
            return "Black wins!";
        }
        if (black.score < white.score) {
            return "White wins!";
        }
        return "It's a tie!";
    }
}
