import java.io.*;
import java.util.Arrays;

public class TestFile {
    public static Move getMove(String file, Board mainBoard) throws  IOException{
        BufferedReader br = new BufferedReader(new FileReader(file));
        String input;
        Square[] pawns = new Square[20];
        char[] outChars;
        int k, l;
        Move move = null;
        int xt, yt, xf ,yf;
        Board comparedBoard;

        input = br.readLine();

        while (input != null) {
            //System.out.println(x);
            k = -1;

            outChars = input.toCharArray();

            for (Square p : pawns) {
                p = null;
            }

            for (int i = 1; i < outChars.length; i += 3) {
                if (outChars[i] == 'w') {
                    k++;
                    pawns[k] = new Square(charToIntTrans(outChars[i + 1]), charToIntTrans(outChars[i + 2]));
                    pawns[k].setOccupier(Color.WHITE);
                } else if (outChars[i] == 'b') {
                    k++;
                    pawns[k] = new Square(charToIntTrans(outChars[i + 1]), charToIntTrans(outChars[i + 2]));
                    pawns[k].setOccupier(Color.BLACK);
                }
            }

            /*
            l = outChars.length;
            xf = charToIntTrans(outChars[l - 5]);
            yf = charToIntTrans(outChars[l - 4]);
            xt = charToIntTrans(outChars[l - 3]);
            yt = charToIntTrans(outChars[l - 2]);


            move = new Move(new Square(xf, yf), new Square(xt, yt), outChars[l - 1] == 't');

            if (input.equals("a")) {
                createBoard(pawns, k + 1).display(Color.WHITE);
                System.out.println("Move is: from (" + (move.getFrom().getX() + 1) + ", " + (move.getFrom().getY() + 1) + ") to (" + (move.getTo().getX() + 1) + ", " + (move.getTo().getY() + 1) + ") isCapture: " + move.isCapture());
            }
            */

            comparedBoard = createBoard(pawns, k + 1);

            if (compareBoards(comparedBoard, mainBoard)) {

                l = outChars.length;
                xf = charToIntTrans(outChars[l - 5]);
                yf = charToIntTrans(outChars[l - 4]);
                xt = charToIntTrans(outChars[l - 3]);
                yt = charToIntTrans(outChars[l - 2]);


                move = new Move(new Square(xf, yf), new Square(xt, yt), outChars[l - 1] == 't');
            }

            // debug
            if (compareBoards(comparedBoard, testBoard())) {
                System.out.println("Yes");
            }
            // end debug

            input = br.readLine();
        }
        System.out.println("done..");

        return move;
    }

    private static int charToIntTrans(char c) {
        return c - '0';
    }

    private static Board createBoard(Square[] pawns, int length){
        Board result = new Board();
        for (int i = 0; i < length; i++) {
            result.getSquare(pawns[i].getX(), pawns[i].getY()).setOccupier(pawns[i].occupiedBy());
        }
        return result;
    }

    private static boolean compareBoards(Board b1, Board b2) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!compareSquares(b1.getSquare(i, j), b2.getSquare(i, j))) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean compareSquares(Square s1, Square s2) {
        return s1.occupiedBy() == s2.occupiedBy();
    }

    private static Board testBoard() {
        // 1w44w46w56b21b22b23b25b67m6756t
        Board b = new Board();

        int x, y;

        //white
        x = 4;
        y = 4;
        b.getSquare(x, y).setOccupier(Color.WHITE);

        x = 4;
        y = 6;
        b.getSquare(x, y).setOccupier(Color.WHITE);

        x = 5;
        y = 6;
        b.getSquare(x, y).setOccupier(Color.WHITE);


        //black
        x = 2;
        y = 1;
        b.getSquare(x, y).setOccupier(Color.BLACK);

        x = 2;
        y = 2;
        b.getSquare(x, y).setOccupier(Color.BLACK);

        x = 2;
        y = 3;
        b.getSquare(x, y).setOccupier(Color.BLACK);

        x = 2;
        y = 5;
        b.getSquare(x, y).setOccupier(Color.BLACK);

        x = 6;
        y = 7;
        b.getSquare(x, y).setOccupier(Color.BLACK);
        return b;
    }
}