import java.util.Scanner;
public class PawnRace {
    public static String input1, input2, input3, input4;
    public static Color p1c, p2c;
    public static char wgap, bgap;
    public static boolean p1comp, p2comp;

    public static void main(String[] args) {
        // menu
        System.out.println("Welcome to Pawn Race version 1.0");
        System.out.println();
        help();
        read1();
        // end menu


        /*
        // TEST ALL MOVES
        Move[] ms = p1.getAllValidMoves();

        for (int i = 0; i < ms.length; i++) {
            System.out.println("X FROM: " + ms[i].getFrom().getX() + " Y FROM: " + ms[i].getFrom().getY());
            System.out.println("X TO: " + ms[i].getTo().getX() + " Y TO: " + ms[i].getTo().getY());
            System.out.println(ms[i].isCapture());
            System.out.println();
        }
        System.out.println(ms.length);*/


        // GAME START
        startGame();
        // GAME END
    }

    public static void startGame() {
        Player p1, p2, current;
        Board board;
        Game game;

        Scanner scanner = new Scanner(System.in);
        String readString, input = "", inputMode;

        // NO EDIT

        // INITIALIZE GAME
        board = new Board(wgap, bgap);
        game  = new Game(board);
        p1    = new Player(game, board, p1c, p1comp);
        p2    = new Player(game, board, p2c, p2comp);
        p1.setOpponent(p2);
        p2.setOpponent(p1);
        // END INIT

        inputMode = input1;

        System.out.println("Game starts:");
        System.out.println();
        // END NO EDIT

        game : while (!game.isFinished()) {
            if (p1.getColor() == game.getCurrentPlayer()) {
                current = p1;
            } else {
                current = p2;
            }

            if (!current.isComputerPlayer()) {
                board.display(current.getColor());
            }

            if (inputMode.equals("3")) {
                board.display(Color.WHITE);

                // AI VS AI
                if (game.getCurrentPlayer() == Color.WHITE) {
                    if (current.g.k > 0) {
                        System.out.println("Last move was BLACK: " + current.g.getLastMove().getSAN());
                    }
                } else {
                    if (current.g.k > 0) {
                        System.out.println("Last move was WHITE: " + current.g.getLastMove().getSAN());
                    }
                }
                System.out.println("Press enter to continue. (Commands: exit, menu, undo, undo2, help still work)");
                readString = scanner.nextLine().toLowerCase();

                if (readString.equals("undo")) {
                    game.unapplyMove(game.getLastMove());
                    continue game;

                } else if (readString.equals("undo2")) {
                    game.unapplyMove(game.getLastMove());
                    game.unapplyMove(game.getLastMove());

                    p1.g = game;
                    p1.b = game.b;

                    p2.g = game;
                    p2.b = game.b;

                    continue game;
                } else if (readString.equals("exit")){
                    endGame();
                } else if (readString.equals("help")) {
                    help();

                    p1.g = game;
                    p1.b = game.b;

                    p2.g = game;
                    p2.b = game.b;

                    continue game;
                } else if (readString.equals("menu")) {
                    System.out.println("Are you sure you want to quit the game and return to the menu (y / n)? (WARNING: GAME WILL NOT BE SAVED)");

                    input = IOUtil.readString().toLowerCase();
                    while (!input.equals("y") && !input.equals("n")) {
                        System.out.println("Invalid input. Try again:");
                        input = IOUtil.readString().toLowerCase();
                    }

                    if (input.equals("y")) {
                        break game;
                    } else {
                        continue game;
                    }
                }
            }

            if (!current.isComputerPlayer()) {
                if (game.getCurrentPlayer() == Color.WHITE) {
                    if (current.g.k > 0) {
                        System.out.println("Last move was BLACK: " + current.g.getLastMove().getSAN());
                    }
                    System.out.println("It's white's turn to move:");
                } else {
                    if (current.g.k > 0) {
                        System.out.println("Last move was WHITE: " + current.g.getLastMove().getSAN());
                    }
                    System.out.println("It's black's turn to move:");
                }
                do {
                    input = IOUtil.readString();
                    /*
                    if (input.toLowerCase().equals("exit")) {
                        endGame();
                    }
                    if (input.toLowerCase().equals("undo") && game.k > 0) {
                        break;
                    }
                    if (input.toLowerCase().equals("undo2") && game.k > 1) {
                        break;
                    }
                    */
                    if (checkCommandGame(input, game)) {
                        break;
                    }
                } while (game.parseMove(input) == null);

                if (input.toLowerCase().equals("undo")) {
                    game.unapplyMove(game.getLastMove());

                } else if (input.toLowerCase().equals("undo2")) {
                    game.unapplyMove(game.getLastMove());
                    game.unapplyMove(game.getLastMove());
                } else if (input.toLowerCase().equals("help")) {
                    help();
                    continue game;
                } else if (input.toLowerCase().equals("menu")) {
                    System.out.println("Are you sure you want to quit the game and return to the menu (y / n)? (WARNING: GAME WILL NOT BE SAVED)");

                    input = IOUtil.readString().toLowerCase();
                    while (!input.equals("y") && !input.equals("n")) {
                        System.out.println("Invalid input. Try again:");
                        input = IOUtil.readString().toLowerCase();
                    }

                    if (input.equals("y")) {
                        break game;
                    } else {
                        continue game;
                    }
                } else {
                    game.applyMove(game.parseMove(input));
                }

                p1.g = game;
                p1.b = game.b;

                p2.g = game;
                p2.b = game.b;
            } else {
                current.makeMove();
                game  = current.g;
                board = current.b;

                p1.g = game;
                p1.b = game.b;

                p2.g = game;
                p2.b = game.b;
            }
        }

        // GAME END

        if (input.toLowerCase().equals("y")) {
            read1();
            startGame();
        } else {
            // DISPLAY RESULTS
            if (p1.getColor() == game.getCurrentPlayer()) {
                current = p1;
            } else {
                current = p2;
            }

            board.display(current.getColor());
            if (game.getGameResult() == Color.WHITE) {
                System.out.println("White has won!!!");
            } else if (game.getGameResult() == Color.BLACK) {
                System.out.println("Black has won!!!");
            } else {
                if (current.getColor() == Color.WHITE) {
                    System.out.println("Stale-Mate! White can't move anywhere. No one has won!");
                } else {
                    System.out.println("Stale-Mate! Black can't move anywhere. No one has won!");
                }
            }
            // END DISPLAY

            endGame();
        }
    }

