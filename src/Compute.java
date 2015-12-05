import java.io.*;

public class Compute {
    private static FileOutputStream outBlack;
    private static FileOutputStream outWhite;

    public static void main(String[] args) throws IOException {
        String fileName;
        for (char wGap = 'a'; wGap <= 'h'; wGap++) {
            for (char bGap = 'a'; bGap <= 'h'; bGap++) {
                String s1 = "" + wGap;
                String s2 = "" + bGap;
                fileName = s1 + s2 + "_W.txt";
                outWhite = new FileOutputStream(fileName);

                fileName = s1 + s2 + "_B.txt";
                outBlack = new FileOutputStream(fileName);

                String outputMessage = "";
                Player p1, p2;
                Board b = new Board(wGap, bGap);
                Game g = new Game(b);

                p1 = new Player(g, b, Color.WHITE, true, false);
                p2 = new Player(g, b, Color.BLACK, true, false);
                p1.setOpponent(p2);
                p2.setOpponent(p1);

                Color[] cs = {Color.NONE};

                int[] start = {100000000};
                backtrack(p1, p2, 0, cs, start);
            }
        }

        outWhite.close();
        outBlack.close();
    }

    private static void backtrack(Player p1, Player p2, int k, Color[] winPlayer, int[] left) {
        String output;
        Board newBoard;
        Game newGame;
        Move[] allMovesCurrent;
        //Player player1, player2,
        Player current;
        Square[] p1Pawns, p2Pawns;
        String colorWin = "";

        //player1 = new Player(p1.getGame(), p1.getGame().getBoard(), p1.getColor(), true, false);
        //player2 = new Player(p2.getGame(), p2.getGame().getBoard(), p2.getColor(), true, false);

        current = p1.getGame().getCurrentPlayer() == Color.WHITE ? p1 : p2;

        if (!current.getGame().isFinished()) {
            allMovesCurrent = current.getAllValidMoves();

            for (int i = 0; i < allMovesCurrent.length; i++) {
                current.getGame().applyMove(allMovesCurrent[i]);

                newGame = current.getGame();
                newBoard = current.getGame().getBoard();

                p1.setGame(newGame, newBoard);
                p2.setGame(newGame, newBoard);

                if (checkBoard(allMovesCurrent[i], current.getColor())) {
                    winPlayer[0] = newBoard.getSquare(allMovesCurrent[i].getTo().getX(), allMovesCurrent[i].getTo().getY()).occupiedBy();
                    left[0] = k;

                    current.getGame().unapplyMove(allMovesCurrent[i]);

                    newGame = current.getGame();
                    newBoard = current.getGame().getBoard();

                    p1.setGame(newGame, newBoard);
                    p2.setGame(newGame, newBoard);
                } else {
                    k++;
                    backtrack(p1, p2, k, winPlayer, left);

                    //System.out.println(k + " " + left[0]);

                    current.getGame().unapplyMove(allMovesCurrent[i]);

                    newGame = current.getGame();
                    newBoard = current.getGame().getBoard();

                    p1.setGame(newGame, newBoard);
                    p2.setGame(newGame, newBoard);

                    if (winPlayer[0] == current.getColor()) {
                        p1Pawns = p1.getAllPawns();
                        output = "" + (left[0] - k);
                        for (Square pawn : p1Pawns) {
                            output = output + "w" + pawn.getX() + pawn.getY();
                        }

                        p2Pawns = p2.getAllPawns();
                        for (Square pawn : p2Pawns) {
                            output = output + "b" + pawn.getX() + pawn.getY();
                        }

                        output = output + "m" + allMovesCurrent[i].getFrom().getX() + allMovesCurrent[i].getFrom().getY() +
                                allMovesCurrent[i].getTo().getX() + allMovesCurrent[i].getTo().getY() + (allMovesCurrent[i].isCapture() ? "t" : "f") + "\n";

                        if (winPlayer[0] == Color.WHITE) {
                            writeWhite(output);
                            /*if (output.equals("0w44w46w47b21b22b23b25b56b67m4757f\n")) {
                                newBoard.display(Color.WHITE);
                                System.out.println("WHITE");
                            }*/
                        } else {
                            writeBlack(output);
                            /*if (output.equals("0w44w46w47b21b22b23b25b56b67m4757f\n")) {
                                newBoard.display(Color.WHITE);
                                System.out.println("BLACK");
                            }*/
                        }
                    }
                }
            }
        } else {
            winPlayer[0] = Color.NONE;
        }
    }

    private static void writeWhite(String output) {
        try {
            outWhite.write(output.getBytes());
        } catch (Exception e){

        }
    }

    private static void writeBlack(String output) {
        try {
            outBlack.write(output.getBytes());
        } catch (Exception e){

        }
    }

    //check the last 3 lines for pawns;
    private static boolean checkBoard(Move move, Color player) {
        if (player == Color.WHITE) {
            if (move.getTo().getX() == 6) {
                return true;
            }
        } else if (player == Color.BLACK) {
            if (move.getTo().getX() == 1) {
                return true;
            }
        }
        return false;
    }
}