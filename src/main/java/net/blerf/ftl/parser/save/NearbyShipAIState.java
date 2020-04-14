package net.blerf.ftl.parser.save;

public class NearbyShipAIState {
    private boolean surrendered = false;
    private boolean escaping = false;
    private boolean destroyed = false;
    private int surrenderThreshold = 0;
    private int escapeThreshold = -1;
    private int escapeTicks = 15000;
    private boolean stalemateTriggered = false;  // TODO: Does this start sudden death, or mark its completion?
    private int stalemateTicks = 0;
    private int boardingAttempts = 0;
    private int boardersNeeded = 0;


    /**
     * Constructor.
     */
    public NearbyShipAIState() {
    }

    /**
     * Toggles whether the ship has offered surrender.
     *
     * FTL sets this the moment it triggers the surrender event (before the
     * player accepts/declines).
     */
    public void setSurrendered( boolean b ) { surrendered = b; }
    public boolean hasSurrendered() { return surrendered; }

    /**
     * Toggles whether the ship is powering up its FTL to escape.
     */
    public void setEscaping( boolean b ) { escaping = b; }
    public boolean isEscaping() { return escaping; }

    /**
     * Toggles whether the ship has been destroyed.
     *
     * TODO: Confirm this.
     */
    public void setDestroyed( boolean b ) { destroyed = b; }
    public boolean isDestroyed() { return destroyed; }

    /**
     * Sets the hull amount that will cause the ship will surrender.
     *
     * For the rebel flagship, this is -100.
     */
    public void setSurrenderThreshold( int n ) { surrenderThreshold = n; }
    public int getSurrenderThreshold() { return surrenderThreshold; }

    /**
     * Sets the hull amount that will cause the ship to flee.
     *
     * For the rebel flagship, this is -101.
     *
     * When not set, this is -1.
     */
    public void setEscapeThreshold( int n ) { escapeThreshold = n; }
    public int getEscapeThreshold() { return escapeThreshold; }

    /**
     * Sets time elapsed while waiting for the FTL drive to charge.
     *
     * This decrements to 0; then the nearby ship jumps away.
     *
     * Observed values: 15000 (initially), 27533 (30000?).
     *
     * TODO: An FTL Jammer augment might only override the default once an
     * escape attempt is initiated. It was still 15000 at the beginning of
     * one battle.
     */
    public void setEscapeTicks( int n ) { escapeTicks = n; }
    public int getEscapeTicks() { return escapeTicks; }

    public void setStalemateTriggered( boolean b ) { stalemateTriggered = b; }
    public boolean isStalemateTriggered() { return stalemateTriggered; }

    public void setStalemateTicks( int n ) { stalemateTicks = n; }
    public int getStalemateTicks() { return stalemateTicks; }

    /**
     * Sets the count of times crew teleported so far.
     *
     * After a certain number, no further boarding attempts will be made.
     *
     * TODO: Determine that limit, and whether it counts crew or parties.
     */
    public void setBoardingAttempts( int n ) { boardingAttempts = n; }
    public int getBoardingAttempts() { return boardingAttempts; }

    /**
     * Sets the number of crew to teleport as boarders.
     *
     * Matthew's hint: It's based on the original crew strength.
     *
     * TODO: Test if this is the limit for setBoardingAttempts().
     */
    public void setBoardersNeeded( int n ) { boardersNeeded = n; }
    public int getBoardersNeeded() { return boardersNeeded; }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append( String.format( "Surrender Offered:   %7b\n", surrendered ) );
        result.append( String.format( "Escaping:            %7b\n", escaping ) );
        result.append( String.format( "Destroyed?:          %7b\n", destroyed ) );
        result.append( String.format( "Surrender Threshold: %7d (Hull amount when surrender is offered)\n", surrenderThreshold ) );
        result.append( String.format( "Escape Threshold:    %7d (Hull amount when escape begins)\n", escapeThreshold ) );
        result.append( String.format( "Escape Ticks:        %7d (Decrements to 0)\n", escapeTicks ) );
        result.append( String.format( "Stalemate Triggered?:%7b\n", stalemateTriggered ) );
        result.append( String.format( "Stalemate Ticks?:    %7d\n", stalemateTicks ) );
        result.append( String.format( "Boarding Attempts?:  %7d\n", boardingAttempts ) );
        result.append( String.format( "Boarders Needed?:    %7d\n", boardersNeeded ) );

        return result.toString();
    }
}
