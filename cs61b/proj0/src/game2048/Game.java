package game2048;

import static game2048.Side.*;

/**
 * The input/output and GUI controller for play of a game of 2048.
 *
 * @author P. N. Hilfinger.
 */
public class Game {

    /**
     * Controller for a game represented by MODEL, using SOURCE as the the source of
     * key inputs and random Tiles.
     */
    public Game(Model model, InputSource source) {
        this.model = model;
        this.source = source;
        this.playing = true;
    }

    /**
     * Return true iff we have not received a Quit command.
     */
    boolean playing() {
        return this.playing;
    }

    /**
     * Clear the board and play one game, until receiving a quit or new-game
     * request. Update the viewer with each added tile or change in the board from
     * tilting.
     */
    void playGame() {
        this.model.clear();
        this.model.addTile(getValidNewTile());
        while (this.playing) {
            if (!this.model.gameOver()) {
                this.model.addTile(getValidNewTile());
                this.model.notifyObservers();
            }

            boolean moved;
            moved = false;
            while (!moved) {
                String cmnd = this.source.getKey();
                switch (cmnd) {
                    case "Quit":
                        this.playing = false;
                        return;
                    case "New Game":
                        return;
                    case "Up":
                    case "Down":
                    case "Left":
                    case "Right":
                    case "\u2190":
                    case "\u2191":
                    case "\u2192":
                    case "\u2193":
                        if (!this.model.gameOver() && this.model.tilt(keyToSide(cmnd))) {
                            this.model.notifyObservers(cmnd);
                            moved = true;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Return the side indicated by KEY ("Up", "Down", "Left", or "Right").
     */
    private Side keyToSide(String key) {
        switch (key) {
            case "Up":
            case "\u2191":
                return NORTH;
            case "Down":
            case "\u2193":
                return SOUTH;
            case "Left":
            case "\u2190":
                return WEST;
            case "Right":
            case "\u2192":
                return EAST;
            default:
                throw new IllegalArgumentException("unknown key designation");
        }
    }

    /**
     * Return a valid tile, using our source's tile input until finding one that
     * fits on the current board. Assumes there is at least one empty square on the
     * board.
     */
    private Tile getValidNewTile() {
        while (true) {
            Tile tile = this.source.getNewTile(this.model.size());
            if (this.model.tile(tile.col(), tile.row()) == null) {
                return tile;
            }
        }
    }

    /**
     * The playing board.
     */
    private Model model;

    /**
     * Input source from standard input.
     */
    private InputSource source;

    /**
     * True while user is still willing to play.
     */
    private boolean playing;

}
