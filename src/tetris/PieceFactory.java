package tetris;

import java.util.*;

public class PieceFactory {

    private static ArrayList<Tetromino> bag = new ArrayList<>();

    public static Tetromino createRandomPiece() {

        if (bag.isEmpty()) {

            bag.add(new IPiece());
            bag.add(new JPiece());
            bag.add(new LPiece());
            bag.add(new OPiece());
            bag.add(new SPiece());
            bag.add(new TPiece());
            bag.add(new ZPiece());

            // Randomize the order
            Collections.shuffle(bag);
        }

        // Remove and return the next piece
        return bag.remove(0);
    }
}