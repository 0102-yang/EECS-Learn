package game2048;

import java.util.Formatter;
import java.util.Observable;

/**
 * The state of a game of 2048.
 *
 * @author yang
 */
public class Model extends Observable {

    /**
     * Current contents of the board.
     */
    private Board board;

    /**
     * Current score.
     */
    private int score;

    /**
     * Maximum score so far. Updated when game ends.
     */
    private int maxScore;

    /**
     * True iff game is ended.
     */
    private boolean gameOver;

    /*
     * Coordinate System: column C, row R of the board (where row 0, column 0 is the
     * lower-left corner of the board) will correspond to board.tile(c, r). Be
     * careful! It works like (x, y) coordinates.
     */

    /**
     * Largest piece value.
     */
    public static final int MAX_PIECE = 2048;

    /**
     * A new 2048 game on a board of size SIZE with no pieces and score 0.
     */
    public Model(int size) {
        this.board = new Board(size);
        this.score = this.maxScore = 0;
        this.gameOver = false;
    }

    /**
     * A new 2048 game where RAWVALUES contain the values of the tiles (0 if null).
     * VALUES is indexed by (row, col) with (0, 0) corresponding to the bottom-left
     * corner. Used for testing purposes.
     */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        this.board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /**
     * Return the current Tile at (COL, ROW), where 0 <= ROW < size(), 0 <= COL <
     * size(). Returns null if there is no tile there. Used for testing. Should be
     * deprecated and removed.
     */
    public Tile tile(int col, int row) {
        return this.board.tile(col, row);
    }

    /**
     * Return the number of squares on one side of the board. Used for testing.
     * Should be deprecated and removed.
     */
    public int size() {
        return this.board.size();
    }

    /**
     * Return true iff the game is over (there are no moves, or there is a tile with
     * value 2048 on the board).
     */
    public boolean gameOver() {
        checkGameOver();
        if (this.gameOver) {
            this.maxScore = Math.max(this.score, this.maxScore);
        }
        return this.gameOver;
    }

    /**
     * Return the current score.
     */
    public int score() {
        return this.score;
    }

    /**
     * Return the current maximum game score (updated at end of game).
     */
    public int maxScore() {
        return this.maxScore;
    }

    /**
     * Clear the board to empty and reset the score.
     */
    public void clear() {
        this.score = 0;
        this.gameOver = false;
        this.board.clear();
        setChanged();
    }

