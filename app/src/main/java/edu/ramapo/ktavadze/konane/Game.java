package edu.ramapo.ktavadze.konane;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

/**
 * Game class.
 */

public class Game {
    public int boardSize;
    public char mode;
    public boolean guess;
    public static Board board;
    public Player black;
    public Player white;
    public char turn = 'B';
    public boolean isMoving = false;
    public boolean isCombo = false;
    public int row;
    public int col;
    public String search = "Best";
    public ArrayList<Move> moves;

    /**
     Game class constructor.
     @param a_size - Integer value of the board boardSize.
     @param a_mode - Character value of the game mode.
     @param a_guess - Boolean value of the guess.
     */
    public Game(int a_size, char a_mode, boolean a_guess) {
        boardSize = a_size;
        mode = a_mode;
        guess = a_guess;
        board = new Board(boardSize);
        if (mode == 'S') {
            if (guess == board.isBlackFirst) {
                black = new Player('B', true);
                white = new Player('W', false);
            }
            else {
                black = new Player('B', false);
                white = new Player('W', true);
            }
        }
        else {
            black = new Player('B', true);
            white = new Player('W', true);
        }
        setSearch(search);
    }

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
                board.move(row, col, a_row, a_col);
                black.score++;

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
                board.move(row, col, a_row, a_col);
                white.score++;

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
     @return String value depending on who is passing.
     */
    public String processPass() {
        isMoving = false;
        isCombo = false;

        // Black passes.
        if (turn == 'B') {
            turn = 'W';
            setSearch(search);

            return "Black passes!";
        }
        // White passes.
        else {
            turn = 'B';
            setSearch(search);

            return "White passes!";
        }
    }

