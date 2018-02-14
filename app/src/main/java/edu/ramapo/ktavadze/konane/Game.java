package edu.ramapo.ktavadze.konane;

import java.util.Scanner;

/**
 * Game class.
 */

public class Game {
    public static Board board = new Board();
    public Player black = new Player('B');
    public Player white = new Player('W');
    public char turn = 'B';
    public boolean isMoving = false;
    public boolean isCombo = false;
    public int row;
    public int col;

    /**
     Game class constructor.
     */
    public Game() {}

    /**
     Processes each move to determine the appropriate course of action.
     @param a_row - Integer row value.
     @param a_col - Integer column value.
     @return String value depending on the type of move being attempted.
     */
    public String processMove(int a_row, int a_col) {
        // Process first move.
        if (!isMoving && !isCombo) {
            // Move starts.
            if ((turn == 'B' && black.canMove(a_row, a_col)) ||
                    (turn == 'W' && white.canMove(a_row, a_col))) {
                row = a_row;
                col = a_col;

                isMoving = true;

                return "Moving " + row + col + "...";
            }
            // Black passes.
            else if (turn == 'B' && !black.canMove()) {
                turn = 'W';

                return "Black passes!";
            }
            // White passes.
            else if (turn == 'W' && !white.canMove()) {
                turn = 'B';

                return "White passes!";
            }
            // Illegal move.
            else {
                return "Illegal move!";
            }
        }
        // Process subsequent moves.
        else {
            // Black moves.
            if (turn == 'B' && black.canMove(row, col, a_row, a_col)) {
                black.makeMove(row, col, a_row, a_col);

                if (black.canMove(a_row, a_col)) {
                    isCombo = true;

                    row = a_row;
                    col = a_col;
                }
                else {
                    isMoving = false;
                    isCombo = false;

                    turn = 'W';
                }

                return "...to " + a_row + a_col;
            }
            // White moves.
            else if (turn == 'W' && white.canMove(row, col, a_row, a_col)) {
                white.makeMove(row, col, a_row, a_col);

                if (white.canMove(a_row, a_col)) {
                    isCombo = true;

                    row = a_row;
                    col = a_col;
                }
                else {
                    isMoving = false;
                    isCombo = false;

                    turn = 'B';
                }

                return "...to " + a_row + a_col;
            }
            // Illegal move.
            else {
                isMoving = false;

                return "Illegal move!";
            }
        }
    }

    /**
     Processes each pass command.
     */
    public String processPass() {
        isMoving = false;
        isCombo = false;

        // Black passes.
        if (turn == 'B') {
            turn = 'W';

            return "Black passes!";
        }
        // White passes.
        else {
            turn = 'B';

            return "White passes!";
        }
    }

    /**
     Gets the state of the game.
     @return String value depending on the current state.
     */
    public String getState() {
        // Get scores.
        String state = "Black: " + black.score + "\nWhite: " + white.score + "\nBoard:\n";

        // Get board.
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                state += board.table[i][j].color + " ";
            }
            state += "\n";
        }

        // Get turn.
        if (turn == 'B') {
            state += "Next Player: Black";
        }
        else {
            state += "Next Player: White";
        }

        return state;
    }

    /**
     Sets the state of the game.
     @param a_state - String state value to be loaded.
     */
    public void setState(String a_state) {
        Scanner scanner = new Scanner(a_state);
        int lineNum = 1;
        int row = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            switch (lineNum) {
                // Set black score.
                case 1:
                    black.score = Character.getNumericValue(line.charAt(7));
                    break;
                // Set white score.
                case 2:
                    white.score = Character.getNumericValue(line.charAt(7));
                    break;
                case 3:
                    break;
                // Set board.
                case 4:case 5:case 6:case 7:case 8:case 9:
                    for (int i = 0, j = 0; j < board.SIZE; i += 2, j++) {
                        if (line.charAt(i) == 'O') {
                            board.table[row][j].empty();
                        }
                        else {
                            board.table[row][j].color = line.charAt(i);
                            board.table[row][j].isEmpty = false;
                        }
                    }
                    row++;
                    break;
                // Set turn.
                case 10:
                    turn = line.charAt(13) == 'B' ? 'B' : 'W';
                    break;
            }
            lineNum++;
        }
        scanner.close();

        // Reset move.
        isMoving = false;
        isCombo = false;
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
