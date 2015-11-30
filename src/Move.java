public class Move {
    private Square from, to;
    private boolean isCapture;

    public Move(Square from, Square to, boolean isCapture) {
        this.from      = from;
        this.to        = to;
        this.isCapture = isCapture;
    }

    public Square getFrom() {
        return this.from;
    }

    public Square getTo() {
        return this.to;
    }

    public boolean isCapture() {
        return this.isCapture;
    }

    public String getSAN() {
        if (isCapture()) {
            char cf = 'A', ct = 'A';
            cf += from.getY();
            ct += to.getY();
            return "" + cf + "x" + ct + (to.getX() + 1);
        } else {
            char c = 'A';
            c += to.getY();
            return "" + c + (to.getX() + 1);
        }
    }
}