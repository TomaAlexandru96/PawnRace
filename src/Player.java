import java.util.Random;

public class Player {
    private Game g;
    private Board b;
    private Color c;
    private boolean isComputer;
    private Player opponent;
    private boolean debug;
    private int gap;
    private int opponentGap;

    public Player(Game game, Board board, Color color, boolean isComputerPlayer, boolean debug) {
        g = game;
        b = board;
        c = color;
        isComputer = isComputerPlayer;
        this.debug = debug;
        gap = getMissing(c);
    }

    public int getGap() {
        return gap;
    }

    public void setGame(Game game, Board board) {
        this.g = game;
        this.b = board;
    }

    public Game getGame() {
        return g;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
        opponentGap = getMissing(opponent.getColor());
    }

    public Player getOpponent() {
        return opponent;
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

        if (square.occupiedBy() == Color.WHITE) {
            if (x != 0) {
                if (y != 0) {
                    if (b.getSquare(x - 1, y - 1).occupiedBy() == Color.WHITE) {
                        isGuarded = true;
                    }
                    //System.out.println("1 : "+ isGuarded + "("+x+","+y+") color: " + b.getSquare(x - 1, y - 1).occupiedBy());
                }
                if (y != 7) {
                    if (b.getSquare(x - 1, y + 1).occupiedBy() == Color.WHITE) {
                        isGuarded = true;
                    }
                }
            }
        } else if (square.occupiedBy() == Color.BLACK) {
            if (x != 7) {
                if (y != 0) {
                    if (b.getSquare(x + 1, y - 1).occupiedBy() == Color.BLACK) {
                        isGuarded = true;
                    }
                }
                if (y != 7) {
                    if (b.getSquare(x + 1, y + 1).occupiedBy() == Color.BLACK) {
                        isGuarded = true;
                    }
                }
            }
        } else {
            isGuarded = false;
        }

        //System.out.println("Final : " + isGuarded);
        return isGuarded;
    }

    public void makeMove() {
        if (isComputer) {
            Random rg      = new Random();
            Move[] moves   = getAllValidMoves();
            Move moveToMake;
            int moveNo = 0;
            int n = moves.length;
            int[] ranks = new int[n];
            int[] chosenMoves;
            int k = 0;
            int min, max;
            int xt, yt, xf, yf;
            char gp, go;
            // END NO EDIT


            // AI ALGORITHM ------------------

            //init ranks
            for (int rank : ranks) {
                rank = 0;
            }

            if (g.getTotalMoves() == 0 || g.getTotalMoves() == 1) {
                // first move
                int moveColumn;
                int moveRow;

                //System.out.println(gap + " "+opponentGap);

                if (gap == 0) {
                    for (int q = 0; q < moves.length; q++) {
                        moveColumn = moves[q].getFrom().getY();
                        moveRow    = moves[q].getTo().getX();
                        if (moveColumn == 2 && (moveRow == 2 || moveRow == 5)) {
                            ranks[q] = 1;
                        }
                    }
                } else if (gap == 7) {
                    for (int q = 0; q < moves.length; q++) {
                        moveColumn = moves[q].getFrom().getY();
                        moveRow    = moves[q].getTo().getX();
                        if (moveColumn == 5 && (moveRow == 2 || moveRow == 5)) {
                            ranks[q] = 1;
                        }
                    }
                } else if (gap == 1 || gap == 6) {
                    // gap at B or G

                    // move other than gap
                    for (int q = 0; q < moves.length; q++) {
                        moveColumn = moves[q].getFrom().getY();

                        //System.out.println(moveColumn + " gap: "+ gap);

                        if (moveColumn != 0 && moveColumn != 7) {
                            ranks[q] = 1;
                        }
                    }
                } else if (gap == 2 || gap == 5) {
                    // gap at C or F
                    for (int q = 0; q < moves.length; q++) {
                        moveColumn = moves[q].getFrom().getY();
                        moveRow    = moves[q].getTo().getX();

                        if (gap == 2) {
                            if (moveColumn == 1 || moveColumn == 0) {
                                if (moveRow == 2 || moveRow == 5) {
                                    ranks[q] = 1;
                                }
                            }
                        } else {
                            if (moveColumn == 6 || moveColumn == 7) {
                                if (moveRow == 2 || moveRow == 5) {
                                    ranks[q] = 1;
                                }
                            }
                        }
                    }
                } else if (gap == 3) {
                    // gap at D
                    for (int q = 0; q < moves.length; q++) {
                        moveColumn = moves[q].getFrom().getY();
                        moveRow    = moves[q].getTo().getX();

                        if (moveColumn == 1 && (moveRow == 2 || moveRow == 5)) {
                            ranks[q] = 1;
                        }
                    }
                } else if (gap == 4) {
                    for (int q = 0; q < moves.length; q++) {
                        moveColumn = moves[q].getFrom().getY();
                        moveRow    = moves[q].getTo().getX();

                        if (moveColumn == 6 && (moveRow == 2 || moveRow == 5)) {
                            ranks[q] = 1;
                        }
                    }
                }

            } else {
                /*
                Move preMove = null;

                gp = (char) (gap + 'a');
                go = (char) (opponentGap + 'a');

                //System.out.println("" + (gp) + (go) + "_"+ (c == Color.WHITE ? "W" : "B"));
                try {
                    preMove = TestFile.getMove("" + (gp) + (go) + "_"+ (c == Color.WHITE ? "W" : "B") + ".txt", b);

                    System.out.println(preMove);

                    if (preMove != null) {
                        // 500
                        boolean found = false;
                        for (int q = 0; q < moves.length; q++) {
                            if (equalMoves(preMove, moves[q])) {
                                found = true;
                                ranks[q] += 500;
                            }
                        }

                        if (!found) {
                            ranks = getRanks(ranks, moves);
                        }
                    } else {
                        ranks = getRanks(ranks, moves);
                    }
                } catch (Exception e) {
                    ranks = getRanks(ranks, moves);
                }
                */
            }

            //---------------------------------
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
            applyToWholeBoard(moveToMake);
        }
    }

