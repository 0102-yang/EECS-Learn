package game2048;

/**
 * Describes a source of input commands.
 *
 * @author P. N. Hilfinger
 */
interface InputSource {

    /**
     * Returns one command string.
     *
     * @return The string.
     */
    String getKey();

    /**
     * Returns a candidate Tile whose row and column is in the range 0 .. SIZE-1.
     *
     * @param size The size.
     * @return The tile.
     */
    Tile getNewTile(int size);

}
