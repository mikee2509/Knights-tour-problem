import java.util.ArrayList;
import java.util.Random;

public class KnightTour {

    private int boardSize;
    private static ChessField board[][];

    private static final int xMove[] = { 2, 1, -1, -2, -2, -1,  1,  2 };
    private static final int yMove[] = { 1, 2,  2,  1, -1, -2, -2, -1 };


    public KnightTour(int boardSize) {
        this.boardSize = boardSize;
        board = new ChessField[boardSize][boardSize];
    }

    private boolean isValid(int x, int y) {
        return (x >= 0 && x < boardSize &&
                y >= 0 && y < boardSize &&
                !board[x][y].isVisited());
    }

    private void printSolution() {
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                System.out.format("%"+ String.valueOf(boardSize * boardSize - 1).length() +"d ",
                        board[x][y].getVal());
            }
            System.out.println();
        }
    }

    private boolean solve(int x, int y, int moveIdx) {
        int next_x, next_y;

        if (moveIdx == boardSize * boardSize)
            return true;

        ChessField bestNeighbour = inspectNeighbours(x, y);
        if (bestNeighbour.getxPos() == -1) return false;
        next_x = bestNeighbour.getxPos();
        next_y = bestNeighbour.getyPos();
        board[next_x][next_y].setVal(moveIdx);

        if (solve(next_x, next_y, moveIdx + 1)) {
            return true;
        }
        return false;
    }

    class nextField {
        public int safeCount;
        public int i;

        public nextField(int safeCount, int i) {
            this.safeCount = safeCount;
            this.i = i;
        }
    }

    private ChessField inspectNeighbours(int x, int y) {
        int safeCount = 0, minSafe = Integer.MAX_VALUE, bestMove = -1;
        ArrayList<nextField> possibleNextFields = new ArrayList<>();

        for (int i = 0; i < xMove.length; i++) {
            int nextX, nextY, nextNextX, nextNextY;
            nextX = x + xMove[i];
            nextY = y + yMove[i];

            if(isValid(nextX, nextY)){
                for (int j = 0; j < xMove.length; j++) {
                    nextNextX = nextX + xMove[j];
                    nextNextY = nextY + yMove[j];
                    if(isValid(nextNextX, nextNextY)) {
                        ++safeCount;
                    }
                }
                possibleNextFields.add(new nextField(safeCount,i));
                if(safeCount < minSafe) {
                    minSafe = safeCount;
                    bestMove = i;
                }
            }
            safeCount = 0;
        }

        possibleNextFields.sort((o1, o2) -> o1.safeCount - o2.safeCount);
        int sameSafeCount = 0;
        for (nextField f : possibleNextFields)
            if (f.safeCount == minSafe)
                sameSafeCount++;
        bestMove = possibleNextFields.get(new Random().nextInt(sameSafeCount)).i;

        if (bestMove == -1) return new ChessField(-1, -1, ChessField.NOT_VISITED);
        return new ChessField(x + xMove[bestMove], y + yMove[bestMove], ChessField.NOT_VISITED);
    }

    public boolean solveKnightTourWrapper(int startX, int startY, boolean disablePrinting) {
        if(!disablePrinting) System.out.println("Rozwiazaniem jest\n");
        boardInit(startX, startY);
        if (!solve(startX, startY, 1)) {
            if(!disablePrinting) System.out.println("Brak rozwiazania");
            return false;
        }
        else {
            if(!disablePrinting) printSolution();
        }
        return true;
    }

    private void boardInit(int startX, int startY) {
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                board[x][y] = new ChessField(x, y, ChessField.NOT_VISITED);
            }
        }
        board[startX][startY].setVal(0);
    }

    public static void main(String... arg) {
        KnightTour knightTour = new KnightTour(8);
        knightTour.solveKnightTourWrapper(0,0, false);
    }
}
