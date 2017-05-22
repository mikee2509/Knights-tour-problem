import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class KnightTourTest {
    private static final int MAX_TESTED_BOARD_SIZE = 80;
    private static final int MIN_TESTED_BOARD_SIZE = 5;
    public static final int NUMBER_OF_ATTEMPTS = 1;
    private static final KnightTour.SolvingMethod solvingMethod = KnightTour.SolvingMethod.WARNSDORFFS_RULE;

    private int boardSize;
    private int startX;
    private int startY;

    @Parameters(name = "boardSize({0})   corner({1})")
    public static Collection<Object[]> data() {
        Integer list[][] = new Integer[4 * (MAX_TESTED_BOARD_SIZE - MIN_TESTED_BOARD_SIZE + 1)][2];
        for (int i = 0, size = MIN_TESTED_BOARD_SIZE - 1, corner; i < list.length; i++) {
            corner = i%4;
            if(corner == 0) ++size;
            list[i][0] = size;
            list[i][1] = corner;
        }
        return Arrays.asList(list);
    }

    // Corners:
    // |0|_|1|
    // |_|_|_|
    // |3|_|2|
    public KnightTourTest(int boardSize, int corner) {
        this.boardSize = boardSize;
        int[] startXY = KnightTour.getStartXY(boardSize, corner);
        startX = startXY[0];
        startY = startXY[1];
    }

    @Test
    public void solveKnightTourWrapper() {
        KnightTour knightTour = new KnightTour(boardSize);
        assertTrue(knightTour.solveKnightTourWrapper(startX, startY, NUMBER_OF_ATTEMPTS, true, solvingMethod));
    }

}


