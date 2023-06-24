package game2048;

/**
 * Represents the image of a numbered tile on a 2048 board.
 *
 * @author P. N. Hilfinger.
 */
public class Tile {

    /**
     * A new tile with VALUE as its value at (ROW, COL). This constructor is private, so all tiles
     * are created by the factory methods create, move, and merge.
     */
    private Tile(int value, int col, int row) {
        this.value = value;
        this.row = row;
        this.col = col;
        this.next = null;
    }

    /**
     * Return my current row.
     */
    public int row() {
        return this.row;
    }

    /**
     * Return my current column.
     */
    public int col() {
        return this.col;
    }

    /**
     * Return the value supplied to my constructor.
     */
    public int value() {
        return this.value;
    }

    /**
     * Return my next state. Before I am moved or merged, I am my own successor.
     */
    public Tile next() {
        return this.next == null ? this : this.next;
    }

    /**
     * Return a new tile at (ROW, COL) with value VALUE.
     */
    public static Tile create(int value, int col, int row) {
        return new Tile(value, col, row);
    }

    /**
     * Return the result of moving me to (COL, ROW).
     */
    public Tile move(int col, int row) {
        Tile result = new Tile(this.value, col, row);
        this.next = result;
        return result;
    }

    /**
     * Return the result of merging OTHERTILE with me after moving to (COL, ROW).
     */
    public Tile merge(int col, int row, Tile otherTile) {
        assert this.value == otherTile.value();
        this.next = otherTile.next = new Tile(2 * this.value, col, row);
        return this.next;
    }

    /**
     * Return the distance in rows or columns between me and my successor tile (0 if I have no
     * successor).
     */
    public int distToNext() {
        if (this.next == null) {
            return 0;
        } else {
            return Math.max(
                    Math.abs(this.row - this.next.row()), Math.abs(this.col - this.next.col()));
        }
    }

    @Override
    public String toString() {
        return String.format("%d@(%d, %d)", value(), col(), row());
    }

    /**
     * My value.
     */
    private final int value;

    /**
     * My last position on the board.
     */
    private final int row, col;

    /**
     * Successor tile: one I am moved to or merged with.
     */
    private Tile next;

}
