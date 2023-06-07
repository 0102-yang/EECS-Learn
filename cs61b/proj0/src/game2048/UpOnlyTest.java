package game2048;

import org.junit.Test;

/**
 * Tests the tilt() method in the up (Side.NORTH) direction only.
 *
 * @author Omar Khan
 */
public class UpOnlyTest extends TestUtils {

    @Test
    /** Move tiles up (no merging). */
    public void testUpNoMerge() {
        int[][] before = new int[][]{{0, 0, 4, 0}, {0, 0, 0, 2}, {0, 0, 0, 0}, {0, 0, 0, 0},};
        int[][] after = new int[][]{{0, 0, 4, 2}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},};

        TestUtils.model = new Model(before, 0, 0, false);
        String prevBoard = TestUtils.model.toString();
        boolean changed = TestUtils.model.tilt(Side.NORTH);
        TestUtils.checkChanged(Side.NORTH, true, changed);
        TestUtils.checkModel(after, 0, 0, prevBoard, Side.NORTH);
    }

    @Test
    /** A basic merge. */
    public void testUpBasicMerge() {
        int[][] before = new int[][]{{0, 0, 0, 0}, {0, 0, 2, 0}, {0, 0, 2, 0}, {0, 0, 0, 0},};
        int[][] after = new int[][]{{0, 0, 4, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},};

        TestUtils.updateModel(before, 0, 0, false);
        String prevBoard = TestUtils.model.toString();
        boolean changed = TestUtils.model.tilt(Side.NORTH);
        TestUtils.checkChanged(Side.NORTH, true, changed);
        TestUtils.checkModel(after, 4, 0, prevBoard, Side.NORTH);
    }

    @Test
    /** A triple merge. Only the leading 2 tiles should merge. */
    public void testUpTripleMerge() {
        int[][] before = new int[][]{{0, 0, 2, 0}, {0, 0, 0, 0}, {0, 0, 2, 0}, {0, 0, 2, 0},};
        int[][] after = new int[][]{{0, 0, 4, 0}, {0, 0, 2, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},};

        TestUtils.updateModel(before, 0, 0, false);
        String prevBoard = TestUtils.model.toString();
        boolean changed = TestUtils.model.tilt(Side.NORTH);
        TestUtils.checkChanged(Side.NORTH, true, changed);
        TestUtils.checkModel(after, 4, 0, prevBoard, Side.NORTH);
    }

    @Test
    /**
     * A tricky merge.
     *
     * <p>
     * The tricky part here is that the 4 tile on the bottom row shouldn't merge
     * with the newly created 4 tile on the top row. If you're failing this test,
     * try seeing how you can ensure that the bottom 4 tile doesn't merge with the
     * newly created 4 tile on top.
     */
    public void testUpTrickyMerge() {
        int[][] before = new int[][]{{0, 0, 2, 0}, {0, 0, 2, 0}, {0, 0, 0, 0}, {0, 0, 4, 0},};
        int[][] after = new int[][]{{0, 0, 4, 0}, {0, 0, 4, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},};

        TestUtils.updateModel(before, 0, 0, false);
        String prevBoard = TestUtils.model.toString();
        boolean changed = TestUtils.model.tilt(Side.NORTH);
        TestUtils.checkChanged(Side.NORTH, true, changed);
        TestUtils.checkModel(after, 4, 0, prevBoard, Side.NORTH);
    }

}
