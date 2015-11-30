public class Game {
    public Board b;
    private Move[] ms;
    public int k;
    private Color currentPlayer;

    public Game(Board board) {
        b  = board;
        ms = new Move[1000];
        k  = 0;
        currentPlayer = Color.WHITE;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public Move getLastMove() {
        return ms[k];
    }

    public void applyMove(Move move) {
        if (currentPlayer == Color.WHITE) {
            currentPlayer = Color.BLACK;
        } else {
            currentPlayer = Color.WHITE;
        }

        k++;
        ms[k] = move;

        b.applyMove(move);
    }

    public void unapplyMove(Move move) {
        if (k > 0) {
            if (currentPlayer == Color.WHITE) {
                currentPlayer = Color.BLACK;
            } else {
                currentPlayer = Color.WHITE;
            }

            boolean enpassant = false;
            if (k > 1) {
                int xf, yf, xt, yt;

                xf = ms[k - 1].getFrom().getX();
                yf = ms[k - 1].getFrom().getY();
                xt = ms[k - 1].getTo().getX();
                yt = ms[k - 1].getTo().getY();

                if ((xf == 1 && xt == 3) || (xf == 6 && xt == 4)) {
                    b.unapplyMove(ms[k], true);
                } else {
                    b.unapplyMove(ms[k], false);
                }
            } else {
                b.unapplyMove(ms[k], false);
            }
            k--;
        }
    }

    public boolean isFinished() {
        if (k == 0) {
            return false;
        }
        if (ms[k].getTo().getX() == 0 || ms[k].getTo().getX() == 7) {
            return true;
        }

        Player randPlayer = new Player(this, b, currentPlayer, false);
        if (randPlayer.getAllValidMoves().length == 0) {
            return true;
        }

        return false;
    }

    public Color getGameResult() {

        if (ms[k].getTo().getX() == 0 || ms[k].getTo().getX() == 7) {
            if (currentPlayer == Color.WHITE) {
                return Color.BLACK;
            } else {
                return Color.WHITE;
            }
        }
        return Color.NONE;
    }

    public Move parseMove(String san) {
        char[] cSan = san.toCharArray();
        String colorPlayer, defError = "Invalid input format. Try again:";
        Move newMove = null;

        if (currentPlayer == Color.WHITE) {
            colorPlayer = "white";
        }
        else {
            colorPlayer = "black";
        }

        if (cSan.length == 2){
            // SHORT FORMAT NON-CAPTURE
            int x, y;
            int index;

            index = 0;
            if (cSan[index] >= 'A' && cSan[index] <= 'H') {
                y = cSan[index] - 'A';
            } else if (cSan[index] >= 'a' && cSan[index] <= 'h') {
                y = cSan[index] - 'a';
            } else {
                System.out.println(defError);
                return null;
            }

            index = 1;
            if (cSan[index] >= '1' && cSan[index] <= '8') {
                x = cSan[index] - '0';
                x--;
            } else {
                System.out.println(defError);
                return null;
            }

            int kk = 0;
            Move test;
            for (int i = 7; i >= 0; i--) {
                if (b.getSquare(i, y).occupiedBy() == currentPlayer) {
                    test = new Move(new Square(i, y), new Square(x, y), false);
                    if (isValid(test, currentPlayer)) {
                        newMove = test;
                    }
                    kk++;
                }
            }

            if (kk == 0) {
                System.out.println("No " + colorPlayer + " pawns have been found on column " + Character.toUpperCase(cSan[0]) + ".");
            } else if (newMove == null){
                System.out.println("Invalid move. Try again:");
            }
        } else if (cSan.length == 5 && cSan[2] == '-') {
            // LONG FORMAT NON-CAPTURE

            int xf, yf, xt, yt;
            int index;

            index = 0;
            if (cSan[index] >= 'A' && cSan[index] <= 'H') {
                yf = cSan[index] - 'A';
            } else if (cSan[index] >= 'a' && cSan[index] <= 'h') {
                yf = cSan[index] - 'a';
            } else {
                System.out.println(defError);
                return null;
            }

            index = 1;
            if (cSan[index] >= '1' && cSan[index] <= '8') {
                xf = cSan[index] - '0';
                xf--;
            } else {
                System.out.println(defError);
                return null;
            }

            index = 3;
            if (cSan[index] >= 'A' && cSan[index] <= 'H') {
                yt = cSan[index] - 'A';
            } else if (cSan[index] >= 'a' && cSan[index] <= 'h') {
                yt = cSan[index] - 'a';
            } else {
                System.out.println(defError);
                return null;
            }

            index = 4;
            if (cSan[index] >= '1' && cSan[index] <= '8') {
                xt = cSan[index] - '0';
                xt--;
            } else {
                System.out.println(defError);
                return null;
            }

            if (currentPlayer == Color.WHITE) {
                if (b.getSquare(xf, yf).occupiedBy() != Color.WHITE) {
                    System.out.println("The square " + Character.toUpperCase(cSan[0]) + cSan[1] + " isn't occupied by a " + colorPlayer + " pawn.");
                    return null;
                }
                if (b.getSquare(xt, yt).occupiedBy() == Color.BLACK) {
                    System.out.println("If you want to capture a piece you need to swap character \'-\' with \'x\'.");
                    return null;
                }
            } else {
                if (b.getSquare(xf, yf).occupiedBy() != Color.BLACK) {
                    System.out.println("The square " + Character.toUpperCase(cSan[0]) + cSan[1] + " isn't occupied by a " + colorPlayer + " pawn.");
                    return null;
                }
                if (b.getSquare(xt, yt).occupiedBy() == Color.WHITE) {
                    System.out.println("If you want to capture a piece you need to swap character \'-\' with \'x\'.");
                    return null;
                }
            }

            newMove = new Move(new Square(xf, yf), new Square(xt, yt), false);

        } else if (cSan.length == 4 && (cSan[1] == 'x' || cSan[1] == 'X')) {
            // SHORT FORMAT CAPTURE
            int xf, yf, xt, yt, position = 0;
            int index;

            index = 0;
            if (cSan[index] >= 'A' && cSan[index] <= 'H') {
                yf = cSan[index] - 'A';
            } else if (cSan[index] >= 'a' && cSan[index] <= 'h') {
                yf = cSan[index] - 'a';
            } else {
                System.out.println(defError);
                return null;
            }

            index = 2;
            if (cSan[index] >= 'A' && cSan[index] <= 'H') {
                yt = cSan[index] - 'A';
            } else if (cSan[index] >= 'a' && cSan[index] <= 'h') {
                yt = cSan[index] - 'a';
            } else {
                System.out.println(defError);
                return null;
            }

            index = 3;
            if (cSan[index] >= '1' && cSan[index] <= '8') {
                xt = cSan[index] - '0';
                xt--;
            } else {
                System.out.println(defError);
                return null;
            }

            if (currentPlayer == Color.WHITE){
                xf = xt - 1;
            } else {
                xf = xt + 1;
            }

            newMove = new Move(new Square(xf, yf), new Square(xt, yt), true);
        } else if (cSan.length == 5 && (cSan[1] == 'x' || cSan[1] == 'X')){
            //LONG FORMAT CAPTURE

            int xf, yf, xt, yt;
            int index;

            index = 0;
            if (cSan[index] >= 'A' && cSan[index] <= 'H') {
                yf = cSan[index] - 'A';
            } else if (cSan[index] >= 'a' && cSan[index] <= 'h') {
                yf = cSan[index] - 'a';
            } else {
                System.out.println(defError);
                return null;
            }

            index = 1;
            if (cSan[index] >= '1' && cSan[index] <= '8') {
                xf = cSan[index] - '0';
                xf--;
            } else {
                System.out.println(defError);
                return null;
            }

            index = 3;
            if (cSan[index] >= 'A' && cSan[index] <= 'H') {
                yt = cSan[index] - 'A';
            } else if (cSan[index] >= 'a' && cSan[index] <= 'h') {
                yt = cSan[index] - 'a';
            } else {
                System.out.println(defError);
                return null;
            }

            index = 4;
            if (cSan[index] >= '1' && cSan[index] <= '8') {
                xt = cSan[index] - '0';
                xt--;
            } else {
                System.out.println(defError);
                return null;
            }

            newMove = new Move(new Square(xf, yf), new Square(xt, yt), true);
        } else {
            System.out.println(defError);
        }

        if (newMove != null) {
            if (isValid(newMove, currentPlayer)) {
                return newMove;
            } else {
                System.out.println("Invalid move. Try again:");
                return null;
            }
        }

        return null;
    }

    public boolean isValid(Move m, Color player){
        int xf, yf, xt, yt;
        xf = m.getFrom().getX();
        yf = m.getFrom().getY();
        xt = m.getTo().getX();
        yt = m.getTo().getY();

        if (b.getSquare(xf, yf).occupiedBy() == player) {
           // System.out.println("if (b.getSquare(xf, yf).occupiedBy() == player)");
            if (player == Color.WHITE) {
               // System.out.println("player == Color.WHITE");
                if (b.getSquare(xt, yt).occupiedBy() == Color.NONE) {
                    //System.out.println("b.getSquare(xt, yt).occupiedBy() == Color.NONE");
                    //NON-CAPTURE
                    if (!m.isCapture()) {
                        if (m.getFrom().getY() == m.getTo().getY()) {
                            if (m.getTo().getX() - m.getFrom().getX() == 1) {
                                return true;
                            }
                            if (m.getFrom().getX() == 1 && m.getTo().getX() == 3 && b.getSquare(2, m.getTo().getY()).occupiedBy() == Color.NONE) {
                                return true;
                            }
                        }
                    } else {
                       // System.out.println("ENPASSANT");
                        //ENPASSANT
                        if (k > 1) {
                            if (ms[k].getFrom().getX() == 6 && ms[k].getTo().getX() == 4 && ms[k].getTo().getY() == yt && ms[k].getFrom().getY() == yt) {
                                //System.out.println("Debug: 1 if");
                                if (b.getSquare(xt, yt).occupiedBy() == Color.NONE) {
                                    /*System.out.println("Debug: 2 if");
                                    System.out.println("yt = " + yt + " yf = "+ yf);
                                    System.out.println("xf = " + xf + " xt = "+ xt);*/
                                    if (Math.abs(yt - yf) == 1 && xf == xt - 1) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                } else if (b.getSquare(xt, yt).occupiedBy() == Color.BLACK && m.isCapture()) {
                    if (Math.abs(yt - yf) == 1 && xf == xt - 1) {
                        return true;
                    }
                }
            } else {
                //System.out.println("player == Color.BLACK");
                if (b.getSquare(xt, yt).occupiedBy() == Color.NONE) {
                    //System.out.println("b.getSquare(xt, yt).occupiedBy() == Color.NONE");
                    //NON-CAPTURE
                    if (!m.isCapture()) {
                        if (m.getFrom().getY() == m.getTo().getY()) {
                            if (m.getFrom().getX() - m.getTo().getX() == 1) {
                                return true;
                            }
                            if (m.getFrom().getX() == 6 && m.getTo().getX() == 4 && b.getSquare(5, m.getTo().getY()).occupiedBy() == Color.NONE) {
                                return true;
                            }
                        }
                    } else {
                        //System.out.println("ENPASSANT");
                        //ENPASANT
                        if (k > 1) {
                            if (ms[k].getFrom().getX() == 1 && ms[k].getTo().getX() == 3 && ms[k].getTo().getY() == yt && ms[k].getFrom().getY() == yt) {
                                //System.out.println("Debug: 1 if");
                                if (b.getSquare(xt, yt).occupiedBy() == Color.NONE) {
                                    /*System.out.println("Debug: 2 if");
                                    System.out.println("yt = " + yt + " yf = "+ yf);
                                    System.out.println("xf = " + xf + " xt = "+ xt);*/
                                    if (Math.abs(yt - yf) == 1 && xf == xt + 1) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                } else if (b.getSquare(xt, yt).occupiedBy() == Color.WHITE && m.isCapture()) {
                    if (Math.abs(yt - yf) == 1 && xf == xt + 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}