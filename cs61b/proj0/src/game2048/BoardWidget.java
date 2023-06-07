package game2048;

import ucb.gui2.Pad;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.*;

/**
 * A widget that displays a 2048 board.
 *
 * @author P. N. Hilfinger
 */
class BoardWidget extends Pad {

    /* Parameters controlling sizes, speeds, colors, and fonts. */

    /**
     * Colors of empty squares and grid lines.
     */
    static final Color EMPTY_SQUARE_COLOR = new Color(205, 192, 176), BAR_COLOR = new Color(184, 173, 158);

    /**
     * Bar width separating tiles and length of tile's side (pixels).
     */
    static final int TILE_SEP = 15, TILE_SIDE = 100, TILE_SIDE_SEP = BoardWidget.TILE_SEP + BoardWidget.TILE_SIDE;

    /**
     * Font used for numbering on tiles with <= 2 digits.
     */
    static final Font TILE_FONT2 = new Font("SansSerif", 1, 48);

    /**
     * Font used for numbering on tiles with 3 digits.
     */
    static final Font TILE_FONT3 = new Font("SansSerif", 1, 40);

    /**
     * Font used for numbering on tiles with 4 digits.
     */
    static final Font TILE_FONT4 = new Font("SansSerif", 1, 32);

    /**
     * Color for overlay text on board.
     */
    static final Color OVERLAY_COLOR = new Color(200, 0, 0, 64);

    /**
     * Font for overlay text on board.
     */
    static final Font OVERLAY_FONT = new Font("SansSerif", 1, 64);

    /**
     * Wait between animation steps (in milliseconds).
     */
    static final int TICK = 10;

    /**
     * Amount to move per second (in rows/columns).
     */
    static final float MOVE_DELTA = 10.0f;

    /**
     * Fractional increase in size for "bloom effect".
     */
    static final float BLOOM_FACTOR = 0.1f;

    /**
     * Time over which a tile "blooms" in seconds.
     */
    static final float BLOOM_TIME = 0.5f;

    /**
     * Ticks over which a tile "blooms" out or in.
     */
    static final int BLOOM_TICKS = (int) (20.0 * BoardWidget.BLOOM_TIME / BoardWidget.TICK);

    /**
     * Mapping from numbers on tiles to their text and background colors.
     */
    static final HashMap<Integer, Color[]> TILE_COLORS = new HashMap<>();

    /**
     * List of tile values and corresponding background and foreground color values.
     */
    private static final int[][] TILE_COLOR_MAP = {{2, 0x776e65, 0xeee4da}, {4, 0x776e65, 0xede0c8},
            {8, 0xf9f6f2, 0xf2b179}, {16, 0xf9f6f2, 0xf59563}, {32, 0xf9f6f2, 0xf67c5f},
            {64, 0xf9f6f2, 0xf65e3b}, {128, 0xf9f6f2, 0xedcf72}, {256, 0xf9f6f2, 0xedcc61},
            {512, 0xf9f6f2, 0xedc850}, {1024, 0xf9f6f2, 0xedc53f}, {2048, 0xf9f6f2, 0xedc22e},};

    static {
        /* { "LABEL", "TEXT COLOR (hex)", "BACKGROUND COLOR (hex)" } */
        for (int[] tileData : BoardWidget.TILE_COLOR_MAP) {
            BoardWidget.TILE_COLORS.put(tileData[0], new Color[]{new Color(tileData[1]), new Color(tileData[2])});
        }
    }

    /**
     * A graphical representation of a 2048 board with SIZE rows and columns.
     */
    BoardWidget(int size) {
        this.size = size;
        this.boardSide = size * BoardWidget.TILE_SIDE_SEP + BoardWidget.TILE_SEP;
        this.tiles = new ArrayList<>();
        setPreferredSize(this.boardSide, this.boardSide);
    }

    /**
     * Clear all tiles from the board.
     */
    synchronized void clear() {
        this.tiles.clear();
        repaint();
    }

    /**
     * Indicate that "GAME OVER" label should be displayed.
     */
    synchronized void markEnd() {
        this.end = true;
        repaint();
    }

    @Override
    /** Render board on G. */
    public synchronized void paintComponent(Graphics2D g) {
        g.setColor(BoardWidget.EMPTY_SQUARE_COLOR);
        g.fillRect(0, 0, this.boardSide, this.boardSide);
        g.setColor(BoardWidget.BAR_COLOR);
        for (int k = 0; k <= this.boardSide; k += BoardWidget.TILE_SIDE_SEP) {
            g.fillRect(0, k, this.boardSide, BoardWidget.TILE_SEP);
            g.fillRect(k, 0, BoardWidget.TILE_SEP, this.boardSide);
        }
        for (Tile tile : this.tiles) {
            render(g, tile);
        }
        if (this.end) {
            g.setFont(BoardWidget.OVERLAY_FONT);
            FontMetrics metrics = g.getFontMetrics();
            g.setColor(BoardWidget.OVERLAY_COLOR);
            g.drawString("GAME OVER", (this.boardSide - metrics.stringWidth("GAME OVER")) / 2,
                    (2 * this.boardSide + metrics.getMaxAscent()) / 4);
        }
    }

