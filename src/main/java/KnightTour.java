import java.util.*;
import java.util.stream.Collectors;

public class KnightTour {

    private int boardSize;
    private static final int DEFAULT_NO_OF_ATTEMPTS = 5;
    public enum SolvingMethod {
        WARNSDORFFS_RULE, BACKTRACKING
    }
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

    private void printSolution(int attempts) {
        System.out.println("Number of attempts: " + attempts + "\nSolution: ");
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                System.out.format("%"+ String.valueOf(boardSize * boardSize - 1).length() +"d ",
                        board[x][y].getVal());
            }
            System.out.println();
        }
    }

    private boolean solveUsingWarnsdorffsRule(int x, int y, int moveIdx) {
        int nextX, nextY;
        if (moveIdx == boardSize * boardSize) {
            return true;
        }

        ChessField bestNeighbour = inspectNeighbours(x, y);
        if (bestNeighbour.getxPos() < 0) {
            return false;
        }

        nextX = bestNeighbour.getxPos();
        nextY = bestNeighbour.getyPos();
        board[nextX][nextY].setVal(moveIdx);
        if (solveUsingWarnsdorffsRule(nextX, nextY, moveIdx + 1)) {
            return true;
        }
        return false;
    }

    private boolean solveUsingBacktracking(int x, int y, int moveIdx) {
        int nextX, nextY;
        if (moveIdx == boardSize * boardSize) {
            return true;
        }

        for(int i = 0; i < 8; i++) {
            nextX = x + xMove[i];
            nextY = y + yMove[i];
            if(isValid(nextX, nextY)) {
                board[nextX][nextY].setVal(moveIdx);
                if (solveUsingBacktracking(nextX, nextY, moveIdx + 1)) {
                    return true;
                } else
                    board[nextX][nextY].setVal(ChessField.NOT_VISITED);
            }
        }
        return false;
    }

    private ChessField inspectNeighbours(int x, int y) {
        int nextX, nextY, nextNextX, nextNextY, safeCount = 0, minSafe = Integer.MAX_VALUE;
        ChessField emptyField = new ChessField(-1, -1, ChessField.NOT_VISITED);
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
                    nextCandidates.put(board[nextX][nextY], safeCount);
                    minSafe = safeCount;
                }
            }
            safeCount = 0;
        }
        if (nextCandidates.size() == 0) {
            return emptyField;
        }

        Integer min = nextCandidates.values().stream()
                .min(Integer::compare)
                .get();

        List<ChessField> minCandidates = nextCandidates.keySet().stream()
                .filter(t->nextCandidates.get(t).equals(min))
                .collect(Collectors.toList());

        return minCandidates.get(new Random().nextInt(minCandidates.size()));
    }

    public boolean solveKnightTourWrapper(int startX, int startY, int numAttempts, boolean disablePrinting, SolvingMethod solvingMethod) {
        boardInit(startX, startY);
        int i = 1;

        switch (solvingMethod) {
            case WARNSDORFFS_RULE:
                while(!solveUsingWarnsdorffsRule(startX, startY, 1)) {
                    boardInit(startX, startY);
                    ++i;
                    if(i == numAttempts) {
                        if(!disablePrinting) {
                            System.out.println("Solution not found");
                        }
                        return false;
                    }
                }
                break;
            case BACKTRACKING:
                solveUsingBacktracking(startX, startY,1);
                break;
        }

        if(!disablePrinting) {
            printSolution(i);
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

    // Corners:
    // |0|_|1|
    // |_|_|_|
    // |3|_|2|
    public static int[] getStartXY(int boardSize, int corner) {
        int[] startXY = new int[2];
        switch (corner){
            case 0:
                startXY[0] = startXY[1] = 0;
                break;
            case 1:
                startXY[0] = 0;
                startXY[1] = boardSize - 1;
                break;
            case 2:
                startXY[0] = startXY[1] = boardSize - 1;
                break;
            case 3:
                startXY[0] = boardSize - 1;
                startXY[1] = 0;
                break;
            default:
                startXY[0] = startXY[1] = -1;
        }
        return startXY;
    }

    public static void main(String... arg) {
        if(arg.length < 2 || arg.length > 4) {
            printUsage();
            return;
        }

        int boardSize, corner, numAttempts, startX, startY;
        SolvingMethod solvingMethod = SolvingMethod.WARNSDORFFS_RULE;
        try {
            boardSize = Integer.parseInt(arg[0]);
            corner = Integer.parseInt(arg[1]);
            if(arg.length >= 3)
                switch(arg[2]) {
                    case "warnsdorffs_rule":
                        solvingMethod = SolvingMethod.WARNSDORFFS_RULE;
                        break;
                    case "backtracking":
                        solvingMethod = SolvingMethod.BACKTRACKING;
                        break;
                    default:
                        printUsage();
                        break;
                }

            numAttempts = arg.length == 4 ? Integer.parseInt(arg[2]) : DEFAULT_NO_OF_ATTEMPTS;
        } catch (NumberFormatException e) {
            printUsage();
            return;
        }
        int[] startXY = getStartXY(boardSize, corner);
        if(startXY[0] != -1) {
            startX = startXY[0];
            startY = startXY[1];
        }
        else {
            printUsage();
            return;
        }
        if(boardSize < 0 || numAttempts < 0) {
            printUsage();
            return;
        }
        if(boardSize <= 4) {
            System.out.println("The Knight's tour is only possible on board of size larger than 4");
            return;
        }
        KnightTour knightTour = new KnightTour(boardSize);
        knightTour.solveKnightTourWrapper(startX, startY, numAttempts, false, solvingMethod);
    }

    private static void printUsage(){
        System.out.println("Usage:\n" +
                "KnightTour <board_size> <start_corner> [<method>] [<max_attempts>]\n" +
                "Corner numbers:\n" +
                "|0|_|1|\n" +
                "|_|_|_|\n" +
                "|3|_|2|\n" +
                "Method (warnsdorffs_rule by default):\n" +
                "warnsdorffs_rule\n" +
                "backtracking\n" +
                "max_attempts = 5 by default");
    }
}
