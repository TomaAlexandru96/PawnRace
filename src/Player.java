import java.util.Random;

public class Player {
    public Game g;
    public Board b;
    private Color c;
    private boolean isComputer;
    private Player opponent;

    public Player(Game game, Board board, Color color, boolean isComputerPlayer) {
        g = game;
        b = board;
        c = color;
        isComputer = isComputerPlayer;
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
        if (c == Color.WHITE) {
            if (b.getSquare(square.getX() - 1, square.getY() - 1).occupiedBy() == Color.WHITE) {
                return true;
            }
            if (b.getSquare(square.getX() - 1, square.getY() + 1).occupiedBy() == Color.WHITE) {
                return true;
            }
        } else {
            if (b.getSquare(square.getX() + 1, square.getY() - 1).occupiedBy() == Color.BLACK) {
                return true;
            }
            if (b.getSquare(square.getX() + 1, square.getY() + 1).occupiedBy() == Color.BLACK) {
                return true;
            }
        }
        return false;
    }

    public void makeMove() {
        if (isComputer) {
            Random rg    = new Random();
            Move[] moves = getAllValidMoves();
            Move moveToMake;
            int n        = moves.length;
            // NO EDIT


            // AI ALGORITHM
            moveToMake = moves[rg.nextInt(n)];
            // END AI ALGORITHM


            // NO EDIT
            g.applyMove(moveToMake);
            b = g.b;
        }
    }
}
