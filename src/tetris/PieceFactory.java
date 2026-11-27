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
    
    public static Tetromino createPiece(String type) {

        switch (type) {

            case "I":
                return new IPiece();

            case "J":
                return new JPiece();

            case "L":
                return new LPiece();

            case "O":
                return new OPiece();

            case "S":
                return new SPiece();

            case "T":
                return new TPiece();

            case "Z":
                return new ZPiece();

            default:
                return null;
        }
    }
    
    public static String getPieceType(Tetromino piece) {

        if (piece instanceof IPiece) {
            return "I";
        }
        if (piece instanceof JPiece) {
            return "J";
        }
        if (piece instanceof LPiece) {
            return "L";
        }
        if (piece instanceof OPiece) {
            return "O";
        }
        if (piece instanceof SPiece) {
            return "S";
        }
        if (piece instanceof TPiece) {
            return "T";
        }
        if (piece instanceof ZPiece) {
            return "Z";
        }

        return null;
    }
}