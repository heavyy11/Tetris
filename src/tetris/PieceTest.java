package tetris;

public class PieceTest {

    public static void main(String[] args) {

        Tetromino piece = new OPiece();

        System.out.println("X: " + piece.getX());
        System.out.println("Y: " + piece.getY());
        System.out.println("Color: " + piece.getColor());

        int[][] shape = piece.getShape();

        for (int i = 0; i < shape.length; i++) {

            for (int j = 0; j < shape[i].length; j++) {
                System.out.print(shape[i][j] + " ");
            }

            System.out.println();
        }
    }
}