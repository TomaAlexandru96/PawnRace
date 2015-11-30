import java.util.Random;

public class Player {
    public Game g;
    public Board b;
    private Color c;
    private boolean isComputer;
    private Player opponent;
    boolean debug;

    public Player(Game game, Board board, Color color, boolean isComputerPlayer, boolean debug) {
        g = game;
        b = board;
        c = color;
        isComputer = isComputerPlayer;
        this.debug = debug;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public Color getColor() {
        return c;
    }

    public boolean isComputerPlayer() {
        return isComputer;
    }

    public Square[] getAllPawns() {
        int k = 0;
        Square[] result;

        for (int i = 0; i < 8 ; i ++ ) {
            for (int j = 0; j < 8; j++) {
                if (b.getSquare(i, j).occupiedBy() == c) {
                    k++;
                }
            }
        }
        result = new Square[k];
        k = 0;

        for (int i = 0; i < 8 ; i ++ ) {
            for (int j = 0; j < 8; j++) {
                if (b.getSquare(i, j).occupiedBy() == c) {
                    result[k] = new Square(i, j);
                    k++;
                }
            }
        }
        return result;
    }

    public Move[] getAllValidMoves(){
        int k = 0;
        Move[] ms;
        Square[] ps = getAllPawns();
        Move move;

        for (int q = 0; q < ps.length; q++) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    move = new Move(new Square(ps[q].getX(), ps[q].getY()), new Square(i, j), false);
                    if (g.isValid(move, c)) {
                        k++;
                    }
                    move = new Move(new Square(ps[q].getX(), ps[q].getY()), new Square(i, j), true);
                    if (g.isValid(move, c)) {
                        k++;
                    }
                }
            }
        }

        ms = new Move[k];
        k = 0;

        for (int q = 0; q < ps.length; q++) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    move = new Move(new Square(ps[q].getX(), ps[q].getY()), new Square(i, j), false);
                    if (g.isValid(move, c)) {
                        ms[k] = move;
                        k++;
                    }
                    move = new Move(new Square(ps[q].getX(), ps[q].getY()), new Square(i, j), true);
                    if (g.isValid(move, c)) {
                        ms[k] = move;
                        k++;
                    }
                }
            }
        }
        return ms;
    }

    public boolean isPassedPawn(Square square) {
        int xp, yp;
        boolean isPassed = true;
        Color opposite, aux;
        Move move;

        xp = square.getX();
        yp = square.getY();
        if (b.getSquare(xp, yp).occupiedBy() != c) {
            return false;
        }

        if (c == Color.WHITE) {
            opposite = Color.BLACK;

            for (int i = xp; i < 7; i++) {
                aux = b.getSquare(i, yp).occupiedBy();
                b.getSquare(i, yp).setOccupier(Color.WHITE);

                if (b.getSquare(i + 1, yp).occupiedBy() == opposite) {
                    isPassed = false;
                }

                if (yp != 0) {
                    move = new Move(new Square(i + 1, yp - 1), new Square(i, yp), true);
                    //System.out.println("Move is " + (i+1) + (yp-1) + i + yp +" " + g.isValid(move, c));
                    if (g.isValid(move, opposite)) {
                        isPassed = false;
                    }
                }
                if (yp != 7) {
                    move = new Move(new Square(i + 1, yp + 1), new Square(i, yp), true);
                    //System.out.println("Move is " + (i+1) + (yp+1) + i + yp +" " + g.isValid(move, c));
                    if (g.isValid(move, opposite)) {
                        isPassed = false;
                    }
                }

                b.getSquare(i, yp).setOccupier(aux);
            }
        } else {
            opposite = Color.WHITE;

            for (int i = xp; i > 0; i--) {
                aux = b.getSquare(i, yp).occupiedBy();
                b.getSquare(i, yp).setOccupier(Color.BLACK);

                if (b.getSquare(i + 1, yp).occupiedBy() == opposite) {
                    isPassed = false;
                }
                if (yp != 0) {
                    move = new Move(new Square(i - 1, yp - 1), new Square(i, yp), true);
                    //System.out.println("Move is " + (i-1) + (yp-1) + i + yp +" " + g.isValid(move, c));
                    if (g.isValid(move, opposite)) {
                        isPassed = false;
                    }
                }
                if (yp != 7) {
                    move = new Move(new Square(i - 1, yp + 1), new Square(i, yp), true);
                    //System.out.println("Move is " + (i-1) + (yp-1) + i + yp +" " + g.isValid(move, c));
                    if (g.isValid(move, opposite)) {
                        isPassed = false;
                    }
                }

                b.getSquare(i, yp).setOccupier(aux);
            }
        }

        return isPassed;
    }

    public void makeMove() {
        if (isComputer) {
            Random rg    = new Random();
            Move[] moves = getAllValidMoves();
            Move moveToMake;
            int randomNumber;
            int n        = moves.length;
            // NO EDIT


            // AI ALGORITHM

            randomNumber = rg.nextInt(n);

            if (debug) {
                System.out.println("LOOKUP ^");
                System.out.println("LOOKUP |");
                for (int i = 0; i < moves.length; i++) {
                    String check = "";

                    if (randomNumber == i) {
                        check = "x";
                    } else {
                        check = "";
                    }

                    System.out.println("MOVE " + c + ": from (" + (moves[i].getFrom().getX() + 1) + "," + (moves[i].getFrom().getY() + 1) + ") " +
                            "to (" + (moves[i].getTo().getX() + 1) + "," + (moves[i].getTo().getY() + 1) + ") isCapture: " + moves[i].isCapture() + " " + check);
                }
                System.out.println(moves.length + " moves available.");
            }

            moveToMake = moves[randomNumber];


            // END AI ALGORITHM


            // NO EDIT
            g.applyMove(moveToMake);
            b = g.b;
        }
    }
}