    /**
     Gets the state of the game.
     @return String value depending on the current state.
     */
    public String getState() {
        // Get scores.
        String state = "Black: ";
        if (black.isHuman) {
            state += "T";
        }
        else {
            state += "F";
        }
        state += black.score + "\nWhite: ";
        if (white.isHuman) {
            state += "T";
        }
        else {
            state += "F";
        }
        state += white.score + "\n";

        // Get boardSize.
        state += "Board: " + boardSize + "\n";

        // Get board.
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
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
        int size = 0;
        int row = 0;

        while (scanner.hasNextLine()) {
            String lineString = scanner.nextLine();
            char[] lineChars = lineString.toCharArray();

            // Set black player.
            if (lineNum == 1) {
                if (lineChars[7] == 'T') {
                    black = new Player('B', true);
                }
                else {
                    black = new Player('B', false);
                }
                int blackScore = Character.getNumericValue(lineChars[8]);
                if (lineChars.length == 10) {
                    blackScore *= 10;
                    blackScore += Character.getNumericValue(lineChars[9]);
                }
                black.score = blackScore;
            }
            // Set white player.
            else if (lineNum == 2) {
                if (lineChars[7] == 'T') {
                    white = new Player('W', true);
                }
                else {
                    white = new Player('W', false);
                }
                int whiteScore = Character.getNumericValue(lineChars[8]);
                if (lineChars.length == 10) {
                    whiteScore *= 10;
                    whiteScore += Character.getNumericValue(lineChars[9]);
                }
                white.score = whiteScore;
            }
            // Set mode and board size.
            else if (lineNum == 3) {
                if (white.isHuman && black.isHuman) {
                    mode = 'M';
                }
                else {
                    mode = 'S';
                }
                size = Character.getNumericValue(lineChars[7]);
                if (boardSize != size) {
                    boardSize = size;
                    if (boardSize == 1) {
                        boardSize *= 10;
                    }
                    board = new Board(boardSize);
                }
            }
            // Set board state.
            else if (lineNum < boardSize + 4) {
                for (int i = 0, j = 0; j < boardSize; i += 2, j++) {
                    if (lineChars[i] == 'O') {
                        board.table[row][j].empty();
                    }
                    else {
                        board.table[row][j].color = lineChars[i];
                        board.table[row][j].isEmpty = false;
                    }
                }
                row++;
            }
            // Set turn.
            else if (lineNum == boardSize + 4) {
                turn = lineChars[13] == 'B' ? 'B' : 'W';
            }
            lineNum++;
        }
        scanner.close();

        // Reset move.
        isMoving = false;
        isCombo = false;
        setSearch(search);
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

    /**
     Sets the search value and updates the moves accordingly.
     @param a_search - String search value to be set.
     */
    public void setSearch(String a_search) {
        search = a_search;
        switch (a_search) {
            case "Depth":
                updateMovesDepth();
                break;
            case "Breadth":
                updateMovesBreadth();
                break;
            case "Best":
                updateMovesBest();
                break;
        }
    }

    /**
     Updates the moves using Depth First Search.
     */
    public void updateMovesDepth() {
        // Populate starting stack.
        Stack<Move> s = new Stack<>();
        Player player = turn == 'B' ? black : white;
        for (int i = boardSize - 1; i >= 0; i--) {
            for (int j = boardSize - 1; j >= 0; j--) {
                if (player.canMove(i, j)) {
                    Square square = board.table[i][j];
                    s.push(new Move(square, square, square, 0));
                }
            }
        }

        // Populate moves list.
        moves = new ArrayList<>();
        while (!s.empty()) {
            Move move = s.pop();
            Square start = move.start;
            Square prev = move.prev;
            Square end = move.end;

            Integer score = move.score;
            if (score == 9) break;

            // Check move up.
            if (player.canMoveUp(end.row, end.col)) {
                Square up = board.table[end.row - 2][end.col];
                Move moveUp = new Move(start, end, up, score + 1);
                if (!prev.equals(up) && !moves.contains(moveUp)) {
                    // Add move up to moves list.
                    moves.add(moveUp);
                    if (player.canMoveUp(up.row, up.col) ||
                            player.canMoveRight(up.row, up.col) ||
                            player.canMoveLeft(up.row, up.col)) {
                        // Add moves to stack.
                        s.push(move);
                        s.push(moveUp);
                        continue;
                    }
                }
            }
            // Check move right.
            if (player.canMoveRight(end.row, end.col)) {
                Square right = board.table[end.row][end.col + 2];
                Move moveRight = new Move(start, end, right, score + 1);
                if (!prev.equals(right) && !moves.contains(moveRight)) {
                    // Add move right to moves list.
                    moves.add(moveRight);
                    if (player.canMoveUp(right.row, right.col) ||
                            player.canMoveRight(right.row, right.col) ||
                            player.canMoveDown(right.row, right.col)) {
                        // Add moves to stack.
                        s.push(move);
                        s.push(moveRight);
                        continue;
                    }
                }
            }
            // Check move down.
            if (player.canMoveDown(end.row, end.col)) {
                Square down = board.table[end.row + 2][end.col];
                Move moveDown = new Move(start, end, down, score + 1);
                if (!prev.equals(down) && !moves.contains(moveDown)) {
                    // Add move down to moves list.
                    moves.add(moveDown);
                    if (player.canMoveRight(down.row, down.col) ||
                            player.canMoveDown(down.row, down.col) ||
                            player.canMoveLeft(down.row, down.col)) {
                        // Add moves to stack.
                        s.push(move);
                        s.push(moveDown);
                        continue;
                    }
                }
            }
            // Check move left.
            if (player.canMoveLeft(end.row, end.col)) {
                Square left = board.table[end.row][end.col - 2];
                Move moveLeft = new Move(start, end, left, score + 1);
                if (!prev.equals(left) && !moves.contains(moveLeft)) {
                    // Add move left to moves list.
                    moves.add(moveLeft);
                    if (player.canMoveUp(left.row, left.col) ||
                            player.canMoveDown(left.row, left.col) ||
                            player.canMoveLeft(left.row, left.col)) {
                        // Add moves to stack.
                        s.push(move);
                        s.push(moveLeft);
                        continue;
                    }
                }
            }
        }
    }

    /**
     Updates the moves using Breadth First Search.
     */
    public void updateMovesBreadth() {
        // Populate starting queue.
        Queue<Move> q = new LinkedList<>();
        Player player = turn == 'B' ? black : white;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (player.canMove(i, j)) {
                    Square square = board.table[i][j];
                    Move move = new Move(square, square, square, 0);
                    q.add(move);
                }
            }
        }

