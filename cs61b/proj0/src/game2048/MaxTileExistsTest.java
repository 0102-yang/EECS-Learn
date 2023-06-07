package game2048;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the maxTileExists() static method of Model.
 *
 * @author Omar Khan
 */
public class MaxTileExistsTest {

    /**
     * The board we'll be testing.
     */
    static Board b;

    @Test
    /** Note that this isn't a possible board state. */
    public void testEmptyBoard() {
        int[][] rawVals = new int[][]{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},};

        MaxTileExistsTest.b = new Board(rawVals, 0);

        assertFalse("Board is empty\n" + MaxTileExistsTest.b, Model.maxTileExists(MaxTileExistsTest.b));
    }

    @Test
    /** Tests a full board with no max piece. */
    public void testFullBoardNoMax() {
        int[][] rawVals = new int[][]{{2, 2, 2, 2}, {2, 2, 2, 2}, {2, 2, 2, 2}, {2, 2, 2, 2},};

        MaxTileExistsTest.b = new Board(rawVals, 0);

        assertFalse("No 2048 tile on board\n" + MaxTileExistsTest.b, Model.maxTileExists(MaxTileExistsTest.b));
    }

    @Test
    /** Tests a full board with the max piece. */
    public void testFullBoardMax() {
        int[][] rawVals = new int[][]{{2, 2, 2, 2}, {2, 2, 2, 2}, {2, 2, 2, 2}, {2, 2, 2, 2048},};

        MaxTileExistsTest.b = new Board(rawVals, 0);

        assertTrue("One 2048 tile on board\n" + MaxTileExistsTest.b, Model.maxTileExists(MaxTileExistsTest.b));
    }

    @Test
    /** Tests multiple max pieces. */
    public void testMultipleMax() {
        int[][] rawVals = new int[][]{{2, 2, 2, 2}, {2, 2048, 0, 0}, {0, 0, 0, 2}, {0, 0, 2, 2048},};

        MaxTileExistsTest.b = new Board(rawVals, 0);

        assertTrue("Two 2048 tile on board\n" + MaxTileExistsTest.b, Model.maxTileExists(MaxTileExistsTest.b));
    }

    @Test
    /** Tests when the max piece is in the top right corner. */
    public void testTopRightCorner() {
        int[][] rawVals = new int[][]{{0, 0, 0, 2048}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};

        MaxTileExistsTest.b = new Board(rawVals, 0);

        assertTrue("One 2048 tile on board\n" + MaxTileExistsTest.b, Model.maxTileExists(MaxTileExistsTest.b));
    }

    @Test
    /** Tests when the max piece is in the top left corner. */
    public void testTopLeftCorner() {
        int[][] rawVals = new int[][]{{2048, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};

        MaxTileExistsTest.b = new Board(rawVals, 0);

        assertTrue("One 2048 tile on board\n" + MaxTileExistsTest.b, Model.maxTileExists(MaxTileExistsTest.b));
    }

    @Test
    /** Tests when the max piece is in the bottom left corner. */
    public void testBottomLeftCorner() {
        int[][] rawVals = new int[][]{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {2048, 0, 0, 0}};

        MaxTileExistsTest.b = new Board(rawVals, 0);

        assertTrue("One 2048 tile on board\n" + MaxTileExistsTest.b, Model.maxTileExists(MaxTileExistsTest.b));
    }

    @Test
    /** Tests when the max piece is in the bottom right corner. */
    public void testBottomRightCorner() {
        int[][] rawVals = new int[][]{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 2048}};

        MaxTileExistsTest.b = new Board(rawVals, 0);

        assertTrue("One 2048 tile on board\n" + MaxTileExistsTest.b, Model.maxTileExists(MaxTileExistsTest.b));
    }

}
