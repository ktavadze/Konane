package edu.ramapo.ktavadze.konane;

/**
 * Move class.
 */

public class Move {
    public Square start;
    public Square prev;
    public Square end;
    public Integer score;

    /**
     Move class constructor.
     @param a_start - Starting square object.
     @param a_prev - Previous square object.
     @param a_end - Ending square object.
     @param a_score - Integer value of the score.
     */
    public Move(Square a_start, Square a_prev, Square a_end, Integer a_score) {
        start = a_start;
        prev = a_prev;
        end = a_end;
        score = a_score;
    }

    /**
     Compares the calling move with another.
     @param a_move - Move to be compared to.
     @return Boolean value depending on whether the moves match.
     */
    @Override
    public boolean equals(Object a_move) {
        Move move = (Move) a_move;
        if (start.equals(move.start) && end.equals(move.end) && score == move.score) {
            return true;
        }
        return false;
    }
}
