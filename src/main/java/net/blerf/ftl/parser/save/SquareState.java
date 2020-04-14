package net.blerf.ftl.parser.save;

public class SquareState {
    private int fireHealth = 0;
    private int ignitionProgress = 0;
    private int extinguishmentProgress = -1;


    public SquareState() {
    }

    public SquareState(int fireHealth, int ignitionProgress, int extinguishmentProgress ) {
        this.fireHealth = fireHealth;
        this.ignitionProgress = ignitionProgress;
        this.extinguishmentProgress = extinguishmentProgress;
    }

    /**
     * Copy constructor.
     */
    public SquareState(SquareState srcSquare ) {
        this.fireHealth = srcSquare.getFireHealth();
        this.ignitionProgress = srcSquare.getIgnitionProgress();
        this.extinguishmentProgress = srcSquare.getExtinguishmentProgress();
    }

    /**
     * Sets the health of a fire in this square, or 0.
     *
     * @param n 0-100
     */
    public void setFireHealth( int n ) { fireHealth = n; }
    public int getFireHealth() { return fireHealth; }

    /**
     * Sets the square's ignition progress.
     *
     * Squares adjacent to a fire grow closer to igniting as
     * time passes. Then a new fire spawns in them at full health.
     *
     * @param n 0-100
     */
    public void setIgnitionProgress( int n ) { ignitionProgress = n; }
    public int getIgnitionProgress() { return ignitionProgress; }

    /**
     * Unknown.
     *
     * This is a rapidly decrementing number, as a fire disappears in a puff
     * of smoke. When not set, this is -1.
     *
     * Starving a fire of oxygen does not affect its health.
     *
     * In FTL 1.01-1.5.10 this always seemed to be -1. In FTL 1.5.13, other
     * values were finally observed.
     *
     * Observed values: -1 (almost always), 9,8,7,6,5,2,1,0.
     */
    public void setExtinguishmentProgress( int n ) { extinguishmentProgress = n; }
    public int getExtinguishmentProgress() { return extinguishmentProgress; }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append( String.format( "Fire HP: %3d, Ignition: %3d%%, Extinguishment: %2d\n", fireHealth, ignitionProgress, extinguishmentProgress ) );

        return result.toString();
    }
}
