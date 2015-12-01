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
                    result[k] = b.getSquare(i, j);
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

    public boolean isPassedPawn(Square square, Color currentPlayer) {
        int xp, yp;
        Color opposite, aux;
        Move move;
        xp = square.getX();
        yp = square.getY();

        if (b.getSquare(xp, yp).occupiedBy() != currentPlayer) {
            //System.out.println("intra1");
            return false;
        }

        if (currentPlayer == Color.WHITE) {
            //white
            opposite = Color.BLACK;

            for (int i = xp; i < 7; i++) {
                aux = b.getSquare(i, yp).occupiedBy();
                b.getSquare(i, yp).setOccupier(Color.WHITE);

                if (b.getSquare(i + 1, yp).occupiedBy() != Color.NONE) {
                    b.getSquare(i, yp).setOccupier(aux);
                    //System.out.println("intra2");
                    return false;
                }

                //System.out.println("In front1 is: ("+(i + 1)+","+yp+")" + b.getSquare(i + 1, yp).occupiedBy());

                if (yp != 0) {
                    move = new Move(new Square(i + 1, yp - 1), new Square(i, yp), true);
                    //System.out.println("Move1 is " + (i+1) + (yp-1) + i + yp +" " + g.isValid(move, c));

                    if (g.isValid(move, opposite)) {
                        b.getSquare(i, yp).setOccupier(aux);
                        //System.out.println("intra3");
                        return false;
                    }
                }
                if (yp != 7) {
                    move = new Move(new Square(i + 1, yp + 1), new Square(i, yp), true);
                    //System.out.println("Move2 is " + (i+1) + (yp+1) + i + yp +" " + g.isValid(move, c));

                    if (g.isValid(move, opposite)) {
                        b.getSquare(i, yp).setOccupier(aux);
                        //System.out.println("intra4");
                        return false;
                    }
                }

                b.getSquare(i, yp).setOccupier(aux);
            }
        } else {
            //black
            opposite = Color.WHITE;

            for (int i = xp; i > 0; i--) {
                aux = b.getSquare(i, yp).occupiedBy();
                b.getSquare(i, yp).setOccupier(Color.BLACK);

                if (b.getSquare(i - 1, yp).occupiedBy() != Color.NONE) {

                    /*System.out.println("i = " + i + ", xp = " + xp);
                    System.out.println("intra5 i:" + b.getSquare(i, yp).occupiedBy());
                    System.out.println("intra5 i-1:" + b.getSquare(i + 1, yp).occupiedBy());*/

                    b.getSquare(i, yp).setOccupier(aux);
                    return false;
                }
                //System.out.println("In front2 is: ("+(i + 1)+","+yp+")" + b.getSquare(i + 1, yp).occupiedBy());

                if (yp != 0) {
                    move = new Move(new Square(i - 1, yp - 1), new Square(i, yp), true);
                    //System.out.println("Move3 is " + (i-1) + (yp-1) + i + yp +" " + g.isValid(move, c));

                    if (g.isValid(move, opposite)) {
                        b.getSquare(i, yp).setOccupier(aux);
                        //System.out.println("intra6");
                        return false;
                    }
                }
                if (yp != 7) {
                    move = new Move(new Square(i - 1, yp + 1), new Square(i, yp), true);
                    //System.out.println("Move4 is " + (i-1) + (yp-1) + i + yp +" " + g.isValid(move, c));

                    if (g.isValid(move, opposite)) {
                        b.getSquare(i, yp).setOccupier(aux);
                        //System.out.println("intra7");
                        return false;
                    }
                }

                b.getSquare(i, yp).setOccupier(aux);
            }
        }

        //debug
        //System.out.println("Testing: " + "("+square.getX()+","+square.getY()+")");
        //System.out.println("TRUE");
        //debug

        return true;
    }

    public boolean guardedPawn(Square square) {
        int x, y;
        boolean isGuarded = false;
        x = square.getX();
        y = square.getY();
        //System.out.println(square.occupiedBy());
        if (square.occupiedBy() == Color.WHITE) {
            if (x != 0) {
                if (y != 0) {
                    isGuarded = b.getSquare(x - 1, y - 1).occupiedBy() == Color.WHITE;
                }
                if (y != 7) {
                    isGuarded = b.getSquare(x - 1, y + 1).occupiedBy() == Color.WHITE;
                }
            }
        } else if (square.occupiedBy() == Color.BLACK) {
            if (x != 7) {
                if (y != 0) {
                    isGuarded = b.getSquare(x + 1, y - 1).occupiedBy() == Color.BLACK;
                }
                if (y != 7) {
                    isGuarded = b.getSquare(x + 1, y + 1).occupiedBy() == Color.BLACK;
                }
            }
        } else {
            isGuarded = true;
        }
        return isGuarded;
    }

    public void makeMove() {
        if (isComputer) {
            Random rg      = new Random();
            Move[] moves   = getAllValidMoves();
            Square[] oppoistePawns;
            Square[] myPawns;
            Square pawn;
            Move moveToMake;
            Color opposite;
            int moveNo = 0;
            int n = moves.length;
            int[] ranks = new int[n];
            int[] chosenMoves;
            int k = 0;
            int min, max;
            int xt, yt, xf, yf;
            // END NO EDIT


            // AI ALGORITHM ------------------
            // if move and make opponent have passedPawn and current no passedPawn -100
            // guard pawn = 1
            // capture unguarded pawn = 2
            // isPassedPawn = 5
            // isPassedPawn and capture = 7
            // last line = 1000

            opposite = opponent.getColor();

            if (g.k == 0 || g.k == 1) {
                if (getMissing(c) == 0 || getMissing(c) == 7) {
                    int difference;
                    difference = Math.abs(getMissing(c) - 2);
                    for (int i = 0; i < moves.length; i++) {

                        xt = moves[i].getTo().getX();
                        yt = moves[i].getTo().getY();
                        xf = moves[i].getFrom().getX();
                        yf = moves[i].getFrom().getY();

                        if (yf == difference && (xt == 3 || xt == 4)) {
                            moveNo = i;
                        }
                    }
                } else {
                    moveNo = rg.nextInt(n);
                }
            } else {
                //init ranks
                for (int rank : ranks) {
                    rank = 0;
                }

                //find passed pawns
                min = 10;
                int tillFin = 0;
                for (int i = 0; i < moves.length; i++) {
                    applyToWholeBoard(moves[i]);
                    pawn = b.getSquare(moves[i].getTo().getX(), moves[i].getTo().getY());

                    if (isPassedPawn(pawn, c)) {
                        if (c == Color.WHITE) {
                            tillFin = 7 - moves[i].getTo().getX();

                        } else {
                            tillFin = moves[i].getTo().getX() - 1;
                        }
                        if (min > tillFin){
                            min = tillFin;
                        }
                    }

                    unappltoWholeBoard(moves[i]);
                }

                boolean isOpPassed;

                // main for
                for (int i = 0; i < moves.length; i++) {

                    xt = moves[i].getTo().getX();
                    yt = moves[i].getTo().getY();
                    xf = moves[i].getFrom().getX();
                    yf = moves[i].getFrom().getY();

                    //debug
                    /*System.out.println("For move " + i + ": from ("+(moves[i].getFrom().getX()+1)+","+(moves[i].getFrom().getY()+1)+") " +
                            "to ("+(moves[i].getTo().getX()+1)+","+(moves[i].getTo().getY()+1)+") isCapture: "+moves[i].isCapture() + ":");*/
                    //debug

                    // is pawn guarded, if not find a move if possible
                    //TODO BUG WHEN IS ITSELF
                    myPawns = getAllPawns();
                    for (int j = 0; j < myPawns.length; j++) {
                        if (myPawns[j].getX() != 1 && myPawns[j].getX() != 6) {
                            if (!guardedPawn(myPawns[j])) {
                                int x, y;
                                x = myPawns[j].getX();
                                y = myPawns[j].getY();

                                applyToWholeBoard(moves[i]);

                                if (guardedPawn(b.getSquare(x, y))) {
                                    ranks[i] += 1;
                                }
                                unappltoWholeBoard(moves[i]);
                            }
                        }
                    }

                    applyToWholeBoard(moves[i]);
                    pawn = b.getSquare(xt, yt);

                    if (isPassedPawn(pawn, c)) {
                        if (c == Color.WHITE) {
                            //WHITE
                            tillFin = 7 - xt;

                            //last line
                            if (pawn.getX() == 7) {
                                ranks[i] += 1000;
                            }
                            //end last line

                        } else {
                            // BLACK
                            tillFin = xt - 1;

                            //last line
                            if (pawn.getX() == 0) {
                                ranks[i] += 1000;
                            }
                            //end last line
                        }

                        if (min == tillFin) {
                            ranks[i] += 5;
                            if (moves[i].isCapture()) {
                                ranks[i] += 2;
                            }
                        }

                        unappltoWholeBoard(moves[i]);

                    } else {
                        isOpPassed = false;
                        oppoistePawns = opponent.getAllPawns();

                        for (Square opPawn : oppoistePawns) {
                            if (isPassedPawn(opPawn, opposite)) {
                                isOpPassed = true;
                            }
                        }
                        if (isOpPassed) {
                            ranks[i] -= 100;
                        }
                        unappltoWholeBoard(moves[i]);

                        if (b.getSquare(xt, yt).occupiedBy() == opposite) {
                            // NORMAL CAPTURE
                            if (!guardedPawn(b.getSquare(xt, yt))) {
                                ranks[i] += 2;
                            }
                        } else if (b.getSquare(xt, yt).occupiedBy() == Color.NONE && moves[i].isCapture()) {
                            // EN PASSANT

                            // set none to opposite
                            b.getSquare(xt, yt).setOccupier(opposite);
                            if (!guardedPawn(b.getSquare(xt, yt))) {
                                ranks[i] += 2;
                            }

                            // set opposite to none
                            b.getSquare(xt, yt).setOccupier(Color.NONE);
                        } else if (b.getSquare(xt, yt).occupiedBy() == Color.NONE && !moves[i].isCapture()) {
                            // NORMAL MOVE
                        }
                    }
                }
                //end main for
                //end find passed pawns



                //NO EDIT AFTER THIS LINE RANKS END
                //find max and random rank max
                max = ranks[0];
                for (int rank : ranks) {
                    if (max < rank) {
                        max = rank;
                    }
                }

                for (int rank : ranks) {
                    if (max == rank) {
                        k++;
                    }
                }

                chosenMoves = new int[k];
                k = 0;
                for (int i = 0; i < ranks.length; i++) {
                    if (max == ranks[i]) {
                        chosenMoves[k] = i;
                        k++;
                    }
                }

                int random = rg.nextInt(k);

                moveNo = chosenMoves[random];

            }


            // NO EDIT -----------------------
            if (debug) {
                System.out.println("LOOKUP ^");
                System.out.println("LOOKUP |");
                for (int i = 0; i < moves.length; i++) {
                    String check = "";

                    if (moveNo == i) {
                        check = "x";
                    } else {
                        check = "";
                    }

                    System.out.println("MOVE " + c + ": from (" + (moves[i].getFrom().getX() + 1) + "," + (moves[i].getFrom().getY() + 1) + ") " +
                            "to (" + (moves[i].getTo().getX() + 1) + "," + (moves[i].getTo().getY() + 1) + ") isCapture: " + moves[i].isCapture() + " rank: " + ranks[i] + " " + check);
                }
                System.out.println(moves.length + " moves available.");
            }

            moveToMake = moves[moveNo];

            // END AI ALGORITHM


            // NO EDIT
            g.applyMove(moveToMake);
            b = g.b;
        }
    }

    private void applyToWholeBoard(Move move) {
        g.applyMove(move);
        b = g.b;
    }

    private void unappltoWholeBoard(Move move) {
        g.unapplyMove(move);
        b = g.b;
    }

    private int getMissing(Color playerColor){
        if (playerColor == Color.WHITE) {
            //white
            for (int j = 0; j < 8; j++) {
                if (b.getSquare(1, j).occupiedBy() == Color.NONE) {
                    return j;
                }
            }
        } else {
            //black
            for (int j = 0; j < 8; j++) {
                if (b.getSquare(6, j).occupiedBy() == Color.NONE) {
                    return j;
                }
            }
        }
        return 0;
    }
}
