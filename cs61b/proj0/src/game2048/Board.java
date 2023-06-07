package game2048;

import java.util.Arrays;
import java.util.Formatter;
import java.util.Iterator;

/**
 * @author hug
 */
public class Board implements Iterable<Tile> {

    /**
     * Current contents of the board.
     */
    private Tile[][] values;

    /**
     * Side that the board currently views as north.
     */
    private Side viewPerspective;

    public Board(int size) {
        this.values = new Tile[size][size];
        this.viewPerspective = Side.NORTH;
    }

    /**
     * Shifts the view of the board such that the board behaves as if side S is
     * north.
     */
    public void setViewingPerspective(Side s) {
        this.viewPerspective = s;
    }

    /**
     * Create a board where RAWVALUES hold the values of the tiles on the board (0
     * is null) with a current score of SCORE and the viewing perspective set to
     * north.
     */
    public Board(int[][] rawValues, int score) {
        int size = rawValues.length;
        this.values = new Tile[size][size];
        this.viewPerspective = Side.NORTH;
        for (int col = 0; col < size; col += 1) {
            for (int row = 0; row < size; row += 1) {
                int value = rawValues[size - 1 - row][col];
                Tile tile;
                if (value == 0) {
                    tile = null;
                } else {
                    tile = Tile.create(value, col, row);
                }
                this.values[col][row] = tile;
            }
        }
    }

    /**
     * Returns the size of the board.
     */
    public int size() {
        return this.values.length;
    }

    /**
     * Shifts the view of the Board.
     */
    public void startViewingFrom(Side s) {
        this.viewPerspective = s;
    }

    /**
     * Return the current Tile at (COL, ROW), when sitting with the board oriented
     * so that SIDE is at the top (farthest) from you.
     */
    private Tile vtile(int col, int row, Side side) {
        return this.values[side.col(col, row, size())][side.row(col, row, size())];
    }

    /**
     * Return the current Tile at (COL, ROW), where 0 <= ROW < size(), 0 <= COL <
     * size(). Returns null if there is no tile there.
     */
    public Tile tile(int col, int row) {
        return vtile(col, row, this.viewPerspective);
    }

    /**
     * Clear the board to empty and reset the score.
     */
    public void clear() {
        for (Tile[] column : this.values) {
            Arrays.fill(column, null);
        }
    }

    /**
     * Adds the tile T to the board
     */
    public void addTile(Tile t) {
        this.values[t.col()][t.row()] = t;
    }

    /**
     * Places the Tile TILE at column COL, row ROW where COL and ROW are treated as
     * coordinates with respect to the current viewPerspective.
     *
     * <p>
     * Returns whether or not this move is a merge.
     */
    public boolean move(int col, int row, Tile tile) {
        int pcol = this.viewPerspective.col(col, row, size()), prow = this.viewPerspective.row(col, row, size());
        if (tile.col() == pcol && tile.row() == prow) {
            return false;
        }
        Tile tile1 = vtile(col, row, this.viewPerspective);
        this.values[tile.col()][tile.row()] = null;

        if (tile1 == null) {
            this.values[pcol][prow] = tile.move(pcol, prow);
            return false;
        } else {
            this.values[pcol][prow] = tile.merge(pcol, prow, tile1);
            return true;
        }
    }

    /**
     * Returns the board as a string, used for debugging.
     */
    @Override
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
        String res = out.toString();
        out.close();
        return res;
    }

    /**
     * Iterates through teach tile in the board.
     */
    private class AllTileIterator implements Iterator<Tile>, Iterable<Tile> {

        int r, c;

        AllTileIterator() {
            this.r = 0;
            this.c = 0;
        }

        @Override
        public boolean hasNext() {
            return this.r < size();
        }

        @Override
        public Tile next() {
            Tile t = tile(this.c, this.r);
            this.c = this.c + 1;
            if (this.c == size()) {
                this.c = 0;
                this.r = this.r + 1;
            }
            return t;
        }

        @Override
        public Iterator<Tile> iterator() {
            return this;
        }

    }

    @Override
    public Iterator<Tile> iterator() {
        return new AllTileIterator();
    }

}