    /**
     * Add TILE to the board. There must be no Tile currently at the same position.
     */
    public void addTile(Tile tile) {
        this.board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /**
     * Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * <p>
     * 1. If two Tile objects are adjacent in the direction of motion and have the
     * same value, they are merged into one Tile of twice the original value and
     * that new value is added to the score instance variable 2. A tile that is the
     * result of a merge will not merge again on that tilt. So each move, every tile
     * will only ever be part of at most one merge (perhaps zero). 3. When three
     * adjacent tiles in the direction of motion have the same value, then the
     * leading two tiles in the direction of motion merge, and the trailing tile
     * does not.
     */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // DONE: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.
        int size = this.board.size();
        for (int i = 0; i < size; i++) {
            boolean flag = merge(i, side);
            if (!changed && flag) {
                changed = true;
            }
        }

        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    private static class Coordinate {

        int col;

        int row;

        private Coordinate(int col, int row) {
            this.col = col;
            this.row = row;
        }

        public static Coordinate generateCoordinate(int col, int row) {
            return new Coordinate(col, row);
        }

    }

    private void reverseArray(Coordinate[] coordinates) {
        for (int i = 0; i < coordinates.length >> 1; i++) {
            Coordinate coordinate = coordinates[i];
            coordinates[i] = coordinates[coordinates.length - 1 - i];
            coordinates[coordinates.length - 1 - i] = coordinate;
        }
    }

    /**
     * Generate coordinates array for merge.
     *
     * @param index The index represent the col or row.
     * @param side  The merge direction right now.
     * @return The coordinates array.
     */
    private Coordinate[] generateCoordinates(int index, Side side) {
        Coordinate[] coordinates = new Coordinate[this.board.size()];
        if (side == Side.EAST || side == Side.WEST) {
            for (int i = 0; i < this.board.size(); i++) {
                Coordinate coordinate = Coordinate.generateCoordinate(i, index);
                coordinates[i] = coordinate;
            }
            if (side == Side.EAST) {
                reverseArray(coordinates);
            }
        } else {
            for (int i = 0; i < this.board.size(); i++) {
                Coordinate coordinate = Coordinate.generateCoordinate(index, i);
                coordinates[i] = coordinate;
            }
            if (side == Side.NORTH) {
                reverseArray(coordinates);
            }
        }
        return coordinates;
    }

    /**
     * Return the next coordinate of specified moving direction.
     *
     * @param col  Current col.
     * @param row  Current row.
     * @param side Current moving direction.
     * @return The next coordinate.
     */
    private Coordinate next(int col, int row, Side side) {
        switch (side) {
            case NORTH -> {
                return Coordinate.generateCoordinate(col, row + 1);
            }
            case SOUTH -> {
                return Coordinate.generateCoordinate(col, row - 1);
            }
            case EAST -> {
                return Coordinate.generateCoordinate(col + 1, row);
            }
            case WEST -> {
                return Coordinate.generateCoordinate(col - 1, row);
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * Check if this coordinate is valid.
     *
     * @param coordinate The coordinate.
     * @return True for valid.
     */
    private boolean validCoordinate(Coordinate coordinate) {
        return coordinate.col >= 0 && coordinate.col < size() && coordinate.row >= 0 && coordinate.row < size();
    }

    /**
     * Merge one col or one row.
     *
     * @param index The index represent col or row.
     * @param side  Current moving direction.
     * @return True for each of the tile has been moved.
     */
    private boolean merge(int index, Side side) {
        boolean isChanged = false;
        Coordinate[] coordinates = generateCoordinates(index, side);
        boolean[][] isMerged = new boolean[size()][size()];
        for (Coordinate c : coordinates) {
            Tile currentTile = this.board.tile(c.col, c.row);
            if (currentTile == null) {
                continue;
            }

            boolean flag = moveSingleTile(currentTile, side, isMerged);
            if (!isChanged && flag) {
                isChanged = true;
            }
        }
        return isChanged;
    }

    /**
     * Move single tile to its destination.
     *
     * @param tile     Current tile.
     * @param side     Current moving side.
     * @param isMerged The bit map of whether tile is formed by merge.
     * @return True for this tile has been moved.
     */
    private boolean moveSingleTile(Tile tile, Side side, boolean[][] isMerged) {
        boolean isChanged = false;
        while (true) {
            Coordinate next = next(tile.col(), tile.row(), side);
            assert next != null;
            if (!validCoordinate(next)) {
                break;
            }

            Tile nextTile = this.board.tile(next.col, next.row);
            if (nextTile == null) {
                this.board.move(next.col, next.row, tile);
                tile = tile.next();
                isChanged = true;
            } else if (nextTile.value() == tile.value() && !isMerged[next.col][next.row]) {
                isMerged[next.col][next.row] = this.board.move(next.col, next.row, tile);
                int value = tile.value();
                this.score += value * 2;
                isChanged = true;
                break;
            } else {
                break;
            }
        }
        return isChanged;
    }

    /**
     * Checks if the game is over and sets the gameOver variable appropriately.
     */
    private void checkGameOver() {
        this.gameOver = Model.checkGameOver(this.board);
    }

    /**
     * Determine whether game is over.
     */
    private static boolean checkGameOver(Board b) {
        return Model.maxTileExists(b) || !Model.atLeastOneMoveExists(b);
    }

    /**
     * Returns true if at least one space on the Board is empty. Empty spaces are
     * stored as null.
     */
    public static boolean emptySpaceExists(Board b) {
        // DONE: Fill in this function.
        for (Tile tile : b) {
            if (tile == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value. Maximum valid
     * value is given by MAX_PIECE. Note that given a Tile object t, we get its
     * value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        // DONE: Fill in this function.
        var itr = b.iterator();
        while (itr.hasNext()) {
            Tile tile = itr.next();
            if (tile != null && tile.value() == Model.MAX_PIECE) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board. There are two ways
     * that there can be valid moves: 1. There is at least one empty space on the
     * board. 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        // DONE: Fill in this function.
        if (Model.emptySpaceExists(b)) {
            return true;
        }
        int size = b.size();
        for (int i = 0; i < size; i++) {
            if (Model.checkCol(b, i) || Model.checkRow(b, i)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkRow(Board b, int row) {
        int size = b.size() - 1;
        for (int i = 0; i < size; i++) {
            Tile curr = b.tile(i, row);
            Tile next = b.tile(i + 1, row);
            if (curr != null && next != null && curr.value() == next.value()) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkCol(Board b, int col) {
        int size = b.size() - 1;
        for (int i = 0; i < size; i++) {
            Tile curr = b.tile(col, i);
            Tile next = b.tile(col, i + 1);
            if (curr != null && next != null && curr.value() == next.value()) {
                return true;
            }
        }
        return false;
    }

    @Override
    /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        String res = out.toString();
        out.close();
        return res;
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }

}
