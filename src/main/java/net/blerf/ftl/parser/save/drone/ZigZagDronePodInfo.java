package net.blerf.ftl.parser.save.drone;

/**
 * Extended Combat/Beam/Ship_Repair drone info.
 *
 * These drones flit to a random (?) point, stop, then move to another,
 * and so on.
 */
public class ZigZagDronePodInfo extends ExtendedDronePodInfo {
    private int lastWaypointX = 0;
    private int lastWaypointY = 0;
    private int transitTicks = 0;
    private int exhaustAngle = 0;
    private int unknownEpsilon = 0;

    /**
     * Constructor.
     */
    public ZigZagDronePodInfo() {
        super();
    }

    /**
     * Copy constructor.
     */
    protected ZigZagDronePodInfo(ZigZagDronePodInfo srcInfo ) {
        super( srcInfo );
        lastWaypointX = srcInfo.getLastWaypointX();
        lastWaypointY = srcInfo.getLastWaypointY();
        transitTicks = srcInfo.getTransitTicks();
        exhaustAngle = srcInfo.getExhaustAngle();
        unknownEpsilon = srcInfo.getUnknownEpsilon();
    }

    @Override
    public ZigZagDronePodInfo copy() { return new ZigZagDronePodInfo( this ); }


    /**
     * Resets aspects of an existing object to be viable for player use.
     *
     * This will be called by the ship object when it is commandeered.
     *
     * Warning: Dangerous while values remain undeciphered.
     */
    @Override
    public void commandeer() {
    }


    /**
     * Sets the cached position from when the drone last stopped.
     *
     * TODO: Modify this value in the editor. In CheatEngine, changing
     * this has no effect, appearing to be read-only field for reference.
     *
     * @param n a pseudo-float
     */
    public void setLastWaypointX( int n ) { lastWaypointX = n; }
    public void setLastWaypointY( int n ) { lastWaypointY = n; }
    public int getLastWaypointX() { return lastWaypointX; }
    public int getLastWaypointY() { return lastWaypointY; }

    /**
     * Sets time elapsed while this drone moves.
     *
     * This increments from 0 to 1000 as the drone drifts toward a new
     * waypoint. While this value is below 200, exhaust flames are
     * visible. Then they vanish. The moment the drone pauses at the
     * destination, this is set to 1000.
     *
     * When not set, this is MIN_INT. This happens when stationary while
     * stunned.
     *
     * Observed values: 153 (stunned drift begins), 153000 (mid drift),
     * 153000000 (near end of drift).
     *
     * TODO: Modify this value in the editor. In CheatEngine, changing
     * this has no effect, appearing to be read-only field for reference.
     *
     * @see #setExhaustAngle(int)
     */
    public void setTransitTicks( int n ) { transitTicks = n; }
    public int getTransitTicks() { return transitTicks; }

    /**
     * Sets the angle to display exhaust flames thrusting toward.
     *
     * When not set, this is MIN_INT.
     *
     * TODO: Modify this value in the editor. In CheatEngine, changing
     * this DOES work.
     *
     * @param n a pseudo-float (n degrees clockwise from east)
     *
     * @see #setTransitTicks(int)
     */
    public void setExhaustAngle( int n ) { exhaustAngle = n; }
    public int getExhaustAngle() { return exhaustAngle; }

    /**
     * Unknown.
     *
     * When not set, this is MIN_INT.
     */
    public void setUnknownEpsilon( int n ) { unknownEpsilon = n; }
    public int getUnknownEpsilon() { return unknownEpsilon; }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append( String.format( "Last Waypoint:      %7d,%7d\n", lastWaypointX, lastWaypointY ) );
        result.append( String.format( "TransitTicks:       %7s\n", (transitTicks == Integer.MIN_VALUE ? "N/A" : transitTicks) ) );
        result.append( String.format( "Exhaust Angle:      %7s\n", (exhaustAngle == Integer.MIN_VALUE ? "N/A" : exhaustAngle) ) );
        result.append( String.format( "Epsilon?:           %7s\n", (unknownEpsilon == Integer.MIN_VALUE ? "N/A" : unknownEpsilon) ) );

        return result.toString();
    }
}
