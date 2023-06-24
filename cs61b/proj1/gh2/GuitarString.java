package gh2;

import deque.Deque;
import deque.LinkedListDeque;

/**
 * @author yang
 */
public class GuitarString {

    /*
     * Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday.
     */

    /**
     * Sampling Rate
     */
    private static final int SR = 44100;

    /**
     * energy decay factor
     */
    private static final double DECAY = .996;

    /**
     * Size of string.
     */
    private final int size;

    /**
     * Buffer for storing sound data.
     */
    private final Deque<Double> buffer;

    /**
     * Create a guitar string of the given frequency.
     *
     * @param frequency The frequency.
     */
    public GuitarString(double frequency) {
        this.size = (int) Math.round(SR / frequency);
        this.buffer = new LinkedListDeque<>();
        for (int i = 0; i < this.size; i++) {
            this.buffer.addLast(0.0);
        }
    }

    /**
     * Pluck the guitar string by replacing the buffer with white noise.
     */
    public void pluck() {
        while (!this.buffer.isEmpty()) {
            this.buffer.removeFirst();
        }

        for (int i = 0; i < this.size; i++) {
            this.buffer.addLast(Math.random() - 0.5);
        }
    }

    /**
     * Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        double first = this.buffer.removeFirst();
        double second = this.buffer.get(0);
        this.buffer.addLast((first + second) * 0.5 * DECAY);
    }

    /**
     * Return the double at the front of the buffer.
     */
    public double sample() {
        return this.buffer.get(0);
    }

}