    public static void endGame() {
        System.out.println("Thank you for playing my game (Copyright \u00A9 Toma Alexandru)");
        System.exit(0);
    }

    public static boolean isBetweenAH(String str) {
        char[] c = str.toLowerCase().toCharArray();

        if (str.length() != 1) {
            return false;
        }


        return c[0] >= 'a' && c[0] <= 'h';
    }

    public static void help() {
        System.out.println("The game accepts short form instructions as well as long form instructions (eg. a4, A5-a4, a6xb7, axB7).");
        System.out.println("Useful commands:");
        System.out.println("a) \"return\" (return to the previous option, only works before starting the game.)");
        System.out.println("b) \"undo\" (undo the last move, only works during the game, doesn't work in AI VS AI)");
        System.out.println("c) \"undo2\" (undo the last 2 moves, only works during the game, doesn't work in AI VS AI)");
        System.out.println("e) \"menu\" (return to initial menu, only works during the game, WARNING: GAME WILL NOT BE SAVED)");
        System.out.println("d) \"exit\" (exit the game, works any time)");
        System.out.println("e) \"help\" (displays this list of commands, works any time)");
        System.out.println();
    }

    public static void read1() {
        String input = "";

        System.out.println("Select your desired mode:");
        System.out.println("1) Player VS Player");
        System.out.println("2) Player VS AI");
        System.out.println("3) AI VS AI");

        input = IOUtil.readString().toLowerCase();
        while (!input.equals("1") && !input.equals("2") && !input.equals("3")){
            if (checkCommandMenu(input)) {
                break;
            }

            System.out.println("Input invalid. Try again:");
            input = IOUtil.readString().toLowerCase();
        }

        if (input.equals("return")) {
            read1();
        } else if (input.equals("help")) {
            help();
            read1();
        } else {
            input1 = input;
            read2();
        }
    }

    public static void read2() {
        String input = "";

        if (input1.equals("2")) {
            System.out.println("Select first player's color (w / b): ");
            input = IOUtil.readString().toLowerCase();
            while (!input.equals("w") && !input.equals("b")) {
                if (checkCommandMenu(input)) {
                    break;
                }

                System.out.println("Input invalid. Try again:");
                input = IOUtil.readString().toLowerCase();
            }
            if (input.equals("return")) {
                read1();
            } else if (input.equals("help")) {
                help();
                read2();
            } else {
                if (input.equals("w")) {
                    p1c = Color.WHITE;
                    p2c = Color.BLACK;
                } else {
                    p1c = Color.BLACK;
                    p2c = Color.WHITE;
                }
                input2 = input;
                read3();
            }
        } else {
            p1c = Color.WHITE;
            p2c = Color.BLACK;

            input2 = input;
            read3();
        }
    }

    public static void read3() {
        String input = "";

        System.out.println("Select white's gap and black's gap in this order (eg. W: a, B: h):");

        System.out.print("White's gap: ");
        input = IOUtil.readString().toLowerCase();
        System.out.println();

        while (!isBetweenAH(input)) {
            if (checkCommandMenu(input)) {
                break;
            }

            System.out.println("Input invalid. Try again:");
            System.out.print("White's gap: ");
            input = IOUtil.readString().toLowerCase();
            System.out.println();
        }

        if (input.equals("return")) {
            if (input1.equals("2")) {
                read2();
            } else {
                read1();
            }
        } else if (input.equals("help")) {
            help();
            read3();
        } else {
            input3 = input;
            read4();
        }
    }

    public static void read4() {
        String input = "";

        System.out.print("Black's gap: ");
        input = IOUtil.readString().toLowerCase();
        System.out.println();

        while (!isBetweenAH(input)) {
            if (checkCommandMenu(input)) {
                break;
            }

            System.out.println("Input invalid. Try again:");
            System.out.print("Black's gap: ");
            input = IOUtil.readString().toLowerCase();
            System.out.println();
        }

        if (input.equals("return")) {
            read3();
        } else if (input.equals("help")) {
            help();
            read4();
        } else {
            input4 = input;

            char[] cs2 = input3.toLowerCase().toCharArray(), cs3 = input4.toLowerCase().toCharArray();
            wgap = cs2[0];
            bgap = cs3[0];

            if (input1.equals("1")) {
                p1comp = false;
                p2comp = false;
            } else if (input1.equals("2")){
                p1comp = false;
                p2comp = true;
            } else {
                p1comp = true;
                p2comp = true;
            }
        }
    }

    public static boolean checkCommandMenu(String input) {
        input = input.toLowerCase();

        if (input.equals("exit")) {
            endGame();
        }
        if (input.equals("return") || input.equals("help")) {
            return true;
        }
        return false;
    }

    public static boolean checkCommandGame(String input, Game game) {
        input = input.toLowerCase();

        if (input.toLowerCase().equals("exit")) {
            endGame();
        }
        if ((input.equals("undo") && game.k > 0) || (input.equals("undo2") && game.k > 1) || input.equals("help") || input.equals("menu")) {
            return true;
        }
        return false;
    }
}