    /**
     * Render TILE on G.
     */
    private void render(Graphics2D g, Tile tile) {
        int col0 = tile.col(), row0 = tile.row(), col1 = tile.next().col(), row1 = tile.next().row();
        int dcol = col0 < col1 ? 1 : col0 == col1 ? 0 : -1, drow = row0 < row1 ? 1 : row0 == row1 ? 0 : -1;

        float vcol, vrow;
        if (this.distMoved >= max(abs(col0 - col1), abs(row0 - row1))) {
            vcol = col1;
            vrow = row1;
        } else {
            vcol = col0 + this.distMoved * dcol;
            vrow = row0 + this.distMoved * drow;
        }

        int ulx = Math.round(vcol * BoardWidget.TILE_SIDE_SEP + BoardWidget.TILE_SEP),
                uly = Math.round((this.size - vrow - 1) * BoardWidget.TILE_SIDE_SEP + BoardWidget.TILE_SEP);

        if (tile.value() < 100) {
            g.setFont(BoardWidget.TILE_FONT2);
        } else if (tile.value() < 1000) {
            g.setFont(BoardWidget.TILE_FONT3);
        } else {
            g.setFont(BoardWidget.TILE_FONT4);
        }
        FontMetrics metrics = g.getFontMetrics();
        int bloom;
        if (this.bloomingTiles != null && this.bloomingTiles.contains(tile)) {
            bloom = this.bloom;
        } else {
            bloom = 0;
        }
        g.setColor(BoardWidget.TILE_COLORS.get(tile.value())[1]);
        g.fillRect(ulx - bloom, uly - bloom, 2 * bloom + BoardWidget.TILE_SIDE, 2 * bloom + BoardWidget.TILE_SIDE);
        g.setColor(BoardWidget.TILE_COLORS.get(tile.value())[0]);

        String label = Integer.toString(tile.value());
        g.drawString(label, ulx + (BoardWidget.TILE_SIDE - metrics.stringWidth(label)) / 2,
                uly + (2 * BoardWidget.TILE_SIDE + metrics.getMaxAscent()) / 4);
    }

    /**
     * Return the list of all Tiles in MODEL.
     */
    private ArrayList<Tile> modelTiles(Model model) {
        ArrayList<Tile> result = new ArrayList<>();
        for (int col = 0; col < model.size(); col += 1) {
            for (int row = 0; row < model.size(); row += 1) {
                Tile tile = model.tile(col, row);
                if (tile != null) {
                    result.add(tile);
                }
            }
        }
        return result;
    }

    /**
     * Return the list of all tiles in NEXTTILES that are newly created or the
     * result of merging of current tiles.
     */
    private ArrayList<Tile> newTiles(ArrayList<Tile> nextTiles) {
        ArrayList<Tile> bloomers = new ArrayList<>();
        bloomers.addAll(nextTiles);
        for (Tile tile : this.tiles) {
            if (tile.next().value() == tile.value()) {
                bloomers.remove(tile.next());
            }
        }
        return bloomers;
    }

    /**
     * Wait for one tick (TICK milliseconds).
     */
    private void tick() {
        try {
            wait(BoardWidget.TICK);
        } catch (InterruptedException excp) {
            assert false : "Internal error: unexpected interrupt";
        }
    }

    /**
     * Create the blooming effect on tiles in BLOOMINGTILES.
     */
    private void doBlooming(ArrayList<Tile> bloomingTiles) {
        this.bloomingTiles = bloomingTiles;
        if (bloomingTiles.isEmpty()) {
            return;
        }
        for (int k = 1; k <= BoardWidget.BLOOM_TICKS; k += 1) {
            this.bloom = round(BoardWidget.TILE_SIDE * BoardWidget.BLOOM_FACTOR * k / BoardWidget.BLOOM_TICKS);
            repaint();
            tick();
        }
        for (int k = BoardWidget.BLOOM_TICKS - 1; k >= 0; k -= 1) {
            this.bloom = round(BoardWidget.TILE_SIDE * BoardWidget.BLOOM_FACTOR * k / BoardWidget.BLOOM_TICKS);
            repaint();
            tick();
        }
        this.bloomingTiles = null;
    }

    /**
     * Move tiles to their new positions and save a new set of tiles from MODEL,
     * which is assumed to reflect the next state of the tiles after the completion
     * of all movement.
     */
    synchronized void update(Model model) {
        float dist;
        ArrayList<Tile> nextTiles = modelTiles(model);

        dist = 0.0f;
        for (Tile tile : this.tiles) {
            dist = Math.max(dist, tile.distToNext());
        }
        this.distMoved = 0.0f;
        while (this.distMoved < dist) {
            repaint();
            tick();
            this.distMoved = Math.min(dist, this.distMoved + BoardWidget.TICK * BoardWidget.MOVE_DELTA / 1000.0f);
        }

        ArrayList<Tile> bloomers = newTiles(nextTiles);
        this.tiles = nextTiles;
        doBlooming(bloomers);
        this.end = model.gameOver();
        this.distMoved = 0.0f;
        repaint();
    }

    /**
     * A list of Tiles currently being displayed.
     */
    private ArrayList<Tile> tiles;

    /**
     * A list of Tiles currently being displayed with blooming effect.
     */
    private ArrayList<Tile> bloomingTiles;

    /**
     * Distance tiles have moved toward their next positions, in units of rows and
     * columns.
     */
    private float distMoved;

    /**
     * Amount to add to sides of tiles in _bloomingTiles.
     */
    private int bloom;

    /**
     * Number of rows and of columns.
     */
    private final int size;

    /**
     * Length (in pixels) of the side of the board.
     */
    private int boardSide;

    /**
     * True iff "GAME OVER" message is being displayed.
     */
    private boolean end;

}
