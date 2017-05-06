import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.maxBy;

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
        if (moveIdx == boardSize * boardSize) {
            return true;
        }

        ChessField bestNeighbour = inspectNeighbours(x, y);
        if (bestNeighbour.getxPos() == -1) {
            return false;
        }

        next_x = bestNeighbour.getxPos();
        next_y = bestNeighbour.getyPos();
        board[next_x][next_y].setVal(moveIdx);
        if (solve(next_x, next_y, moveIdx + 1)) {
            return true;
        }
        return false;
    }

    private ChessField inspectNeighbours(int x, int y) {
        int nextX, nextY, nextNextX, nextNextY, safeCount = 0, minSafe = Integer.MAX_VALUE;
        ChessField bestCandidate = new ChessField(-1, -1, ChessField.NOT_VISITED);
        Map<ChessField, Integer> nextCandidates = new HashMap<>();

        for (int i = 0; i < xMove.length; i++) {
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
                if(safeCount <= minSafe) {
                    nextCandidates.put(new ChessField(nextX, nextY, ChessField.NOT_VISITED), safeCount);
                    minSafe = safeCount;
                }
            }
            safeCount = 0;
        }
        if (nextCandidates.size() == 0) {
            return bestCandidate;
        }

        Integer min = nextCandidates.values().stream().min(Integer::compare).get();
        List<ChessField> minCandidates = nextCandidates.keySet().stream().filter(
                t->nextCandidates.get(t).equals(min)).collect(Collectors.toList());

        return minCandidates.get(new Random().nextInt(minCandidates.size()));
    }

    public boolean solveKnightTourWrapper(int startX, int startY, boolean disablePrinting) {
        if(!disablePrinting) {
            System.out.println("Rozwiazaniem jest\n");
        }
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
        KnightTour knightTour = new KnightTour(7);
        knightTour.solveKnightTourWrapper(6,6, false);
    }
}
