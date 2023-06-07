package speed;

/**
 * @author yang
 */
public class Stopwatch {

    private final long startTime = System.currentTimeMillis();

    public double elapsedTime() {
        return (System.currentTimeMillis() - this.startTime) / 1000.0;
    }

}
