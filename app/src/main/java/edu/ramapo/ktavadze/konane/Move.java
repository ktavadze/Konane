package edu.ramapo.ktavadze.konane;

/**
 * Move class.
 */

public class Move {
    public Square start;
    public Square end;
    public Square captured;

    /**
     Move class constructor.
     @param a_start - Starting square object.
     @param a_end - Ending square object.
     */
    public Move(Square a_start, Square a_end) {
        start = a_start;
        end = a_end;
        if (start.equals(end)) {
            captured = start;
        }
        else if (start.row == end.row) {
            if (start.col - 2 == end.col) {
                captured = Game.board.table[start.row][start.col - 1];
            }
            else if (start.col + 2 == end.col) {
                captured = Game.board.table[start.row][start.col + 1];
            }
        }
        else if (start.col == end.col) {
            if (start.row + 2 == end.row) {
                captured = Game.board.table[start.row + 1][start.col];
            }
            else if (start.row - 2 == end.row) {
                captured = Game.board.table[start.row - 1][start.col];
            }
        }
    }

    /**
     Compares the calling move with another.
     @param a_move - Move to be compared to.
     @return Boolean value depending on whether the moves match.
     */
    @Override
    public boolean equals(Object a_move) {
        Move move = (Move) a_move;
        if (start.equals(move.start) && end.equals(move.end)) {
            return true;
        }
        return false;
    }
}
