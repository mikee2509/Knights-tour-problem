public class ChessField {

    public static final int NOT_VISITED = -1;

    private int xPos;
    private int yPos;
    private int val;

    public ChessField(int xPos, int yPos, int val) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.val  = val;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public boolean isVisited() {
        return this.val != NOT_VISITED;
    }
}
