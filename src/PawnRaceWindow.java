import javax.swing.*;
import javax.swing.colorchooser.ColorChooserComponentFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PawnRaceWindow {
    private static JButton[][] tiles = new JButton[8][8];
    private static JLabel[][] pawns = new JLabel[8][8];
    private static ImageIcon black = new ImageIcon("black.png");
    private static ImageIcon blue = new ImageIcon("blue.png");
    private static ImageIcon darkBlue = new ImageIcon("darkBlue.png");
    private static ImageIcon darkGreen = new ImageIcon("darkGreen.png");
    private static ImageIcon darkRed = new ImageIcon("darkRed.png");
    private static ImageIcon green = new ImageIcon("green.png");
    private static ImageIcon white = new ImageIcon("white.png");
    private static ImageIcon red = new ImageIcon("red.png");
    private static ImageIcon whitePawn = new ImageIcon("whitePawn.png");
    private static ImageIcon blackPawn = new ImageIcon("blackPawn.png");
    private static ImageIcon[][] defaultImage = new ImageIcon[10][10];
    private static JLabel lastMove;
    private static JLabel turn;
    private static JButton nextMove;
    private static JPanel menuPanel = new JPanel();
    private static JPanel gamePanel = new JPanel();

    private static boolean nextMovePushed = false;
    private static boolean isSelectedFrom = false, isSelectedTo = false;
    private static boolean[][] select = new boolean[10][10];
    private static int selectXFrom = 0, selectYFrom = 0, selectXTo = 0, selectYTo = 0;
    private static Board board;
    private static Game game = new Game(board);
    private static int mode;
    private static char gapW, gapB;
    private static Color color;
    private static Player p1, p2;
    private static Move[] allMoves;
    private static boolean run = false;
    private static boolean oneTime = true;
    private static boolean returnToMenu = false;

    public static void main(String[] args) {
        createWindow();
        while (true) {
            try {
                Thread.sleep(10);
            }
            catch (InterruptedException ie) {
                // Handle the exception
            }

            if (run) {
                if (oneTime) {
                    runGame();
                } else {
                    oneTime = false;
                }
            }
        }
    }

    private static void runGame() {
        boolean p1c, p2c, valid = false;
        //int selectX = 0, selectY = 0;
        int xf = 0, yf = 0, xt = 0, yt = 0;
        Color opponentColor;
        Move currentMove;

        /*
        // set default
        mode = 1;
        gapW = 'a';
        gapB = 'h';
        color = Color.WHITE;
        // set default
        */

        // init game
        if (mode == 1) {
            p1c = false;
            p2c = false;
            color = Color.WHITE;
        } else if (mode == 2) {
            p1c = false;
            p2c = true;
        } else {
            p1c = true;
            p2c = true;
            color = Color.WHITE;
            nextMove.setVisible(true);
        }

        opponentColor = color == Color.WHITE ? Color.BLACK : Color.WHITE;

        board = new Board(gapW, gapB);
        game = new Game(board);
        p1 = new Player(game, board, color, p1c, false);
        p2 = new Player(game, board, opponentColor, p2c, false);

        p1.setOpponent(p2);
        p2.setOpponent(p1);

        placePawns();
        // end init

        Player current;
        current = new Player(game, board, opponentColor, p1c, false);

        while (!game.isFinished() && !returnToMenu) {
            current = p1.getColor() == game.getCurrentPlayer() ? p1 : p2;

            try {
                Thread.sleep(10);
            }
            catch (InterruptedException ie) {
                // Handle the exception
            }

            if (!current.isComputerPlayer()) {
                if (isSelectedFrom) {
                    xf = selectXFrom;
                    yf = selectYFrom;

                    if (defaultImage[xf][yf] == white) {
                        tiles[xf][yf].setIcon(blue);
                    } else {
                        tiles[xf][yf].setIcon(darkBlue);
                    }

                    allMoves = current.getAllValidMoves();

                    for (Move move : allMoves) {
                        if (move.getFrom().getX() == xf && move.getFrom().getY() == yf) {
                            if (move.isCapture()) {
                                if (defaultImage[move.getTo().getX()][move.getTo().getY()] == white) {
                                    tiles[move.getTo().getX()][move.getTo().getY()].setIcon(red);
                                } else {
                                    tiles[move.getTo().getX()][move.getTo().getY()].setIcon(darkRed);
                                }
                            } else {
                                if (defaultImage[move.getTo().getX()][move.getTo().getY()] == white) {
                                    tiles[move.getTo().getX()][move.getTo().getY()].setIcon(green);
                                } else {
                                    tiles[move.getTo().getX()][move.getTo().getY()].setIcon(darkGreen);
                                }
                            }
                        }
                    }

                    if (isSelectedTo) {
                        valid = false;

                        xt = selectXTo;
                        yt = selectYTo;

                        currentMove = new Move(new Square(xf, yf), new Square(xt, yt), false);

                        if (game.isValid(currentMove, current.getColor())) {
                            valid = true;
                        } else {
                            currentMove = new Move(new Square(xf, yf), new Square(xt, yt), true);
                            if (game.isValid(currentMove, current.getColor())) {
                                valid = true;
                            } else {
                                isSelectedTo = false;
                            }
                        }

                        if (valid) {
                            applyMove(currentMove);
                        }
                    }
                }
            } else {
                if (mode == 2) {
                    current.makeMove();
                    game = current.getGame();
                    setGame("<html><font style=\"color: white;\">Last move: " + game.getLastMove().getSAN() + "</font></html>");
                } else {
                    if (nextMovePushed) {
                        nextMovePushed = false;
                        current.makeMove();
                        game = current.getGame();
                        setGame("<html><font style=\"color: white;\">Last move: " + game.getLastMove().getSAN() + "</font></html>");
                    }
                }
            }
        }
        if (!returnToMenu) {
            String text= "";
            if (game.getGameResult() == Color.WHITE) {
                text = "White has won !!!";
            } else if (game.getGameResult() == Color.BLACK) {
                text = "Black has won !!!";
            } else {
                text = "Stale-mate. No one has won !!!";
            }
            JOptionPane.showMessageDialog(null, text);
            turn.setText("");
            lastMove.setText("");
            menuPanel.setVisible(true);
            gamePanel.setVisible(false);

            run = false;
            returnToMenu = true;
        }
    }

    private static void createWindow() {
        JLabel[] labels = new JLabel[20];
        JFrame frame = new JFrame("Pawn Race");

        char c;
        int offset = 0, labelOffset = 20;
        int windowW = 640, windowH = 740;
        int windowDifference = Math.abs(windowH - windowW);
        int menuOffset = 200;
        int menuCol2 = 270;
        int boxSize = 75;
        int buttonsDist = 30;
        JButton undo, undo2, menu;
        JButton start, yes, no;
        JComboBox modeSelect, gapWC, gapBC, colorBox;
        JLabel modeLabel, gapWL, gapBL, startAs;


        // define close
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // set size
        frame.setSize(new Dimension(windowW, windowH));
        // set location in center
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        //frame.add(mainPanel);
        frame.setLayout(null);
        frame.add(gamePanel);
        frame.add(menuPanel);

        // set menu

        menuPanel.setLocation(new Point(0, 0));
        menuPanel.setSize(windowW, windowH);

        menuPanel.setLayout(null);

        start = new JButton("Start new game");

        start.setSize(120, 20);
        start.setLocation(windowW / 2 - start.getWidth() / 2, 600);

        menuPanel.add(start);

        modeLabel = new JLabel("Mode: ");
        gapWL = new JLabel("White gap: ");
        gapBL = new JLabel("Black gap: ");
        startAs = new JLabel("Start as: ");

        modeLabel.setSize(70, 20);
        modeLabel.setLocation(menuOffset, 150);

        gapWL.setSize(70, 20);
        gapWL.setLocation(menuOffset, modeLabel.getY() + buttonsDist);

        gapBL.setSize(70, 20);
        gapBL.setLocation(menuOffset, gapWL.getY() + buttonsDist);

        startAs.setSize(70, 20);
        startAs.setLocation(menuOffset, gapBL.getY() + buttonsDist);

        menuPanel.add(modeLabel);
        menuPanel.add(gapWL);
        menuPanel.add(gapBL);
        menuPanel.add(startAs);

        modeSelect = new JComboBox();
        gapWC = new JComboBox();
        gapBC = new JComboBox();
        colorBox = new JComboBox();

        modeSelect.setSize(150, 20);
        modeSelect.setLocation(menuCol2, modeLabel.getY());
        modeSelect.addItem("Player VS Player");
        modeSelect.addItem("Player VS AI");
        modeSelect.addItem("AI VS AI");

        gapWC.setSize(60, 20);
        gapWC.setLocation(menuCol2, gapWL.getY());
        for (char q = 'A'; q <= 'H'; q++) {
            gapWC.addItem(q);
        }

        gapBC.setSize(60, 20);
        gapBC.setLocation(menuCol2, gapBL.getY());
        for (char q = 'A'; q <= 'H'; q++) {
            gapBC.addItem(q);
        }

        colorBox.setSize(90, 20);
        colorBox.setLocation(menuCol2, startAs.getY());
        colorBox.addItem("White");
        colorBox.addItem("Black");

        menuPanel.add(modeSelect);
        menuPanel.add(gapWC);
        menuPanel.add(gapBC);
        menuPanel.add(colorBox);

        colorBox.setVisible(false);
        startAs.setVisible(false);

        // set game

        gamePanel.setLocation(offset, offset);
        gamePanel.setSize(windowW - offset * 2, windowH - offset * 2);
        gamePanel.setLayout(null);
        gamePanel.setBackground(new java.awt.Color(0, 0, 0));

        for (int i = 0; i < 8; i++) {
            c = (char) ('A' + i);
            labels[i] = new JLabel("<html><font style=\"color: white;\">" + c + "</font><html>", JLabel.CENTER);
            labels[i].setSize(new Dimension(boxSize, labelOffset));
            labels[i].setLocation(labelOffset + i * boxSize, 0);
            gamePanel.add(labels[i]);
        }

        for (int i = 7; i >= 0; i--) {
            labels[i] = new JLabel("<html><font style=\"color: white;\">" + (i + 1) + "</font><html>");
            labels[i].setVerticalTextPosition(JLabel.CENTER);
            labels[i].setHorizontalAlignment(JLabel.CENTER);
            labels[i].setSize(new Dimension(labelOffset, boxSize));
            labels[i].setLocation(0, labelOffset + (7 - i) * boxSize);
            gamePanel.add(labels[i]);

            for (int j = 0; j < 8; j++) {
                tiles[i][j] = new JButton("");
                tiles[i][j].setSize(new Dimension(boxSize, boxSize));
                tiles[i][j].setLocation(new Point(labelOffset + j * boxSize, labelOffset + (7 - i) * boxSize));

                tiles[i][j].setLayout(null);
                pawns[i][j] = new JLabel();
                pawns[i][j].setSize(new Dimension(boxSize, boxSize));
                tiles[i][j].add(pawns[i][j]);
                if (i % 2 == 0) {
                    if (j % 2 == 0) {
                        // white
                        tiles[i][j].setIcon(white);
                        defaultImage[i][j] = white;
                    } else {
                        tiles[i][j].setIcon(black);
                        defaultImage[i][j] = black;
                    }
                } else {
                    if (j % 2 == 0) {
                        // black
                        tiles[i][j].setIcon(black);
                        defaultImage[i][j] = black;
                    } else {
                        tiles[i][j].setIcon(white);
                        defaultImage[i][j] = white;
                    }
                }

                tiles[i][j].setOpaque(true);
                gamePanel.add(tiles[i][j]);
            }

            labels[i] = new JLabel("<html><font style=\"color: white;\">" + (i + 1) + "</font><html>");
            labels[i].setVerticalTextPosition(JLabel.CENTER);
            labels[i].setHorizontalAlignment(JLabel.CENTER);
            labels[i].setSize(new Dimension(labelOffset, boxSize));
            labels[i].setLocation(labelOffset + 8 * boxSize, labelOffset + (7 - i) * boxSize);
            gamePanel.add(labels[i]);
        }

        for (int i = 0; i < 8; i++) {
            c = (char) ('A' + i);
            labels[i] = new JLabel("<html><font style=\"color: white;\">" + c + "</font><html>", JLabel.CENTER);
            labels[i].setSize(new Dimension(boxSize, labelOffset));
            labels[i].setLocation(labelOffset + i * boxSize, labelOffset + boxSize * 8);
            gamePanel.add(labels[i]);
        }

        undo  = new JButton("Undo");
        undo2 = new JButton("Undo 2 moves");
        menu  = new JButton("Menu");
        nextMove = new JButton("Next Move");

        menu.setSize(100, 20);
        menu.setLocation(new Point(20, windowW + 10));

        undo.setSize(100, 20);
        undo.setLocation(new Point(150, windowW + 10));

        undo2.setSize(100, 20);
        undo2.setLocation(new Point(280, windowW + 10));

        lastMove = new JLabel();
        turn = new JLabel();

        lastMove.setSize(150, 20);
        lastMove.setLocation(new Point(410, windowW + 10));

        turn.setSize(260, 20);
        turn.setLocation(new Point(20, windowW + 10 + menu.getHeight() + 10));
        turn.setText("<html><font style=\"color: white;\">It's white's turn to move.</font></html>");

        nextMove.setSize(100, 20);
        nextMove.setLocation(new Point(turn.getWidth() + 20, windowW + 10 + menu.getHeight() + 10));

        gamePanel.add(undo);
        gamePanel.add(undo2);
        gamePanel.add(menu);
        gamePanel.add(lastMove);
        gamePanel.add(turn);
        gamePanel.add(nextMove);

        nextMove.setVisible(false);

        // set default values for game modes

        mode  = modeSelect.getSelectedIndex() + 1;
        gapW  = gapWC.getSelectedItem().toString().toLowerCase().toCharArray()[0];
        gapB  = gapBC.getSelectedItem().toString().toLowerCase().toCharArray()[0];
        color = colorBox.getSelectedIndex() == 0 ? Color.WHITE : Color.BLACK;

        // set event listeners

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tiles[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton src = (JButton) e.getSource();
                        int y = (src.getX() - labelOffset) / boxSize;
                        int x = 7 - (src.getY() - labelOffset) / boxSize;

                        //System.out.println(x + " " + y);

                        selectSquare(x, y);
                    }
                });
            }
        }

        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (game.getTotalMoves() > 0) {
                    unnapplyMove(game.getLastMove());
                }
            }
        });

        undo2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (game.getTotalMoves() > 1) {
                    unnapplyMove(game.getLastMove());
                    unnapplyMove(game.getLastMove());
                }
            }
        });

        modeSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = modeSelect.getSelectedIndex() + 1;

                if (mode == 2) {
                    startAs.setVisible(true);
                    colorBox.setVisible(true);
                    color = colorBox.getSelectedIndex() == 0 ? Color.WHITE : Color.BLACK;
                } else {
                    startAs.setVisible(false);
                    colorBox.setVisible(false);
                }
            }
        });

        gapWC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gapW  = gapWC.getSelectedItem().toString().toLowerCase().toCharArray()[0];
            }
        });

        gapBC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gapB  = gapBC.getSelectedItem().toString().toLowerCase().toCharArray()[0];
            }
        });

        colorBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color = colorBox.getSelectedIndex() == 0 ? Color.WHITE : Color.BLACK;
            }
        });

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPanel.setVisible(false);
                gamePanel.setVisible(true);

                run = true;
                returnToMenu = false;
                oneTime = true;
                nextMove.setVisible(false);

                lastMove.setText("");
                turn.setText("");
            }
        });

        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dialogResult = JOptionPane.showConfirmDialog (null, "Would you like to return to the menu? (WARNING: Current game will not be saved)","Warning", 0);

                if (dialogResult == JOptionPane.YES_OPTION) {
                    turn.setText("");
                    lastMove.setText("");
                    menuPanel.setVisible(true);
                    gamePanel.setVisible(false);

                    run = false;
                    returnToMenu = true;
                }
            }
        });

        nextMove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextMovePushed = true;
            }
        });

        gamePanel.setVisible(false);

        //display
        frame.setVisible(true);
    }

    private static void resetSelect() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                select[i][j] = false;
            }
        }

        isSelectedFrom = false;
        isSelectedTo = false;
    }

    private static void selectSquare(int x, int y) {
        if (!isSelectedFrom) {
            if (board.getSquare(x, y).occupiedBy() == game.getCurrentPlayer()) {
                isSelectedFrom = true;

                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        select[i][j] = false;
                    }
                }

                select[x][y] = true;
                selectXFrom = x;
                selectYFrom = y;
            }
        } else {
            if (select[x][y]) {
                isSelectedFrom = false;

                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        select[i][j] = false;
                    }
                }

                resetTiles();
            } else {
                isSelectedTo = true;

                selectXTo = x;
                selectYTo = y;
            }
        }
    }

    private static void placePawns() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getSquare(i, j).occupiedBy() == Color.WHITE) {
                    pawns[i][j].setIcon(whitePawn);
                }
                if (board.getSquare(i, j).occupiedBy() == Color.BLACK) {
                    pawns[i][j].setIcon(blackPawn);
                }
                if (board.getSquare(i, j).occupiedBy() == Color.NONE) {
                    pawns[i][j].setIcon(null);
                }
            }
        }
    }

    private static void resetTiles() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tiles[i][j].setIcon(defaultImage[i][j]);
            }
        }
    }


    private static void printArray(boolean[][] x) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (select[i][j]) {
                    System.out.println("Square ("+i+", "+j+") is selected.");
                }
            }
        }
    }

    private static void applyMove(Move move) {
        game.applyMove(move);
        setGame("<html><font style=\"color: white;\">Last move: " + move.getSAN() + "</font></html>");
    }

    private static void unnapplyMove(Move move) {
        game.unapplyMove(move);
        if (game.getTotalMoves() > 0) {
            setGame("<html><font style=\"color: white;\">Last move: " + game.getLastMove().getSAN() + "</font></html>");
        } else {
            setGame("");
        }
    }

    private static void setGame(String text) {
        board = game.getBoard();
        p1.setGame(game, board);
        p2.setGame(game, board);

        placePawns();
        resetSelect();
        resetTiles();
        lastMove.setText(text);

        turn.setText("<html><font style=\"color: white;\">It's " +
                (game.getCurrentPlayer() == Color.WHITE ? "white" : "black")
                + "'s turn to move.</font></html>");
    }
}