    private void applyToWholeBoard(Move move) {
        g.applyMove(move);
        b = g.getBoard();
    }

    private void unapplytoWholeBoard(Move move) {
        g.unapplyMove(move);
        b = g.getBoard();
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

    private boolean isAttackedAndNotEnP(Square p) {
        Color op;
        int x, y;
        x = p.getX();
        y = p.getY();

        //System.out.println("("+x+","+y+") color: " + p.occupiedBy());

        if (p.occupiedBy() == Color.WHITE) {
            op = Color.BLACK;
            if (x != 7 && y != 0) {
                if (b.getSquare(x + 1, y - 1).occupiedBy() == op) {
                    return true;
                }
            }
            if (x != 7 && y != 7) {
                if (b.getSquare(x + 1, y + 1).occupiedBy() == op) {
                    return true;
                }
            }
        } else if (p.occupiedBy() == Color.BLACK){
            op = Color.WHITE;

            if (x != 0 && y != 0) {
                if (b.getSquare(x - 1, y - 1).occupiedBy() == op) {
                    return true;
                }
            }

            if (x != 0 && y != 7) {
                if (b.getSquare(x - 1, y + 1).occupiedBy() == op) {
                    return true;
                }
            }
        }

        return false;
    }

    private int[] getRanks(int[] RANKS, Move[] moves){
        //copy matrix
        int[] ranks = new int[RANKS.length];

        for (int q = 0; q < RANKS.length; q++) {
            ranks[q] = RANKS[q];
        }

        //points allocation
        int[] p = new int[100];


        // TODO IN CASE OF gap == gap opponent and 3 pices on one side AI can be beaten
        // TODO IN CASE OF move and can be attacked(en passant included) after -20 (if after this move and ispassed pawn +8);
        // TODO IF MAKE passed pawn that is better than oders +15
        // if move and make opponent have passedPawn and current no passedPawn -100
        p[0] = -100;
        // if move and make opponent have passedPawn and current has passedPawn but opponent is closer -50
        p[5] = -49;
        // if gapsW == gapsB guard week side and -1 after
        p[7] = -1;
        // guard pawn = 3
        p[1] = 3;
        // capture unguarded pawn = 4
        p[2] = 4;
        // if pawn is attacked and unguarded = 5 && attack isn't en passant
        p[6] = 5;
        // isPassedPawn = 10
        p[3] = 10;
        // isPassedPawn and capture = 12
        // p[2] again
        // last line = 1000
        p[4] = 1000;


        int min, max;
        Square[] oppoistePawns;
        Square[] myPawns, myNewPawns;
        Square pawn;
        Color opposite;
        int xt, yt, xf, yf;

        opposite = opponent.getColor();

        //find passed pawns
        min = 10;
        int tillFin = 0;
        int column;
        int row;

        for (int i = 0; i < moves.length; i++) {
            if (gap == opponentGap) {
                column = moves[i].getTo().getY();
                row = moves[i].getTo().getX();

                if (gap != 0 && gap != 7) {
                    if (gap >= 4) {
                        // gap >=4
                        for (int q = gap + 1; q < 8; q++) {
                            if (column == q) {
                                ranks[i] += p[7];
                            }
                        }
                    } else {
                        // gap < 4
                        for (int q = gap - 1; q >= 0; q--) {
                            if (column == q) {
                                ranks[i] += p[7];
                            }
                        }
                    }
                }
            }
            //              |
            //   APPLY MOVE |
            //              V

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

            unapplytoWholeBoard(moves[i]);

            //              ^
            // UNAPPLY MOVE |
            //              |

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
            myPawns = getAllPawns();
            for (int j = 0; j < myPawns.length; j++) {
                if (myPawns[j].getX() != 1 && myPawns[j].getX() != 6) {
                    if (!guardedPawn(myPawns[j])) {
                        int x, y;
                        x = myPawns[j].getX();
                        y = myPawns[j].getY();

                        //              |
                        //   APPLY MOVE |
                        //              V

                        applyToWholeBoard(moves[i]);

                        if (guardedPawn(b.getSquare(x, y))) {
                            ranks[i] += p[1]; // 1
                        }
                        unapplytoWholeBoard(moves[i]);

                        //              ^
                        // UNAPPLY MOVE |
                        //              |

                    }
                }
            }

            // if pawn is attacked and unguarded && attack is not en passant
            myPawns = getAllPawns();
            for (Square thePawn : myPawns) {
               if (isAttackedAndNotEnP(thePawn) && !guardedPawn(thePawn)) {
                   // if attacked and not guarded

                   //              |
                   //   APPLY MOVE |
                   //              V

                   applyToWholeBoard(moves[i]);

                   if (moves[i].getFrom().getX() == thePawn.getX() && moves[i].getFrom().getY() == thePawn.getY()) {
                       // if moved the same pawn
                       if (!isAttackedAndNotEnP(b.getSquare(moves[i].getTo().getX(), moves[i].getTo().getY()))) {
                            // if is not attacked anymore
                           ranks[i] += p[6]; //3
                       }
                   } else {
                       if (guardedPawn(thePawn)) {
                           // if is guarded
                           ranks[i] += p[6]; //3
                       }
                   }

                   unapplytoWholeBoard(moves[i]);

                   //              ^
                   // UNAPPLY MOVE |
                   //              |
               }
            }

            //              |
            //   APPLY MOVE |
            //              V

            applyToWholeBoard(moves[i]);

            pawn = b.getSquare(xt, yt);

            if (c == Color.WHITE) {
                //last line
                if (pawn.getX() == 7) {
                    ranks[i] += p[4]; // 1000
                }
                //end last line
            } else {
                //last line
                if (pawn.getX() == 0) {
                    ranks[i] += p[4]; // 1000
                }
                //end last line
            }

            if (isPassedPawn(pawn, c)) {
                if (c == Color.WHITE) {
                    //WHITE
                    tillFin = 7 - xt;
                } else {
                    // BLACK
                    tillFin = xt - 1;
                }

                //if my passed pawn is further than the opponent -49
                int tillFinOp;
                oppoistePawns = opponent.getAllPawns();
                for (int q = 0; q < oppoistePawns.length; q++) {
                    if (isPassedPawn(oppoistePawns[q], opposite)) {
                        if (opposite == Color.WHITE) {
                            //WHITE

                            tillFinOp = 7 - oppoistePawns[q].getX();
                        } else {
                            // BLACK

                            tillFinOp = oppoistePawns[q].getX() - 1;
                        }

                        if (tillFinOp <= tillFin) {
                            ranks[i] += p[5]; // -49
                        }
                    }
                }

                if (min == tillFin) {
                    ranks[i] += p[3]; // 5
                    if (moves[i].isCapture()) {
                        ranks[i] += p[2]; // 2
                    }
                }

                unapplytoWholeBoard(moves[i]);

                //              ^
                // UNAPPLY MOVE |
                //              |

            } else {
                //isOpPassed = false;
                oppoistePawns = opponent.getAllPawns();

                for (Square opPawn : oppoistePawns) {
                    if (isPassedPawn(opPawn, opposite)) {
                        //isOpPassed = true;
                        ranks[i] += p[0]; // -100
                    }
                }
                /*if (isOpPassed) {
                    ranks[i] += p[0]; // -100
                }*/
                unapplytoWholeBoard(moves[i]);

                //              ^
                // UNAPPLY MOVE |
                //              |

                if (b.getSquare(xt, yt).occupiedBy() == opposite) {
                    // NORMAL CAPTURE
                    if (!guardedPawn(b.getSquare(xt, yt))) {
                        //System.out.println("Pawn NC ("+(xt+1)+","+(yt+1)+") is unguarded color: "+b.getSquare(xt, yt).occupiedBy());
                        ranks[i] += p[2]; // 2
                    }
                } else if (b.getSquare(xt, yt).occupiedBy() == Color.NONE && moves[i].isCapture()) {
                    // EN PASSANT

                    // set none to opposite
                    b.getSquare(xt, yt).setOccupier(opposite);
                    if (!guardedPawn(b.getSquare(xt, yt))) {
                        //System.out.println("Pawn E-PC ("+(xt+1)+","+(yt+1)+") is unguarded color: "+b.getSquare(xt, yt).occupiedBy());
                        ranks[i] += p[2]; // 2
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

        return ranks;
    }

    public boolean equalMoves(Move m1, Move m2) {
        if (m1.getTo().getX() != m2.getTo().getX()) {
            return false;
        }

        if (m1.getTo().getY() != m2.getTo().getY()) {
            return false;
        }

        if (m1.getFrom().getX() != m2.getFrom().getX()) {
            return false;
        }

        if (m1.getFrom().getY() != m2.getFrom().getY()) {
            return false;
        }

        if (m1.isCapture() != m2.isCapture()) {
            return false;
        }

        return true;
    }
}
