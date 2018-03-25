package edu.ramapo.ktavadze.konane;

import java.util.ArrayList;

/**
 * Path class.
 */

public class Path {
    public ArrayList<Move> visited;
    public ArrayList<Square> captured;

    /**
     Path class constructor.
     */
    public Path() {
        visited = new ArrayList<>();
        captured = new ArrayList<>();
    }

    /**
     Path class constructor.
     @param a_path - Path object to be cloned.
     */
    public Path(Path a_path) {
        if (a_path.visited.isEmpty()) {
            visited = new ArrayList<>();
            captured = new ArrayList<>();
        }
        else {
            visited = new ArrayList<>(a_path.visited);
            captured = new ArrayList<>(a_path.captured);
        }
    }

    /**
     Compares the calling path with another.
     @param a_path - Path to be compared to.
     @return Boolean value depending on whether the paths match.
     */
    @Override
    public boolean equals(Object a_path) {
        Path path = (Path) a_path;
        if (captured.size() != path.captured.size()) {
            return false;
        }
        for (int i = 0; i < captured.size(); i++) {
            if (!captured.get(i).equals(path.captured.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     Adds specified move to visited and updates captured.
     @param a_move - Move object to be added.
     */
    public void addMove(Move a_move) {
        visited.add(a_move);
        captured.add(a_move.captured);
    }
}
