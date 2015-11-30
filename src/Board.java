import java.awt.*;

public class Board {
    private Square[][] b;
    private String emptyW, emptyB, white, black, vLine, hLine, c1, c2 ,c3 ,c4;

    public Board(char whiteGap, char blackGap) {
        emptyB = ".";
        emptyW = ".";
        white  = "\u265F"; // ♟
        black  = "\u2659"; // ♙
        vLine  = "\u2502"; // │
        hLine  = "\u2500"; // ─
        c1     = "\u250C"; // ┌
        c2     = "\u2510"; // ┐
        c3     = "\u2514"; // └
        c4     = "\u2518"; // ┘
        //don't edit after this line

        b = new Square[8][8];

        //reset
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                b[i][j] = new Square(i, j);
            }
        }

        //set white and black
        for (int j = 0; j < b[1].length; j++) {
            b[1][j].setOccupier(Color.WHITE);
            b[6][j].setOccupier(Color.BLACK);
        }
        b[1][whiteGap - 'a'].setOccupier(Color.NONE);
        b[6][blackGap - 'a'].setOccupier(Color.NONE);
    }

    public Square getSquare(int x, int y) {
        return b[x][y];
    }

    public void applyMove(Move move) {
        int xf, yf, xt, yt;

        xf = move.getFrom().getX();
        yf = move.getFrom().getY();
        xt = move.getTo().getX();
        yt = move.getTo().getY();

        if (move.isCapture() && b[xt][yt].occupiedBy() == Color.NONE) {
            b[xf][yt].setOccupier(Color.NONE);
        }

        //to
        b[xt][yt].setOccupier(b[xf][yf].occupiedBy());
        //from
        b[xf][yf].setOccupier(Color.NONE);
    }

    public void unapplyMove(Move move, boolean enpassant) {
        int xf, yf, xt, yt;
        Color current;

        xf = move.getFrom().getX();
        yf = move.getFrom().getY();
        xt = move.getTo().getX();
        yt = move.getTo().getY();

        current = b[xt][yt].occupiedBy();

        if (move.isCapture()) {
            b[xf][yf].setOccupier(b[xt][yt].occupiedBy());
            if (enpassant) {
                b[xt][yt].setOccupier(Color.NONE);
                if (current == Color.WHITE) {
                    b[xf][yt].setOccupier(Color.BLACK);
                } else {
                    b[xf][yt].setOccupier(Color.WHITE);
                }
            } else {
                if (current == Color.WHITE) {
                    b[xt][yt].setOccupier(Color.BLACK);
                } else {
                    b[xt][yt].setOccupier(Color.WHITE);
                }
            }
        } else {
            b[xf][yf].setOccupier(b[xt][yt].occupiedBy());
            b[xt][yt].setOccupier(Color.NONE);
        }
    }

    public void display(Color color) {
        String emptyColor = emptyB;

        //head
        if (color == Color.BLACK) {
            System.out.println("        WHITE");
        } else {
            System.out.println("        BLACK");
        }
        System.out.println("   A B C D E F G H   ");
        System.out.print(" " + c1);
        for (int i = 0; i < 17; i++) {
            System.out.print(hLine);
        }
        System.out.println(c2 + "  ");

        //body
        if (color == Color.WHITE) {
            for (int i = 7; i >= 0; i--) {
                System.out.print((i + 1) + vLine + " ");
                for (int j = 0; j < 8; j++) {
                    if (b[i][j].occupiedBy() == Color.NONE) {
                        if (i % 2 != 0) {
                            //white
                            if (j % 2 == 0) {
                                emptyColor = emptyB;
                            } else {
                                emptyColor = emptyW;
                            }
                        } else {
                            //black
                            if (j % 2 == 0) {
                                emptyColor = emptyW;
                            } else {
                                emptyColor = emptyB;
                            }
                        }
                        System.out.print(emptyColor);
                    }
                    if (b[i][j].occupiedBy() == Color.WHITE) {
                        System.out.print(white);
                    }
                    if (b[i][j].occupiedBy() == Color.BLACK) {
                        System.out.print(black);
                    }
                    System.out.print(" ");
                }
                System.out.println(vLine + (i + 1));
            }
        } else {
            for (int i = 0; i < 8; i++) {
                System.out.print((i + 1) + vLine + " ");
                for (int j = 0; j < 8; j++) {
                    if (b[i][j].occupiedBy() == Color.NONE) {
                        if (i % 2 != 0) {
                            //white
                            if (j % 2 == 0) {
                                emptyColor = emptyB;
                            } else {
                                emptyColor = emptyW;
                            }
                        } else {
                            //black
                            if (j % 2 == 0) {
                                emptyColor = emptyW;
                            } else {
                                emptyColor = emptyB;
                            }
                        }
                        System.out.print(emptyColor);
                    }
                    if (b[i][j].occupiedBy() == Color.WHITE) {
                        System.out.print(white);
                    }
                    if (b[i][j].occupiedBy() == Color.BLACK) {
                        System.out.print(black);
                    }
                    System.out.print(" ");
                }
                System.out.println(vLine + (i + 1));
            }
        }
        //footer
        System.out.print(" " + c3);
        for (int i = 0; i < 17; i++) {
            System.out.print(hLine);
        }
        System.out.println(c4 + "  ");
        System.out.println("   A B C D E F G H   ");
        if (color == Color.BLACK) {
            System.out.println("        BLACK");
        } else {
            System.out.println("        WHITE");
        }
        System.out.println();
    }
}