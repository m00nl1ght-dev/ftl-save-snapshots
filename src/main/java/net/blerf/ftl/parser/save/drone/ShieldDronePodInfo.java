package net.blerf.ftl.parser.save.drone;

public class ShieldDronePodInfo extends ExtendedDronePodInfo {
    private int unknownAlpha = -1000;

    /**
     * Constructor.
     */
    public ShieldDronePodInfo() {
        super();
    }

    /**
     * Copy constructor.
     */
    protected ShieldDronePodInfo(ShieldDronePodInfo srcInfo ) {
        super( srcInfo );
        unknownAlpha = srcInfo.getUnknownAlpha();
    }

    @Override
    public ShieldDronePodInfo copy() { return new ShieldDronePodInfo( this ); }


    /**
     * Resets aspects of an existing object to be viable for player use.
     *
     * This will be called by the ship object when it is commandeered.
     *
     * Warning: Dangerous while values remain undeciphered.
     * TODO: Recurse into all nested objects.
     */
    @Override
    public void commandeer() {
        setUnknownAlpha( -1000 );
    }


    /**
     * Unknown.
     *
     * Zoltan shield recharge ticks?
     *
     * Observed values: -1000 (inactive)
     */
    public void setUnknownAlpha( int n ) { unknownAlpha = n; }
    public int getUnknownAlpha() { return unknownAlpha; }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append( String.format( "Alpha?:             %7d\n", unknownAlpha ) );

        return result.toString();
    }
}