        // Populate moves list.
        moves = new ArrayList<>();
        while (q.peek() != null) {
            Move move = q.remove();
            Square start = move.start;
            Square prev = move.prev;
            Square end = move.end;

            Integer score = move.score;
            if (score == 9) break;

            // Check move up.
            if (player.canMoveUp(end.row, end.col)) {
                Square up = board.table[end.row - 2][end.col];
                Move moveUp = new Move(start, end, up, score + 1);
                if (!prev.equals(up)) {
                    // Add move up to moves list.
                    moves.add(moveUp);
                    if (player.canMoveUp(up.row, up.col) ||
                            player.canMoveRight(up.row, up.col) ||
                            player.canMoveLeft(up.row, up.col)) {
                        // Add move up to queue.
                        q.add(moveUp);
                    }
                }
            }
            // Check move right.
            if (player.canMoveRight(end.row, end.col)) {
                Square right = board.table[end.row][end.col + 2];
                Move moveRight = new Move(start, end, right, score + 1);
                if (!prev.equals(right)) {
                    // Add move right to moves list.
                    moves.add(moveRight);
                    if (player.canMoveUp(right.row, right.col) ||
                            player.canMoveRight(right.row, right.col) ||
                            player.canMoveDown(right.row, right.col)) {
                        // Add move right to queue.
                        q.add(moveRight);
                    }
                }
            }
            // Check move down.
            if (player.canMoveDown(end.row, end.col)) {
                Square down = board.table[end.row + 2][end.col];
                Move moveDown = new Move(start, end, down, score + 1);
                if (!prev.equals(down)) {
                    // Add move down to moves list.
                    moves.add(moveDown);
                    if (player.canMoveRight(down.row, down.col) ||
                            player.canMoveDown(down.row, down.col) ||
                            player.canMoveLeft(down.row, down.col)) {
                        // Add move down to queue.
                        q.add(moveDown);
                    }
                }
            }
            // Check move left.
            if (player.canMoveLeft(end.row, end.col)) {
                Square left = board.table[end.row][end.col - 2];
                Move moveLeft = new Move(start, end, left, score + 1);
                if (!prev.equals(left)) {
                    // Add move left to moves list.
                    moves.add(moveLeft);
                    if (player.canMoveUp(left.row, left.col) ||
                            player.canMoveDown(left.row, left.col) ||
                            player.canMoveLeft(left.row, left.col)) {
                        // Add move left to queue.
                        q.add(moveLeft);
                    }
                }
            }
        }
    }

    /**
     Updates the moves using Best First Search.
     */
    public void updateMovesBest() {
        // Populate starting queue.
        Queue<Move> q = new LinkedList<>();
        Player player = turn == 'B' ? black : white;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (player.canMove(i, j)) {
                    Square square = board.table[i][j];
                    Move move = new Move(square, square, square, 0);
                    q.add(move);
                }
            }
        }

        // Populate moves list.
        moves = new ArrayList<>();
        while (q.peek() != null) {
            Move move = q.remove();
            Square start = move.start;
            Square prev = move.prev;
            Square end = move.end;

            Integer score = move.score;
            if (score == 9) break;

            // Check move up.
            if (player.canMoveUp(end.row, end.col)) {
                Square up = board.table[end.row - 2][end.col];
                Move moveUp = new Move(start, end, up, score + 1);
                if (!prev.equals(up)) {
                    // Add move up to moves list.
                    moves.add(0, moveUp);
                    if (player.canMoveUp(up.row, up.col) ||
                            player.canMoveRight(up.row, up.col) ||
                            player.canMoveLeft(up.row, up.col)) {
                        // Add move up to queue.
                        q.add(moveUp);
                    }
                }
            }
            // Check move right.
            if (player.canMoveRight(end.row, end.col)) {
                Square right = board.table[end.row][end.col + 2];
                Move moveRight = new Move(start, end, right, score + 1);
                if (!prev.equals(right)) {
                    // Add move right to moves list.
                    moves.add(0, moveRight);
                    if (player.canMoveUp(right.row, right.col) ||
                            player.canMoveRight(right.row, right.col) ||
                            player.canMoveDown(right.row, right.col)) {
                        // Add move right to queue.
                        q.add(moveRight);
                    }
                }
            }
            // Check move down.
            if (player.canMoveDown(end.row, end.col)) {
                Square down = board.table[end.row + 2][end.col];
                Move moveDown = new Move(start, end, down, score + 1);
                if (!prev.equals(down)) {
                    // Add move down to moves list.
                    moves.add(0, moveDown);
                    if (player.canMoveRight(down.row, down.col) ||
                            player.canMoveDown(down.row, down.col) ||
                            player.canMoveLeft(down.row, down.col)) {
                        // Add move down to queue.
                        q.add(moveDown);
                    }
                }
            }
            // Check move left.
            if (player.canMoveLeft(end.row, end.col)) {
                Square left = board.table[end.row][end.col - 2];
                Move moveLeft = new Move(start, end, left, score + 1);
                if (!prev.equals(left)) {
                    // Add move left to moves list.
                    moves.add(0, moveLeft);
                    if (player.canMoveUp(left.row, left.col) ||
                            player.canMoveDown(left.row, left.col) ||
                            player.canMoveLeft(left.row, left.col)) {
                        // Add move left to queue.
                        q.add(moveLeft);
                    }
                }
            }
        }
    }

    /**
     Processes each next command.
     @return String value depending on next available move.
     */
    public String processNext() {
        // Check if game is over.
        if (isOver()) {
            return "W0" + (boardSize - 1) + (boardSize - 1) + "00";
        }

        // Check if moves is empty.
        if (moves.isEmpty()) {
            setSearch(search);
            if (moves.isEmpty()) {
                return "W0" + (boardSize - 1) + (boardSize - 1) + "00";
            }
        }

        Move next = moves.remove(0);

        // Check move color.
        if (turn != next.start.color) {
            setSearch(search);
            if (moves.isEmpty()) {
                return "W0" + (boardSize - 1) + (boardSize - 1) + "00";
            }
            next = moves.remove(0);
        }

        return "" + next.start.color + next.start.row + next.start.col +
                next.end.row + next.end.col + next.score;
    }
}
