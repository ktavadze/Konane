package edu.ramapo.ktavadze.konane;

import java.util.ArrayList;
import java.util.Collections;
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
    public int cutoff = 4;
    public ArrayList<Path> minimaxRoots = null;
    public Path minimaxPath = null;
    public boolean useAlphaBeta = false;
    public long minimaxTime = 0;

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
                if (board.table[i][j].isEmpty) {
                    state += "O ";
                }
                else {
                    state += board.table[i][j].color + " ";
                }
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
                        board.table[row][j].isEmpty = true;
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
     Updates the paths using depth first search.
     @param a_player - Player object to be considered.
     */
    public ArrayList<Path> getPathsDepth(Player a_player) {
        // Populate starting stack.
        Stack<Path> s = new Stack<>();
        for (int i = boardSize - 1; i >= 0; i--) {
            for (int j = boardSize - 1; j >= 0; j--) {
                if (a_player.canMove(i, j)) {
                    Square square = board.table[i][j];
                    Move move = new Move(square, square);
                    Path path = new Path();
                    path.addMove(move);
                    s.push(path);
                }
            }
        }

        // Find depth paths.
        ArrayList<Path> paths = new ArrayList<>();
        while (!s.empty()) {
            Path path = s.pop();
            Move move = path.visited.get(path.visited.size() - 1);
            path.visited.get(0).start.isEmpty = true;

            // Check move up.
            if (a_player.canMoveUp(move.end.row, move.end.col)) {
                Square up = board.table[move.end.row - 2][move.end.col];
                Move moveUp = new Move(move.end, up);
                if (!path.captured.contains(moveUp.captured)) {
                    Path pathUp = new Path(path);
                    pathUp.addMove(moveUp);
                    if (!paths.contains(pathUp)) {
                        paths.add(pathUp);
                        if (a_player.canMoveUp(up.row, up.col) ||
                                a_player.canMoveRight(up.row, up.col) ||
                                a_player.canMoveLeft(up.row, up.col)) {
                            s.push(path);
                            s.push(pathUp);
                            continue;
                        }
                    }
                }
            }
            // Check move right.
            if (a_player.canMoveRight(move.end.row, move.end.col)) {
                Square right = board.table[move.end.row][move.end.col + 2];
                Move moveRight = new Move(move.end, right);
                if (!path.captured.contains(moveRight.captured)) {
                    Path pathRight = new Path(path);
                    pathRight.addMove(moveRight);
                    if (!paths.contains(pathRight)) {
                        paths.add(pathRight);
                        if (a_player.canMoveUp(right.row, right.col) ||
                                a_player.canMoveRight(right.row, right.col) ||
                                a_player.canMoveDown(right.row, right.col)) {
                            s.push(path);
                            s.push(pathRight);
                            continue;
                        }
                    }
                }
            }
            // Check move down.
            if (a_player.canMoveDown(move.end.row, move.end.col)) {
                Square down = board.table[move.end.row + 2][move.end.col];
                Move moveDown = new Move(move.end, down);
                if (!path.captured.contains(moveDown.captured)) {
                    Path pathDown = new Path(path);
                    pathDown.addMove(moveDown);
                    if (!paths.contains(pathDown)) {
                        paths.add(pathDown);
                        if (a_player.canMoveRight(down.row, down.col) ||
                                a_player.canMoveDown(down.row, down.col) ||
                                a_player.canMoveLeft(down.row, down.col)) {
                            s.push(path);
                            s.push(pathDown);
                            continue;
                        }
                    }
                }
            }
            // Check move left.
            if (a_player.canMoveLeft(move.end.row, move.end.col)) {
                Square left = board.table[move.end.row][move.end.col - 2];
                Move moveLeft = new Move(move.end, left);
                if (!path.captured.contains(moveLeft.captured)) {
                    Path pathLeft = new Path(path);
                    pathLeft.addMove(moveLeft);
                    if (!paths.contains(pathLeft)) {
                        paths.add(pathLeft);
                        if (a_player.canMoveUp(left.row, left.col) ||
                                a_player.canMoveDown(left.row, left.col) ||
                                a_player.canMoveLeft(left.row, left.col)) {
                            s.push(path);
                            s.push(pathLeft);
                            continue;
                        }
                    }
                }
            }

            path.visited.get(0).start.isEmpty = false;
        }

        return paths;
    }


    /**
     Updates the paths using breadth first search.
     @param a_player - Player object to be considered.
     */
    public ArrayList<Path> getPathsBreadth(Player a_player) {
        // Populate starting queue.
        Queue<Path> q = new LinkedList<>();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (a_player.canMove(i, j)) {
                    Square square = board.table[i][j];
                    Move move = new Move(square, square);
                    Path path = new Path();
                    path.addMove(move);
                    q.add(path);
                }
            }
        }

        // Find breadth paths.
        ArrayList<Path> paths = new ArrayList<>();
        while (q.peek() != null) {
            Path path = q.remove();
            Move move = path.visited.get(path.visited.size() - 1);
            path.visited.get(0).start.isEmpty = true;

            //Check move up.
            if (a_player.canMoveUp(move.end.row, move.end.col)) {
                Square up = board.table[move.end.row - 2][move.end.col];
                Move moveUp = new Move(move.end, up);
                if (!path.captured.contains(moveUp.captured)) {
                    Path pathUp = new Path(path);
                    pathUp.addMove(moveUp);
                    paths.add(pathUp);
                    if (a_player.canMoveUp(up.row, up.col) ||
                            a_player.canMoveRight(up.row, up.col) ||
                            a_player.canMoveLeft(up.row, up.col)) {
                        q.add(pathUp);
                    }
                }
            }
            // Check move right.
            if (a_player.canMoveRight(move.end.row, move.end.col)) {
                Square right = board.table[move.end.row][move.end.col + 2];
                Move moveRight = new Move(move.end, right);
                if (!path.captured.contains(moveRight.captured)) {
                    Path pathRight = new Path(path);
                    pathRight.addMove(moveRight);
                    paths.add(pathRight);
                    if (a_player.canMoveUp(right.row, right.col) ||
                            a_player.canMoveRight(right.row, right.col) ||
                            a_player.canMoveDown(right.row, right.col)) {
                        q.add(pathRight);
                    }
                }
            }
            // Check move down.
            if (a_player.canMoveDown(move.end.row, move.end.col)) {
                Square down = board.table[move.end.row + 2][move.end.col];
                Move moveDown = new Move(move.end, down);
                if (!path.captured.contains(moveDown.captured)) {
                    Path pathDown = new Path(path);
                    pathDown.addMove(moveDown);
                    paths.add(pathDown);
                    if (a_player.canMoveRight(down.row, down.col) ||
                            a_player.canMoveDown(down.row, down.col) ||
                            a_player.canMoveLeft(down.row, down.col)) {
                        q.add(pathDown);
                    }
                }
            }
            // Check move left.
            if (a_player.canMoveLeft(move.end.row, move.end.col)) {
                Square left = board.table[move.end.row][move.end.col - 2];
                Move moveLeft = new Move(move.end, left);
                if (!path.captured.contains(moveLeft.captured)) {
                    Path pathLeft = new Path(path);
                    pathLeft.addMove(moveLeft);
                    paths.add(pathLeft);
                    if (a_player.canMoveUp(left.row, left.col) ||
                            a_player.canMoveDown(left.row, left.col) ||
                            a_player.canMoveLeft(left.row, left.col)) {
                        q.add(pathLeft);
                    }
                }
            }

            path.visited.get(0).start.isEmpty = false;
        }

        return paths;
    }


    /**
     Updates the paths using best first search.
     @param a_player - Player object to be considered.
     */
    public ArrayList<Path> getPathsBest(Player a_player) {
        // Populate starting queue.
        Queue<Path> q = new LinkedList<>();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (a_player.canMove(i, j)) {
                    Square square = board.table[i][j];
                    Move move = new Move(square, square);
                    Path path = new Path();
                    path.addMove(move);
                    q.add(path);
                }
            }
        }

        // Find all paths.
        ArrayList<Path> allPaths = new ArrayList<>();
        while (q.peek() != null) {
            Path path = q.remove();
            Move move = path.visited.get(path.visited.size() - 1);
            path.visited.get(0).start.isEmpty = true;

            //Check move up.
            if (a_player.canMoveUp(move.end.row, move.end.col)) {
                Square up = board.table[move.end.row - 2][move.end.col];
                Move moveUp = new Move(move.end, up);
                if (!path.captured.contains(moveUp.captured)) {
                    Path pathUp = new Path(path);
                    pathUp.addMove(moveUp);
                    allPaths.add(0, pathUp);
                    if (a_player.canMoveUp(up.row, up.col) ||
                            a_player.canMoveRight(up.row, up.col) ||
                            a_player.canMoveLeft(up.row, up.col)) {
                        q.add(pathUp);
                    }
                }
            }
            // Check move right.
            if (a_player.canMoveRight(move.end.row, move.end.col)) {
                Square right = board.table[move.end.row][move.end.col + 2];
                Move moveRight = new Move(move.end, right);
                if (!path.captured.contains(moveRight.captured)) {
                    Path pathRight = new Path(path);
                    pathRight.addMove(moveRight);
                    allPaths.add(0, pathRight);
                    if (a_player.canMoveUp(right.row, right.col) ||
                            a_player.canMoveRight(right.row, right.col) ||
                            a_player.canMoveDown(right.row, right.col)) {
                        q.add(pathRight);
                    }
                }
            }
            // Check move down.
            if (a_player.canMoveDown(move.end.row, move.end.col)) {
                Square down = board.table[move.end.row + 2][move.end.col];
                Move moveDown = new Move(move.end, down);
                if (!path.captured.contains(moveDown.captured)) {
                    Path pathDown = new Path(path);
                    pathDown.addMove(moveDown);
                    allPaths.add(0, pathDown);
                    if (a_player.canMoveRight(down.row, down.col) ||
                            a_player.canMoveDown(down.row, down.col) ||
                            a_player.canMoveLeft(down.row, down.col)) {
                        q.add(pathDown);
                    }
                }
            }
            // Check move left.
            if (a_player.canMoveLeft(move.end.row, move.end.col)) {
                Square left = board.table[move.end.row][move.end.col - 2];
                Move moveLeft = new Move(move.end, left);
                if (!path.captured.contains(moveLeft.captured)) {
                    Path pathLeft = new Path(path);
                    pathLeft.addMove(moveLeft);
                    allPaths.add(0, pathLeft);
                    if (a_player.canMoveUp(left.row, left.col) ||
                            a_player.canMoveDown(left.row, left.col) ||
                            a_player.canMoveLeft(left.row, left.col)) {
                        q.add(pathLeft);
                    }
                }
            }

            path.visited.get(0).start.isEmpty = false;
        }

        // Find best paths.
        ArrayList<Path> paths = new ArrayList<>();
        if (!allPaths.isEmpty()) {
            Path bestPath = allPaths.remove(0);
            paths.add(bestPath);
            while(!allPaths.isEmpty()) {
                Path path = allPaths.remove(0);
                if (path.captured.size() == bestPath.captured.size()) {
                    paths.add(0, path);
                }
            }
        }

        return paths;
    }

    /**
     Processes each next command.
     @return Path object generated by minimax.
     */
    public Path processNext() {
        Player player = turn == 'B' ? black : white;
        updateMinimaxPath(player);

        return minimaxPath;
    }

    /**
     Updates the minimax path for the specified player.
     @param a_player - Player object to be considered.
     */
    public void updateMinimaxPath(Player a_player) {
        minimaxRoots = new ArrayList<>();

        // Call the appropriate minimax function and time it.
        long startTime = System.currentTimeMillis();
        if (a_player.color == 'B') {
            minimaxBlack(0, black, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
        else {
            minimaxWhite(0, white, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
        long endTime = System.currentTimeMillis();
        minimaxTime = endTime - startTime;

        if (minimaxRoots.isEmpty()) {
            minimaxPath = null;
            return;
        }

        // Pick the first best minimax path from minimax roots.
        Path path = minimaxRoots.get(0);
        minimaxPath = path;
        int score = path.minimaxValue;
        System.out.println("Roots");
        for (Path p : minimaxRoots) {
            System.out.println(p.toString());
            if (p.minimaxValue > score) {
                minimaxPath = p;
                score = p.minimaxValue;
            }
        }
    }

    /**
     Populates the minimax roots for the specified depth, player, alpha and beta.
     @param a_depth - Integer depth value.
     @param a_player - Player object to be considered.
     @param a_alpha - Integer alpha value.
     @param a_beta - Integer beta value.
     @return Integer value depending on heuristic function.
     */
    public Integer minimaxBlack(int a_depth, Player a_player, int a_alpha, int a_beta) {
        if (isOver() || a_depth > cutoff) {
            return getHeuristic(black);
        }

        ArrayList<Path> allPaths = getPathsDepth(a_player);
        if (allPaths.isEmpty()) {
            return getHeuristic(black);
        }

        boolean[][] savedTable = new boolean[boardSize][boardSize];
        copyTable(savedTable);

//        System.out.println("All at " + a_depth);
//        for (Path p : allPaths) {
//            System.out.println(p.toString());
//        }

        ArrayList<Integer> scores = new ArrayList<>();
        for (int i = 0; i < allPaths.size(); i++) {
            Path path = allPaths.get(i);
            applyPath(path);

//            System.out.println("Applied at " + a_depth);
//            System.out.println(path.toString());
//            for (int r = 0; r < boardSize; r++) {
//                for (int c = 0; c < boardSize; c++) {
//                    if (board.table[r][c].isEmpty) {
//                        System.out.print("  ");
//                    }
//                    else {
//                        if (board.table[r][c].color == 'B') {
//                            System.out.print("B ");
//                        }
//                        else {
//                            System.out.print("W ");
//                        }
//                    }
//                }
//                System.out.println();
//            }

            if (a_player.color == 'B') {
                int score = minimaxBlack(a_depth + 1, white, a_alpha, a_beta);
                scores.add(score);

                if (useAlphaBeta) {
                    if (path.minimaxValue > a_alpha) {
                        a_alpha = path.minimaxValue;
                    }
                    if (a_beta <= a_alpha) break;
                }

                if (a_depth == 0) {
                    path.minimaxValue = score;
                    minimaxRoots.add(path);
                }
            }
            else {
                int score = minimaxBlack(a_depth + 1, black, a_alpha, a_beta);
                scores.add(score);

                if (useAlphaBeta) {
                    if (path.minimaxValue < a_beta) {
                        a_beta = path.minimaxValue;
                    }
                    if (a_alpha >= a_beta) break;
                }
            }

            resetTable(savedTable);
        }

        if (a_player.color == 'B') {
            return Collections.max(scores);
        }
        return Collections.min(scores);
    }

    /**
     Populates the minimax roots for the specified depth, player, alpha and beta.
     @param a_depth - Integer depth value.
     @param a_player - Player object to be considered.
     @param a_alpha - Integer alpha value.
     @param a_beta - Integer beta value.
     @return Integer value depending on heuristic function.
     */
    public Integer minimaxWhite(int a_depth, Player a_player, int a_alpha, int a_beta) {
        if (isOver() || a_depth > cutoff) {
            return getHeuristic(white);
        }

        ArrayList<Path> allPaths = getPathsDepth(a_player);
        if (allPaths.isEmpty()) {
            return getHeuristic(white);
        }

        boolean[][] savedTable = new boolean[boardSize][boardSize];
        copyTable(savedTable);

//        System.out.println("All at " + a_depth);
//        for (Path p : allPaths) {
//            System.out.println(p.toString());
//        }

        ArrayList<Integer> scores = new ArrayList<>();
        for (int i = 0; i < allPaths.size(); i++) {
            Path path = allPaths.get(i);
            applyPath(path);

//            System.out.println("Applied at " + a_depth);
//            System.out.println(path.toString());
//            for (int r = 0; r < boardSize; r++) {
//                for (int c = 0; c < boardSize; c++) {
//                    if (board.table[r][c].isEmpty) {
//                        System.out.print("  ");
//                    }
//                    else {
//                        if (board.table[r][c].color == 'B') {
//                            System.out.print("B ");
//                        }
//                        else {
//                            System.out.print("W ");
//                        }
//                    }
//                }
//                System.out.println();
//            }

            if (a_player.color == 'W') {
                int score = minimaxWhite(a_depth + 1, black, a_alpha, a_beta);
                scores.add(score);

                if (useAlphaBeta) {
                    if (path.minimaxValue > a_alpha) {
                        a_alpha = path.minimaxValue;
                    }
                    if (a_beta <= a_alpha) break;
                }

                if (a_depth == 0) {
                    path.minimaxValue = score;
                    minimaxRoots.add(path);
                }
            }
            else {
                int score = minimaxWhite(a_depth + 1, white, a_alpha, a_beta);
                scores.add(score);

                if (useAlphaBeta) {
                    if (path.minimaxValue < a_beta) {
                        a_beta = path.minimaxValue;
                    }
                    if (a_alpha >= a_beta) break;
                }
            }

            resetTable(savedTable);
        }

        if (a_player.color == 'W') {
            return Collections.max(scores);
        }
        return Collections.min(scores);
    }

    /**
     Generates a heuristic value based on the current board state for the specified player.
     @param a_player - Player object to be considered.
     @return Integer value depending on current board.
     */
    public int getHeuristic(Player a_player) {
        int blackCount = 0;
        int whiteCount = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (!board.table[i][j].isEmpty) {
                    if (board.table[i][j].color == 'B') {
                        blackCount++;
                    }
                    else {
                        whiteCount++;
                    }
                }
            }
        }

        if (a_player.color == 'B') {
            return blackCount - whiteCount;
        }
        return whiteCount - blackCount;
    }

    /**
     Copies the current board state into the specified 2D boolean array.
     @param a_table - Boolean array to be copied to.
     */
    public void copyTable(boolean[][] a_table) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                a_table[i][j] = board.table[i][j].isEmpty;
            }
        }
    }

    /**
     Resets the current board state using the specified 2D boolean array.
     @param a_table - Boolean array to be copied from.
     */
    public void resetTable(boolean[][] a_table) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board.table[i][j].isEmpty = a_table[i][j];
            }
        }
    }

    /**
     Applies the specified path to the current board state.
     @param a_path - Path object to be applied.
     */
    public void applyPath(Path a_path) {
        Square start = a_path.visited.get(1).start;
        start.isEmpty = true;

        Square end = a_path.visited.get(a_path.visited.size()-1).end;
        end.isEmpty = false;

        for (int i = 1; i < a_path.captured.size(); i++) {
            a_path.captured.get(i).isEmpty = true;
        }
    }
}
