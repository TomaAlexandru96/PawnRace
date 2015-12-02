import java.io.*;

public class Compute {
    private static FileOutputStream out;

    public static void main(String[] args) throws IOException {
        String fileName;
        for (char wGap = 'a'; wGap <= 'h'; wGap++) {
            for (char bGap = 'a'; bGap <= 'h'; bGap++) {
                String s1 = "" + wGap;
                String s2 = "" + bGap;
                fileName = s1 + s2 + ".txt";
                out = new FileOutputStream(fileName);
                String outputMessage = "";
                Player p1, p2;
                Board b = new Board(wGap, bGap);
                Game g = new Game(b);

                p1 = new Player(g, b, Color.WHITE, true, false);
                p2 = new Player(g, b, Color.BLACK, true, false);

                backtrack(p1, p2, 0);
            }
        }

        out.close();
    }

    private static Color backtrack(Player p1, Player p2, int k) {
        String output;
        Board newBoard;
        Game newGame;
        Move[] allMovesCurrent;
        //Player player1, player2,
        Player current;
        Color winPlayer;
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

                k++;
                winPlayer = backtrack(p1, p2, k);

                current.getGame().unapplyMove(allMovesCurrent[i]);

                newGame = current.getGame();
                newBoard = current.getGame().getBoard();

                p1.setGame(newGame, newBoard);
                p2.setGame(newGame, newBoard);

                if (winPlayer != Color.NONE && k >= 8) {
                    if (winPlayer == Color.WHITE) {
                        colorWin = "w";
                    } else if (winPlayer == Color.BLACK){
                        colorWin = "b";
                    }

                    output = colorWin + p1.getGap() + p2.getGap();

                    p1Pawns = p1.getAllPawns();
                    for (Square pawn : p1Pawns) {
                        output = output + "w" + pawn.getX() + pawn.getY();
                    }

                    p2Pawns = p2.getAllPawns();
                    for (Square pawn : p2Pawns) {
                        output = output + "b" + pawn.getX() + pawn.getY();
                    }

                    output = output + "m" + allMovesCurrent[i].getFrom().getX() + allMovesCurrent[i].getFrom().getY() +
                            allMovesCurrent[i].getTo().getX() + allMovesCurrent[i].getTo().getY() + (allMovesCurrent[i].isCapture() ? "t" : "f") + "\n";
                    write(output);
                }
            }
        } else {
            return current.getGame().getGameResult();
        }

        return Color.NONE;
    }

    private static void write(String output) {
        try {
            out.write(output.getBytes());
        } catch (Exception e){

        }
    }
}